package com.example.roundtimer.domain.repository

import com.example.roundtimer.domain.model.AuthUser

interface AuthRepository {

    fun getCurrentUser(): AuthUser?

    suspend fun signInWithGoogle(
        idToken: String,
    ): AuthUser
}