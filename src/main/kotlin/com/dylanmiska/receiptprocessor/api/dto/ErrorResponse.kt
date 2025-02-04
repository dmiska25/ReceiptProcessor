package com.dylanmiska.receiptprocessor.api.dto

import io.swagger.v3.oas.annotations.media.Schema

data class ErrorResponse(
    @Schema(description = "The HTTP status code of the error response", example = "400")
    val status: Int,
    @Schema(description = "The error message", example = "Validation failed")
    val error: String,
    @Schema(description = "The error message(s)", example = "Validation failed")
    val message: Any,
)
