package com.ritense.valtimoplugins.rotterdam.oracleebs.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.annotation.PluginProperty
import com.ritense.processlink.domain.ActivityTypeWithEventName
import com.ritense.valtimoplugins.rotterdam.oracleebs.service.EsbClient
import com.rotterdam.esb.opvoeren.models.Factuurregel
import com.rotterdam.esb.opvoeren.models.Grootboekrekening
import com.rotterdam.esb.opvoeren.models.Journaalpost
import com.rotterdam.esb.opvoeren.models.Journaalpostregel
import com.rotterdam.esb.opvoeren.models.NatuurlijkPersoon
import com.rotterdam.esb.opvoeren.models.NietNatuurlijkPersoon
import com.rotterdam.esb.opvoeren.models.OpvoerenJournaalpostVraag
import com.rotterdam.esb.opvoeren.models.OpvoerenVerkoopfactuurVraag
import com.rotterdam.esb.opvoeren.models.RelatieRotterdam
import com.rotterdam.esb.opvoeren.models.Verkoopfactuur
import io.github.oshai.kotlinlogging.KotlinLogging
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClientResponseException
import java.math.BigDecimal
import java.net.URI
import java.time.LocalDate
import java.time.OffsetDateTime

@Plugin(
    key = "rotterdam-oracle-ebs",
    title = "Gemeente Rotterdam - Oracle EBS plugin",
    description = "Deze plugin maakt het mogelijk om Journaalpost acties uit te voeren in Oracle E-Business Suite via de ESB van de Gemeente Rotterdam"
)
class OracleEbsPlugin(
    private val esbClient: EsbClient,
) {

    @PluginProperty(key = "baseUrl", secret = false, required = true)
    lateinit var baseUrl: URI

    @PluginProperty(key = "authenticationEnabled", secret = false, required = true)
    var authenticationEnabled: String = "false"

    @PluginProperty(key = "base64ServerCertificate", secret = true, required = false)
    var base64ServerCertificate: String? = null

    @PluginProperty(key = "base64ClientPrivateKey", secret = true, required = false)
    var base64ClientPrivateKey: String? = null

    @PluginProperty(key = "base64ClientCertificate", secret = true, required = false)
    var base64ClientCertificate: String? = null

    @PluginAction(
        key = "journaalpost-opvoeren",
        title = "Journaalpost Opvoeren",
        description = "Het opvoeren van een Journaalpost in Oracle EBS",
        activityTypes = [
            ActivityTypeWithEventName.SERVICE_TASK_START
        ]
    )
    fun journaalpostOpvoeren(
        execution: DelegateExecution,
        @PluginActionProperty procesCode: String,
        @PluginActionProperty grootboekSleutel: String,
        @PluginActionProperty sleutel: String,
        @PluginActionProperty categorie: String,
    ) {
        logger.info {
            "Journaalpost Opvoeren(" +
                "processCode: $procesCode, " +
                "grootboekSleutel: $grootboekSleutel, " +
                "sleutel: $sleutel, " +
                "categorie: $categorie" +
            ")"
        }
        OpvoerenJournaalpostVraag(
            procescode = procesCode,
            referentieNummer = "",
            journaalpost = Journaalpost(
                journaalpostsleutel = sleutel,
                journaalpostboekdatumTijd = OffsetDateTime.parse("2020-06-25T00:00:00.000Z"),
                journaalpostcategorie = categorie,
                journaalpostsaldosoort = Journaalpost.Journaalpostsaldosoort.Werkelijk,
                valutacode = Journaalpost.Valutacode.EUR,
                journaalpostregels = listOf(Journaalpostregel(
                    grootboekrekening = Grootboekrekening(
                        grootboeksleutel = grootboekSleutel,
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
                esbClient.journaalPostenApi(restClient()).opvoerenJournaalpost(request).let { response ->
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

    @PluginAction(
        key = "verkoopfactuur-opvoeren",
        title = "Verkoopfactuur Opvoeren",
        description = "Het opvoeren van een Journaalpost in Oracle EBS",
        activityTypes = [
            ActivityTypeWithEventName.SERVICE_TASK_START
        ]
    )
    fun verkoopfactuurOpvoeren(
        execution: DelegateExecution,
        @PluginActionProperty procesCode: String,
        @PluginActionProperty grootboekSleutel: String
    ) {
        logger.info {
            "Verkoopfactuur Opvoeren(" +
                "processCode: $procesCode, " +
                "grootboekSleutel: $grootboekSleutel" +
            ")"
        }
        OpvoerenVerkoopfactuurVraag(
            procescode = procesCode,
            referentieNummer = "",
            factuur = Verkoopfactuur(
                factuurtype = Verkoopfactuur.Factuurtype.Verkoopfactuur,
                factuurklasse= Verkoopfactuur.Factuurklasse.Debetnota,
                factuurdatum = LocalDate.now(),
                inkooporderreferentie = "",
                koper = RelatieRotterdam(
                    natuurlijkPersoon = NatuurlijkPersoon(
                        achternaam = "",
                        voornamen = "",
                        bsn = null,
                        relatienaam = null,
                        tussenvoegsel = null,
                        titel = null,
                        telefoonnummer = null,
                        mobielnummer = null,
                        email = null,
                        vestigingsadres = null
                    ),
                    nietNatuurlijkPersoon = NietNatuurlijkPersoon(
                        statutaireNaam = "",
                        kvknummer = null,
                        kvkvestigingsnummer = null,
                        rsin = null,
                        ion = null,
                        rechtsvorm = null,
                        datumAanvang = null,
                        datumEinde = null,
                        telefoonnummer = null,
                        email = null,
                        website = null,
                        tijdstipRegistratie = null,
                        btwnummer = null,
                        vestigingsadres = null
                    ),
                    relatienummerRotterdam = null
                ),
                factuurregels = listOf(Factuurregel(
                    factuurregelFacturatieHoeveelheid = BigDecimal(0),
                    factuurregelFacturatieTarief = BigDecimal(0),
                    btwPercentage = "",
                    grootboekrekening = Grootboekrekening(
                        grootboeksleutel = grootboekSleutel,
                        bronsleutel = null,
                    ),
                    factuurregelomschrijving = null,
                    factuurregelFacturatieEenheid = null,
                    boekingsregel = null,
                    boekingsregelStartdatum = null,
                    ontvangstenGrootboekrekening = null,
                    factuurregelToeslagKortingen = null,
                    bronspecifiekewaarden = null,
                    artikel = null,
                    regelnummer = null
                )),
                transactiesoort = null,
                factuurnummer = null,
                factuurvervaldatum = null,
                factureerregel = null,
                factuurkenmerk = null,
                factuurtoelichting = null,
                gerelateerdFactuurnummer = null,
                factuuradres = null,
                // Alleen EUR wordt ondersteund
                valutacode = "EUR",
                grootboekdatum = null,
                grootboekjaar = null,
                bronspecifiekewaarden = null
            ),
            bijlage = null
        ).let { request ->
            try {
                esbClient.verkoopFacturenApi(restClient()).opvoerenVerkoopfactuur(request).let { response ->
                    logger.debug { "Verkoopfactuur Opvoeren response: $response" }
                    if (!response.isGeslaagd) {
                        throw RuntimeException("Verkoopfactuur Opvoeren response: $response")
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
            authenticationEnabled = authenticationEnabled.toBoolean(),
            base64PrivateKey = base64ClientPrivateKey,
            base64ClientCert = base64ClientCertificate,
            base64ServerCert = base64ServerCertificate
        )

    companion object {
        private val logger = KotlinLogging.logger {}
    }
}
