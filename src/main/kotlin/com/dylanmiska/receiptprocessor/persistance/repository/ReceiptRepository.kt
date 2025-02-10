package com.dylanmiska.receiptprocessor.persistance.repository

import com.dylanmiska.receiptprocessor.domain.model.Receipt
import org.jooq.DSLContext
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import com.dylanmiska.jooq.generated.tables.Item as ItemTable
import com.dylanmiska.jooq.generated.tables.Receipt as ReceiptTable

@Repository
class ReceiptRepository(
    private val dsl: DSLContext,
) {
    @Transactional
    fun save(receipt: Receipt): UUID {
        val receiptId = receipt.id ?: UUID.randomUUID()

        dsl.insertInto(ReceiptTable.RECEIPT)
            .set(ReceiptTable.RECEIPT.ID, receiptId)
            .set(ReceiptTable.RECEIPT.RETAILER, receipt.retailer)
            .set(ReceiptTable.RECEIPT.PURCHASE_DATE, receipt.purchaseDate)
            .set(ReceiptTable.RECEIPT.PURCHASE_TIME, receipt.purchaseTime)
            .set(ReceiptTable.RECEIPT.TOTAL, receipt.total.toBigDecimal())
            .set(ReceiptTable.RECEIPT.POINTS, receipt.points ?: 0L)
            .execute()

        receipt.items?.forEach {
            val itemId = UUID.randomUUID()
            dsl.insertInto(ItemTable.ITEM)
                .set(ItemTable.ITEM.ID, itemId)
                .set(ItemTable.ITEM.RECEIPT_ID, receiptId)
                .set(ItemTable.ITEM.SHORT_DESCRIPTION, it.shortDescription)
                .set(ItemTable.ITEM.PRICE, it.price.toBigDecimal())
                .execute()
        }

        return receiptId
    }

    fun getPointsForReceipt(receiptId: UUID): Long? {
        return dsl.select(ReceiptTable.RECEIPT.POINTS)
            .from(ReceiptTable.RECEIPT)
            .where(ReceiptTable.RECEIPT.ID.eq(receiptId))
            .fetchOneInto(Long::class.java)
    }
}
