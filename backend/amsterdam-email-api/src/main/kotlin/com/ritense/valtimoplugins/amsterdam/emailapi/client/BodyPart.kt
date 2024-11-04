package com.ritense.valtimoplugins.amsterdam.emailapi.client

import com.fasterxml.jackson.annotation.JsonProperty

data class BodyPart(
    val content: String,
    val mimeType: String,
    val encoding: String
)
