package com.dylanmiska.receiptprocessor.repository

import com.dylanmiska.receiptprocessor.domain.model.Item
import com.dylanmiska.receiptprocessor.domain.model.Receipt
import com.dylanmiska.receiptprocessor.persistance.repository.ReceiptRepository
import org.jooq.DSLContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.LocalTime
import java.util.*
import com.dylanmiska.jooq.generated.tables.Item as ItemTable
import com.dylanmiska.jooq.generated.tables.Receipt as ReceiptTable

@SpringBootTest
class ReceiptRepositoryIntegrationTest {
    @Autowired
    private lateinit var receiptRepository: ReceiptRepository

    @Autowired
    private lateinit var dsl: DSLContext

    @Test
    fun `should save and retrieve receipt with items`() {
        val receipt =
            Receipt(
                retailer = "Target",
                purchaseDate = LocalDate.of(2025, 2, 2),
                purchaseTime = LocalTime.of(14, 0),
                total = 25.50,
                points = 123,
                items =
                    listOf(
                        Item(shortDescription = "Soda", price = 1.99),
                        Item(shortDescription = "Chips", price = 2.50),
                    ),
            )

        // Call repository method
        val savedReceiptId = receiptRepository.save(receipt)

        // Fetch points to ensure data was saved
        val retrievedReceipt =
            dsl.selectFrom(ReceiptTable.RECEIPT)
                .where(ReceiptTable.RECEIPT.ID.eq(savedReceiptId))
                .fetchOneInto(Receipt::class.java)

        val retrievedItems =
            dsl.selectFrom(ItemTable.ITEM)
                .where(ItemTable.ITEM.RECEIPT_ID.eq(savedReceiptId))
                .fetchInto(Item::class.java)

        assertNotNull(retrievedReceipt)
        assertEquals(123, retrievedReceipt?.points)

        assertNotNull(retrievedItems)
        assertEquals(2, retrievedItems.size)
    }

    @Test
    fun `should save and retrieve receipt without items`() {
        val receipt =
            Receipt(
                retailer = "Target",
                purchaseDate = LocalDate.of(2025, 2, 2),
                purchaseTime = LocalTime.of(14, 0),
                total = 25.50,
                points = 123,
                items = emptyList(),
            )

        // Call repository method
        val savedReceiptId = receiptRepository.save(receipt)

        // Fetch Receipt to ensure data was saved
        val retrievedReceipt =
            dsl.selectFrom(ReceiptTable.RECEIPT)
                .where(ReceiptTable.RECEIPT.ID.eq(savedReceiptId))
                .fetchOneInto(Receipt::class.java)

        assertNotNull(retrievedReceipt)
        assertEquals(123, retrievedReceipt?.points)
    }

    @Test
    fun `should return null if receipt not found`() {
        val randomId = UUID.randomUUID()

        // Call repository method with non-existent receipt
        val retrievedPoints = receiptRepository.getPointsForReceipt(randomId)

        assertNull(retrievedPoints)
    }
}
