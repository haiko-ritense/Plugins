package com.ritense.valtimo.backend.plugin.domain

import java.util.UUID

data class PublicTaskData(
    val publicTaskId: UUID,
    val userTaskId: UUID,
    val processBusinessKey: String,
    val assigneeCandidateContactData: String,
    val timeToLive: Int,
    var isCompletedByPublicTask: Boolean
) {

    companion object {
        fun from(
            userTaskId: UUID,
            processBusinessKey: String,
            assigneeCandidateContactData: String,
            timeToLive: String?,
        ): PublicTaskData = PublicTaskData(
            publicTaskId = UUID.randomUUID(),
            userTaskId = userTaskId,
            processBusinessKey = processBusinessKey,
            assigneeCandidateContactData = assigneeCandidateContactData,
            timeToLive = timeToLive?.toInt() ?: 28,
            isCompletedByPublicTask = false
        )
    }
}