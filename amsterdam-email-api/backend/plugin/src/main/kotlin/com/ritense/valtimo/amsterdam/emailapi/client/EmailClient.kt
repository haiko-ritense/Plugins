package com.ritense.valtimo.amsterdam.emailapi.client

import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.net.URI


private val logger = KotlinLogging.logger {}

class EmailClient(
    private val restTemplate: RestTemplate,
    private val eventPublisher: ApplicationEventPublisher,
) {
    fun send(message: EmailMessage, baseUri: URI, apiKey: String?) {
        try {
            logger.debug { "sending email naar "  + message.to.toString() + " met onderwerp " + message.subject}

            val headers = HttpHeaders()
            headers.set("X-APIKEY", apiKey)
            val httpEntity: HttpEntity<EmailMessage> = HttpEntity(message, headers)
            var response: ResponseEntity<EmailMessage> = restTemplate.postForEntity(baseUri, httpEntity, EmailMessage::class.java)

            if(response.statusCode.is2xxSuccessful) {
                eventPublisher.publishEvent(EmailApiEvent("successfully send email"))
            }
            else if (response.statusCode.equals(HttpStatus.BAD_REQUEST)) {
                logger.warn { "email invalide of incompleet \n" + message.toString()}
            }
            else if (response.statusCode.equals(HttpStatus.UNAUTHORIZED)) {
                logger.warn { "email request unauthorized" }
            }
        } catch (e: Exception) {
            logger.error { "error sending email \n" + e.message }
            throw e
        }
    }
}
