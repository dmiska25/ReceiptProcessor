package com.dylanmiska.receiptprocessor.api.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class ProcessReceiptRequest(
    @Schema(description = "The name of the retailer or store the receipt is from.", example = "M&M Corner Market")
    @field:Pattern(regexp = "^[\\w\\s\\-&]+$", message = "Retailer name must be alphanumeric and can contain spaces")
    val retailer: String,
    @Schema(description = "The date of the purchase printed on the receipt in format yyyy-MM-dd.", example = "2022-01-01")
    @field:Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Purchase date must be in the format yyyy-MM-dd")
    val purchaseDate: String,
    @Schema(description = "The time of the purchase printed on the receipt in 24-hour format HH:mm", example = "13:01")
    @field:Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Purchase time must be in the format HH:mm")
    val purchaseTime: String,
    @Schema(description = "List of items in the receipt")
    @field:Valid
    @field:Size(min = 1, message = "There must be at least one item")
    val items: List<@Valid ItemRequest>,
    @Schema(description = "The total amount paid on the receipt in format d.dd", example = "6.49")
    @field:Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Total must be in the format d.dd")
    val total: String,
)
