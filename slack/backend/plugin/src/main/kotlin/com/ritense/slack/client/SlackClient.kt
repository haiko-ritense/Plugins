/*
 * Copyright 2015-2022 Ritense BV, the Netherlands.
 *
 * Licensed under EUPL, Version 1.2 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ritense.slack.client

import mu.KotlinLogging
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.io.InputStream
import java.net.URI

class SlackClient(
    private val webClient: WebClient,
    var baseUri: URI?,
    var token: String?,
) {

    /**
     * https://api.slack.com/methods/chat.postMessage
     */
    fun chatPostMessage(channel: String, message: String) {
        logger.debug { "Post message in slack ('$message')" }

        val multipartFormData = mutableMapOf(
            "channel" to channel,
            "text" to message,
        )

        post("/api/chat.postMessage", multipartFormData)
    }

    /**
     * https://api.slack.com/methods/files.upload
     */
    fun filesUpload(channels: String, message: String?, fileName: String, file: InputStream) {
        logger.debug { "Post message with file in slack ('$message', '$fileName')" }
        val fileNameParts = fileName.split('.')

        val multipartFormData = mutableMapOf(
            "channels" to channels,
            "filename" to fileName,
            "title" to fileNameParts[0],
            "filetype" to fileNameParts[1],
            "content" to InputStreamResource(file)
        )

        message?.let { multipartFormData["initial_message"] = it }

        post("/api/files.upload", multipartFormData)
    }

    private fun post(path: String, multipartFormData: Map<String, Any>) {
        val builder = MultipartBodyBuilder()
        for (entry in multipartFormData.entries) {
            builder.part(entry.key, entry.value)
        }

        val response = webClient
            .mutate()
            .defaultHeaders { httpHeaders ->
                httpHeaders.set(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
                httpHeaders.setBearerAuth(token!!)
            }
            .build()
            .post()
            .uri {
                it.scheme(baseUri!!.scheme)
                    .host(baseUri!!.host)
                    .path(baseUri!!.path)
                    .path(path)
                    .port(baseUri!!.port)
                    .build()
            }
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromMultipartData(builder.build()))
            .retrieve()
            .bodyToMono<SlackResponse>()
            .block()!!

        if (!response.ok) {
            throw SlackException(response.error)
        }
    }

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
