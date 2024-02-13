package com.ritense.valtimo.backend.plugin.domain

import java.util.UUID

data class PublicTaskEntity(
    val publicTaskId: UUID,
    val userTaskId: UUID,
    val assigneeContactData: String,
    val timeToLive: Int?,
    var isCompletedByPublicTask: Boolean?
) {

    companion object {
        fun from(
            userTaskId: UUID,
            assigneeContactData: String,
            timeToLive: String?,
        ): PublicTaskEntity = PublicTaskEntity(
            publicTaskId = UUID.randomUUID(),
            userTaskId = userTaskId,
            assigneeContactData = assigneeContactData,
            timeToLive = timeToLive?.toInt() ?: 28,
            isCompletedByPublicTask = false
        )
    }
}
