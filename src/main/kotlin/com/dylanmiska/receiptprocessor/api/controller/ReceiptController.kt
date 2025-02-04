package com.dylanmiska.receiptprocessor.api.controller

import com.dylanmiska.receiptprocessor.api.dto.ErrorResponse
import com.dylanmiska.receiptprocessor.api.dto.IdResponse
import com.dylanmiska.receiptprocessor.api.dto.PointsResponse
import com.dylanmiska.receiptprocessor.api.dto.ProcessReceiptRequest
import com.dylanmiska.receiptprocessor.api.extensions.toDomain
import com.dylanmiska.receiptprocessor.domain.service.ReceiptService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@Tag(name = "Receipt API", description = "API for processing and retrieving receipts")
@RestController
@RequestMapping("/receipts")
class ReceiptController(
    val receiptService: ReceiptService,
) {
    @Operation(summary = "Process a receipt", description = "Submits a receipt for processing and returns an ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Successfully processed receipt",
                content = [Content(schema = Schema(implementation = IdResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    @PostMapping("/process")
    fun processReceipt(
        @Valid @RequestBody request: ProcessReceiptRequest,
    ): ResponseEntity<Any> {
        val receipt = request.toDomain()
        val id = receiptService.processReceipt(receipt)
        return ResponseEntity.ok(
            IdResponse(
                id = id,
            ),
        )
    }

    @Operation(summary = "Get points for a receipt", description = "Retrieves the points for a given receipt ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Points retrieved successfully",
                content = [Content(schema = Schema(implementation = PointsResponse::class))],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid input",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Receipt not found",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "500",
                description = "Internal server error",
                content = [Content(schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    @GetMapping("/{id}/points")
    fun getPoints(
        @PathVariable id: UUID,
    ): ResponseEntity<Any> {
        val points = receiptService.getPoints(id)
        return ResponseEntity.ok(
            PointsResponse(
                points = points,
            ),
        )
    }
}
