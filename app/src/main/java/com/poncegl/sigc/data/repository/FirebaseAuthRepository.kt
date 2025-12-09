package com.poncegl.sigc.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.poncegl.sigc.domain.model.User
import com.poncegl.sigc.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: Flow<User?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser?.toDomain())
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user?.toDomain() ?: throw Exception("El usuario autenticado es nulo")
            Result.success(user)
        } catch (e: Exception) {
            val safeErrorMessage = when (e) {
                is FirebaseAuthInvalidUserException,
                is FirebaseAuthInvalidCredentialsException -> "Credenciales inválidas. Verifica tu correo y contraseña."

                else -> "Error al iniciar sesión. Intenta nuevamente."
            }
            Result.failure(Exception(safeErrorMessage))
        }
    }

    override suspend fun signUp(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user?.toDomain() ?: throw Exception("Error al crear usuario")
            Result.success(user)
        } catch (e: Exception) {
            val safeErrorMessage = when (e) {
                is FirebaseAuthWeakPasswordException -> "La contraseña no es segura. Intenta con una más larga y compleja."
                is FirebaseAuthInvalidCredentialsException -> "El formato del correo electrónico no es válido."
                is FirebaseAuthUserCollisionException -> "Ya existe una cuenta vinculada a este correo."
                else -> "No se pudo crear la cuenta. Por favor intenta más tarde."
            }
            Result.failure(Exception(safeErrorMessage))
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    private fun FirebaseUser.toDomain(): User {
        return User(
            id = this.uid,
            email = this.email,
            displayName = this.displayName,
            photoUrl = this.photoUrl?.toString()
        )
    }
}