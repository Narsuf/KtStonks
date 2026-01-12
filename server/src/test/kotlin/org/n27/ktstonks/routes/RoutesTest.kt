package org.n27.ktstonks.routes

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.install
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.n27.ktstonks.domain.UseCase
import org.n27.ktstonks.test_data.getStock
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation

class RoutesTest {

    private val useCase: UseCase = mock()

    @Test
    fun `test get stock by symbol success`() = testApplication {
        application {
            install(ServerContentNegotiation) { json() }
            routing { stockRoutes(useCase) }
        }

        val stock = getStock(symbol = "AAPL")
        whenever(useCase.getStock("AAPL")) doReturn Result.success(stock)

        val client = createClient {
            install(ClientContentNegotiation) {
                json()
            }
        }

        val response = client.get("/stock/AAPL")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(
            """{"symbol":"AAPL","companyName":"Apple Inc.","logo":"/9j/2wCEAAEBAQEBAQEBAQEBAQEB","price":259.369995117188,"dividendYield":0.4,"eps":7.47,"pe":34.7215522245231,"pb":51.967537,"earningsQuarterlyGrowth":86.4,"expectedEpsGrowth":null,"valuationFloor":null,"currentIntrinsicValue":119.52,"forwardIntrinsicValue":null,"currency":"USD","lastUpdated":0}""",
            response.bodyAsText()
        )
    }

    @Test
    fun `test get stock by symbol error`() = testApplication {
        application {
            install(ServerContentNegotiation) { json() }
            routing { stockRoutes(useCase) }
        }

        whenever(useCase.getStock("AAPL")) doReturn Result.failure(Exception())

        val response = client.get("/stock/AAPL")

        assertEquals(HttpStatusCode.InternalServerError, response.status)
    }
}
