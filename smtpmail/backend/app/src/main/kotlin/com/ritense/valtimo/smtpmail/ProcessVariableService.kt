package com.ritense.valtimo.smtpmail

import com.ritense.resource.domain.MetadataType
import com.ritense.resource.service.TemporaryResourceStorageService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.InputStream

@Service
class ProcessVariableService(
    private val storageService: TemporaryResourceStorageService,
    @Value("classpath:file.txt")
    private val resourceFile: Resource
) {

    //Here you can set the variables to test the SmtpMail plugin action
    fun setVariablesForSmtpMail(execution: DelegateExecution) {

        val resourceId = storageService.store(
            inputStream = getContentInputStream(),
        )

        val attachmentId1 = storageService.store(
            inputStream =
            resourceFile.inputStream,
            mapOf(
                MetadataType.FILE_NAME.key to "TestFile",
                MetadataType.CONTENT_TYPE.key to "txt"
            )
        )
        val attachmentId2 = storageService.store(
            inputStream =
            resourceFile.inputStream,
            mapOf(
                MetadataType.FILE_NAME.key to "anotherTestFile",
                MetadataType.CONTENT_TYPE.key to "txt"
            )
        )

        execution.setVariable(SENDER_PROCESS_VAR, "")
        execution.setVariable(RECIPIENTS_PROCESS_VAR, listOf(""))
        execution.setVariable(CC_PROCESS_VAR, listOf("l"))
        execution.setVariable(BCC_PROCESS_VAR, listOf(""))
        execution.setVariable(SUBJECT_PROCESS_VAR, "SMTP mail test")
        execution.setVariable(RESOURCE_ID_PROCESS_VAR, resourceId)
        execution.setVariable(ATTACHMENTS_PROCESS_VAR, listOf(attachmentId1, attachmentId2))
    }

    // Write your own testmail
    private fun getContentInputStream(): InputStream {
        val htmlString = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>Sample Email</title>
        </head>
        <body>
            <p>Hello,</p>
        <p>Dit is een testmailtje voor de SMTP mail plugin :) .</p>
        <p>Sincerely,<br></p>
        </body>
        </html>
    """.trimIndent()

        return ByteArrayInputStream(htmlString.toByteArray())
    }

    companion object {
        const val SENDER_PROCESS_VAR = "sender"
        const val RECIPIENTS_PROCESS_VAR = "recipients"
        const val CC_PROCESS_VAR = "cc"
        const val BCC_PROCESS_VAR = "bcc"
        const val SUBJECT_PROCESS_VAR = "subject"
        const val RESOURCE_ID_PROCESS_VAR = "resourceId"
        const val ATTACHMENTS_PROCESS_VAR = "attachments"
    }
}