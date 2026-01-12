package org.n27.ktstonks.extensions

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import kotlinx.serialization.Serializable
import org.junit.Test
import kotlin.test.assertEquals

class ApplicationCallExtensionsTest {

    @Serializable
    private data class TestBody(val message: String)

    @Test
    fun `respondSuccess should return OK with JSON body`() = testApplication {
        application {
            routing {
                get("/success") {
                    call.respondSuccess(TestBody("Hello"))
                }
            }
        }

        val response = client.get("/success")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(ContentType.Application.Json.toString(), response.headers[HttpHeaders.ContentType])
        assertEquals("""{"message":"Hello"}""", response.bodyAsText())
    }

    @Test
    fun `respondError should return InternalServerError with error message`() = testApplication {
        application {
            routing {
                get("/error") {
                    call.respondError(Exception("Something failed"), "Custom message")
                }
            }
        }

        val response = client.get("/error")

        assertEquals(HttpStatusCode.InternalServerError, response.status)
        assertEquals("Custom message: Something failed", response.bodyAsText())
    }
}
