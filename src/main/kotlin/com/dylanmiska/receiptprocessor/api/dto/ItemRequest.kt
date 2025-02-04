package com.dylanmiska.receiptprocessor.api.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Pattern

data class ItemRequest(
    @Schema(description = "The Short Product Description for the item.", example = "Bananas")
    @field:Pattern(regexp = "^[\\w\\s\\-]+$", message = "Short description must be alphanumeric and can contain spaces")
    val shortDescription: String,
    @Schema(description = "The total price payed for this item.", example = "1.00")
    @field:Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Price must be in the format d.dd")
    val price: String,
)
