package com.ritense.valtimo.backend.plugin.repository

import com.ritense.valtimo.backend.plugin.domain.PublicTaskEntity
import java.util.UUID

interface PublicTaskRepository: JpaRepository<PublicTaskEntity, UUID>