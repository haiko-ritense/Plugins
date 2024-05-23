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


package com.ritense.valtimo.amsterdam.emailapi.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.plugin.domain.ActivityType
import com.ritense.valtimo.amsterdam.emailapi.client.BodyPart
import com.ritense.valtimo.amsterdam.emailapi.client.EmailClient
import com.ritense.valtimo.amsterdam.emailapi.client.EmailMessage
import com.ritense.valtimo.amsterdam.emailapi.client.Recipient
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.http.*
import org.springframework.util.MimeTypeUtils
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.util.*


private const val UTF8 = "utf-8"

@Plugin(
    key = "amsterdamemailapi",
    title = "Email API Amsterdam",
    description = "Zend emails via Email API"
)
class EmailApiPlugin(
    private val emailClient: EmailClient,
    private val restTemplate: RestTemplate,
) {

    @PluginProperty(key = "emailApiBaseUrl", secret = false, required = true)
    lateinit var emailApiBaseUrl: String

    @PluginProperty(key = "subscriptionKey", secret = true, required = true)
    lateinit var subscriptionKey: String

    @PluginAction(
        key = "zend-email",
        title = "Zend email via API",
        description = "Zend een email via de Email API",
        activityTypes = [ActivityType.SERVICE_TASK_START]
    )
    fun sendEmail(
        execution: DelegateExecution,
        @PluginActionProperty toEmail: String,
        @PluginActionProperty toName: String?,
        @PluginActionProperty fromAddress: String,
        @PluginActionProperty emailSubject: String,
        @PluginActionProperty contentHtml: String,
        @PluginActionProperty ccEmail: String?,
        @PluginActionProperty ccName: String?,
        @PluginActionProperty bccEmail: String?,
        @PluginActionProperty bccName: String?,
    ) {
        val message = EmailMessage(
            to = setOf(
                Recipient(
                    address = toEmail,
                    name = toName as String
                )
            ),
            from = Recipient(address = fromAddress),
            content = setOf(
                BodyPart(
                    content = contentHtml,
                    mimeType = MimeTypeUtils.TEXT_HTML.toString(),
                    encoding = UTF8
                )
            ),
            subject = emailSubject,
        )


        // set optional values
        if(ccEmail != null) {
            message.cc = setOf(
                Recipient(
                    address = ccEmail,
                    name = ccName
                )
            )

        }

        if(bccEmail != null) {
            message.bcc = setOf(
                Recipient(
                    address = bccEmail,
                    name = bccName
                )
            )
        }

        // send
        emailClient.send(message, URI.create(emailApiBaseUrl), subscriptionKey)
    }
}
