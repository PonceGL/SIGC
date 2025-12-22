package com.poncegl.sigc.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.poncegl.sigc.BuildConfig
import com.poncegl.sigc.core.constants.FirestoreConstants
import com.poncegl.sigc.data.repository.dto.UserDto
import com.poncegl.sigc.data.repository.dto.UserFields
import com.poncegl.sigc.domain.model.RegistrationMethod
import com.poncegl.sigc.domain.model.RegistrationPlatform
import com.poncegl.sigc.domain.model.User
import com.poncegl.sigc.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.Instant
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val credentialManager: CredentialManager
) : AuthRepository {

    override val currentUser: Flow<User?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val firebaseUser = auth.currentUser
            if (firebaseUser != null) {
                trySend(mapToDomain(firebaseUser))
            } else {
                trySend(null)
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: throw Exception("El usuario autenticado es nulo")
            val user = mapToDomain(firebaseUser)
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

    override suspend fun signUp(name: String, email: String, password: String): Result<User> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("Error crítico al crear usuario")

            val newUser = mapToDomain(firebaseUser).copy(displayName = name)

            val userDto = UserDto(
                id = newUser.id,
                email = newUser.email,
                displayName = newUser.displayName,
                photoUrl = newUser.photoUrl,
                createdAt = Date.from(newUser.createdAt),
                updatedAt = newUser.updatedAt?.let { Date.from(it) },
                lastLoginAt = newUser.lastLoginAt?.let { Date.from(it) },
                registrationMethod = newUser.registrationMethod.name,
                registrationPlatform = newUser.registrationPlatform.name
            )

            firestore.collection(FirestoreConstants.USERS_COLLECTION)
                .document(firebaseUser.uid)
                .set(userDto.toMap())
                .await()

            Result.success(newUser)

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

    override suspend fun signInWithGoogle(context: Context): Result<User> {
        return try {
            val webClientId = BuildConfig.SERVER_CLIENT_ID
            val hashedNonce = UUID.randomUUID().toString()

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setNonce(hashedNonce)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = context
            )

            val credential = result.credential

            val googleIdToken = when {
                credential is GoogleIdTokenCredential -> {
                    credential.idToken
                }
                credential is androidx.credentials.CustomCredential &&
                        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL -> {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        googleIdTokenCredential.idToken
                    } catch (e: Exception) {
                        throw Exception("Error al leer los datos de Google: ${e.message}")
                    }
                }
                else -> {
                    throw Exception("Tipo de credencial no soportado: ${credential.javaClass.name}")
                }
            }

            val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            val authResult = firebaseAuth.signInWithCredential(authCredential).await()
            val firebaseUser = authResult.user ?: throw Exception("Error al autenticar con Google")

            val domainUser = mapToDomain(firebaseUser)

            val userDto = UserDto(
                id = domainUser.id,
                email = domainUser.email,
                displayName = domainUser.displayName,
                photoUrl = domainUser.photoUrl,
                createdAt = Date.from(domainUser.createdAt),
                updatedAt = null,
                lastLoginAt = Date.from(Instant.now()),
                registrationMethod = domainUser.registrationMethod.name,
                registrationPlatform = domainUser.registrationPlatform.name
            )

            val updates = userDto.toMap().toMutableMap()
            updates.remove(UserFields.UPDATED_AT)

            firestore.collection(FirestoreConstants.USERS_COLLECTION)
                .document(firebaseUser.uid)
                .set(updates, SetOptions.merge())
                .await()

            Result.success(domainUser)

        } catch (e: Exception) {
            if (e is androidx.credentials.exceptions.GetCredentialCancellationException) {
                Result.failure(Exception("Inicio de sesión cancelado."))
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun logout() {
        firebaseAuth.signOut()
    }

    private fun mapToDomain(firebaseUser: FirebaseUser): User {
        val creationTimestamp = firebaseUser.metadata?.creationTimestamp ?: System.currentTimeMillis()
        val lastSignInTimestamp = firebaseUser.metadata?.lastSignInTimestamp ?: System.currentTimeMillis()

        val isGoogle = firebaseUser.providerData.any { it.providerId == GoogleAuthProvider.PROVIDER_ID }
        val method = if (isGoogle) RegistrationMethod.GOOGLE else RegistrationMethod.EMAIL_AND_PASSWORD

        return User(
            id = firebaseUser.uid,
            email = firebaseUser.email,
            displayName = firebaseUser.displayName,
            photoUrl = firebaseUser.photoUrl?.toString(),
            createdAt = Instant.ofEpochMilli(creationTimestamp),
            updatedAt = null,
            lastLoginAt = Instant.ofEpochMilli(lastSignInTimestamp),
            registrationMethod = method,
            registrationPlatform = RegistrationPlatform.ANDROID
        )
    }
}
