package com.poncegl.sigc.domain.repository

import com.poncegl.sigc.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    /**
     * Flujo observable del usuario actual.
     * Emite null si no hay sesión activa.
     */
    val currentUser: Flow<User?>
    /**
     * Inicia sesión con correo y contraseña.
     * Retorna un Result que contiene el User o la excepción.
     */
    suspend fun login(email: String, password: String): Result<User>

    /**
     * Registra un nuevo usuario.
     */
    suspend fun signUp(name: String, email: String, password: String): Result<User>

    /**
     * Cierra la sesión actual.
     */
    suspend fun logout()
}