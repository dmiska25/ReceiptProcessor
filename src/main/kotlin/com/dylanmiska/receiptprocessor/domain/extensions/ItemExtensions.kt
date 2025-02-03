package com.dylanmiska.receiptprocessor.domain.extensions

import com.dylanmiska.receiptprocessor.domain.model.Item
import com.dylanmiska.receiptprocessor.persistance.entity.ItemEntity
import com.dylanmiska.receiptprocessor.persistance.entity.ReceiptEntity

fun Item.toEntity(receipt: ReceiptEntity): ItemEntity =
    ItemEntity(
        receipt = receipt,
        shortDescription = this.shortDescription,
        price = this.price,
    )
