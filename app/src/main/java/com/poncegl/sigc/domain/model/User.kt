package com.poncegl.sigc.domain.model

data class User (
    val id: String,
    val email: String?,
    val displayName: String? = null,
    val photoUrl: String? = null
)