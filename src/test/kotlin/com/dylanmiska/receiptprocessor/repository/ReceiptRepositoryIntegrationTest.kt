package com.dylanmiska.receiptprocessor.repository

import com.dylanmiska.receiptprocessor.persistance.entity.ItemEntity
import com.dylanmiska.receiptprocessor.persistance.entity.ReceiptEntity
import com.dylanmiska.receiptprocessor.persistance.repository.ReceiptRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@SpringBootTest
class ReceiptRepositoryIntegrationTest {
    @Autowired
    private lateinit var receiptRepository: ReceiptRepository

    @Test
    @Transactional
    fun `should save and retrieve receipt with items`() {
        val receipt =
            ReceiptEntity(
                retailer = "Target",
                purchaseDate = LocalDate.of(2025, 2, 2),
                purchaseTime = LocalTime.of(14, 0),
                total = 25.50,
                items = mutableListOf(),
                points = 123,
            )

        receipt.items.addAll(
            mutableListOf(
                ItemEntity(shortDescription = "Soda", price = 1.99, receipt = receipt),
                ItemEntity(shortDescription = "Chips", price = 2.50, receipt = receipt),
            ),
        )

        val savedReceipt = receiptRepository.save(receipt)
        val foundReceipt = receiptRepository.findById(savedReceipt.id!!)

        assertTrue(foundReceipt.isPresent)
        assertEquals("Target", foundReceipt.get().retailer)
        assertEquals(LocalDate.of(2025, 2, 2), foundReceipt.get().purchaseDate)
        assertEquals(LocalTime.of(14, 0), foundReceipt.get().purchaseTime)
        assertEquals(25.50, foundReceipt.get().total)
        assertEquals(123, foundReceipt.get().points)
        assertEquals(2, foundReceipt.get().items.size)
    }

    @Test
    @Transactional
    fun `should save and retrieve receipt without items`() {
        val receipt =
            ReceiptEntity(
                retailer = "Target",
                purchaseDate = LocalDate.of(2025, 2, 2),
                purchaseTime = LocalTime.of(14, 0),
                total = 25.50,
                items = mutableListOf(),
                points = 123,
            )

        val savedReceipt = receiptRepository.save(receipt)
        val foundReceipt = receiptRepository.findById(savedReceipt.id!!)

        assertTrue(foundReceipt.isPresent)
        assertEquals("Target", foundReceipt.get().retailer)
        assertEquals(LocalDate.of(2025, 2, 2), foundReceipt.get().purchaseDate)
        assertEquals(LocalTime.of(14, 0), foundReceipt.get().purchaseTime)
        assertEquals(25.50, foundReceipt.get().total)
        assertEquals(123, foundReceipt.get().points)
        assertEquals(0, foundReceipt.get().items.size)
    }

    @Test
    @Transactional
    fun `should retrieve points for receipt`() {
        val receipt =
            ReceiptEntity(
                retailer = "Target",
                purchaseDate = LocalDate.of(2025, 2, 2),
                purchaseTime = LocalTime.of(14, 0),
                total = 25.50,
                items = mutableListOf(),
                points = 123,
            )

        val savedReceipt = receiptRepository.save(receipt)
        val points = receiptRepository.getPointsForReceipt(savedReceipt.id!!)

        assertEquals(123, points)
    }

    @Test
    @Transactional
    fun `shouldn't throw if not found`() {
        val response = receiptRepository.getPointsForReceipt(UUID.randomUUID())
        assertFalse(response.isPresent)
    }
}
