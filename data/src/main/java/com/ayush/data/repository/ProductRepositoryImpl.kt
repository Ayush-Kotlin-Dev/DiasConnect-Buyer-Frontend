package com.ayush.data.repository

import com.apollographql.apollo.ApolloClient
import com.ayush.data.GetProductByIdQuery
import com.ayush.data.GetProductsByCategoryQuery
import com.ayush.data.GetProductsQuery
import com.ayush.data.SearchProductsQuery

import com.ayush.domain.model.Product
import com.ayush.domain.repository.ProductRepository
import com.ayush.domain.model.Result
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : ProductRepository {
    override suspend fun getProducts(): Result<List<Product>> {
        return try {
            val response = apolloClient.query(GetProductsQuery()).execute()
            when {
                response.hasErrors() -> {
                    Result.error(Exception(response.errors?.first()?.message))
                }
                else -> {
                    val products = response.data?.products?.map { it.toProduct() } ?: emptyList()
                    Result.success(products)
                }
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun getProductsByCategory(categoryId: Long): Result<List<Product>> {
        return try {
            val response = apolloClient.query(GetProductsByCategoryQuery(categoryId)).execute()
            when {
                response.hasErrors() -> {
                    Result.error(Exception(response.errors?.first()?.message))
                }
                else -> {
                    val products = response.data?.productsByCategory?.map { it.toProduct() } ?: emptyList()
                    Result.success(products)
                }
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun getProductById(productId: Long): Result<Product> {
        return try {
            val response = apolloClient.query(GetProductByIdQuery(productId)).execute()
            when {
                response.hasErrors() -> {
                    Result.error(Exception(response.errors?.first()?.message))
                }
                else -> {
                    val product = response.data?.product?.toProduct()
                    if (product != null) {
                        Result.success(product)
                    } else {
                        Result.error(Exception("Product not found"))
                    }
                }
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val response = apolloClient.query(SearchProductsQuery(query)).execute()
            when {
                response.hasErrors() -> {
                    Result.error(Exception(response.errors?.first()?.message))
                }
                else -> {
                    val products = response.data?.searchProducts?.map { it.toProduct() } ?: emptyList()
                    Result.success(products)
                }
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }
    private fun SearchProductsQuery.SearchProduct.toProduct(): Product {
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

    private fun GetProductsByCategoryQuery.ProductsByCategory.toProduct(): Product {
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
    private fun GetProductByIdQuery.Product.toProduct(): Product {
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