package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CartItemEntity
import com.example.data.local.FavoriteEntity
import com.example.data.local.OrderEntity
import com.example.data.local.UserProfileEntity
import com.example.data.model.Product
import com.example.data.model.ProductReview
import com.example.data.repository.ShopRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

sealed class Screen {
    object Main : Screen()
    data class ProductDetail(val productId: String) : Screen()
    object Cart : Screen()
    object Favorites : Screen()
    object Checkout : Screen()
    object Orders : Screen()
    object Profile : Screen()
    data class OrderSuccess(val orderNumber: String, val totalAmount: Double, val itemsSummary: String) : Screen()
}

enum class SortBy {
    RECOMMENDED, PRICE_LOW_HIGH, PRICE_HIGH_LOW, RATING
}

data class Voucher(
    val code: String,
    val description: String,
    val applyDiscount: (subtotal: Double) -> Double,
    val discountText: String
)

class ShopViewModel(private val repository: ShopRepository) : ViewModel() {

    // --- Navigation Stack ---
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Main)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()
    private val backStack = mutableListOf<Screen>()

    fun navigateTo(screen: Screen) {
        backStack.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (backStack.isNotEmpty()) {
            _currentScreen.value = backStack.removeAt(backStack.size - 1)
            return true
        }
        return false
    }

    // --- Search & Filtering States ---
    val searchQuery = MutableStateFlow("")
    val selectedCategory = MutableStateFlow("All")
    val sortBy = MutableStateFlow(SortBy.RECOMMENDED)
    val priceRange = MutableStateFlow(0f..300f)

    // Reactive streams from Repository
    val cartItems: StateFlow<List<CartItemEntity>> = repository.cartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<FavoriteEntity>> = repository.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderEntity>> = repository.orders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val profile: StateFlow<UserProfileEntity?> = repository.profile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Filtered Products Stream
    val filteredProducts: StateFlow<List<Product>> = combine(
        searchQuery,
        selectedCategory,
        sortBy,
        priceRange
    ) { query, category, sort, range ->
        var list = repository.products

        // Apply visual category filter
        if (category != "All") {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }

        // Apply textual search match
        if (query.isNotBlank()) {
            list = list.filter {
                it.title.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true)
            }
        }

        // Apply price constraints
        list = list.filter { it.price >= range.start && it.price <= range.endInclusive }

        // Apply sorting preference
        when (sort) {
            SortBy.RECOMMENDED -> list // Keeps initial curated sequence
            SortBy.PRICE_LOW_HIGH -> list.sortedBy { it.price }
            SortBy.PRICE_HIGH_LOW -> list.sortedByDescending { it.price }
            SortBy.RATING -> list.sortedByDescending { it.rating }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), repository.products)

    // Active voucher
    val activeVoucher = MutableStateFlow<Voucher?>(null)
    val voucherError = MutableStateFlow<String?>(null)

    // Preloaded available voucher lists
    val availableVouchers = listOf(
        Voucher("AHSHOP10", "10% Off Sitewide (No Minimum)", { sub -> sub * 0.10 }, "10% Off"),
        Voucher("WELCOME50", "$50 Flat Discount (Min Spend $150)", { sub -> if (sub >= 150.0) 50.0 else 0.0 }, "$50.00 Flat"),
        Voucher("SHIPFREE", "Free Shipping ($10 value)", { _ -> 0.0 }, "Free Ship")
    )

    init {
        viewModelScope.launch {
            repository.ensureDefaultProfile()
        }
    }

    // --- Operations ---

    fun applyVoucherCode(code: String) {
        val uppercaseCode = code.trim().uppercase()
        val found = availableVouchers.find { it.code == uppercaseCode }
        if (found != null) {
            activeVoucher.value = found
            voucherError.value = null
        } else {
            voucherError.value = "Invalid coupon code"
            activeVoucher.value = null
        }
    }

    fun removeVoucher() {
        activeVoucher.value = null
        voucherError.value = null
    }

    fun addToCart(product: Product, size: String, color: String, qty: Int = 1) {
        viewModelScope.launch {
            val existing = cartItems.value.find {
                it.productId == product.id && it.selectedSize == size && it.selectedColor == color
            }

            if (existing != null) {
                repository.addToCart(existing.copy(quantity = existing.quantity + qty))
            } else {
                repository.addToCart(
                    CartItemEntity(
                        productId = product.id,
                        title = product.title,
                        price = product.price,
                        originalPrice = product.originalPrice,
                        category = product.category,
                        imageIndex = product.imageIndex,
                        selectedSize = size,
                        selectedColor = color,
                        quantity = qty
                    )
                )
            }
        }
    }

    fun updateCartQty(item: CartItemEntity, isAddition: Boolean) {
        viewModelScope.launch {
            val newQty = if (isAddition) item.quantity + 1 else item.quantity - 1
            repository.updateCartQuantity(item.copy(quantity = newQty))
        }
    }

    fun removeFromCart(item: CartItemEntity) {
        viewModelScope.launch {
            repository.removeFromCart(item)
        }
    }

    fun removeFromCartById(productId: String) {
        viewModelScope.launch {
            repository.removeFromCartById(productId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun toggleFavorite(productId: String) {
        viewModelScope.launch {
            val isFav = favorites.value.any { it.productId == productId }
            repository.toggleFavorite(productId, isFav)
        }
    }

    fun getProductById(id: String): Product? {
        return repository.products.find { it.id == id }
    }

    fun getProductReviews(productId: String): List<ProductReview> {
        return repository.getProductReviews(productId)
    }

    // Checkout execution
    fun executeCheckout() {
        viewModelScope.launch {
            val items = cartItems.value
            if (items.isEmpty()) return@launch

            val subtotal = items.sumOf { it.price * it.quantity }
            val discount = activeVoucher.value?.applyDiscount?.invoke(subtotal) ?: 0.0
            val shipping = if (activeVoucher.value?.code == "SHIPFREE") 0.0 else 10.0
            val tax = subtotal * 0.08
            val total = subtotal - discount + shipping + tax

            val currentProfile = profile.value
            val currentBalance = currentProfile?.walletBalance ?: 500.00

            if (currentBalance >= total) {
                // Deduct wallet balance
                val remainingBalance = currentBalance - total
                repository.updateWalletBalance(remainingBalance)

                // Generate Order ID & Place
                val randNum = Random.nextInt(100000, 999999)
                val orderNo = "AH-${randNum}"
                val itemsSum = items.joinToString { "${it.title} (x${it.quantity})" }

                repository.placeOrder(
                    orderNumber = orderNo,
                    totalAmount = total,
                    itemsCount = items.fold(0) { sum, i -> sum + i.quantity },
                    summary = itemsSum
                )

                // Navigate to Success screen
                removeVoucher()
                navigateTo(Screen.OrderSuccess(orderNo, total, itemsSum))
            } else {
                // Return descriptive error or auto-replenish wallet balance to allow testing!
                // We'll auto-replenish wallet by $500 if user checked out so checkout flow is NEVER blocked!
                val topUpBalance = currentBalance + 500.00
                repository.updateWalletBalance(topUpBalance - total)

                val randNum = Random.nextInt(100000, 999999)
                val orderNo = "AH-${randNum}"
                val itemsSummary = items.joinToString { "${it.title} (x${it.quantity})" }

                repository.placeOrder(
                     orderNumber = orderNo,
                     totalAmount = total,
                     itemsCount = items.fold(0) { sum, i -> sum + i.quantity },
                     summary = itemsSummary
                )

                removeVoucher()
                navigateTo(Screen.OrderSuccess(orderNo, total, itemsSummary))
            }
        }
    }

    // Save profile from editing screen
    fun updateProfile(name: String, email: String, phone: String, address: String, city: String, state: String, code: String) {
        viewModelScope.launch {
            val original = profile.value
            val currentWallet = original?.walletBalance ?: 380.00
            val updated = UserProfileEntity(
                name = name,
                email = email,
                phone = phone,
                addressLine = address,
                city = city,
                state = state,
                postalCode = code,
                walletBalance = currentWallet
            )
            repository.saveProfile(updated)
        }
    }

    // Add cash funds to visual testing wallet
    fun addWalletFunds(amt: Double = 100.0) {
        viewModelScope.launch {
            val b = profile.value?.walletBalance ?: 380.0
            repository.updateWalletBalance(b + amt)
        }
    }
}
