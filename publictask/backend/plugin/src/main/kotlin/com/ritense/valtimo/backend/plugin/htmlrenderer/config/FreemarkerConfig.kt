package com.ritense.valtimo.backend.plugin.htmlrenderer.config

import freemarker.cache.ClassTemplateLoader
import freemarker.template.Configuration
import freemarker.template.TemplateExceptionHandler

class FreemarkerConfig : Configuration(VERSION_2_3_31) {
    init {
        templateLoader = ClassTemplateLoader(javaClass, "/config/template")
        defaultEncoding = Charsets.UTF_8.toString()
        templateExceptionHandler = TemplateExceptionHandler.RETHROW_HANDLER
    }
}