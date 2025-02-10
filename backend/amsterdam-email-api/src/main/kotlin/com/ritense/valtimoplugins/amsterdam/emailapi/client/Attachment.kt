package com.ritense.valtimoplugins.amsterdam.emailapi.client

data class Attachment (
    val disposition: String,
    val filename: String?,
    val content: String?,
    val contentType: String?
)
