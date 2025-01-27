package com.ritense.valtimoplugin.rotterdam.oracleebs.domain

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.DateTime

data class Journaalpost(
    @JsonProperty("journaalpostsleutel")
    val sleutel: String,
    @JsonProperty("journaalpostomschrijving")
    val omschrijving: String,
    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "dd-MM-yyyy hh:mm:ss"
    )
    @JsonProperty("journaalpostboekdatumTijd")
    val boekdatumTijd: DateTime,
    val grootboek: String,
    val boekjaar: String,
    val boekperiode: String,
    @JsonProperty("journaalpostcategorie")
    val categorie: String,
    @JsonProperty("journaalpostsaldosoort")
    val saldoSoort: String,
    @JsonProperty("valutacode")
    val valutaCode: String = "EUR",
    @JsonProperty("journaalpostregels")
    val journaalpostRegels: List<JournaalpostRegel>
) {

    data class JournaalpostRegel(
        val grootboekrekening: Grootboekrekening,
        @JsonProperty("journaalpostregelomschrijving")
        val omschrijving: String,
        @JsonProperty("journaalpostregelboekingtype")
        val boekingtype: String,
        @JsonProperty("journaalpostregelbedrag")
        val bedrag: Double,
        @JsonProperty("bronspecifiekewaarden")
        val bronspecifiekewaarden: List<BronspecifiekeWaarde>
    )

    data class Grootboekrekening(
        @JsonProperty("grootboeksleutel")
        val sleutel: String
    )

    data class BronspecifiekeWaarde(
        @JsonProperty("bronspecifiekewaardesegmentnaam")
        val segmentNaam: String,
        @JsonProperty("bronspecifiekewaardesegmentwaarde")
        val segmentWaarde: String,
        val volgorde: Int
    )
}
