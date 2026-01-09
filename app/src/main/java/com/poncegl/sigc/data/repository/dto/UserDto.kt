package com.poncegl.sigc.data.repository.dto

import java.util.Date

object UserFields {
    const val ID = "id"
    const val EMAIL = "email"
    const val DISPLAY_NAME = "displayName"
    const val PHOTO_URL = "photoUrl"
    const val CREATED_AT = "createdAt"
    const val UPDATED_AT = "updatedAt"
    const val LAST_LOGIN_AT = "lastLoginAt"
    const val REGISTRATION_METHOD = "registrationMethod"
    const val REGISTRATION_PLATFORM = "registrationPlatform"
    const val ACTIVE_TEAMS = "activeTeams"

    val IMMUTABLE_FIELDS = setOf(
        ID,
        EMAIL,
        CREATED_AT,
        REGISTRATION_METHOD,
        REGISTRATION_PLATFORM
    )
}

data class UserDto(
    val id: String,
    val email: String?,
    val displayName: String?,
    val photoUrl: String?,
    val createdAt: Date,
    val updatedAt: Date?,
    val lastLoginAt: Date?,
    val registrationMethod: String,
    val registrationPlatform: String,
    val activeTeams: List<Map<String, String>>? = null
) {
    fun toMap(): Map<String, Any?> = mapOf(
        UserFields.ID to id,
        UserFields.EMAIL to email,
        UserFields.DISPLAY_NAME to displayName,
        UserFields.PHOTO_URL to photoUrl,
        UserFields.CREATED_AT to createdAt,
        UserFields.UPDATED_AT to updatedAt,
        UserFields.LAST_LOGIN_AT to lastLoginAt,
        UserFields.REGISTRATION_METHOD to registrationMethod,
        UserFields.REGISTRATION_PLATFORM to registrationPlatform,
        UserFields.ACTIVE_TEAMS to activeTeams
    )
}
