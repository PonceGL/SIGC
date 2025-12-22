package com.poncegl.sigc.domain.model

import java.time.Instant
enum class RegistrationMethod {
    EMAIL_AND_PASSWORD,
    GOOGLE
    // FACEBOOK,
    // APPLE,
    // BACKEND
}
enum class RegistrationPlatform {
    ANDROID
    // IOS,
    // WEB
}
data class User (
    val id: String,
    val email: String?,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val createdAt: Instant,
    val updatedAt: Instant? = null,
    val lastLoginAt: Instant? = null,
    val registrationMethod: RegistrationMethod,
    val registrationPlatform: RegistrationPlatform = RegistrationPlatform.ANDROID
)