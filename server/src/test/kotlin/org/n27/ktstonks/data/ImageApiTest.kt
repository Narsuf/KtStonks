package org.n27.ktstonks.data

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class ImageApiTest {

    private lateinit var api: ImageApi

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

        api = ImageApi(HttpClient(mockEngine))
    }

    @Test
    fun `downloadImage returns base64 string`() = runBlocking {
        val expected = Base64.getEncoder().encodeToString(byteArrayOf(1, 2, 3))

        val actual = api.downloadImage("http://localhost/image.png")

        assertEquals(expected, actual)
    }
}
