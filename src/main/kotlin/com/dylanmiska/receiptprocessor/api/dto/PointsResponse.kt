package com.dylanmiska.receiptprocessor.api.dto

import io.swagger.v3.oas.annotations.media.Schema

data class PointsResponse(
    @Schema(description = "The total points earned from the receipt", example = "100")
    val points: Long,
)
