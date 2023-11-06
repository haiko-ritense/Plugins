package com.ritense.valtimo.backend.plugin.dto

data class SmtpMailPluginPropertyDto(
    val host: String,
    val port: String,
    val username: String,
    val password: String,
    val protocol: String,
    val debug: Boolean,
    val auth: Boolean,
    val startTlsEnable:Boolean,
)