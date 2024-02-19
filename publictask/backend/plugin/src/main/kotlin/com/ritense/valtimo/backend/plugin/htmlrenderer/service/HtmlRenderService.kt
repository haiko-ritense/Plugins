package com.ritense.valtimo.backend.plugin.htmlrenderer.service

import com.ritense.valtimo.backend.plugin.htmlrenderer.config.FreemarkerConfig
import freemarker.template.Template
import java.io.StringWriter

class HtmlRenderService(
    private val freemarkerConfig: FreemarkerConfig
) {

   fun generatePublicTaskHtml(
       fileName: String,
       variables: Map<String, Any> = emptyMap()): String =
    with(StringWriter()) {
        Template(
            fileName,
            getResourceFileAsString("$PUBLIC_TASK_TEMPLATE_PATH/$fileName.ftl"),
            freemarkerConfig
        ).process(variables, this)
        return this.toString()
    }

    private fun getResourceFileAsString(filePath: String): String? =
        javaClass.classLoader.getResourceAsStream(filePath)?.bufferedReader().use { it?.readText() }

    companion object {
        private const val PUBLIC_TASK_TEMPLATE_PATH = "config/template"
    }
}