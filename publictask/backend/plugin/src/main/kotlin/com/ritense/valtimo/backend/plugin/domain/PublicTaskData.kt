package com.ritense.valtimo.backend.plugin.domain

import java.time.ZonedDateTime
import java.util.UUID

data class PublicTaskData(
    val publicTaskId: UUID,
    val userTaskId: UUID,
    val processBusinessKey: String,
    val assigneeCandidateContactData: String,
    val taskExpirationDate: String,
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
            taskExpirationDate = ZonedDateTime.now().plusDays(timeToLive?.toLong() ?: 28L ).toString(),
            isCompletedByPublicTask = false
        )
    }
}