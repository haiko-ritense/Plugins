package com.ritense.valtimoplugin.rotterdam.oracleebs.domain

data class JournaalpostOpvoeren(
    val procescode: String,
    val referentieNummer: String,
    val journaalpost: Journaalpost
)
