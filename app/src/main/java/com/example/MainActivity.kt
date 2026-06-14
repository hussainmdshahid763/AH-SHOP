package com.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.example.data.local.ShopDatabase
import com.example.data.repository.ShopRepository
import com.example.ui.screens.MainShopLayout
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ShopViewModel

class MainActivity : ComponentActivity() {
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            ShopDatabase::class.java,
            "ah_shop_database"
        )
        .fallbackToDestructiveMigration() // safe for rapid iteration if database layout shifts
        .build()
    }

    private val repository by lazy {
        ShopRepository(database.shopDao())
    }

    private val viewModel by lazy {
        ShopViewModel(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            if (FirebaseApp.getApps(applicationContext).isEmpty()) {
                FirebaseApp.initializeApp(applicationContext)
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Firebase manual fallback initialization error", e)
        }
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainShopLayout(viewModel = viewModel)
            }
        }
    }
}

