package com.dylanmiska.receiptprocessor.persistance.repository

import com.dylanmiska.receiptprocessor.persistance.entity.ReceiptEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.Optional
import java.util.UUID

interface ReceiptRepository : JpaRepository<ReceiptEntity, UUID> {
    @Query("SELECT r.points FROM ReceiptEntity r WHERE r.id = :id")
    fun getPointsForReceipt(
        @Param("id") id: UUID,
    ): Optional<Long>
}
