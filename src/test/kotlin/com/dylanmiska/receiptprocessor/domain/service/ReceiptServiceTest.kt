package com.dylanmiska.receiptprocessor.domain.service

import com.dylanmiska.receiptprocessor.domain.extensions.updatePoints
import com.dylanmiska.receiptprocessor.domain.model.Receipt
import com.dylanmiska.receiptprocessor.persistance.repository.ReceiptRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import kotlin.NoSuchElementException
import kotlin.test.assertSame

class ReceiptServiceTest {
    private lateinit var receiptService: ReceiptService
    private lateinit var receiptRepository: ReceiptRepository

    @BeforeEach
    fun setUp() {
        receiptRepository = mockk()
        receiptService = ReceiptService(receiptRepository)
    }

    @Test
    fun testProcessReceipt() {
        val receipt =
            Receipt(
                id = UUID.randomUUID(),
                retailer = "Retailer",
                purchaseDate = LocalDate.now(),
                purchaseTime = LocalTime.now(),
                items = listOf(),
                total = 100.0,
                points = null,
            )
        val receiptEntity = receipt.apply { updatePoints() }

        every { receiptRepository.save(any()) } returns receiptEntity.id!!

        val id = receiptService.processReceipt(receipt)

        verify { receiptRepository.save(receiptEntity) }
        assertSame(receipt.id, id)
    }

    @Test
    fun testGetPoints() {
        val receiptId = UUID.randomUUID()
        val points = 100L

        every { receiptRepository.getPointsForReceipt(receiptId) } returns points

        val result = receiptService.getPoints(receiptId)

        assertSame(points, result)
        verify { receiptRepository.getPointsForReceipt(receiptId) }
    }

    @Test
    fun testGetPointsReturnsNull() {
        val receiptId = UUID.randomUUID()

        every { receiptRepository.getPointsForReceipt(receiptId) } returns null

        assertThrows<NoSuchElementException> {
            receiptService.getPoints(receiptId)
        }

        verify { receiptRepository.getPointsForReceipt(receiptId) }
    }
}
