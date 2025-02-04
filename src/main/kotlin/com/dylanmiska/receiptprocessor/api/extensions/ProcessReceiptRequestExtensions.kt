package com.dylanmiska.receiptprocessor.api.extensions

import com.dylanmiska.receiptprocessor.api.dto.ProcessReceiptRequest
import com.dylanmiska.receiptprocessor.domain.model.Receipt
import jakarta.validation.ValidationException
import java.time.LocalDate
import java.time.LocalTime

fun ProcessReceiptRequest.toDomain(): Receipt {
    return Receipt(
        retailer = retailer,
        purchaseDate =
            try {
                LocalDate.parse(purchaseDate)
            } catch (e: Exception) {
                throw ValidationException("Invalid purchase date: `$purchaseDate`")
            },
        purchaseTime =
            try {
                LocalTime.parse(purchaseTime)
            } catch (e: Exception) {
                throw ValidationException("Invalid purchase time: `$purchaseTime`")
            },
        items = items.map { it.toDomain() },
        total =
            try {
                total.toDouble()
            } catch (e: Exception) {
                throw ValidationException("Invalid total: `$total`")
            },
        points = null,
    )
}
