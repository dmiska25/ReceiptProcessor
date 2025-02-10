package com.dylanmiska.receiptprocessor.domain.service

import com.dylanmiska.receiptprocessor.domain.extensions.updatePoints
import com.dylanmiska.receiptprocessor.domain.model.Receipt
import com.dylanmiska.receiptprocessor.persistance.repository.ReceiptRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ReceiptService(
    val receiptRepository: ReceiptRepository,
) {
    fun processReceipt(receipt: Receipt): UUID {
        receipt.updatePoints()
        return receiptRepository.save(receipt)
    }

    fun getPoints(id: UUID): Long {
        return receiptRepository.getPointsForReceipt(id) ?: throw NoSuchElementException()
    }
}
