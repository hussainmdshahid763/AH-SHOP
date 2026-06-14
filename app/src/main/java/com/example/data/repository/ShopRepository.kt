package com.example.data.repository

import androidx.compose.ui.graphics.Color
import com.example.data.local.*
import com.example.data.model.Product
import com.example.data.model.ProductReview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ShopRepository(private val shopDao: ShopDao) {

    // Mock Products List
    val products = listOf(
        Product(
            id = "p1",
            title = "Nordic Wool Trench Coat",
            price = 189.00,
            originalPrice = 245.00,
            rating = 4.8,
            reviewsCount = 124,
            category = "Fashion",
            imageIndex = 1,
            description = "A sophisticated, heavyweight wool trench coat precision-crafted for cold climates. Designed with structured shoulders, classic waist tie belt, fully lined satin interior, and side slip pockets.",
            sizes = listOf("S", "M", "L", "XL"),
            colors = listOf("Obsidian Black", "Coastal Sage", "Warm Cream"),
            colorHexes = listOf(Color(0xFF222222), Color(0xFF6B8672), Color(0xFFEFE8DA)),
            isFeatured = true
        ),
        Product(
            id = "p2",
            title = "Acoustic Noise-Canceling Buds",
            price = 149.00,
            originalPrice = 199.00,
            rating = 4.9,
            reviewsCount = 238,
            category = "Electronics",
            imageIndex = 2,
            description = "Experience deep sonic immersion. True wireless audio featuring 45dB active noise cancelation, custom spatial audio tuning, dual beamforming mics, and 30-hour playback with standard charging case.",
            sizes = listOf("Standard"),
            colors = listOf("Obsidian Black", "Cobalt Iris", "Warm Cream"),
            colorHexes = listOf(Color(0xFF222222), Color(0xFF435FCC), Color(0xFFEFE8DA)),
            isFeatured = true
        ),
        Product(
            id = "p3",
            title = "Ultra-Light Knit Trainer Shoes",
            price = 89.00,
            originalPrice = 120.00,
            rating = 4.6,
            reviewsCount = 92,
            category = "Shoes",
            imageIndex = 3,
            description = "Zero gravity comfort for everyday paces. Constructed from highly breathable recycled knit weave mesh, flexible shock-absorbent multi-grid rubber outsoles, and memory foam Ortholite cushions.",
            sizes = listOf("40", "41", "42", "43", "44"),
            colors = listOf("Obsidian Black", "Heather Gray"),
            colorHexes = listOf(Color(0xFF222222), Color(0xFF909497)),
            isNewArrival = true
        ),
        Product(
            id = "p4",
            title = "Modern Minimalist Leather Watch",
            price = 199.00,
            originalPrice = 249.00,
            rating = 4.7,
            reviewsCount = 156,
            category = "Accessories",
            imageIndex = 4,
            description = "Timeless balance of form and function. Elegantly minimal 40mm brushed alloy casing, scratch-resistant sapphire dome glass, Swiss quartz precision movement, and full-grain Italian leather strap.",
            sizes = listOf("One Size"),
            colors = listOf("Warm Amber", "Obsidian Black"),
            colorHexes = listOf(Color(0xFFD09040), Color(0xFF222222)),
            isFeatured = true
        ),
        Product(
            id = "p5",
            title = "Terracotta Ribbed Living Vase",
            price = 35.00,
            originalPrice = 45.00,
            rating = 4.5,
            reviewsCount = 45,
            category = "Living",
            imageIndex = 5,
            description = "Add an earthy texturing anchor to your console or shelf space. Hand-turned ribbed clay terracotta pottery vase, finished with a subtle rustic matte glaze inside and out to safely hold botanicals.",
            sizes = listOf("Medium", "Large"),
            colors = listOf("Terracotta", "Warm Cream"),
            colorHexes = listOf(Color(0xFFD67D65), Color(0xFFEFE8DA)),
            isNewArrival = true
        ),
        Product(
            id = "p6",
            title = "Oversized Organic Cotton Tee",
            price = 29.00,
            originalPrice = 39.00,
            rating = 4.7,
            reviewsCount = 312,
            category = "Fashion",
            imageIndex = 6,
            description = "A standard daily essential. Crafted from luxurious 240GSM organic ring-spun cotton that feels soft against the skin, designed with a relaxed dropped-shoulder silhouette and resilient ribbed crew collar.",
            sizes = listOf("S", "M", "L", "XL"),
            colors = listOf("Warm Cream", "Obsidian Black", "Heather Gray"),
            colorHexes = listOf(Color(0xFFEFE8DA), Color(0xFF222222), Color(0xFF909497))
        ),
        Product(
            id = "p7",
            title = "Slim Hybrid Wireless Charger",
            price = 45.00,
            originalPrice = 55.00,
            rating = 4.5,
            reviewsCount = 78,
            category = "Electronics",
            imageIndex = 7,
            description = "Declutter your workspace in style. Ultra-slim 8mm profile hybrid charging dock layered with structural slate textile surface and brushed aluminum backing. Supports 15W fast charge protocols.",
            sizes = listOf("Standard"),
            colors = listOf("Obsidian Black", "Coastal Sage"),
            colorHexes = listOf(Color(0xFF222222), Color(0xFF6B8672))
        ),
        Product(
            id = "p8",
            title = "Chunky Sole Leather Loafers",
            price = 129.00,
            originalPrice = 160.00,
            rating = 4.8,
            reviewsCount = 61,
            category = "Shoes",
            imageIndex = 8,
            description = "A bold contemporary twist on a heritage dress shoe. Handmade from water-resistant smooth calf leather, featuring secure moc-toe stitching, premium memory foam lining, and heavy-duty lightweight EVA treaded platform outsoles.",
            sizes = listOf("41", "42", "43"),
            colors = listOf("Obsidian Black"),
            colorHexes = listOf(Color(0xFF222222)),
            isNewArrival = true
        ),
        Product(
            id = "p9",
            title = "Vegan Leather Shoulder Satchel",
            price = 79.00,
            originalPrice = 99.00,
            rating = 4.7,
            reviewsCount = 110,
            category = "Accessories",
            imageIndex = 9,
            description = "The ultimate everyday companion bag. Fabricated from thick, wear-resistant grain vegan leather with gold-toned brass elements. Equipped with zip-pocket organizers, detachable canvas strap, and magnetic front lock.",
            sizes = listOf("One Size"),
            colors = listOf("Warm Amber", "Obsidian Black"),
            colorHexes = listOf(Color(0xFFD09040), Color(0xFF222222))
        ),
        Product(
            id = "p10",
            title = "Brushed Gold Task Table Lamp",
            price = 68.00,
            originalPrice = 85.00,
            rating = 4.6,
            reviewsCount = 84,
            category = "Living",
            imageIndex = 10,
            description = "Sleek directional task visual anchor for smart desks or late-night study drawers. Fully adjustable rotating brass head with dual articulation, layered brushed gold plating, and matching fabric cord.",
            sizes = listOf("Standard"),
            colors = listOf("Brushed Gold"),
            colorHexes = listOf(Color(0xFFD4AF37)),
            isFeatured = true
        ),
        Product(
            id = "p11",
            title = "Smart Ambient LED Screen Bar",
            price = 59.00,
            originalPrice = 79.00,
            rating = 4.8,
            reviewsCount = 142,
            category = "Electronics",
            imageIndex = 11,
            description = "Erase desk eye fatigue. Premium monitor-mounted asymmetrical LED bar lamp that highlights only the screen's workspace path without reflecting off display panels. Built-in capacitive touch color controls.",
            sizes = listOf("Standard"),
            colors = listOf("Obsidian Black"),
            colorHexes = listOf(Color(0xFF222222)),
            isNewArrival = true
        ),
        Product(
            id = "p12",
            title = "Double-Walled Travel Thermos",
            price = 28.00,
            originalPrice = 35.00,
            rating = 4.9,
            reviewsCount = 210,
            category = "Living",
            imageIndex = 12,
            description = "Seals hot drinks for 12 hours or ice-cold refreshers for 24 hours. Built with food-grade double-walled 18/8 structural stainless steel insulation under a matte exterior powder coat. Leak-proof steel magnetic cap.",
            sizes = listOf("500ml", "750ml"),
            colors = listOf("Coastal Sage", "Obsidian Black", "Warm Cream"),
            colorHexes = listOf(Color(0xFF6B8672), Color(0xFF222222), Color(0xFFEFE8DA))
        )
    )

    // Dynamic Database Operations
    val cartItems: Flow<List<CartItemEntity>> = shopDao.getCartItems()
    val favorites: Flow<List<FavoriteEntity>> = shopDao.getFavorites()
    val orders: Flow<List<OrderEntity>> = shopDao.getOrders()
    val profile: Flow<UserProfileEntity?> = shopDao.getProfile()

    // Mock Reviews provider
    fun getProductReviews(productId: String): List<ProductReview> {
        return listOf(
            ProductReview("Jessica K.", 5, "Remarkable build. Fits flawlessly into my routine. Exceeds standard expectations!", "2 weeks ago"),
            ProductReview("David L.", 4, "Truly beautiful design ethos. Materials look and feel incredibly high quality. Highly recommended.", "3 days ago"),
            ProductReview("Michael S.", 5, "Pure craftsmanship! Shipping was clean, and support resolved sizing inquiries instantly.", "Yesterday")
        )
    }

    // Cart Handlers
    // --- Firestore Sync Engine ---
    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: java.lang.Exception) {
            Log.e("ShopRepository", "Firestore not initialized. Ensure google-services.json is valid or Firebase is initialized.", e)
            null
        }
    }

    private fun syncToFirestore(collectionPath: String, documentId: String, data: Any) {
        try {
            val db = firestore ?: return
            db.collection(collectionPath)
                .document(documentId)
                .set(data, SetOptions.merge())
                .addOnSuccessListener {
                    Log.d("ShopRepository", "Synced $documentId successfully to $collectionPath")
                }
                .addOnFailureListener { e ->
                    Log.e("ShopRepository", "Failed to sync $documentId to $collectionPath", e)
                }
        } catch (e: Exception) {
            Log.e("ShopRepository", "Error syncing to Firestore: ${e.message}")
        }
    }

    private suspend fun getUserEmail(): String {
        return try {
            val p = shopDao.getProfile().map { it }.firstOrNull()
            p?.email?.replace(".", "_")?.ifBlank { "anonymous_user" } ?: "anonymous_user"
        } catch (e: Exception) {
            "anonymous_user"
        }
    }

    private suspend fun syncCartProgress() {
        try {
            val email = getUserEmail()
            val list = shopDao.getCartItems().map { it }.firstOrNull() ?: emptyList()
            val data = mapOf(
                "items" to list.map { item ->
                    mapOf(
                        "productId" to item.productId,
                        "title" to item.title,
                        "price" to item.price,
                        "originalPrice" to item.originalPrice,
                        "category" to item.category,
                        "imageIndex" to item.imageIndex,
                        "selectedSize" to item.selectedSize,
                        "selectedColor" to item.selectedColor,
                        "quantity" to item.quantity
                    )
                },
                "lastUpdated" to System.currentTimeMillis()
            )
            syncToFirestore("users/$email/progress", "cart", data)
        } catch (e: Exception) {
            Log.e("ShopRepository", "syncCartProgress error", e)
        }
    }

    private suspend fun syncFavoritesProgress() {
        try {
            val email = getUserEmail()
            val list = shopDao.getFavorites().map { it }.firstOrNull() ?: emptyList()
            val data = mapOf(
                "productIds" to list.map { it.productId },
                "lastUpdated" to System.currentTimeMillis()
            )
            syncToFirestore("users/$email/progress", "favorites", data)
        } catch (e: Exception) {
            Log.e("ShopRepository", "syncFavoritesProgress error", e)
        }
    }

    private suspend fun syncOrdersProgress() {
        try {
            val email = getUserEmail()
            val list = shopDao.getOrders().map { it }.firstOrNull() ?: emptyList()
            val data = mapOf(
                "orders" to list.map { order ->
                    mapOf(
                        "orderNumber" to order.orderNumber,
                        "timestamp" to order.timestamp,
                        "totalAmount" to order.totalAmount,
                        "itemsCount" to order.itemsCount,
                        "status" to order.status,
                        "itemsSummary" to order.itemsSummary
                    )
                },
                "lastUpdated" to System.currentTimeMillis()
            )
            syncToFirestore("users/$email/progress", "orders", data)
        } catch (e: Exception) {
            Log.e("ShopRepository", "syncOrdersProgress error", e)
        }
    }

    private suspend fun syncProfileProgress(profile: UserProfileEntity) {
        try {
            val email = profile.email.replace(".", "_").ifBlank { "anonymous_user" }
            val data = mapOf(
                "name" to profile.name,
                "email" to profile.email,
                "phone" to profile.phone,
                "addressLine" to profile.addressLine,
                "city" to profile.city,
                "state" to profile.state,
                "postalCode" to profile.postalCode,
                "walletBalance" to profile.walletBalance,
                "lastUpdated" to System.currentTimeMillis()
            )
            syncToFirestore("users/$email/progress", "profile", data)
        } catch (e: Exception) {
            Log.e("ShopRepository", "syncProfileProgress error", e)
        }
    }

    // Cart Handlers
    suspend fun addToCart(item: CartItemEntity) {
        shopDao.insertCartItem(item)
        syncCartProgress()
    }

    suspend fun updateCartQuantity(item: CartItemEntity) {
        if (item.quantity <= 0) {
            shopDao.deleteCartItem(item)
        } else {
            shopDao.insertCartItem(item)
        }
        syncCartProgress()
    }

    suspend fun removeFromCart(item: CartItemEntity) {
        shopDao.deleteCartItem(item)
        syncCartProgress()
    }

    suspend fun removeFromCartById(productId: String) {
        shopDao.deleteCartItemById(productId)
        syncCartProgress()
    }

    suspend fun clearCart() {
        shopDao.clearCart()
        syncCartProgress()
    }

    // Favorites Handlers
    suspend fun toggleFavorite(productId: String, isFav: Boolean) {
        if (isFav) {
            shopDao.deleteFavorite(productId)
        } else {
            shopDao.insertFavorite(FavoriteEntity(productId))
        }
        syncFavoritesProgress()
    }

    fun isFavoriteItem(productId: String): Flow<Boolean> = shopDao.isFavorite(productId)

    // Order Handlers
    suspend fun placeOrder(orderNumber: String, totalAmount: Double, itemsCount: Int, summary: String) {
        val order = OrderEntity(
            orderNumber = orderNumber,
            timestamp = System.currentTimeMillis(),
            totalAmount = totalAmount,
            itemsCount = itemsCount,
            status = "Processing",
            itemsSummary = summary
        )
        shopDao.insertOrder(order)
        shopDao.clearCart()
        syncOrdersProgress()
        syncCartProgress()
    }

    // Profile Handlers
    suspend fun saveProfile(profileEntity: UserProfileEntity) {
        shopDao.saveProfile(profileEntity)
        syncProfileProgress(profileEntity)
    }

    suspend fun updateWalletBalance(newBalance: Double) {
        shopDao.updateWalletBalance(newBalance)
        val p = shopDao.getProfile().map { it }.firstOrNull() ?: shopDao.getProfile().map { it }.firstOrNull()
        if (p != null) {
            syncProfileProgress(p.copy(walletBalance = newBalance))
        }
    }

    suspend fun ensureDefaultProfile() {
        // Run safely to pre-load Shahid's user profile if empty
        val currentProfile = shopDao.getProfile().map { it }.firstOrNull() ?: shopDao.getProfile().map { it }.firstOrNull()
        if (currentProfile == null) {
            val defaultProfile = UserProfileEntity(
                name = "Shahid Hussain",
                email = "hussainmdshahid763@gmail.com",
                phone = "+1 (555) 763-0892",
                addressLine = "763 Maple Boulevard, Suite 10",
                city = "Austin",
                state = "Texas",
                postalCode = "78701",
                walletBalance = 380.00 // Rich default budget for initial purchases
            )
            shopDao.saveProfile(defaultProfile)
            syncProfileProgress(defaultProfile)
        }
    }
}
