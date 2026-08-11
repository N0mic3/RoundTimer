package com.example.roundtimer.data.repository

import com.example.roundtimer.domain.model.AuthUser
import com.example.roundtimer.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override fun getCurrentUser(): AuthUser? {
       return firebaseAuth.currentUser?.let {
           AuthUser(
               uid = it.uid,
               displayName = it.displayName
           )
        }
    }

    override suspend fun signInWithGoogle(idToken: String): AuthUser {
        val credential = GoogleAuthProvider.getCredential(
            idToken,
            null
        )
        val firebaseUser = firebaseAuth
            .signInWithCredential(credential)
            .await()
            .user ?: error("Firebase did not return a signed user")
        return AuthUser(
            uid = firebaseUser.uid,
            displayName = firebaseUser.displayName
        )
    }

}