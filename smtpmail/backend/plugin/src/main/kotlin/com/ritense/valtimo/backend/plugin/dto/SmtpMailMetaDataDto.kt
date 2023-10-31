package com.ritense.valtimo.backend.plugin.dto

data class SmtpMailContextDto(
    val sender: Email,
    val recipient: Email,
    val cc: Email,
    val bcc: Email,
    val subject: String,
    val contentResourceId: String,
    val attachmentResourceIds: List<String>
)

@JvmInline
value class Email(private val address: String) {
    init {
        require( address.matches(Regex("^[A-Za-z0-9+_.-]+@(.+)$"))) { "This is not a valid email address" }
    }
}