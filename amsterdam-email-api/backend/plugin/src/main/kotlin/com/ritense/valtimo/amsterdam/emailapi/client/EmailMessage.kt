package com.ritense.valtimo.amsterdam.emailapi.client

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)

data class EmailMessage(
    val to: Set<Recipient>,
    val content: Set<BodyPart>,
    val subject: String = "",
    val from: Recipient,
    val priority: String = "normal",
    val channel: String = "mail",
    var cc: Set<Recipient> = emptySet(),
    var bcc: Set<Recipient> = emptySet()
)
