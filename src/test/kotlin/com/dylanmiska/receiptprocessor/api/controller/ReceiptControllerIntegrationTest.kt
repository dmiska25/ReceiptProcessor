package com.dylanmiska.receiptprocessor.api.controller

import com.dylanmiska.receiptprocessor.api.dto.ItemRequest
import com.dylanmiska.receiptprocessor.api.dto.ProcessReceiptRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReceiptControllerIntegrationTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper,
) {
    @LocalServerPort
    var port: Int = 0

    private fun validProcessReceiptRequest(
        retailer: String = "M&M Corner Market",
        purchaseDate: String = "2022-01-01",
        purchaseTime: String = "13:01",
        items: List<ItemRequest> = listOf(ItemRequest("Bananas", "1.00")),
        total: String = "6.49",
    ) = ProcessReceiptRequest(retailer, purchaseDate, purchaseTime, items, total)

    private fun createReceipt(body: ProcessReceiptRequest = validProcessReceiptRequest()): String {
        val result =
            mockMvc.post("/receipts/process") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }
                .andExpect { status { isOk() } }
                .andReturn()

        return objectMapper.readTree(result.response.contentAsString)["id"].asText()
    }

    @Nested
    inner class TestProcessReceipt {
        @Test
        @Transactional
        fun `should return 200 when request is valid`() {
            val body = validProcessReceiptRequest()
            mockMvc.post("/receipts/process") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.id") { exists() }
                }
        }

        @Test
        fun `should return 400 when retailer has invalid characters`() {
            val body = validProcessReceiptRequest(retailer = "Invalid#@!")
            mockMvc.post("/receipts/process") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.error") { value("Validation failed") }
                    jsonPath("$.message.retailer") { exists() }
                }
        }

        @Test
        fun `should return 400 when retailer is missing`() {
            val bodyMap =
                mapOf(
                    "purchaseDate" to "2022-01-01",
                    "purchaseTime" to "13:01",
                    "items" to listOf(mapOf("shortDescription" to "Bananas", "price" to "1.00")),
                    "total" to "6.49",
                )
            mockMvc.post("/receipts/process") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(bodyMap)
            }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.error") { value("Validation failed") }
                }
        }

        @Test
        fun `should return 400 when purchaseDate has wrong format`() {
            val body = validProcessReceiptRequest(purchaseDate = "01-01-2022")
            mockMvc.post("/receipts/process") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.error") { value("Validation failed") }
                    jsonPath("$.message.purchaseDate") { exists() }
                }
        }

        @Test
        fun `should return 400 when purchaseTime has wrong format`() {
            val body = validProcessReceiptRequest(purchaseTime = "9:00")
            mockMvc.post("/receipts/process") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.error") { value("Validation failed") }
                    jsonPath("$.message.purchaseTime") { exists() }
                }
        }

        @Test
        fun `should return 400 when items list is empty`() {
            val body = validProcessReceiptRequest(items = emptyList())
            mockMvc.post("/receipts/process") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.error") { value("Validation failed") }
                    jsonPath("$.message.items") { exists() }
                }
        }

        @Test
        fun `should return 400 when item shortDescription is invalid`() {
            val items = listOf(ItemRequest("Bananas*#@", "1.00"))
            val body = validProcessReceiptRequest(items = items)
            mockMvc.post("/receipts/process") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.error") { value("Validation failed") }
                    jsonPath("$.message['items[0].shortDescription']") { exists() }
                }
        }

        @Test
        fun `should return 400 when item price format is invalid`() {
            val items = listOf(ItemRequest("Bananas", "1.0"))
            val body = validProcessReceiptRequest(items = items)
            mockMvc.post("/receipts/process") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.error") { value("Validation failed") }
                    jsonPath("$.message['items[0].price']") { exists() }
                }
        }

        @Test
        fun `should return 400 when total format is invalid`() {
            val body = validProcessReceiptRequest(total = "6.4")
            mockMvc.post("/receipts/process") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(body)
            }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.error") { value("Validation failed") }
                    jsonPath("$.message.total") { exists() }
                }
        }
    }

    @Nested
    inner class TestGetPoints {
        @Test
        @Transactional
        fun `should return 200 when receipt ID exists`() {
            val id = createReceipt()
            mockMvc.get("/receipts/$id/points") {
                contentType = MediaType.APPLICATION_JSON
            }
                .andExpect {
                    status { isOk() }
                    jsonPath("$.points") { exists() }
                }
        }

        @Test
        fun `should return 400 when ID is not a valid UUID`() {
            mockMvc.get("/receipts/not-a-uuid/points") {
                contentType = MediaType.APPLICATION_JSON
            }
                .andExpect {
                    status { isBadRequest() }
                    jsonPath("$.error") { value("Validation failed") }
                }
        }

        @Test
        fun `should return 404 when valid UUID doesn't exist`() {
            mockMvc.get("/receipts/${UUID.randomUUID()}/points") {
                contentType = MediaType.APPLICATION_JSON
            }
                .andExpect {
                    status { isNotFound() }
                    jsonPath("$.error") { value("Not Found") }
                }
        }
    }
}
