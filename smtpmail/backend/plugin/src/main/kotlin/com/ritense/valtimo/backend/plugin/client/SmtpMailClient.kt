package com.ritense.valtimo.backend.plugin.client

import com.ritense.plugin.service.PluginService
import com.ritense.valtimo.backend.plugin.dto.SmtpMailContentDto
import com.ritense.valtimo.backend.plugin.dto.SmtpMailContextDto
import com.ritense.valtimo.backend.plugin.dto.SmtpMailPluginPropertyDto
import com.ritense.valtimo.backend.plugin.plugin.SmtpMailPlugin
import mu.KotlinLogging
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.mail.javamail.MimeMessageHelper
import javax.mail.internet.MimeMessage

class SmtpMailClient(
    private val pluginService: PluginService
) {

    fun sendEmail(
        mailContext: SmtpMailContextDto,
        mailContent: SmtpMailContentDto
    ) {
        try {
            val message: MimeMessage = javaMailSender().createMimeMessage()
            MimeMessageHelper(message, true).apply {
                setFrom(mailContext.sender.toString())
                setTo(mailContext.recipient.toString())
                setSubject(mailContext.subject)
                setText(mailContent.mailMessage)

                mailContent.attachments.forEach {
                    this.addAttachment(it.fileName, it.file)
                }
            }
        } catch (e: Exception) {
            logger.warn {
                "Sending the secured email with subject ${mailContext.subject} has failed with error message ${e.message}"
            }
        }
    }

    private fun javaMailSender(): JavaMailSender = JavaMailSenderImpl().apply {
        with(getZivverPluginData()) {
            this@apply.host = host
            this@apply.port = port.toInt()
            this@apply.username = username
            this@apply.password = password
            this@apply.protocol = protocol
            this@apply.javaMailProperties["mail.transport.protocol"] = protocol
            this@apply.javaMailProperties["mail.smtp.auth"] = auth
            this@apply.javaMailProperties["mail.smtp.starttls.enable"] = startTlsEnable
            this@apply.javaMailProperties["mail.debug"] = debug
        }
    }

    private fun getZivverPluginData(): SmtpMailPluginPropertyDto {
        val pluginInstance = pluginService
            .createInstance(SmtpMailPlugin::class.java) { true }

        requireNotNull(pluginInstance) { "No plugin found" }

        return SmtpMailPluginPropertyDto(
            host = pluginInstance.host.toString(),
            port = pluginInstance.port,
            username = pluginInstance.username,
            password = pluginInstance.password,
            protocol = pluginInstance.protocol.ifEmpty { null },
            debug = if (pluginInstance.debug.isNotEmpty()) pluginInstance.debug.toBoolean() else null,
            auth = if (pluginInstance.auth.isNotEmpty()) pluginInstance.auth.toBoolean() else null,
            startTlsEnable = if (pluginInstance.starttlsenable.isNotEmpty()) pluginInstance.starttlsenable.toBoolean() else null
        )
    }

    companion object {
        val logger = KotlinLogging.logger {}
    }
}