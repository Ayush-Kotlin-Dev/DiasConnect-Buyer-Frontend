package com.ayush.data.repository

import com.apollographql.apollo.ApolloClient
import com.ayush.GetProductsQuery
import com.ayush.domain.repository.product.Product
import com.ayush.domain.repository.product.ProductRepository
import javax.inject.Inject

//TODO  apollo coroutines
class ProductRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : ProductRepository {
    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = apolloClient.query(GetProductsQuery()).execute()
            if (response.hasErrors()) {
                Result.failure(Exception(response.errors?.first()?.message))
            } else {
                val products = response.data?.products?.map { it.toProduct() } ?: emptyList()
                Result.success(products)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun GetProductsQuery.Product.toProduct(): Product {
        return Product(
            id = this.id,
            name = this.name,
            description = this.description,
            price = this.price,
            stock = this.stock,
            images = this.images,
            categoryId = this.categoryId,
            sellerId = this.sellerId,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}