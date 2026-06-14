package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// --- Room Entities ---

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: String,
    val title: String,
    val price: Double,
    val originalPrice: Double,
    val category: String,
    val imageIndex: Int,
    val selectedSize: String,
    val selectedColor: String,
    val quantity: Int
)

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val productId: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val orderNumber: String,
    val timestamp: Long,
    val totalAmount: Double,
    val itemsCount: Int,
    val status: String, // e.g. "Processing", "Shipped", "Delivered"
    val itemsSummary: String // e.g., "Nordic Wool Coat (1), Active Pro Watch (2)"
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1, // Only 1 active profile locally
    val name: String,
    val email: String,
    val phone: String,
    val addressLine: String,
    val city: String,
    val state: String,
    val postalCode: String,
    val walletBalance: Double = 500.00 // Default credit balance for standard users as pre-loaded
)

// --- DAOs ---

@Dao
interface ShopDao {
    // Cart operations
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity)

    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    @Delete
    suspend fun deleteCartItem(item: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun deleteCartItemById(productId: String)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    // Favorites operations
    @Query("SELECT * FROM favorites")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE productId = :productId)")
    fun isFavorite(productId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE productId = :productId")
    suspend fun deleteFavorite(productId: String)

    // Order History operations
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    // Profile operations
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: UserProfileEntity)

    @Query("UPDATE user_profile SET walletBalance = :newBalance WHERE id = 1")
    suspend fun updateWalletBalance(newBalance: Double)
}

// --- App Database ---

@Database(
    entities = [CartItemEntity::class, FavoriteEntity::class, OrderEntity::class, UserProfileEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ShopDatabase : RoomDatabase() {
    abstract fun shopDao(): ShopDao
}
