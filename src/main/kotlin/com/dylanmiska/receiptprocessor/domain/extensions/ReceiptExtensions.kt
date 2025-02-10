package com.dylanmiska.receiptprocessor.domain.extensions

import com.dylanmiska.receiptprocessor.domain.model.Receipt
import java.time.LocalTime
import kotlin.math.ceil

fun Receipt.updatePoints() {
    this.points =
        // One point for every alphanumeric character in the retailer name
        (this.retailer.count { it.isLetterOrDigit() }) +
        // 50 points if the total is a round dollar amount with no cents
        (if (this.total % 1 == 0.0) 50L else 0L) +
        // 25 points if the total is a multiple of 0.25
        (if (this.total % 0.25 == 0.0) 25L else 0L) +
        // 5 points for every two items on the receipt
        (((this.items?.size ?: 0) / 2) * 5L) +
        // If the trimmed length of the item description is a multiple of 3, multiply the price by 0.2 and round up to the nearest integer. The result is the number of points earned.
        (
            this.items?.sumOf { item ->
                if (item.shortDescription.trim().length % 3 == 0) {
                    ceil(item.price * 0.2).toLong()
                } else {
                    0L
                }
            } ?: 0
        ) +
        // 6 points if the day in the purchase date is odd
        (if (this.purchaseDate.dayOfMonth % 2 != 0) 6L else 0L) +
        // 10 points if the time of purchase is after 2:00pm and before 4:00pm.
        (
            if (
                this.purchaseTime > LocalTime.of(14, 0) &&
                this.purchaseTime < LocalTime.of(16, 0)
            ) {
                10L
            } else {
                0L
            }
        )
}
