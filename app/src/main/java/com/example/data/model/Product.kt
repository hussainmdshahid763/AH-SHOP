package com.example.data.model

import androidx.compose.ui.graphics.Color

data class Product(
    val id: String,
    val title: String,
    val price: Double,
    val originalPrice: Double,
    val rating: Double,
    val reviewsCount: Int,
    val category: String,
    val imageIndex: Int, // Refers to custom local visual drawing index
    val description: String,
    val sizes: List<String> = emptyList(),
    val colors: List<String> = emptyList(),
    val isFeatured: Boolean = false,
    val isNewArrival: Boolean = false,
    val colorHexes: List<Color> = emptyList()
)

data class ProductReview(
    val author: String,
    val rating: Int,
    val comment: String,
    val date: String
)
