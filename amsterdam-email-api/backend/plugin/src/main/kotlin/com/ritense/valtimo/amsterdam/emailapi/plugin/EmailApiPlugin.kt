package com.ritense.valtimo.amsterdam.emailapi.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.valtimo.amsterdam.emailapi.EmailService


@Plugin(
    key = "amsterdam_email_api",
    title = "Email API Amsterdam",
    description = ""
)
class EmailApiPlugin(
    val emailService: EmailService
) {

    @PluginProperty(key = "clientId", secret = false, required = true)
    lateinit var clientId: String

    @PluginProperty(key = "clientSecret", secret = true, required = true)
    lateinit var clientSecret: String

}
