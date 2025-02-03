package com.dylanmiska.receiptprocessor.domain.model

import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class Receipt(
    val id: UUID? = null,
    val retailer: String,
    val purchaseDate: LocalDate,
    val purchaseTime: LocalTime,
    val items: List<Item> = listOf(),
    val total: Double,
    var points: Long?,
)
