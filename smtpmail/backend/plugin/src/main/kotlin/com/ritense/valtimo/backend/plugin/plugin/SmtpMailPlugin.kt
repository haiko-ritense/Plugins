package com.ritense.valtimo.backend.plugin.plugin

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

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.plugin.domain.ActivityType
import com.ritense.valtimo.backend.plugin.service.SmtpMailService
import com.ritense.valueresolver.ValueResolverService
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.net.URI

@Plugin(
    key = "smtp-mail",
    title = "SMTP mail Plugin",
    description = "Send mail through SMTP with the SMTP mail plugin"
)
class SmtpMailPlugin(
    private val smtpMailService: SmtpMailService,
) {

    @PluginProperty(key = "host", secret = false, required = true)
    lateinit var host: String

    @PluginProperty(key = "port", secret = false, required = true)
    lateinit var port: String

    @PluginProperty(key = "username", required = false, secret = false)
    lateinit var username: String

    @PluginProperty(key = "password", required = false, secret = true)
    lateinit var password: String

    @PluginProperty(key = "protocol", required = false, secret = false)
    lateinit var protocol: String

    @PluginProperty(key = "debug", required = false, secret = false)
    lateinit var debug: String

    @PluginProperty(key = "auth", required = false, secret = false)
    lateinit var auth: String

    @PluginProperty(key = "starttlsenable", required = false, secret = false)
    lateinit var starttlsenable: String

    @PluginAction(
        key = "send-mail",
        title = "Send mail",
        description = "Send an email",
        activityTypes = [ActivityType.SERVICE_TASK_START]
    )
    fun sendMail(
        execution: DelegateExecution,
        @PluginActionProperty sender: String,
        ) = smtpMailService.sendZivverMail(execution)
}