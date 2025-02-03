package com.dylanmiska.receiptprocessor.domain.extensions

import com.dylanmiska.receiptprocessor.domain.model.Item
import com.dylanmiska.receiptprocessor.persistance.entity.ReceiptEntity
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ItemExtensionsTest {
    @Test
    fun `test Item toEntity extension function`() {
        val receiptEntity: ReceiptEntity = mockk()
        val item =
            Item(
                shortDescription = "Milk",
                price = 2.99,
            )

        val itemEntity = item.toEntity(receiptEntity)

        assertEquals(receiptEntity, itemEntity.receipt)
        assertEquals(item.shortDescription, itemEntity.shortDescription)
        assertEquals(item.price, itemEntity.price)
    }
}
