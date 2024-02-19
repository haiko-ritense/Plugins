package com.ritense.valtimo.backend.plugin.web.rest

import com.ritense.valtimo.backend.plugin.service.PublicTaskService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/public-task"])

class PublicTaskResource(
    private val publicTaskService: PublicTaskService
) {

    @GetMapping
    fun sendPublicTaskHtml(@RequestParam publicTaskId: String): ResponseEntity<String> =
        publicTaskService.createPublicTaskHtml(publicTaskId)

    @PostMapping
    fun completeUserTask(
        @RequestParam publicTaskId: String,
        @RequestBody submission: Map<String, Any>
    ): ResponseEntity<String> = publicTaskService.completeUserTaskWithPublicTaskSubmission(submission)
}