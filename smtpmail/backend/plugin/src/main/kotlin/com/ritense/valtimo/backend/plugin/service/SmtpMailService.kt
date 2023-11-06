package com.ritense.valtimo.backend.plugin.service

import com.ritense.resource.domain.MetadataType
import com.ritense.resource.service.TemporaryResourceStorageService
import com.ritense.valtimo.backend.plugin.client.SmtpMailClient
import com.ritense.valtimo.backend.plugin.dto.SmtpMailContentDto
import com.ritense.valtimo.backend.plugin.dto.SmtpMailContextDto
import mu.KotlinLogging
import java.io.InputStream

class SmtpMailService(
    private val smtpMailClient: SmtpMailClient,
    private val storageService: TemporaryResourceStorageService
) {

    fun sendSmtpMail(
        mailContext: SmtpMailContextDto
    ) {
        val mailContent = prepareMailContent(
            contentResourceId = mailContext.contentResourceId,
            attachmentResourceIds = mailContext.attachmentResourceIds
        )

        smtpMailClient.sendEmail(mailContext, mailContent).also {
            logger.info { "Attempted to send SMTP mail with subject ${mailContext.subject}" }
        }
    }

    private fun prepareMailContent(
        contentResourceId: String,
        attachmentResourceIds: List<String>
    ): SmtpMailContentDto {
        val attachments: MutableList<SmtpMailContentDto.Attachment> = mutableListOf()

        var totalAttachmentsSize = 0

        attachmentResourceIds.forEach { attachmentResourceId ->
            totalAttachmentsSize += checkForMaxAttachmentSize(
                storageService.getResourceContentAsInputStream(attachmentResourceId),
                totalAttachmentsSize
            )

            val attachmentMetadata = storageService.getResourceMetadata(attachmentResourceId)
            val fileName = "${attachmentMetadata[MetadataType.FILE_NAME.key]}.${attachmentMetadata[MetadataType.CONTENT_TYPE.key]}"

            attachments.add(SmtpMailContentDto.Attachment(fileName, attachmentResourceId))
        }

        val mailMessageAsInputStream = storageService.getResourceContentAsInputStream(contentResourceId).also {
            logger.debug { "Fetching mail message with resourceId '$contentResourceId" }
        }

        return SmtpMailContentDto(
            mailMessage = mailMessageAsInputStream.toPlainText(),
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

    private fun InputStream.toPlainText(): String = use { stream ->
        stream.bufferedReader().readText()
    }

    companion object {
        val logger = KotlinLogging.logger {}

        private const val MAX_SIZE_EMAIL_BODY_IN_BYTES: Int = 25000000  // 25mb
    }
}