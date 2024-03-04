/*
 * Copyright 2015-2024. Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.ritense.valtimo.berkelybridge.client

import com.ritense.valtimo.berkelybridge.plugin.BerkelyBridgeClientEvent
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import java.awt.Stroke
import java.net.URI


private val logger = KotlinLogging.logger {}

class BerkelyBridgeClient(
    private val restTemplate: RestTemplate,
    private val eventPublisher: ApplicationEventPublisher,
) {
    fun generate(bbUrl: String, modelId: String, templateId: String, parameterMap: MutableMap<String, Object>, naam: String) {
        try {
            logger.debug { "generating with template $templateId and model: $modelId" }

            val requestBody = BerkelyBridgeRequestBody(templateId = templateId, parameters = parameterMap, naam = naam)

            val headers = HttpHeaders()
            headers.set("Content-Type", MediaType.APPLICATION_JSON.toString())
            val httpEntity: HttpEntity<BerkelyBridgeRequestBody> = HttpEntity(requestBody, headers)

            var response: ResponseEntity<String> = restTemplate.postForEntity(bbUrl, httpEntity, String::class.java)

            if(response.statusCode.is2xxSuccessful) {
                var event = BerkelyBridgeClientEvent("successfully generated")
                eventPublisher.publishEvent(event)
            }
            else if (response.statusCode.equals(HttpStatus.BAD_REQUEST)) {
                var event = BerkelyBridgeClientEvent("failed to generate")
                eventPublisher.publishEvent(event)
                logger.warn { "failed to generate for templated: $templateId and model: $modelId"  }
            }
            else if (response.statusCode.equals(HttpStatus.UNAUTHORIZED)) {
                logger.warn { "berkely bridge generating unauthorized" }
            }
        } catch (e: Exception) {
            logger.error { "error berkely bridge generating  \n" + e.message }
            throw e
        }
    }
}
