package com.ritense.valtimo.amsterdam.emailapi.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.plugin.domain.ActivityType
import com.ritense.valtimo.amsterdam.emailapi.client.BodyPart
import com.ritense.valtimo.amsterdam.emailapi.client.EmailClient
import com.ritense.valtimo.amsterdam.emailapi.client.EmailMessage
import com.ritense.valtimo.amsterdam.emailapi.client.Recipient
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.security.crypto.util.EncodingUtils
import org.springframework.util.MimeType
import org.springframework.util.MimeTypeUtils
import java.net.URI


@Plugin(
    key = "amsterdam_email_api",
    title = "Email API Amsterdam",
    description = "Zend emails via Email API"
)
class EmailApiPlugin(
    private val emailClient: EmailClient,
) {

    @PluginProperty(key = "emailApiBaseUrl", secret = false, required = true)
    lateinit var emailApiBaseUrl: String

    @PluginProperty(key = "clientId", secret = true, required = true)
    lateinit var clientId: String

    @PluginProperty(key = "clientId", secret = true, required = true)
    lateinit var clientSecret: String

    @PluginProperty(key = "clientId", secret = true, required = true)
    lateinit var tokenEndpoint: String

    @PluginAction(
        key = "send-email-api",
        title = "Zend email via API",
        description = "Zend een email via de Email API",
        activityTypes = [ActivityType.SEND_TASK]
    )
    fun sendEmail(
        ex: DelegateExecution
    ) {

        var token: String = getToken();
        val message = EmailMessage(
            to = setOf(Recipient(address = ex.getVariable("toEmail") as String,
                name = ex.getVariable("toName") as String)),
            from = Recipient( address = ex.getVariable("fromAddress") as String),
            content = setOf(BodyPart(content = ex.getVariable("contentHtml") as String,
                mimeType =  MimeTypeUtils.TEXT_HTML_VALUE,
                encoding = "utf-8")),
            subject = ex.getVariable("emailSubject") as String,
            )

        // set optional values
        ex.getVariable("ccEmail")?.let {
            message.cc = setOf(Recipient(address =  it as String,
                name = ex.getVariable("ccName") as String?))
        }
        ex.getVariable("bccEmail")?.let {
            message.bcc = setOf(Recipient(address =  it as String,
                name = ex.getVariable("bccName") as String?))
        }

        // send
        emailClient.send(message, URI.create(emailApiBaseUrl), token)
    }

    private fun getToken(): String {

    }
}
