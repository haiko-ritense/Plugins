package com.ritense.valtimo.backend.plugin.service

import mu.KotlinLogging

class PublicTaskService {

    fun createPublicTaskUrl(
        ttl: Int,
        taskHandler: String
    ) {
        // TODO: build it ;)
        logger.info { "Created an URL with ttl: $ttl and taskhandler: $taskHandler" }

    }

    companion object {
        val logger = KotlinLogging.logger {}
    }
}