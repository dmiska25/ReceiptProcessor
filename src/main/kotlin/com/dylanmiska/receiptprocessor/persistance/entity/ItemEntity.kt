package com.dylanmiska.receiptprocessor.persistance.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "item")
data class ItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    @ManyToOne
    @JoinColumn(name = "receipt_id", nullable = false)
    val receipt: ReceiptEntity? = null,
    val shortDescription: String,
    val price: Double,
)
