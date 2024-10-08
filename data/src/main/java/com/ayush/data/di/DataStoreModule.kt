package com.ayush.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ayush.data.datastore.UserPreferences
import com.ayush.data.datastore.UserPreferencesImpl
import com.ayush.data.datastore.UserSettings
import com.ayush.data.datastore.UserSettingsSerializer
import com.ayush.data.repository.UserManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideUserPreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<UserSettings> {
        return DataStoreFactory.create(
            serializer = UserSettingsSerializer,
            produceFile = { context.dataStoreFile("app_user_settings") }
        )
    }

    @Provides
    @Singleton
    fun provideUserPreferences(
        dataStore: DataStore<UserSettings>
    ): UserPreferences {
        return UserPreferencesImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideUserManager(
        userPreferences: UserPreferences
    ): UserManager {
        return UserManager(userPreferences)
    }
    @Provides
    @Singleton
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}