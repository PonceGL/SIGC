package com.poncegl.sigc.domain.repository

import android.content.Context
import com.poncegl.sigc.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    /**
     * Flujo observable del usuario actual.
     * Emite null si no hay sesión activa.
     */
    val currentUser: Flow<User?>

    /**
     * Verifica si existe una sesión activa actualmente.
     */
    fun isUserLoggedIn(): Boolean

    /**
     * Inicia sesión con correo y contraseña.
     * Retorna un Result que contiene el User o la excepción.
     */
    suspend fun login(email: String, password: String): Result<User>

    /**
     * Registra un nuevo usuario.
     */
    suspend fun signUp(name: String, email: String, password: String): Result<User>

    suspend fun signInWithGoogle(context: Context): Result<User>

    /**
     * Envía un correo electrónico para restablecer la contraseña.
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit>

    /**
     * Confirma el cambio de contraseña utilizando el código (oobCode) recibido por correo
     * y la nueva contraseña ingresada por el usuario.
     */
    suspend fun confirmPasswordReset(oobCode: String, newPassword: String): Result<Unit>

    /**
     * Cierra la sesión actual.
     */
    suspend fun logout()
}
