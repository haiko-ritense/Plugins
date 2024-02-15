package com.ritense.valtimo.backend.plugin.service

import com.ritense.document.service.DocumentService
import com.ritense.formlink.service.impl.CamundaFormAssociationService
import com.ritense.valtimo.backend.plugin.domain.PublicTaskEntity
import com.ritense.valtimo.backend.plugin.domain.PublicTaskData
import com.ritense.valtimo.backend.plugin.domain.PublicTaskEntity
import com.ritense.valtimo.backend.plugin.repository.PublicTaskRepository
import mu.KotlinLogging
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.delegate.DelegateExecution
import org.camunda.bpm.engine.delegate.DelegateTask
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.util.Optional

class PublicTaskService(
    private val runtimeService: RuntimeService,
    private val formAssociationService: CamundaFormAssociationService,
    private val documentService: DocumentService
) {

    @Value("\${valtimo.url}") private lateinit var baseUrl: String
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

    fun startNotifyAssigneeCandidateProcess(task: DelegateTask) {
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

        execution.setVariable("assigneeContactData", publicTaskEntity.pvAssigneeCandidateContactData)
        execution.setVariable("url", publicTaskUrl)
    }

    fun createPublicTaskHtml(
        taskUuid: String,
        businessKey: String
    ): ResponseEntity<String> {
        // TODO: build it ;)

        // step 1: get entity and get the userTaskId

        // step 2: check if task (still) exists, if not return a html with message: "task does not exist"

        // step 3: get form.io json with the userTaskId
        val formKey = ""

        // step 4: get prefilled data from ?
        val document = Optional.of(documentService.get(businessKey).id())
        val prefilledFormIoForm = formAssociationService.getPreFilledFormDefinitionByFormKey(formKey, document)

        // step 5: render HTML with the form.io

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