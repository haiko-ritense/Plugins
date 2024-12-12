package com.ritense.valtimoplugins.amsterdam.emailapi.plugin

import com.github.ksuid.Ksuid
import com.ritense.documentenapi.DocumentenApiAuthentication
import com.ritense.documentenapi.client.DocumentInformatieObject
import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.processlink.domain.ActivityTypeWithEventName
import com.ritense.valtimoplugins.amsterdam.emailapi.client.*
import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.util.MimeTypeUtils
import org.springframework.util.StringUtils
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

import java.net.URI
import java.nio.ByteBuffer
import java.util.Base64

private const val UTF8 = "utf-8"
private val logger = KotlinLogging.logger {}


private const val ATTACHEMENT = "attachement"

@Plugin(
    key = "amsterdamemailapi",
    title = "Email API Amsterdam",
    description = "Zend emails via Email API"
)
class EmailApiPlugin(
    private val emailClient: EmailClient,
    private val restClientBuilder: RestClient.Builder
    ) {

    @PluginProperty(key = "emailApiBaseUrl", secret = false, required = true)
    lateinit var emailApiBaseUrl: String

    @PluginProperty(key = "subscriptionKey", secret = true, required = true)
    lateinit var subscriptionKey: String

    @PluginProperty(key = "authenticationPluginConfiguration", secret = false)
    lateinit var authenticationPluginConfiguration: DocumentenApiAuthentication

    @PluginAction(
        key = "zend-email",
        title = "Zend email via API met optioneel zaak ID en relatiecode",
        description = "Zend een email via de Email API waarbij optioneel de zaak ID en relatiecode meegegeven kan worden. Deze worden verwerkt in de messageId.",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START]
    )
    fun sendEmail(
        execution: DelegateExecution,
        @PluginActionProperty zaakId: String?,
        @PluginActionProperty relatieCode: String?,
        @PluginActionProperty toEmail: String,
        @PluginActionProperty toName: String?,
        @PluginActionProperty fromAddress: String,
        @PluginActionProperty emailSubject: String,
        @PluginActionProperty contentHtml: String,
        @PluginActionProperty attachmentsPvName: String?,
        @PluginActionProperty ccEmail: String?,
        @PluginActionProperty ccName: String?,
        @PluginActionProperty bccEmail: String?,
        @PluginActionProperty bccName: String?,
    ) {
        val message = EmailMessage(
            to = setOf(
                Recipient(
                    address = toEmail,
                    name = toName
                )
            ),
            from = Recipient(address = fromAddress),
            content = setOf(
                BodyPart(
                    content = contentHtml,
                    mimeType = MimeTypeUtils.TEXT_HTML.toString(),
                    encoding = UTF8
                )
            ),
            subject = emailSubject,
            messageId = generateMessageId(zaakId, relatieCode),
        )

        // set optional values
        if(StringUtils.hasText(attachmentsPvName)) {
            var documentNames: List<String> = execution.getVariable(attachmentsPvName) as List<String>
            if(documentNames.isNotEmpty()) {
                handleAttachements(message, documentNames)
            }
        }

        if(ccEmail != null) {
            message.cc = setOf(
                Recipient(
                    address = ccEmail,
                    name = ccName
                )
            )

        }

        if(bccEmail != null) {
            message.bcc = setOf(
                Recipient(
                    address = bccEmail,
                    name = bccName
                )
            )
        }

        // send
        emailClient.send(message, URI.create(emailApiBaseUrl), subscriptionKey)
    }

    private fun handleAttachements(message: EmailMessage, documentNames: List<String>) {
        var restClient = authenticationPluginConfiguration.applyAuth(restClientBuilder).build()

        documentNames.forEach{
            var informatieObject =  restClient
                .get()
                .uri(it)
                .retrieve()
                .body<DocumentInformatieObject>()!!
            logger.debug { "found object for url:" + it }

            if(tooBigAsAttachement(informatieObject.bestandsomvang)) {
                logger.info(it + "is too big. Size is " + informatieObject.bestandsomvang)
            }
            else {
                var content = restClient
                    .get()
                    .uri(informatieObject.link)
                    .retrieve()
                    .body<ByteArray>()!!

                var attachement: Attachement = Attachement(
                    ATTACHEMENT,
                    informatieObject.bestandsnaam,
                    Base64.getEncoder().encodeToString(content),
                    informatieObject.formaat
                )
                message.attachements.add(attachement)
            }
        }

    }

    private fun tooBigAsAttachement(bestandsomvang: Long?): Boolean {
        if(bestandsomvang != null && bestandsomvang/1024*1024 > 20) {
            return true
        }
        return false
    }

    private fun generateMessageId(zaakId: String?, relatieCode: String?) = listOfNotNull(zaakId, relatieCode, Ksuid.newKsuid()).joinToString(separator = "-")
}
