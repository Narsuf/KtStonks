package org.n27.ktstonks.data

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class LogoApiTest {

    private lateinit var api: LogoApi

    @Before
    fun setup() {
        val mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/image.png" -> respond(
                    content = byteArrayOf(1, 2, 3),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, "image/png")
                )
                else -> error("Unhandled ${request.url.encodedPath}")
            }
        }

        api = LogoApi(HttpClient(mockEngine))
    }

    @Test
    fun `downloadLogo returns base64 string`() = runBlocking {
        val expected = Base64.getEncoder().encodeToString(byteArrayOf(1, 2, 3))

        val actual = api.downloadLogo("http://localhost/image.png")

        assertEquals(expected, actual)
    }
}
