package com.ritense.valtimoplugins.objectmanagement.plugin

import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.processlink.domain.ActivityTypeWithEventName
import com.ritense.valtimoplugins.objectmanagement.service.ObjectManagementCrudService

@Plugin(
    key = "object-management",
    title = "Object Management",
    description = "Plugin for CRUD actions on the Objects registration"
)
open class ObjectManagementPlugin(
    objectManagementCrudService: ObjectManagementCrudService
) {
    @PluginAction(
        key = "create-object",
        title = "Create Object",
        description = "Create a new Object",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START]
    )
    open fun createObject() {

    }

    @PluginAction(
        key = "update-object",
        title = "Update Object",
        description = "Update an existing Object",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START]
    )
    open fun updateObject() {

    }

    @PluginAction(
        key = "delete-object",
        title = "Delete Object",
        description = "Delete an existing Object",
        activityTypes = [ActivityTypeWithEventName.SERVICE_TASK_START]
    )
    open fun deleteObject() {

    }
}