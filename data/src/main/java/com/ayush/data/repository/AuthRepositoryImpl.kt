package com.ayush.data.repository

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.ayush.data.SignInMutation
import com.ayush.data.SignUpMutation
import com.ayush.data.datastore.UserPreferences
import com.ayush.data.datastore.UserSettings
import com.ayush.data.datastore.toUser
import com.ayush.data.datastore.toUserSettings
import com.ayush.domain.model.AuthResponse
import com.ayush.domain.model.AuthResponseData
import com.ayush.domain.model.Result
import com.ayush.domain.model.User
import com.ayush.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apolloClient: ApolloClient,
    private val userPreferences: UserPreferences
) : AuthRepository {
    override suspend fun signUp(name: String, email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apolloClient.mutation(
                SignUpMutation(name = name, email = email, password = password)
            ).execute()

            when {
                response.hasErrors() -> {
                    Result.error(
                        Exception(
                            response.errors?.firstOrNull()?.message ?: "Unknown error"
                        )
                    )
                }

                response.data?.signUp?.data != null -> {
                    val authResponse = AuthResponse(
                        data = response.data?.signUp?.data?.let {
                            AuthResponseData(
                                id = it.id,
                                name = it.name,
                                email = it.email,
                                token = it.token,
                                created = it.created,
                                updated = it.updated,
                                cartId = it.cartId
                            )
                        },
                        errorMessage = null
                    )
                    Log.d("AuthRepositoryImpl", "Sign up response: ${authResponse.data?.cartId}")

                    authResponse.data?.let { authData ->
                        userPreferences.setUserData(authData.toUserSettings())
                    }

                    Result.success(authResponse)
                }

                else -> Result.error(
                    Exception(
                        response.data?.signUp?.errorMessage ?: "Unknown error"
                    )
                )
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = apolloClient.mutation(
                SignInMutation(email = email, password = password)
            ).execute()

            when {
                response.hasErrors() -> {
                    Result.error(
                        Exception(
                            response.errors?.firstOrNull()?.message ?: "Unknown error"
                        )
                    )
                }

                response.data?.signIn?.data != null -> {
                    val authResponse = AuthResponse(
                        data = response.data?.signIn?.data?.let {
                            AuthResponseData(
                                id = it.id,
                                name = it.name,
                                email = it.email,
                                token = it.token,
                                created = it.created,
                                updated = it.updated,
                                cartId = it.cartId
                            )
                        },
                        errorMessage = null
                    )

                    authResponse.data?.let { authData ->
                        userPreferences.setUserData(authData.toUserSettings())
                    }

                    Result.success(authResponse)
                }

                else -> Result.error(
                    Exception(
                        response.data?.signIn?.errorMessage ?: "Unknown error"
                    )
                )
            }
        } catch (e: Exception) {
            Result.error(e)
        }
    }

    override suspend fun signOut() {
        // Clear user data from DataStore
        userPreferences.setUserData(UserSettings())
    }

    override suspend fun getUser(): Flow<User?> = flow {
        val userData = userPreferences.getUserData()
        emit(if (userData.token.isNotEmpty()) userData.toUser() else null)
    }
}