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

package com.ritense.valtimo.backend.plugin.plugin


import com.ritense.plugin.annotation.Plugin
import com.ritense.plugin.annotation.PluginAction
import com.ritense.plugin.annotation.PluginActionProperty
import com.ritense.plugin.domain.ActivityType
import com.ritense.valtimo.backend.plugin.domain.PublicTaskEntity
import com.ritense.valtimo.backend.plugin.service.PublicTaskService
import org.camunda.bpm.engine.delegate.DelegateExecution
import java.util.*

@Plugin(
    key = "public-task",
    title = "Public Task Plugin",
    description = "Expose a public task outside the Valtimo UI with the Public Task plugin"
)
class PublicTaskPlugin(
    private val publicTaskService: PublicTaskService
) {

    @PluginAction(
        key = "create-public-task",
        title = "Create Public Task",
        description = "create a public task and expose it",
        activityTypes = [ActivityType.SERVICE_TASK_START]
    )

    fun createPublicTask(
        execution: DelegateExecution,
        @PluginActionProperty pvAssigneeContactData: String,
        @PluginActionProperty ttl: String?,
    ) {

        val publicTaskEntity = PublicTaskEntity.from(
            userTaskId = UUID.fromString(execution.getVariableLocal("userTaskId") as String),
            assigneeContactData = pvAssigneeContactData,
            timeToLive = ttl
        )

        publicTaskService.createAndSendPublicTaskUrl(
            execution = execution,
            publicTaskEntity =  publicTaskEntity
        )

        // TODO: store publicTaskEntity in DB
    }
}