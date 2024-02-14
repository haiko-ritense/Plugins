package com.ritense.valtimo.backend.plugin.domain

import java.util.UUID

data class PublicTaskEntity(
    val publicTaskId: UUID,
    val userTaskId: UUID,
    val pvAssigneeCandidateContactData: String,
    val timeToLive: Int?,
    var isCompletedByPublicTask: Boolean?
) {

    companion object {
        fun from(
            userTaskId: UUID,
            assigneeCandidateContactData: String,
            timeToLive: String?,
        ): PublicTaskEntity = PublicTaskEntity(
            publicTaskId = UUID.randomUUID(),
            userTaskId = userTaskId,
            pvAssigneeCandidateContactData = assigneeCandidateContactData,
            timeToLive = timeToLive?.toInt() ?: 28,
            isCompletedByPublicTask = false
        )
    }
}
