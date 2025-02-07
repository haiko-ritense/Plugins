package com.ritense.valtimoplugin.rotterdam.oracleebs.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.processlink.domain.ActivityTypeWithEventName
import com.ritense.valtimoplugin.rotterdam.oracleebs.service.EsbClient
import com.rotterdam.esb.opvoeren.models.Grootboekrekening
import com.rotterdam.esb.opvoeren.models.Journaalpost
import com.rotterdam.esb.opvoeren.models.Journaalpostregel
import com.rotterdam.esb.opvoeren.models.OpvoerenJournaalpostVraag
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientResponseException
import java.net.URI
import java.time.OffsetDateTime
import java.util.UUID

@Plugin(
    key = "rotterdam-oracle-ebs-journaal-post",
    title = "Gemeente Rotterdam - Oracle EBS - Journaal Post Plugin",
    description = "Deze plugin maakt het mogelijk om Journaalpost acties uit te voeren in Oracle E-Business Suite via de ESB van de Gemeente Rotterdam"
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
    fun opvoeren(
        execution: DelegateExecution,
        @PluginActionProperty templateId: UUID,
    ) {
        logger.info { "Journaalpost Opvoeren" }
        OpvoerenJournaalpostVraag(
            procescode = "",
            referentieNummer = "",
            journaalpost = Journaalpost(
                journaalpostsleutel = "",
                journaalpostboekdatumTijd = OffsetDateTime.parse("2020-06-25T00:00:00.000Z"),
                journaalpostcategorie = "",
                journaalpostsaldosoort = Journaalpost.Journaalpostsaldosoort.Werkelijk,
                valutacode = Journaalpost.Valutacode.EUR,
                journaalpostregels = listOf(Journaalpostregel(
                    grootboekrekening = Grootboekrekening(
                        grootboeksleutel = "",
                        bronsleutel = null
                    ),
                    journaalpostregelboekingtype = Journaalpostregel.Journaalpostregelboekingtype.Debet,
                    journaalpostregelbedrag = 0.0,
                    journaalpostregelomschrijving = null,
                    bronspecifiekewaarden = null
                )),
                journaalpostomschrijving = null,
                grootboek = null,
                boekjaar = null,
                boekperiode = null
            )
        ).let { request ->
            try {
                esbClient.journaalPostApi(restClient()).opvoerenJournaalpost(request).let { response ->
                    logger.debug { "Journaalpost Opvoeren response: $response" }
                    if (!response.isGeslaagd) {
                        throw RuntimeException("Journaalpost Opvoeren response: $response")
                    }
                }
            } catch (ex: RestClientResponseException) {
                logger.error(ex) { "Something went wrong. ${ex.message}" }
                throw ex
            }
        }
    }

    private fun restClient(): RestClient =
        esbClient.createRestClient(
            baseUrl = baseUrl.toString(),
            base64PrivateKey = clientPrivateKey,
            base64ClientCert = clientCertificate,
            base64ServerCert = serverCertificate
        )

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
