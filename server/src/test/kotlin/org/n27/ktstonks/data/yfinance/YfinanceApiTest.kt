package org.n27.ktstonks.data.yfinance

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.n27.ktstonks.test_data.data.getStockRaw
import org.n27.ktstonks.test_data.data.getStocksRaw
import org.n27.ktstonks.utils.getJson
import java.util.*
import kotlin.test.assertEquals

class YfinanceApiTest {

    private lateinit var api: YfinanceApi
    private lateinit var mockEngine: MockEngine

    @Before
    fun setup() {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/stock/AAPL" -> respond(
                    content = ByteReadChannel(getJson("stock_raw.json")),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
                "/stocks" -> respond(
                    content = ByteReadChannel(getJson("stocks_raw.json")),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "application/json")
                )
                "/image.png" -> respond(
                    content = byteArrayOf(1, 2, 3),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "image/png")
                )
                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json()
            }
        }

        api = YfinanceApi("http://localhost", httpClient)
    }

    @Test
    fun `getStock returns stock data`() = runBlocking {
        val actual = api.getStock("AAPL")

        assertEquals(getStockRaw(), actual)
    }

    @Test
    fun `getStocks returns list of stock data`() = runBlocking {
        val actual = api.getStocks("AAPL")

        assertEquals(getStocksRaw(), actual)
    }

    @Test
    fun `downloadImage returns base64 string`() = runBlocking {
        val expected = Base64.getEncoder().encodeToString(byteArrayOf(1, 2, 3))

        val actual = api.downloadImage("http://localhost/image.png")

        assertEquals(expected, actual)
    }
}
