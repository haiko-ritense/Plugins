package com.ritense.valtimo.backend.plugin.repository

import com.ritense.valtimo.backend.plugin.domain.PublicTaskEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PublicTaskRepository: JpaRepository<PublicTaskEntity, UUID>