package com.ritense.valtimo.amsterdam.emailapi.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.plugin.domain.ActivityType
import com.ritense.valtimo.amsterdam.emailapi.EmailService
import com.ritense.valtimo.amsterdam.emailapi.client.EmailClient
import com.ritense.valtimo.amsterdam.emailapi.client.EmailMessage
import com.ritense.valueresolver.ValueResolverService
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.net.URI


@Plugin(
    key = "amsterdam_email_api",
    title = "Email API Amsterdam",
    description = "Zend emails via Email API"
)
class EmailApiPlugin(
    private val emailClient: EmailClient,
) {

    @PluginProperty(key = "baseUrl", secret = false, required = true)
    lateinit var baseUrl: String

    @PluginProperty(key = "apiKey", secret = true, required = true)
    lateinit var apiKey: String

    @PluginAction(
        key = "send-email-api",
        title = "Zend email via API",
        description = "Zend een email via de Email API",
        activityTypes = [ActivityType.SEND_TASK]
    )
    fun sendEmail(
        execution: DelegateExecution,
        @PluginActionProperty to: String,
        @PluginActionProperty from: String,
        @PluginActionProperty cc: String,
    ) {
        EmailMessage message = EmailMessage(to.);

        emailClient.send(message, URI.create(baseUrl), apiKey)
    }
}
