package org.n27.ktstonks.extensions

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import org.n27.ktstonks.domain.exceptions.ApiLimitExceededException

suspend inline fun <reified T> ApplicationCall.respondSuccess(body: T) = respondText(
    text = Json.encodeToString(body), contentType = ContentType.Application.Json
)

suspend fun ApplicationCall.respondError(
    exception: Throwable,
    message: String = "Error fetching data"
) {
    val (status, errorMessage) = when (exception) {
        is ApiLimitExceededException -> HttpStatusCode.TooManyRequests to (exception.message ?: "API limit exceeded")
        else -> HttpStatusCode.InternalServerError to "$message: ${exception.message}"
    }
    respondText(text = errorMessage, status = status)
}
