package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.CartItemEntity
import com.example.data.local.OrderEntity
import com.example.data.model.Product
import com.example.ui.components.ProductVisualDrawing
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.ShopViewModel
import com.example.ui.viewmodel.SortBy

// Custom Nordic Theme colors
val NordicMidnightIndigo = Color(0xFF1E2E5D)
val AccentWarmSand = Color(0xFFD49E35)
val NordicOffWhite = Color(0xFFF5F7FA)
val CleanDark = Color(0xFF171A21)
val AccentGreen = Color(0xFF47A06F)

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainShopLayout(viewModel: ShopViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val totalCartCount = cartItems.sumOf { it.quantity }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar(currentScreen)) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier.testTag("app_navigation_bar")
                ) {
                    val isHome = currentScreen == Screen.Main || currentScreen is Screen.ProductDetail
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Shop") },
                        selected = isHome,
                        onClick = { viewModel.navigateTo(Screen.Main) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NordicMidnightIndigo,
                            selectedTextColor = NordicMidnightIndigo,
                            indicatorColor = NordicMidnightIndigo.copy(alpha = 0.12f)
                        )
                    )

                    val isFavs = currentScreen == Screen.Favorites
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                        label = { Text("Favorites") },
                        selected = isFavs,
                        onClick = { viewModel.navigateTo(Screen.Favorites) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NordicMidnightIndigo,
                            selectedTextColor = NordicMidnightIndigo,
                            indicatorColor = NordicMidnightIndigo.copy(alpha = 0.12f)
                        )
                    )

                    val isCart = currentScreen == Screen.Cart || currentScreen == Screen.Checkout || currentScreen is Screen.OrderSuccess
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (totalCartCount > 0) {
                                        Badge(containerColor = AccentWarmSand) {
                                            Text(
                                                text = totalCartCount.toString(),
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                            }
                        },
                        label = { Text("Cart") },
                        selected = isCart,
                        onClick = { viewModel.navigateTo(Screen.Cart) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NordicMidnightIndigo,
                            selectedTextColor = NordicMidnightIndigo,
                            indicatorColor = NordicMidnightIndigo.copy(alpha = 0.12f)
                        )
                    )

                    val isOrders = currentScreen == Screen.Orders
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.History, contentDescription = "Orders") },
                        label = { Text("History") },
                        selected = isOrders,
                        onClick = { viewModel.navigateTo(Screen.Orders) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NordicMidnightIndigo,
                            selectedTextColor = NordicMidnightIndigo,
                            indicatorColor = NordicMidnightIndigo.copy(alpha = 0.12f)
                        )
                    )

                    val isProfile = currentScreen == Screen.Profile
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile") },
                        selected = isProfile,
                        onClick = { viewModel.navigateTo(Screen.Profile) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = NordicMidnightIndigo,
                            selectedTextColor = NordicMidnightIndigo,
                            indicatorColor = NordicMidnightIndigo.copy(alpha = 0.12f)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(NordicOffWhite)
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentScreen,
                transitionSpec = {
                    slideInHorizontally { width -> width } + fadeIn() with slideOutHorizontally { width -> -width } + fadeOut()
                },
                label = "ScreenTransition"
            ) { targetState ->
                when (targetState) {
                    is Screen.Main -> MainDashboardScreen(viewModel)
                    is Screen.ProductDetail -> ProductDetailScreen(viewModel, targetState.productId)
                    is Screen.Cart -> CartScreen(viewModel)
                    is Screen.Favorites -> FavoritesScreen(viewModel)
                    is Screen.Checkout -> CheckoutAndPaymentScreen(viewModel)
                    is Screen.Orders -> OrderHistoryScreen(viewModel)
                    is Screen.Profile -> ProfileScreen(viewModel)
                    is Screen.OrderSuccess -> OrderSuccessScreen(viewModel, targetState.orderNumber, targetState.totalAmount, targetState.itemsSummary)
                }
            }
        }
    }
}

private fun shouldShowBottomBar(screen: Screen): Boolean {
    return when (screen) {
        is Screen.Checkout -> false
        is Screen.OrderSuccess -> false
        else -> true
    }
}

// --- SCREEN 1: MAIN DASHBOARD ---

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainDashboardScreen(viewModel: ShopViewModel) {
    val products by viewModel.filteredProducts.collectAsStateWithLifecycle()
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val activeCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val activeSort by viewModel.sortBy.collectAsStateWithLifecycle()
    val activePriceRange by viewModel.priceRange.collectAsStateWithLifecycle()

    var showFiltersSetup by remember { mutableStateOf(false) }

    val categories = listOf("All", "Fashion", "Electronics", "Shoes", "Accessories", "Living")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "AH SHOP",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = NordicMidnightIndigo,
                        letterSpacing = (-1).sp
                    )
                    Text(
                        text = "Minimal design, premium quality",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
                IconButton(
                    onClick = { showFiltersSetup = !showFiltersSetup },
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color.LightGray.copy(alpha = 0.5f), CircleShape)
                        .testTag("filter_toggle_button")
                ) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "Toggle Filters",
                        tint = if (showFiltersSetup) AccentWarmSand else NordicMidnightIndigo
                    )
                }
            }
        }

        // Search Bar & Hot Deal Promo
        item {
            TextField(
                value = query,
                onValueChange = { viewModel.searchQuery.value = it },
                placeholder = { Text("Search products, premium garments...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.searchQuery.value = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_input_field")
            )
        }

        // Conditional Filter Configuration Panel
        if (showFiltersSetup) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Filter & Sort Options",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = NordicMidnightIndigo,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Sorting
                        Text(text = "Sort By", fontSize = 12.sp, color = Color.Gray)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SortBy.values().forEach { option ->
                                val selected = activeSort == option
                                FilterChip(
                                    selected = selected,
                                    onClick = { viewModel.sortBy.value = option },
                                    label = {
                                        Text(
                                            text = when (option) {
                                                SortBy.RECOMMENDED -> "Recommended"
                                                SortBy.PRICE_LOW_HIGH -> "Price: Low to High"
                                                SortBy.PRICE_HIGH_LOW -> "Price: High to Low"
                                                SortBy.RATING -> "Top Rated"
                                            },
                                            fontSize = 11.sp
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = NordicMidnightIndigo.copy(alpha = 0.1f),
                                        selectedLabelColor = NordicMidnightIndigo
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Price Slider
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Price Cap Range", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                text = "$${activePriceRange.start.toInt()} - $${activePriceRange.endInclusive.toInt()}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = NordicMidnightIndigo
                            )
                        }
                        RangeSlider(
                            value = activePriceRange.start..activePriceRange.endInclusive,
                            onValueChange = { viewModel.priceRange.value = it },
                            valueRange = 0f..300f,
                            colors = SliderDefaults.colors(
                                activeTrackColor = NordicMidnightIndigo,
                                thumbColor = AccentWarmSand
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Horizontal visual Banner Carousel
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = NordicMidnightIndigo)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1.3f)) {
                        Box(
                            modifier = Modifier
                                .background(AccentWarmSand, RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "SUMMER BONUS",
                                color = Color.White,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Save $50 flat on spendings over $150",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 19.sp
                        )
                        Text(
                            text = "Use code: WELCOME50",
                            color = AccentWarmSand,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(0.7f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalMall,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.15f),
                            modifier = Modifier.size(90.dp)
                        )
                    }
                }
            }
        }

        // Custom Categories Horizontal list
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = activeCategory == category
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NordicMidnightIndigo else Color.White)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Color.Transparent else Color.LightGray.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { viewModel.selectedCategory.value = category }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                            .testTag("category_pill_${category.lowercase()}")
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else Color.DarkGray,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        // Hot Spot: Standard grids of e-commerce items
        if (products.isEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "No products fit those parameters",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Try adjusting tags or pricing slider",
                        color = Color.LightGray,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // LazyVerticalGrid inside LazyColumn workarounds or raw Chunk lists
            val chunks = products.chunked(2)
            items(chunks) { rowOfTwo ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowOfTwo.forEach { product ->
                        Box(modifier = Modifier.weight(1f)) {
                            ProductItemCard(product = product, onClick = {
                                viewModel.navigateTo(Screen.ProductDetail(product.id))
                            }, onFavoriteClick = {
                                viewModel.toggleFavorite(product.id)
                            }, isFav = viewModel.favorites.collectAsStateWithLifecycle().value.any { it.productId == product.id })
                        }
                    }
                    if (rowOfTwo.size == 1) {
                        Box(modifier = Modifier.weight(1f)) // balances out
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun ProductItemCard(
    product: Product,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    isFav: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .testTag("product_card_${product.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                // Vector Canvas drawing of products
                ProductVisualDrawing(
                    index = product.imageIndex,
                    category = product.category,
                    modifier = Modifier.fillMaxSize()
                )

                // Favorite round button overlay
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.White.copy(alpha = 0.9f), CircleShape)
                        .size(34.dp)
                ) {
                    Icon(
                        imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite Toggle",
                        tint = if (isFav) Color.Red else Color.LightGray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                // Title
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = NordicMidnightIndigo
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Ratings row
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = AccentWarmSand, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = product.rating.toString(), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "(${product.reviewsCount})", fontSize = 10.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Price tag lines
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "$${product.price}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = NordicMidnightIndigo
                        )
                        Text(
                            text = "$${product.originalPrice}",
                            fontSize = 11.sp,
                            color = Color.LightGray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(NordicMidnightIndigo, RoundedCornerShape(10.dp))
                            .padding(6.dp)
                    ) {
                        Icon(
                            Icons.Default.AddShoppingCart,
                            contentDescription = "Quick add",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// --- SCREEN 2: PRODUCT DETAIL ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(viewModel: ShopViewModel, productId: String) {
    val product = viewModel.getProductById(productId) ?: return

    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val isFav = favorites.any { it.productId == product.id }

    var selectedSize by remember { mutableStateOf(product.sizes.firstOrNull() ?: "One Size") }
    var selectedColor by remember { mutableStateOf(product.colors.firstOrNull() ?: "Default") }
    var qtyCount by remember { mutableStateOf(1) }

    val reviews = viewModel.getProductReviews(product.id)

    Column(modifier = Modifier.fillMaxSize()) {
        // Overlay Header
        TopAppBar(
            title = { Text("Product Details", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            navigationIcon = {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { viewModel.toggleFavorite(product.id) }) {
                    Icon(
                        imageVector = if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Fav",
                        tint = if (isFav) Color.Red else NordicMidnightIndigo
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(Color.White)
        ) {
            // Detailed Product Drawing Canvas
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .background(NordicOffWhite)
                        .padding(16.dp)
                ) {
                    ProductVisualDrawing(
                        index = product.imageIndex,
                        category = product.category,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Specs
            item {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = product.category.uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = AccentWarmSand,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = product.title,
                        fontWeight = FontWeight.Black,
                        fontSize = 24.sp,
                        color = NordicMidnightIndigo
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Price & Rating specs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "$${product.price}",
                                fontWeight = FontWeight.Black,
                                fontSize = 24.sp,
                                color = NordicMidnightIndigo
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$${product.originalPrice}",
                                fontSize = 16.sp,
                                textDecoration = TextDecoration.LineThrough,
                                color = Color.LightGray
                            )
                        }

                        // Star badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(NordicOffWhite, RoundedCornerShape(10.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Star, contentDescription = null, tint = AccentWarmSand, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = product.rating.toString(), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "(${product.reviewsCount} reviews)", fontSize = 11.sp, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color.LightGray.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Description text lines
                    Text(text = "Description", fontWeight = FontWeight.Bold, color = NordicMidnightIndigo, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = product.description,
                        color = Color.DarkGray,
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Colors Selector
                    if (product.colors.isNotEmpty()) {
                        Text(text = "Select Color", fontWeight = FontWeight.Bold, color = NordicMidnightIndigo, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            product.colors.forEachIndexed { i, colorName ->
                                val rgb = product.colorHexes.getOrNull(i) ?: Color.DarkGray
                                val isSelected = selectedColor == colorName
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(rgb)
                                        .border(
                                            width = if (isSelected) 3.dp else 1.dp,
                                            color = if (isSelected) AccentWarmSand else Color.LightGray.copy(alpha = 0.4f),
                                            shape = CircleShape
                                        )
                                        .clickable { selectedColor = colorName }
                                        .testTag("color_option_$colorName")
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Sizes Selector
                    if (product.sizes.isNotEmpty() && product.sizes.first() != "One Size") {
                        Text(text = "Select Size", fontWeight = FontWeight.Bold, color = NordicMidnightIndigo, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            product.sizes.forEach { sizeOption ->
                                val isSelected = selectedSize == sizeOption
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) NordicMidnightIndigo else NordicOffWhite)
                                        .border(
                                            width = 1.dp,
                                            color = if (isSelected) Color.Transparent else Color.LightGray.copy(alpha = 0.5f),
                                            shape = RoundedCornerShape(10.dp)
                                        )
                                        .clickable { selectedSize = sizeOption }
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .testTag("size_option_$sizeOption")
                                ) {
                                    Text(
                                        text = sizeOption,
                                        color = if (isSelected) Color.White else Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Reviews Panel Accordion
                    Text(text = "Customer Feedback", fontWeight = FontWeight.Bold, color = NordicMidnightIndigo, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    reviews.forEach { r ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(NordicOffWhite, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                                .padding(bottom = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = r.author, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NordicMidnightIndigo)
                                Text(text = r.date, fontSize = 10.sp, color = Color.Gray)
                            }
                            Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                repeat(5) { starIndex ->
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (starIndex < r.rating) AccentWarmSand else Color.LightGray,
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            }
                            Text(text = r.comment, fontSize = 12.sp, color = Color.DarkGray, lineHeight = 16.sp)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // Floating Bottom Actions
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Quantity selectors
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .background(NordicOffWhite, RoundedCornerShape(14.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    IconButton(
                        onClick = { if (qtyCount > 1) qtyCount-- },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Decrement")
                    }
                    Text(
                        text = qtyCount.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    IconButton(
                        onClick = { qtyCount++ },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Increment")
                    }
                }

                Button(
                    onClick = {
                        viewModel.addToCart(product, selectedSize, selectedColor, qtyCount)
                        viewModel.navigateTo(Screen.Cart)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                        .height(50.dp)
                        .testTag("add_to_cart_detail_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NordicMidnightIndigo)
                ) {
                    Icon(Icons.Default.ShoppingBag, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Add to Cart • $${String.format("%.2f", product.price * qtyCount)}", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// --- SCREEN 3: INTERACTIVE CART ---

@Composable
fun CartScreen(viewModel: ShopViewModel) {
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val rawVoucher by viewModel.activeVoucher.collectAsStateWithLifecycle()
    val voucherErr by viewModel.voucherError.collectAsStateWithLifecycle()

    val subtotal = cartItems.sumOf { it.price * it.quantity }
    val discount = rawVoucher?.applyDiscount?.invoke(subtotal) ?: 0.0
    val shipping = if (subtotal > 0.0 && rawVoucher?.code != "SHIPFREE") 10.0 else 0.0
    val tax = subtotal * 0.08
    val grandTotal = subtotal - discount + shipping + tax

    var couponInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Text(
                text = "Shopping Cart",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = NordicMidnightIndigo,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            if (cartItems.isNotEmpty()) {
                Text(
                    text = "Clear All",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable { viewModel.clearCart() }
                )
            }
        }

        if (cartItems.isEmpty()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.RemoveShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Your shopping bag is clean", fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(text = "Add some pieces from the store catalog!", fontSize = 12.sp, color = Color.LightGray)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { viewModel.navigateTo(Screen.Main) },
                    colors = ButtonDefaults.buttonColors(containerColor = NordicMidnightIndigo),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Explore Store")
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }

                items(cartItems) { item ->
                    CartItemRow(item = item, viewModel = viewModel)
                }

                // Coupon box setup
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .padding(14.dp)
                    ) {
                        Text(text = "Promo Discount Offers", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = NordicMidnightIndigo)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                "WELCOME50",
                                color = AccentWarmSand,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .background(NordicOffWhite, RoundedCornerShape(6.dp))
                                    .clickable {
                                        couponInput = "WELCOME50"
                                        viewModel.applyVoucherCode("WELCOME50")
                                    }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                            Text(
                                "AHSHOP10",
                                color = AccentWarmSand,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .background(NordicOffWhite, RoundedCornerShape(6.dp))
                                    .clickable {
                                        couponInput = "AHSHOP10"
                                        viewModel.applyVoucherCode("AHSHOP10")
                                    }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                            Text(
                                "SHIPFREE",
                                color = AccentWarmSand,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .background(NordicOffWhite, RoundedCornerShape(6.dp))
                                    .clickable {
                                        couponInput = "SHIPFREE"
                                        viewModel.applyVoucherCode("SHIPFREE")
                                    }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextField(
                                value = couponInput,
                                onValueChange = { couponInput = it },
                                placeholder = { Text("Code: WELCOME50", fontSize = 12.sp) },
                                shape = RoundedCornerShape(10.dp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = NordicOffWhite,
                                    unfocusedContainerColor = NordicOffWhite,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                maxLines = 1,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("voucher_input_field")
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    focusManager.clearFocus()
                                    viewModel.applyVoucherCode(couponInput)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NordicMidnightIndigo)
                            ) {
                                Text("Apply")
                            }
                        }

                        // Code details responses
                        if (rawVoucher != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Code applied: ${rawVoucher!!.code} (-$${String.format("%.2f", discount)})",
                                    color = AccentGreen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Remove",
                                    color = Color.Red,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        viewModel.removeVoucher()
                                        couponInput = ""
                                    }
                                )
                            }
                        } else if (voucherErr != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = voucherErr!!, color = Color.Red, fontSize = 11.sp)
                        }
                    }
                }

                // Pricing receipt invoice breakdown
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Shopping Invoice",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = NordicMidnightIndigo,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Subtotal Items", color = Color.Gray, fontSize = 13.sp)
                            Text("$${String.format("%.2f", subtotal)}", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        if (discount > 0.0) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Voucher Savings", color = AccentGreen, fontSize = 13.sp)
                                Text("-$${String.format("%.2f", discount)}", color = AccentGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Local State Taxes (8%)", color = Color.Gray, fontSize = 13.sp)
                            Text("$${String.format("%.2f", tax)}", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Premium Delivery", color = Color.Gray, fontSize = 13.sp)
                            Text(
                                text = if (shipping == 0.0) "FREE" else "$${String.format("%.2f", shipping)}",
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.sp,
                                color = if (shipping == 0.0) AccentGreen else Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Divider(color = Color.LightGray.copy(alpha = 0.4f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Outlays", fontWeight = FontWeight.Black, fontSize = 16.sp, color = NordicMidnightIndigo)
                            Text(
                                "$${String.format("%.2f", grandTotal)}",
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = NordicMidnightIndigo
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }

            // Checkout CTA
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(16.dp)
            ) {
                Button(
                    onClick = { viewModel.navigateTo(Screen.Checkout) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(52.dp)
                        .testTag("checkout_trigger_button"),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NordicMidnightIndigo)
                ) {
                    Text(text = "Proceed to Checkout ($${String.format("%.2f", grandTotal)})", fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItemEntity, viewModel: ShopViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(10.dp))
            ) {
                ProductVisualDrawing(index = item.imageIndex, category = item.category, modifier = Modifier.fillMaxSize())
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = NordicMidnightIndigo
                )
                Text(
                    text = "${item.selectedSize} • ${item.selectedColor}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
                Text(
                    text = "$${item.price}",
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = NordicMidnightIndigo,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Quantity buttons steppers
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.background(NordicOffWhite, RoundedCornerShape(8.dp))
            ) {
                IconButton(
                    onClick = { viewModel.updateCartQty(item, isAddition = false) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(14.dp))
                }
                Text(
                    text = item.quantity.toString(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
                IconButton(
                    onClick = { viewModel.updateCartQty(item, isAddition = true) },
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { viewModel.removeFromCart(item) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.8f))
            }
        }
    }
}

// --- SCREEN 4: FAVORITES SCREEN ---

@Composable
fun FavoritesScreen(viewModel: ShopViewModel) {
    val favorites by viewModel.favorites.collectAsStateWithLifecycle()
    val products = viewModel.filteredProducts.collectAsStateWithLifecycle().value

    val favList = products.filter { p -> favorites.any { f -> f.productId == p.id } }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "My Wishlist",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = NordicMidnightIndigo
            )
        }

        if (favList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Your wishlist is empty", fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(
                    text = "Save items to monitor prices and easily order them",
                    fontSize = 12.sp,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item { Spacer(modifier = Modifier.height(10.dp)) }
                items(favList) { product ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.navigateTo(Screen.ProductDetail(product.id)) },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            ) {
                                ProductVisualDrawing(index = product.imageIndex, category = product.category, modifier = Modifier.fillMaxSize())
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = product.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NordicMidnightIndigo)
                                Text(text = "$${product.price}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = AccentWarmSand)
                            }
                            IconButton(onClick = { viewModel.toggleFavorite(product.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Remove", tint = Color.LightGray)
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- SCREEN 5: CHECKOUT SCREEN ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutAndPaymentScreen(viewModel: ShopViewModel) {
    val basket by viewModel.cartItems.collectAsStateWithLifecycle()
    val activeVoucher by viewModel.activeVoucher.collectAsStateWithLifecycle()

    val subtotal = basket.sumOf { it.price * it.quantity }
    val voucherDed = activeVoucher?.applyDiscount?.invoke(subtotal) ?: 0.0
    val shipping = if (subtotal > 0.0 && activeVoucher?.code != "SHIPFREE") 10.0 else 0.0
    val tax = subtotal * 0.08
    val grandTotal = subtotal - voucherDed + shipping + tax

    val profileEntity by viewModel.profile.collectAsStateWithLifecycle()
    val isReady = profileEntity != null

    var activePaymentMode by remember { mutableStateOf("Wallet") }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Checkout Delivery", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            navigationIcon = {
                IconButton(onClick = { viewModel.navigateBack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }

            // Part 1: Shipping details
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Delivery Location Address", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NordicMidnightIndigo)
                            Text(
                                "Modify",
                                color = AccentWarmSand,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.clickable { viewModel.navigateTo(Screen.Profile) }
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        if (isReady) {
                            val prof = profileEntity!!
                            Text(text = prof.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = prof.phone, fontSize = 12.sp, color = Color.Gray)
                            Text(text = "${prof.addressLine}, ${prof.city}", fontSize = 12.sp, color = Color.Gray)
                            Text(text = "${prof.state} - ${prof.postalCode}", fontSize = 12.sp, color = Color.Gray)
                        } else {
                            Text(text = "Loading delivery configurations...", fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }

            // Part 2: Interactive Payment selections
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Choose Payment Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NordicMidnightIndigo)
                        Spacer(modifier = Modifier.height(12.dp))

                        val modes = listOf(
                            Triple("Wallet", "Premium Account Balance", Icons.Default.Wallet),
                            Triple("Card", "Visa / Mastercard", Icons.Default.Payment),
                            Triple("Delivery", "Cash on Delivery", Icons.Default.LocalShipping)
                        )

                        modes.forEach { (mode, desc, icon) ->
                            val selected = activePaymentMode == mode
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (selected) NordicOffWhite else Color.Transparent)
                                    .border(
                                        width = if (selected) 2.dp else 1.dp,
                                        color = if (selected) NordicMidnightIndigo else Color.LightGray.copy(alpha = 0.4f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { activePaymentMode = mode }
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(icon, contentDescription = null, tint = if (selected) NordicMidnightIndigo else Color.Gray)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = mode, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = if (selected) NordicMidnightIndigo else Color.Black)
                                    Text(text = desc, fontSize = 11.sp, color = Color.Gray)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .border(2.dp, if (selected) NordicMidnightIndigo else Color.Gray, CircleShape)
                                        .padding(2.dp)
                                ) {
                                    if (selected) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(NordicMidnightIndigo, CircleShape)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        if (activePaymentMode == "Wallet") {
                            val prof = profileEntity
                            val walletAmt = prof?.walletBalance ?: 0.0
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (walletAmt >= grandTotal) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Info,
                                        contentDescription = null,
                                        tint = if (walletAmt >= grandTotal) AccentGreen else Color.Red,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Your Balance: $${String.format("%.2f", walletAmt)}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = if (walletAmt >= grandTotal) AccentGreen else Color.Red
                                    )
                                }

                                if (walletAmt < grandTotal) {
                                    Text(
                                        text = "Will trigger Auto-Topup",
                                        color = Color.Red,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        modifier = Modifier
                                            .background(Color.White, RoundedCornerShape(4.dp))
                                            .padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                } else {
                                    Text(
                                        text = "Funds Ready",
                                        color = AccentGreen,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Part 3: Items rundown summary
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Final Invoice Breakdown", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NordicMidnightIndigo)
                        Spacer(modifier = Modifier.height(8.dp))
                        basket.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${item.quantity}x ${item.title}",
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.width(200.dp),
                                    color = Color.DarkGray
                                )
                                Text(text = "$${String.format("%.2f", item.price * item.quantity)}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 6.dp), color = Color.LightGray.copy(alpha = 0.4f))

                        if (voucherDed > 0.0) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Applied Voucher Savings", color = AccentGreen, fontSize = 12.sp)
                                Text("-$${String.format("%.2f", voucherDed)}", color = AccentGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Delivery & Local Taxes", color = Color.Gray, fontSize = 12.sp)
                            Text("$${String.format("%.2f", shipping + tax)}", color = Color.DarkGray, fontSize = 12.sp)
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Grand Total Due", fontWeight = FontWeight.Black, fontSize = 14.sp, color = NordicMidnightIndigo)
                            Text("$${String.format("%.2f", grandTotal)}", fontWeight = FontWeight.Black, fontSize = 16.sp, color = NordicMidnightIndigo)
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        // Place Order Floating Bar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(16.dp)
        ) {
            Button(
                onClick = { viewModel.executeCheckout() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(52.dp)
                    .testTag("place_order_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NordicMidnightIndigo)
            ) {
                Text(text = "Authorize & Confirm Payment", fontWeight = FontWeight.Black)
            }
        }
    }
}

// --- SCREEN 6: ORDER HISTORY ---

@Composable
fun OrderHistoryScreen(viewModel: ShopViewModel) {
    val orderList by viewModel.orders.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp)
        ) {
            Text(
                text = "My Order Ledger",
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                color = NordicMidnightIndigo
            )
        }

        if (orderList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color.LightGray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "No history records found", fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(text = "Your placed shopping receipts will load here.", fontSize = 12.sp, color = Color.LightGray)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.height(10.dp)) }

                items(orderList) { order ->
                    OrderHistoryItemCard(order)
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}

@Composable
fun OrderHistoryItemCard(order: OrderEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = order.orderNumber, fontWeight = FontWeight.Black, fontSize = 14.sp, color = NordicMidnightIndigo)
                Box(
                    modifier = Modifier
                        .background(AccentGreen.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = order.status.uppercase(),
                        color = AccentGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            val sdf = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
            val dateStr = sdf.format(java.util.Date(order.timestamp))
            Text(text = "Payment verified: $dateStr", fontSize = 11.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.LightGray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Items rundown:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Text(
                text = order.itemsSummary,
                fontSize = 12.sp,
                color = Color.DarkGray,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Total amount paid", fontSize = 11.sp, color = Color.Gray)
                Text(text = "$${String.format("%.2f", order.totalAmount)}", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = NordicMidnightIndigo)
            }
        }
    }
}

// --- SCREEN 7: PROFILE SETTINGS ---

@Composable
fun ProfileScreen(viewModel: ShopViewModel) {
    val rawProfile by viewModel.profile.collectAsStateWithLifecycle()

    var profileName by remember { mutableStateOf("") }
    var profileEmail by remember { mutableStateOf("") }
    var profilePhone by remember { mutableStateOf("") }
    var profileAddress by remember { mutableStateOf("") }
    var profileCity by remember { mutableStateOf("") }
    var profileState by remember { mutableStateOf("") }
    var profilePostalCode by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(rawProfile) {
        rawProfile?.let {
            profileName = it.name
            profileEmail = it.email
            profilePhone = it.phone
            profileAddress = it.addressLine
            profileCity = it.city
            profileState = it.state
            profilePostalCode = it.postalCode
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Profile & Account",
                fontWeight = FontWeight.Black,
                fontSize = 24.sp,
                color = NordicMidnightIndigo
            )
            Text(
                text = "Fulfill local shipping targets dynamically",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Part A: Wallet Card
        item {
            rawProfile?.let { p ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = NordicMidnightIndigo)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "AH WALLET BALANCE", fontSize = 10.sp, color = Color.White.copy(alpha = 0.6f), fontWeight = FontWeight.Medium, letterSpacing = 1.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = "$${String.format("%.2f", p.walletBalance)}", fontSize = 28.sp, fontWeight = FontWeight.Black, color = Color.White)
                            }
                            Icon(Icons.Default.Wallet, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.addWalletFunds(100.0) },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentWarmSand),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(34.dp)
                        ) {
                            Text("Top Up +$100 (Testing Free Funds)", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.White)
                        }
                    }
                }
            }
        }

        // Part B: Inputs Edit Form
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Shipping Specifications", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = NordicMidnightIndigo)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = profileName,
                        onValueChange = { profileName = it },
                        label = { Text("Client Name") },
                        modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = profileEmail,
                        onValueChange = { profileEmail = it },
                        label = { Text("Email Contact") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = profilePhone,
                        onValueChange = { profilePhone = it },
                        label = { Text("Phone Reach") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = profileAddress,
                        onValueChange = { profileAddress = it },
                        label = { Text("Street Address") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = profileCity,
                            onValueChange = { profileCity = it },
                            label = { Text("City") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = profileState,
                            onValueChange = { profileState = it },
                            label = { Text("State") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = profilePostalCode,
                            onValueChange = { profilePostalCode = it },
                            label = { Text("ZIP") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            viewModel.updateProfile(
                                name = profileName,
                                email = profileEmail,
                                phone = profilePhone,
                                address = profileAddress,
                                city = profileCity,
                                state = profileState,
                                code = profilePostalCode
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = NordicMidnightIndigo),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("profile_save_button")
                    ) {
                        Text("Save Delivery Credentials")
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

// --- SCREEN 8: ORDER SUCCESS ---

@Composable
fun OrderSuccessScreen(viewModel: ShopViewModel, orderNumber: String, totalAmount: Double, summary: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Success",
            tint = AccentGreen,
            modifier = Modifier.size(90.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Payment Approved!",
            fontWeight = FontWeight.Black,
            fontSize = 24.sp,
            color = NordicMidnightIndigo
        )
        Text(
            text = "Your order is now being dispatched dynamically.",
            fontSize = 13.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = NordicOffWhite)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = "AH SHOP TRANSACTION SLIP", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color.Gray, letterSpacing = 0.5.sp)
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Order Receipt #", fontSize = 12.sp, color = Color.DarkGray)
                    Text(orderNumber, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = NordicMidnightIndigo)
                }
                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Fulfillment state", fontSize = 12.sp, color = Color.DarkGray)
                    Text("APPROVED PACKING", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = AccentGreen)
                }
                Spacer(modifier = Modifier.height(6.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Items Paid", fontSize = 12.sp, color = Color.DarkGray)
                    Text("Verified", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = Color.LightGray.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(10.dp))

                Text(text = "Fulfillment overview:", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                Text(text = summary, fontSize = 11.sp, color = Color.DarkGray, maxLines = 3, overflow = TextOverflow.Ellipsis)

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Outlaid", fontWeight = FontWeight.Black, fontSize = 13.sp, color = NordicMidnightIndigo)
                    Text("$${String.format("%.2f", totalAmount)}", fontWeight = FontWeight.Black, fontSize = 15.sp, color = NordicMidnightIndigo)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { viewModel.navigateTo(Screen.Main) },
            colors = ButtonDefaults.buttonColors(containerColor = NordicMidnightIndigo),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.width(220.dp).height(46.dp).testTag("routing_success_button")
        ) {
            Text("Back to Store Catalogs")
        }
    }
}
