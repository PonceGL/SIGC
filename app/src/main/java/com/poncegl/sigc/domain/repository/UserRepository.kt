package com.poncegl.sigc.domain.repository

import com.poncegl.sigc.domain.model.User
import java.time.Instant

interface UserRepository {
    /**
     * Guarda un usuario nuevo en el repositorio.
     * Solo debe usarse durante el registro inicial.
     */
    suspend fun saveUser(user: User): Result<Unit>

    /**
     * Actualiza la fecha del último inicio de sesión.
     */
    suspend fun updateLastLogin(userId: String, timestamp: Instant): Result<Unit>

    /**
     * Actualiza campos permitidos del usuario.
     * Rechazará automáticamente intentos de modificar campos inmutables (id, email, createdAt, etc.)
     */
    suspend fun updateUserProfile(userId: String, updates: Map<String, Any?>): Result<Unit>
}
