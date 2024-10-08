package com.ayush.data.di

import com.ayush.data.repository.AuthRepositoryImpl
import com.ayush.data.repository.CartRepositoryImpl
import com.ayush.data.repository.OrderRepositoryImpl
import com.ayush.data.repository.ProductRepositoryImpl
import com.ayush.data.repository.ProfileRepoImpl
import com.ayush.domain.repository.AuthRepository
import com.ayush.domain.repository.CartRepository
import com.ayush.domain.repository.OrderRepository
import com.ayush.domain.repository.ProductRepository
import com.ayush.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository {
        return authRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository {
        return productRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository {
        return cartRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideProfileRepository(profileRepoImpl: ProfileRepoImpl):
            ProfileRepository {
        return profileRepoImpl
    }
    @Provides
    @Singleton
    fun provideOrderRepository(orderRepositoryImpl: OrderRepositoryImpl):
            OrderRepository {
        return orderRepositoryImpl
    }
}
