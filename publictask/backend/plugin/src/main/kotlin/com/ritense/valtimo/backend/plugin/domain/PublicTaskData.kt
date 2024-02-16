package com.ritense.valtimo.backend.plugin.domain

import java.util.UUID

data class PublicTaskData(
    val publicTaskId: UUID,
    val userTaskId: UUID,
    val assigneeCandidateContactData: String,
    val timeToLive: Int,
    var isCompletedByPublicTask: Boolean
) {

    companion object {
        fun from(
            userTaskId: UUID,
            assigneeCandidateContactData: String,
            timeToLive: String?,
        ): PublicTaskData = PublicTaskData(
            publicTaskId = UUID.randomUUID(),
            userTaskId = userTaskId,
            assigneeCandidateContactData = assigneeCandidateContactData,
            timeToLive = timeToLive?.toInt() ?: 28,
            isCompletedByPublicTask = false
        )
    }
}