package com.dylanmiska.receiptprocessor.api.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.util.UUID

data class IdResponse(
    @Schema(description = "The ID of the receipt", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: UUID,
)
