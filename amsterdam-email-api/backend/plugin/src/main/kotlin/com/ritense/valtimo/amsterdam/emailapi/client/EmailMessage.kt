package com.ritense.valtimo.amsterdam.emailapi.client

data class EmailMessage( val to: Set<Recipient>, val content: Set<BodyPart>) {

    var subject: String = ""
    var priority: String = "normal"
    var channel: String = "mail"

    var cc: Set<Recipient> = emptySet();
    var bcc: Set<Recipient> = emptySet();

}
