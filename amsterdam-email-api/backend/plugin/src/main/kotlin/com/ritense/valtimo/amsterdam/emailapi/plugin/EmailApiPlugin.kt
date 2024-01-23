package com.ritense.valtimo.amsterdam.emailapi.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.plugin.domain.ActivityType
import com.ritense.valtimo.amsterdam.emailapi.client.BodyPart
import com.ritense.valtimo.amsterdam.emailapi.client.EmailClient
import com.ritense.valtimo.amsterdam.emailapi.client.EmailMessage
import com.ritense.valtimo.amsterdam.emailapi.client.Recipient
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.http.*
import org.springframework.util.MimeTypeUtils
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.util.*


private const val UTF8 = "utf-8"

@Plugin(
    key = "amsterdam_email_api",
    title = "Email API Amsterdam",
    description = "Zend emails via Email API"
)
class EmailApiPlugin(
    private val emailClient: EmailClient,
    private val restTemplate: RestTemplate
) {

    @PluginProperty(key = "emailApiBaseUrl", secret = false, required = true)
    lateinit var emailApiBaseUrl: String

    @PluginProperty(key = "clientId", secret = true, required = true)
    lateinit var clientId: String

    @PluginProperty(key = "clientId", secret = true, required = true)
    lateinit var clientSecret: String

    @PluginProperty(key = "tokenEndpoint", secret = false, required = true)
    lateinit var tokenEndpoint: String

    @PluginAction(
        key = "zend-email-api",
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
                encoding = UTF8
            )),
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
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED

        val body = "grant_type=client_credentials"

        val auth = "$clientId:$clientSecret"
        val base64Auth = Base64.getEncoder().encodeToString(auth.toByteArray())
        headers.set(HttpHeaders.AUTHORIZATION, "Basic $base64Auth")

        val requestEntity = HttpEntity(body, headers)

        val responseEntity: ResponseEntity<Map<*, *>> =
            restTemplate.exchange(tokenEndpoint, HttpMethod.POST, requestEntity, Map::class.java)

        val accessToken = responseEntity.body?.get("access_token")?.toString()

        return accessToken ?: throw RuntimeException("Token retrieval failed.")
    }
}
