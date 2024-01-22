package com.ritense.valtimo.amsterdam.emailapi.client

import com.ritense.valtimo.contract.audit.AuditEvent
import com.ritense.valtimo.contract.utils.SecurityUtils
import java.time.LocalDateTime
import java.util.*

class EmailApiEvent(val eventMessage: String): AuditEvent {

    override fun getId(): UUID {
        return UUID.randomUUID();
    }

    override fun getOrigin(): String {
        return "unidentified"
    }

    override fun getOccurredOn(): LocalDateTime {
        return LocalDateTime.now()
    }

    override fun getUser(): String {
       return SecurityUtils.getCurrentUserAuthentication().name
    }

    fun getMessage(): String {
        return eventMessage
    }
}
