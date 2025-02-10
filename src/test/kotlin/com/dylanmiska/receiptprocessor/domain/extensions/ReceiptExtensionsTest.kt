package com.dylanmiska.receiptprocessor.domain.extensions

import com.dylanmiska.receiptprocessor.domain.model.Item
import com.dylanmiska.receiptprocessor.domain.model.Receipt
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalTime
import kotlin.test.assertEquals

class ReceiptExtensionsTest {
    @Nested
    inner class TestUpdatePoints {
        val item =
            Item(
                shortDescription = "A",
                price = 2.99,
            )

        val receipt =
            Receipt(
                retailer = "!!",
                purchaseDate = LocalDate.of(2021, 1, 2),
                purchaseTime = LocalTime.of(13, 0),
                items = listOf(item),
                total = 2.99,
                points = null,
            )

        @Test
        fun `One point for every alphanumeric character in the retailer name`() {
            val receipt = receipt.copy(retailer = "Walmart 1 % *")
            receipt.updatePoints()
            assertEquals(8, receipt.points)
        }

        @Test
        fun `50 points if the total is a round dollar amount with no cents`() {
            val receipt = receipt.copy(total = 3.0)
            receipt.updatePoints()
            // Both no cents rule and round dollar amount rule are satisfied
            assertEquals(75, receipt.points)
        }

        @Test
        fun `25 points if the total is a multiple of point 25`() {
            val receipt = receipt.copy(total = 3.25)
            receipt.updatePoints()
            assertEquals(25, receipt.points)
        }

        @Test
        fun `5 points for every two items on the receipt`() {
            val receipt = receipt.copy(items = listOf(item, item))
            receipt.updatePoints()
            assertEquals(5, receipt.points)
        }

        @Test
        fun `Points for item description length multiple of 3`() {
            val receipt = receipt.copy(items = listOf(Item(shortDescription = "ABC", price = 1.0)))
            receipt.updatePoints()
            assertEquals(1, receipt.points)
        }

        @Test
        fun `6 points if the day in the purchase date is odd`() {
            val receipt = receipt.copy(purchaseDate = LocalDate.of(2021, 1, 1))
            receipt.updatePoints()
            assertEquals(6, receipt.points)
        }

        @Test
        fun `10 points if the time of purchase is after 2pm and before 4pm`() {
            val receipt = receipt.copy(purchaseTime = LocalTime.of(15, 0))
            receipt.updatePoints()
            assertEquals(10, receipt.points)
        }

        @Test
        fun `no points if the time of purchase is 2pm`() {
            val receipt = receipt.copy(purchaseTime = LocalTime.of(14, 0))
            receipt.updatePoints()
            assertEquals(0, receipt.points)
        }

        @Test
        fun `no points if the time of purchase is 4pm`() {
            val receipt = receipt.copy(purchaseTime = LocalTime.of(16, 0))
            receipt.updatePoints()
            assertEquals(0, receipt.points)
        }

        @Test
        fun `All points combined`() {
            val receipt =
                receipt.copy(
                    // 8
                    retailer = "Walmart 1 % *",
                    // 50 + 25
                    total = 3.0,
                    // 5 + 1
                    items = listOf(Item(shortDescription = "ABC", price = 1.0), item),
                    // 6
                    purchaseDate = LocalDate.of(2021, 1, 1),
                    // 10
                    purchaseTime = LocalTime.of(15, 0),
                )

            receipt.updatePoints()
            assertEquals(105, receipt.points)
        }
    }
}
