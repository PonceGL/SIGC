package com.poncegl.sigc.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.poncegl.sigc.core.constants.FirestoreConstants
import com.poncegl.sigc.data.repository.dto.UserDto
import com.poncegl.sigc.data.repository.dto.UserFields
import com.poncegl.sigc.domain.model.User
import com.poncegl.sigc.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import java.time.Instant
import java.util.Date
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun saveUser(user: User): Result<Unit> {
        return try {
            val userDto = UserDto(
                id = user.id,
                email = user.email,
                displayName = user.displayName,
                photoUrl = user.photoUrl,
                createdAt = Date.from(user.createdAt),
                updatedAt = user.updatedAt?.let { Date.from(it) },
                lastLoginAt = user.lastLoginAt?.let { Date.from(it) },
                registrationMethod = user.registrationMethod.name,
                registrationPlatform = user.registrationPlatform.name
            )

            firestore.collection(FirestoreConstants.USERS_COLLECTION)
                .document(user.id)
                .set(userDto.toMap())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateLastLogin(userId: String, timestamp: Instant): Result<Unit> {
        return try {
            val updates = mapOf(
                UserFields.LAST_LOGIN_AT to Date.from(timestamp)
            )

            firestore.collection(FirestoreConstants.USERS_COLLECTION)
                .document(userId)
                .set(updates, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserProfile(userId: String, updates: Map<String, Any?>): Result<Unit> {
        return try {
            val safeUpdates = updates.filterKeys { key ->
                !UserFields.IMMUTABLE_FIELDS.contains(key)
            }

            if (safeUpdates.isEmpty()) {
                return Result.success(Unit) // Nada válido para actualizar
            }

            val finalUpdates = safeUpdates.toMutableMap()
            finalUpdates[UserFields.UPDATED_AT] = Date.from(Instant.now())

            firestore.collection(FirestoreConstants.USERS_COLLECTION)
                .document(userId)
                .set(finalUpdates, SetOptions.merge())
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
