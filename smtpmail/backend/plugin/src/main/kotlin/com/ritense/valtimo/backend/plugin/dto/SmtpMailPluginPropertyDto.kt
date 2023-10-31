package com.ritense.valtimo.backend.plugin.dto

data class SmtpMailPluginPropertyDto(
    val host: String,
    val port: String,
    val username: String,
    val password: String,
    val protocol: String? = "smtp",
    val debug: Boolean? = true,
    val auth: Boolean? = true,
    val startTlsEnable:Boolean? = true,
)