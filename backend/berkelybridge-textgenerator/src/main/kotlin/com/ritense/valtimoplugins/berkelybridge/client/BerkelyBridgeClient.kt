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

package com.ritense.valtimoplugins.berkelybridge.client

import com.ritense.valtimoplugins.berkelybridge.plugin.BerkelyBridgeClientEvent
import com.ritense.valtimoplugins.berkelybridge.plugin.TemplateProperty
import mu.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.*
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestTemplate
import java.net.URL
import kotlin.io.path.fileVisitor


private val logger = KotlinLogging.logger {}
private const val headerKey = "X-BB-SUBSCRIPTIONKEY"


class BerkelyBridgeClient(
    private val restTemplate: RestTemplate,
    private val eventPublisher: ApplicationEventPublisher,
) {
    lateinit var subscriptionKey: String

    fun addSubscriptionKey() {
        logger.debug { "setting header subscription key: $headerKey" }
        restTemplate.interceptors.add(ClientHttpRequestInterceptor { request, body, execution ->
            request.headers.set(headerKey, subscriptionKey)
            execution.execute(request, body)
        })
    }


    fun generate(bbUrl: String, modelId: String, templateId: String, parameters: List<TemplateProperty>?, naam: String, format: String): String {
        this.addSubscriptionKey()
        val openResponse = openFile(bbUrl, templateId, modelId, parameters, naam, format)
        val fileUrl = getDataLink(bbUrl, modelId, openResponse.sessionid, openResponse.uniqueid, format)
        return getFile(bbUrl, fileUrl)
    }

    fun generateFile(bbUrl: String, modelId: String, templateId: String, parameters: List<TemplateProperty>?, naam: String, format: String): String {
        this.addSubscriptionKey()
        val openResponse = openFile(bbUrl, templateId, modelId, parameters, naam, format)
        val fileUrl = getDataLink(bbUrl, modelId, openResponse.sessionid, openResponse.uniqueid, format)
        return fileUrl
    }

    private fun openFile(
        bbUrl: String,
        templateId: String,
        modelId: String,
        parameters: List<TemplateProperty>?,
        naam: String,
        format: String
    ): OpenResponse {
        try {
            logger.debug { "generating with template $templateId and model: $modelId" }

            val openUrl = "$bbUrl/open?modelId=$modelId&fmt=json"

            val requestBody = OpenRequestBody(templateId = templateId, naam = naam, format = format)
            parameters?.forEach { prop ->
                requestBody.parameters.put(prop.key, prop.value);
            }

            val headers = HttpHeaders()
            headers.set("Content-Type", MediaType.APPLICATION_JSON.toString())
            val httpEntity: HttpEntity<OpenRequestBody> = HttpEntity(requestBody, headers)

            var response: ResponseEntity<OpenResponse> =
                restTemplate.postForEntity(openUrl, httpEntity, OpenResponse::class.java)

            if (response.statusCode.is2xxSuccessful) {
                var event = BerkelyBridgeClientEvent("successfully generated")
                eventPublisher.publishEvent(event)
                return response.body
            }
            else {
                logger.error { "failed to generate for templated: $templateId and model: $modelId \n status: ${response.statusCode}"}
                throw IllegalStateException("could not generate a file")
            }
        } catch (e: Exception) {
            logger.error { "error berkely bridge generating  \n" + e.message }
            throw e
        }
    }

    private fun getDataLink(bbUrl: String, modelId: String, sessionId: String, uniqueId: String, format: String): String {
        try {
            logger.debug { "getting files for sessionId: $sessionId and uniqueId:$uniqueId" }

            val getFilesUrl = "$bbUrl/getfiles?modelid=$modelId&sessionid=$sessionId&uniqueid=$uniqueId&fmt=json"


            var response: ResponseEntity<GetFilesResponse> =
                restTemplate.getForEntity(getFilesUrl, GetFilesResponse::class.java)

            if (response.statusCode.is2xxSuccessful && response.body.filelist.size > 0) {
                var event = BerkelyBridgeClientEvent("successfully retrieved filelist")
                eventPublisher.publishEvent(event)

                var filtered = response.body.filelist.filter { entry -> entry.value.contains(format, ignoreCase = true)}

                if(filtered.size == 1 ) {
                    return filtered.get(0).href
                }
                else {
                    logger.error { "failed to retrieve file with format ${format} " +
                            "\n Present formats are ${response.body.filelist.map{ entry -> entry.value }}"}
                    throw IllegalStateException("could not retrieve file with format ${format}")
                }
            }
            else {
                logger.error { "failed to retrieve filelist status: ${response.statusCode}"}
                throw IllegalStateException("could not retrieve filelist")
            }
        } catch (e: Exception) {
            logger.error { "failed to retrieve filelist  \n" + e.message }
            throw e
        }
    }

    private fun getFile(bbUrl: String, fileUrl: String): String {
        try {
            logger.debug { "getting file for fileUrl: $fileUrl " }

            val getFileUrl = "$bbUrl/$fileUrl"


            var response: ResponseEntity<String> =
                restTemplate.getForEntity(getFileUrl, String::class.java)

            if (response.statusCode.is2xxSuccessful) {
                var event = BerkelyBridgeClientEvent("successfully retrieved file")
                eventPublisher.publishEvent(event)

                return response.body
            }
            else {
                logger.error { "failed to retrieve file status: ${response.statusCode}"}
                throw IllegalStateException("could not retrieve file")
            }
        } catch (e: Exception) {
            logger.error { "error berkely bridge retrieving file  \n" + e.message }
            throw e
        }
    }
}
