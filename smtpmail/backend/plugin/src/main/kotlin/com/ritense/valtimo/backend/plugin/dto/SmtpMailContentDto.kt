package com.ritense.valtimo.backend.plugin.dto

data class SmtpMailContentDto(
    val mailMessage: String,
    val attachments: List<Attachment>
) {

    data class Attachment(
        val fileName: String,
        val fileResourceId: String
    )
}