package com.ritense.valtimo.amsterdam.emailapi.client

import com.fasterxml.jackson.annotation.JsonProperty

data class BodyPart(val content: String,
                    @JsonProperty("mime-type") val mimeType: String,
                    val encoding: String)
