package org.n27.ktstonks.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.n27.ktstonks.domain.UseCase

fun Route.stockRoutes(useCase: UseCase) {
    get("/stocks") {
        useCase.getStocks().fold(
            onSuccess = { call.respondText(Json.encodeToString(it), ContentType.Application.Json) },
            onFailure = { call.respondText("Error fetching data: ${it.message}", status = HttpStatusCode.InternalServerError) }
        )
    }

    get("/stock/{symbol}") {
        val symbol = call.parameters["symbol"]
            ?: return@get call.respondText(
                text = "No symbol",
                status = HttpStatusCode.BadRequest,
            )

        useCase.execute(symbol).fold(
            onSuccess = { call.respondText(Json.encodeToString(it), ContentType.Application.Json) },
            onFailure = { call.respondText("Error fetching data: ${it.message}", status = HttpStatusCode.InternalServerError) }
        )
    }

    get("/search/stock/{symbol}") {
        val symbol = call.parameters["symbol"]
            ?: return@get call.respondText(
                text = "No symbol provided for search",
                status = HttpStatusCode.BadRequest,
            )

        useCase.searchStock(symbol).fold(
            onSuccess = { call.respondText(Json.encodeToString(it), ContentType.Application.Json) },
            onFailure = { call.respondText("Error searching for stock: ${it.message}", status = HttpStatusCode.InternalServerError) }
        )
    }
}
