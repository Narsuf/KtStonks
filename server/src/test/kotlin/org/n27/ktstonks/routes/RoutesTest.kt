package org.n27.ktstonks.routes

import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.install
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import org.junit.Test
import org.mockito.ArgumentMatchers.anyBoolean
import org.mockito.ArgumentMatchers.anyDouble
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.kotlin.*
import org.n27.ktstonks.domain.UseCase
import org.n27.ktstonks.test_data.getStock
import org.n27.ktstonks.test_data.getStocks
import kotlin.test.assertEquals
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation as ClientContentNegotiation
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation

class RoutesTest {

    private val useCase: UseCase = mock()

    @Test
    fun `test get stock by symbol success`() = testWithApplication { client ->
        whenever(useCase.getStock(anyString())) doReturn Result.success(getStock("AAPL"))

        val response = client.get("/stock/AAPL")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(getJson("get_stock.json"), response.bodyAsText())
    }

    @Test
    fun `test get stock by symbol error`() = testWithApplication { client ->
        whenever(useCase.getStock("AAPL")) doReturn Result.failure(Exception())

        val response = client.get("/stock/AAPL")

        assertEquals(HttpStatusCode.InternalServerError, response.status)
    }

    @Test
    fun `test post valuation success`() = testWithApplication { client ->
        whenever(useCase.addCustomValuation(anyString(), anyDouble(), anyDouble())) doReturn Result.success(Unit)

        val response = client.post("/stock/AAPL/valuation?valuationFloor=1.0&epsGrowth=1.0")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Valuation updated", response.bodyAsText())
    }

    @Test
    fun `test post valuation error`() = testWithApplication { client ->
        whenever(useCase.addCustomValuation(anyString(), anyDouble(), anyDouble())) doReturn Result.failure(Exception())

        val response = client.post("/stock/AAPL/valuation?valuationFloor=1.0&epsGrowth=1.0")

        assertEquals(HttpStatusCode.InternalServerError, response.status)
    }

    @Test
    fun `test get stocks success`() = testWithApplication { client ->
        whenever(useCase.getStocks(anyInt(), anyInt(), anyBoolean(), anyOrNull())) doReturn Result.success(getStocks())

        val response = client.get("/stocks")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(getJson("get_stocks.json"), response.bodyAsText())
    }

    @Test
    fun `test get stocks error`() = testWithApplication { client ->
        whenever(useCase.getStocks(anyInt(), anyInt(), anyBoolean(), anyOrNull())) doReturn Result.failure(Exception())

        val response = client.get("/stocks")

        assertEquals(HttpStatusCode.InternalServerError, response.status)
    }

    private fun testWithApplication(
        block: suspend ApplicationTestBuilder.(client: HttpClient) -> Unit
    ) = testApplication {
        application {
            install(ServerContentNegotiation) { json() }
            routing { stockRoutes(useCase) }
        }
        val client = createClient {
            install(ClientContentNegotiation) { json() }
        }
        block(client)
    }

    private fun getJson(path: String): String = this::class.java.classLoader.getResource(path)!!.readText().removeSuffix("\n")
}
