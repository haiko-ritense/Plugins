package com.ritense.valtimoplugins.objectmanagement.service

import com.fasterxml.jackson.databind.JsonNode
import com.ritense.objectenapi.ObjectenApiPlugin
import com.ritense.objectenapi.client.ObjectRecord
import com.ritense.objectenapi.client.ObjectRequest
import com.ritense.objectmanagement.domain.ObjectManagement
import com.ritense.objectmanagement.repository.ObjectManagementRepository
import com.ritense.objecttypenapi.ObjecttypenApiPlugin
import com.ritense.plugin.service.PluginService
import java.net.URI
import java.time.LocalDate
import java.util.*

class ObjectManagementCrudService(
    val pluginService: PluginService,
    val objectManagementRepository: ObjectManagementRepository
) {
    fun createObject(
        objectManagementId: UUID,
        objectData: JsonNode,
    ): URI {
        val objectManagement = getObjectManagement(objectManagementId)
        val objectenApiPlugin = getObjectenApiPlugin(objectManagement.objectenApiPluginConfigurationId)
        val objecttypenApiPlugin = getObjecttypenApiPlugin(objectManagement.objecttypenApiPluginConfigurationId)
        val objectRequest = ObjectRequest(
            objecttypenApiPlugin.getObjectTypeUrlById(objectManagement.objecttypeId),
            ObjectRecord(
                typeVersion = objectManagement.objecttypeVersion,
                data = objectData,
                startAt = LocalDate.now()
            )
        )
        return objectenApiPlugin.createObject(objectRequest).url
    }

    fun updateObject(
        objectUrl: URI,
        objectData: JsonNode,
    ) {
    }

    fun deleteObject(objectUrl: URI) {
        return
    }

    private fun getObjectenApiPlugin(objectenApiPluginConfigurationId: UUID): ObjectenApiPlugin {
        return pluginService.createInstance<ObjectenApiPlugin>(objectenApiPluginConfigurationId)

    }

    private fun getObjecttypenApiPlugin(objecttypenApiPluginConfigurationId: UUID): ObjecttypenApiPlugin {
        return pluginService.createInstance<ObjecttypenApiPlugin>(objecttypenApiPluginConfigurationId)

    }

    private fun getObjectManagement(objectManagementId: UUID): ObjectManagement {
        return objectManagementRepository.findById(objectManagementId).orElseThrow()
    }
}