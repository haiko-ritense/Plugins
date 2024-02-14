package com.ritense.valtimo.backend.plugin.service

import com.ritense.valtimo.backend.plugin.domain.PublicTaskData
import com.ritense.valtimo.backend.plugin.domain.PublicTaskEntity
import com.ritense.valtimo.backend.plugin.repository.PublicTaskRepository
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class PublicTaskService(
    private val publicTaskRepository: PublicTaskRepository
) {

    fun savePublicTaskEntity(publicTaskData: PublicTaskData) {
        publicTaskRepository.save(
            PublicTaskEntity(
                publicTaskId = publicTaskData.publicTaskId,
                userTaskId = publicTaskData.userTaskId,
                assigneeCandidateContactData = publicTaskData.assigneeContactData,
                timeToLive = publicTaskData.timeToLive,
                isCompletedByPublicTask = publicTaskData.isCompletedByPublicTask
            )
        )
    }

    fun createPublicTaskUrl(
        ttl: Int,
        taskHandler: String
    ) {
        // TODO: build it ;)
        logger.info { "Created an URL with ttl: $ttl and taskhandler: $taskHandler" }

    }

    fun createPublicTaskHtml(taskUuid: String): ResponseEntity<String> {
        // TODO: build it ;)
        val formHtml = """<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Spring Boot Thymeleaf Example</title>
</head>
<body>
    <h1>Welcome to Thymeleaf with Spring Boot!</h1>
</body>
</html>
"""
        return ResponseEntity(formHtml, HttpStatus.OK)
    }

    fun completeUserTaskWithPublicTaskSubmission(submission: Map<String, Any>): ResponseEntity<String> {
        // TODO: build it ;)
        return ResponseEntity("Your response has been submitted", HttpStatus.OK)
    }

    companion object {
        val logger = KotlinLogging.logger {}
    }
}