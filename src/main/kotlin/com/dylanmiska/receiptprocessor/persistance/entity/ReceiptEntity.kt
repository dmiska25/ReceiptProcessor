package com.dylanmiska.receiptprocessor.persistance.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@Entity
@Table(name = "receipt")
data class ReceiptEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    val retailer: String,
    val purchaseDate: LocalDate,
    val purchaseTime: LocalTime,
    @OneToMany(mappedBy = "receipt", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    val items: MutableList<ItemEntity> = mutableListOf(),
    val total: Double,
    val points: Long,
)
