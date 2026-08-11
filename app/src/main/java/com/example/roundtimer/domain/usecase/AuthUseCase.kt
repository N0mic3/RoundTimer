package com.example.roundtimer.domain.usecase

import com.example.roundtimer.domain.model.AuthUser
import com.example.roundtimer.domain.repository.AuthRepository
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun getCurrentUser(): AuthUser? = authRepository.getCurrentUser()

    suspend fun signInWithGoogle(
        idToken: String,
    ): AuthUser = authRepository.signInWithGoogle(idToken = idToken)
}