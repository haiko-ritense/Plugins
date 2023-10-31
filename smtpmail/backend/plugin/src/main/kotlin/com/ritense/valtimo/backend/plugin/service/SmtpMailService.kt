package com.ritense.valtimo.backend.plugin.service

import com.ritense.resource.domain.MetadataType
import com.ritense.resource.service.TemporaryResourceStorageService
import com.ritense.valtimo.backend.plugin.client.SmtpMailClient
import com.ritense.valtimo.backend.plugin.dto.Email
import com.ritense.valtimo.backend.plugin.dto.SmtpMailContentDto
import com.ritense.valtimo.backend.plugin.dto.SmtpMailContextDto
import mu.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.core.io.InputStreamResource
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class SmtpMailService(
    private val smtpMailClient: SmtpMailClient,
    private val storageService: TemporaryResourceStorageService
) {

    fun sendZivverMail(execution: DelegateExecution) {
        val mailContext = getMailContextFromProcessVariables(execution)
        val mailContent = getMailContentFromTemporaryStorage(execution)

        smtpMailClient.sendEmail(mailContext, mailContent).also {
            logger.info { "Sending secured email with Zivver with subject ${mailContext.subject}" }
        }
    }

    private fun getMailContentFromTemporaryStorage(execution: DelegateExecution): SmtpMailContentDto {
        val mailMessageResourceId = execution.getVariable(RESOURCE_ID_PROCESS_VAR) as String

        val attachments: MutableList<SmtpMailContentDto.Attachment> = mutableListOf()

        var totalAttachmentsSize: Int = 0

        if (execution.hasVariable(ATTACHMENTS_PROCESS_VAR)) {
            val attachmentResourceIds = execution.getVariable(ATTACHMENTS_PROCESS_VAR) as List<String>
            attachmentResourceIds.forEach { attachmentResourceId ->
                val attachmentAsInputStream = storageService.getResourceContentAsInputStream(attachmentResourceId)

                totalAttachmentsSize += checkForMaxAttachmentSize(attachmentAsInputStream, totalAttachmentsSize)

                val attachment = InputStreamResource(attachmentAsInputStream)
                val attachmentMetadata = storageService.getResourceMetadata(mailMessageResourceId)
                val fileName = attachmentMetadata[MetadataType.FILE_NAME.key] as String //TODO: is filename incl extension?

                attachments.add(SmtpMailContentDto.Attachment(fileName, attachment)).also {
                    logger.debug { "Fetching attachment with resourceId '$attachmentResourceId" }
                }
            }
        }

        val mailMessageAsInputStream = storageService.getResourceContentAsInputStream(mailMessageResourceId).also {
            logger.debug { "Fetching mail message with resourceId '$mailMessageResourceId" }
        }

        return SmtpMailContentDto(
            mailMessage = inputStreamToPlainText(mailMessageAsInputStream),
            attachments = attachments
        )
    }

    private fun checkForMaxAttachmentSize(attachment: InputStream, totalAttachmentSize: Int): Int {
        val attachmentSize = attachment.readAllBytes().size

        if (totalAttachmentSize + attachmentSize > MAX_SIZE_EMAIL_BODY_IN_BYTES) {
            throw IllegalStateException("Email exceeds max size of 25 mb")
        }

        return attachmentSize
    }

    @Suppress("UNCHECKED_CAST")
    private fun getMailContextFromProcessVariables(execution: DelegateExecution): SmtpMailContextDto =
        SmtpMailContextDto(
            sender = Email(execution.getVariable("sender") as String),
            recipient = Email(execution.getVariable("recipient") as String),
            cc = Email(execution.getVariable("cc") as String),
            bcc = Email(execution.getVariable("bcc") as String),
            subject = execution.getVariable("subject") as String,
            contentResourceId = execution.getVariable("subject") as String,
            attachmentResourceIds = execution.getVariable("attachments") as List<String>
        )

    private fun inputStreamToPlainText(inputStream: InputStream):String {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val text = StringBuilder()
        var line: String?

        try {
            while (reader.readLine().also { line = it } != null) {
                text.append(line).append("\n")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return text.toString()
    }

    companion object {
        val logger = KotlinLogging.logger {}

        private const val RESOURCE_ID_PROCESS_VAR = "resourceId"
        private const val ATTACHMENTS_PROCESS_VAR = "attachments"

        private const val MAX_SIZE_EMAIL_BODY_IN_BYTES: Int = 25000000  // 25mb
    }
}