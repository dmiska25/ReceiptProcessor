package com.dylanmiska.receiptprocessor.api.extensions

import com.dylanmiska.receiptprocessor.api.dto.ItemRequest
import com.dylanmiska.receiptprocessor.domain.model.Item
import jakarta.validation.ValidationException

fun ItemRequest.toDomain(): Item {
    return Item(
        shortDescription = shortDescription,
        price =
            try {
                price.toDouble()
            } catch (e: Exception) {
                throw ValidationException("Invalid price: `$price`")
            },
    )
}
