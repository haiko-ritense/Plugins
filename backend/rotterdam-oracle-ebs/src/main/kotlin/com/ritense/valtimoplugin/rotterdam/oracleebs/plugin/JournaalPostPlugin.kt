package com.ritense.valtimoplugin.rotterdam.oracleebs.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.processlink.domain.ActivityTypeWithEventName
import com.ritense.valtimoplugin.rotterdam.oracleebs.service.EsbClient
import com.rotterdam.opvoeren.models.OpvoerenJournaalpostVraag
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.net.URI

@Plugin(
    key = "twitter",
    title = "Twitter Plugin",
    description = "Tweet and retweet with this new Twitter plugin"
)
class JournaalPostPlugin(
    private val esbClient: EsbClient,
) {

    @PluginProperty(key = "baseUrl", secret = false, required = true)
    lateinit var baseUrl: URI

    @PluginProperty(key = "serverCertificate", secret = true, required = false)
    var serverCertificate: String? = null

    @PluginProperty(key = "clientPrivateKey", secret = true, required = false)
    var clientPrivateKey: String? = null

    @PluginProperty(key = "clientCertificate", secret = true, required = false)
    var clientCertificate: String? = null

    @PluginAction(
        key = "journaalpost-opvoeren",
        title = "Journaalpost Opvoeren",
        description = "",
        activityTypes = [
            ActivityTypeWithEventName.SERVICE_TASK_START
        ]
    )
    fun opvoeren(execution: DelegateExecution) {
        OpvoerenJournaalpostVraag(

        ).let { request ->
            esbClient.journaalPostApi().opvoerenJournaalpost(request).let { response ->

            }
        }
    }
}
