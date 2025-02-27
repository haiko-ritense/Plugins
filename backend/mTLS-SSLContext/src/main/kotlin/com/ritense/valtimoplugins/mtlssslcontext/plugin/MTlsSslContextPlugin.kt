/*
 * Copyright 2015-2025 Ritense BV, the Netherlands.
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


package com.ritense.valtimoplugins.mtlssslcontext.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.valtimoplugins.mtlssslcontext.plugin.MTlsSslContextPlugin.Companion.PLUGIN_KEY
import io.github.oshai.kotlinlogging.KotlinLogging

@Plugin(
    key = PLUGIN_KEY,
    title = "mTLS SSLContext Plugin",
    description = "Creates SSLContexts to acts as an environment for implementing Transport Layer Security (TLS) or Secure Sockets Layer (SSL) protocols"
)
@Suppress("UNUSED")
class MTlsSslContextPlugin {
    @PluginProperty(key = "serverCertificate", secret = true, required = false)
    var serverCertificate: String? = null

    @PluginProperty(key = "clientPrivateKey", secret = true, required = false)
    var clientPrivateKey: String? = null

    @PluginProperty(key = "clientCertificate", secret = true, required = false)
    var clientCertificate: String? = null

    companion object {
        private val logger = KotlinLogging.logger { }
        const val PLUGIN_KEY = "mtls-sslcontext"
    }

}
