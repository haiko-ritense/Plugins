package com.ritense.valtimo.backend.plugin.service

import com.ritense.valtimo.backend.plugin.domain.PublicTaskEntity
import mu.KotlinLogging
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.DelegateTask
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class PublicTaskService(
    private val runtimeService: RuntimeService,
) {

    @Value("\${valtimo.url}") private lateinit var baseUrl: String

    fun startNotifyAssigneeProcess(task: DelegateTask) {
        runtimeService.createMessageCorrelation(NOTIFY_ASSIGNEE_PROCESS_MESSAGE_NAME)
            .processInstanceId(task.processInstanceId)
            .setVariables(mapOf("userTaskId" to task.id))
            .processInstanceBusinessKey(task.execution.processBusinessKey)
            .correlateAll()
    }

    fun createAndSendPublicTaskUrl(
        execution: DelegateExecution,
        publicTaskEntity: PublicTaskEntity
    ) {
        val publicTaskUrl = baseUrl + PUBLIC_TASK_URL + publicTaskEntity.publicTaskId

        execution.setVariable("assigneeContactData", publicTaskEntity.assigneeContactData)
        execution.setVariable("url", publicTaskUrl)
    }

    fun createPublicTaskHtml(taskUuid: String): ResponseEntity<String> {
        // TODO: build it ;)

        // step 1: get entity and get the userTaskId

        //step 2: get form.io json with the userTaskId

        //step 3: check if task (still) exists

        // step 4: if exists-render HTML with the form.io else-send response with message: "task does not exist"
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

        private const val PUBLIC_TASK_URL = "/api/v1/public-task/"
        private const val NOTIFY_ASSIGNEE_PROCESS_MESSAGE_NAME = "startNotifyAssigneeMessage"
    }
}