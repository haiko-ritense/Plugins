package com.ritense.valtimo.backend.plugin.client

import com.ritense.plugin.service.PluginService
import com.ritense.resource.service.TemporaryResourceStorageService
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
    private val pluginService: PluginService,
    private val storageService: TemporaryResourceStorageService
) {

    fun sendEmail(
        mailContext: SmtpMailContextDto,
        mailContent: SmtpMailContentDto
    ) {
        try {
            val javaMailSender = javaMailSender()

            val message: MimeMessage = javaMailSender.createMimeMessage()

            with(MimeMessageHelper(message, true)) {
                setFrom(mailContext.sender.address)
                mailContext.recipients.forEach { addTo(it.address) }
                mailContext.ccList.forEach { addTo(it.address) }
                mailContext.bccList.forEach { addTo(it.address) }
                setSubject(mailContext.subject)
                setText(mailContent.mailMessage, true)
                mailContent.attachments.forEach {
                    addAttachment(it.fileName) { storageService.getResourceContentAsInputStream(it.fileResourceId) }
                }

            javaMailSender.send(message)
            }
        } catch (e: Exception) {
            logger.warn {
                "Sending email with subject ${mailContext.subject} has failed with error message ${e.message}"
            }
        }
    }

    private fun javaMailSender(): JavaMailSender = JavaMailSenderImpl().apply {
        with(getSmtpMailPluginData()) {
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

    private fun getSmtpMailPluginData(): SmtpMailPluginPropertyDto {
        val pluginInstance = pluginService
            .createInstance(SmtpMailPlugin::class.java) { true }

        requireNotNull(pluginInstance) { "No plugin found" }

        return SmtpMailPluginPropertyDto(
            host = pluginInstance.host,
            port = pluginInstance.port,
            username = pluginInstance.username,
            password = pluginInstance.password,
            protocol = pluginInstance.protocol!!,
            debug = pluginInstance.debug!!,
            auth = pluginInstance.auth!!,
            startTlsEnable = pluginInstance.startTlsEnable!!
        )
    }

    companion object {
        val logger = KotlinLogging.logger {}
    }
}