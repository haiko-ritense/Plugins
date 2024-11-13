package com.ritense.valtimo.amsterdam.emailapi.plugin

import com.ritense.valtimo.amsterdam.emailapi.client.EmailClient
import com.ritense.valtimo.amsterdam.emailapi.client.EmailMessage
import org.assertj.core.api.Assertions.assertThat
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.verify

class EmailApiPluginTest {

    val emailClient = mock(EmailClient::class.java)

    val emailApiPlugin = EmailApiPlugin(
        emailClient
    )

    @BeforeEach
    fun setUp() {
        emailApiPlugin.emailApiBaseUrl = "https://example.com/api"
        emailApiPlugin.subscriptionKey = "1234567890"
    }

    @Test
    fun shouldAddZaakIdToMessageId() {
        val messageCaptor = argumentCaptor<EmailMessage>()

        emailApiPlugin.sendEmail(
            execution = mock(DelegateExecution::class.java),
            zaakId = "zaak1",
            toEmail = "test@test.com",
            toName = null,
            fromAddress = "noreply@test.com",
            emailSubject = "Test mail",
            contentHtml = "<p><strong>test</strong></p>",
            ccEmail = null,
            ccName = null,
            bccEmail = null,
            bccName = null,
        )

        verify(emailClient).send(messageCaptor.capture(), any(), any())

        val message = messageCaptor.firstValue

        assertThat(message.messageId).endsWith("-zaak1")
    }
}
