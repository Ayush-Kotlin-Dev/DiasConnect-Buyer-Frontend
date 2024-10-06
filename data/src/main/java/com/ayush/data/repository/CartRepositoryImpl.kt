package com.ayush.data.repository

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import com.apollographql.apollo.ApolloClient
import com.ayush.data.AddItemToCartMutation
import com.ayush.data.GetCartByIdQuery
import com.ayush.data.GetProductByIdQuery
import com.ayush.data.RemoveCartItemMutation
import com.ayush.data.UpdateCartItemQuantityMutation
import com.ayush.domain.model.Cart
import com.ayush.domain.model.CartItem
import com.ayush.domain.model.CartStatus
import com.ayush.domain.model.Product
import com.ayush.domain.repository.CartRepository
import javax.inject.Inject
import com.ayush.domain.model.Result

class CartRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
//    private val dataStore: Preferences
): CartRepository {
    override suspend fun getActiveCartByUserId(userId: Long): Result<Cart> {
        TODO("Not yet implemented")
    }

    override suspend fun getCartById(cartId: Long): Result<Cart> {
        return try {
            val response = apolloClient.query(GetCartByIdQuery(cartId)).execute()
            val cartData = response.data?.getCartById

            if (cartData != null) {
                val cartItems = cartData.items.map { item ->
                    CartItem(
                        id = item.id,
                        cartId = cartId,
                        productId = item.productId,
                        quantity = item.quantity,
                        price = item.price.toFloat(),
                        productName = item.productName,
                        productDescription = item.productDescription,
                        createdAt = item.createdAt,
                        updatedAt = item.updatedAt
                    )
                }

                val cart = Cart(
                    id = cartData.id,
                    userId = cartData.userId,
                    items = cartItems,
                    status = CartStatus.valueOf(cartData.status.name),
                    total = cartData.total.toFloat(),
                    currency = cartData.currency,
                    createdAt = cartData.createdAt,
                    updatedAt = cartData.updatedAt,
                    expiresAt = cartData.expiresAt
                )
                Result.success(cart)
            } else {
                Result.error(Exception("Failed to get cart"))
            }
        } catch (e: Exception) {
            Result.error(e)
        }



    }

    override suspend fun createOrGetCart(userId: Long): Result<String> {

        TODO("Not yet implemented")
    }

    override suspend fun addItemToCart(
        cartId: Long,
        productId: Long,
        quantity: Int,
        price: String
    ): Result<Long> {
        return try {
            Log.d("CartRepositoryImpl", "Adding item to cart with cartId: $cartId, productId: $productId, quantity: $quantity, price: $price")
            val response = apolloClient.mutation(AddItemToCartMutation(cartId, price, productId, quantity)).execute()
            val cartItemId = response.data?.addItemToCart
            Log.d("CartRepositoryImpl", "Cart item id: $cartItemId")


            if (cartItemId != null) {
                Log.d("CartRepositoryImpl", "Item added to cart")
                Result.success(cartItemId)
            } else {
                Log.e("CartRepositoryImpl", "Failed to add item to cart")
                Result.error(Exception("Failed to add item to cart"))
            }
        } catch (e: Exception) {
            Log.e("CartRepositoryImpl", "Error adding item to cart", e)
            Result.error(e)
        }
    }

    override suspend fun updateCartItemQuantity(cartItemId: Long, quantity: Int): Result<Boolean> {
        return try {

            val isUpdated = apolloClient.mutation(UpdateCartItemQuantityMutation(cartItemId, quantity)).execute().data?.updateCartItemQuantity
            if (isUpdated == true) {
                Result.success(true)
            } else {
                Result.error(Exception("Failed to update cart item quantity"))
            }

        } catch (e: Exception) {
            Log.e("CartRepositoryImpl", "Error updating cart item quantity", e)
            Result.error(e)
        }

    }

    override suspend fun removeCartItem(cartItemId: Long): Result<Boolean> {
        return try {
            val response = apolloClient.mutation(RemoveCartItemMutation(cartItemId)).execute()
            val isRemoved = response.data?.removeCartItem ?: false
            
            if (isRemoved) {
                Result.success(true)
            } else {
                Result.error(Exception("Failed to remove cart item"))
            }
        } catch (e: Exception) {
            Log.e("CartRepositoryImpl", "Error removing cart item", e)
            Result.error(e)
        }

    }

    override suspend fun clearCart(cartId: Long): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun updateCartStatus(cartId: Long, status: CartStatus): Result<Boolean> {
        TODO("Not yet implemented")
    }
}