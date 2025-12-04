package org.n27.ktstonks.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.n27.ktstonks.DEFAULT_PAGE_SIZE
import org.n27.ktstonks.domain.UseCase

fun Route.stockRoutes(useCase: UseCase) {

    route("/stock") {
        get("/{symbol}") {
            call.withSymbol { symbol ->
                useCase.getStock(symbol).fold(
                    onSuccess = { call.respond(it) },
                    onFailure = { call.respondError(it) }
                )
            }
        }

        get("/search/{symbol}") {
            call.withSymbol("No symbol provided for search") { symbol ->
                val (page, pageSize) = call.getPageAndSize()
                useCase.searchStock(symbol, page, pageSize).fold(
                    onSuccess = { call.respond(it) },
                    onFailure = { call.respondError(it, "Error searching for stock") }
                )
            }
        }
    }

    get("/stocks") {
        val (page, pageSize) = call.getPageAndSize()
        useCase.getStocks(page, pageSize).fold(
            onSuccess = { call.respond(it) },
            onFailure = { call.respondError(it) }
        )
    }

    route("/watchlist") {
        get {
            val (page, pageSize) = call.getPageAndSize()
            useCase.getWatchlist(page, pageSize).fold(
                onSuccess = { call.respond(it) },
                onFailure = { call.respondError(it, "Error fetching watchlist") }
            )
        }

        post("/{symbol}") {
            call.withSymbol("No symbol provided to add to watchlist") { symbol ->
                useCase.addStockToWatchlist(symbol).fold(
                    onSuccess = { call.respondText("Stock $symbol added to watchlist", status = HttpStatusCode.OK) },
                    onFailure = { call.respondError(it, "Error adding stock to watchlist") }
                )
            }
        }

        delete("/{symbol}") {
            call.withSymbol("No symbol provided to remove from watchlist") { symbol ->
                useCase.removeStockFromWatchlist(symbol).fold(
                    onSuccess = { call.respondText("Stock $symbol removed from watchlist", status = HttpStatusCode.OK) },
                    onFailure = { call.respondError(it, "Error removing stock from watchlist") }
                )
            }
        }
    }
}

private suspend fun ApplicationCall.withSymbol(
    errorMessage: String = "No symbol",
    block: suspend (String) -> Unit
) {
    parameters["symbol"]?.let { block(it) } ?: respondText(
        text = errorMessage,
        status = HttpStatusCode.BadRequest
    )
}

private fun ApplicationCall.getPageAndSize(): Pair<Int, Int> {
    val page = request.queryParameters["page"]?.toIntOrNull() ?: 0
    val pageSize = request.queryParameters["pageSize"]?.toIntOrNull() ?: DEFAULT_PAGE_SIZE
    return page to pageSize
}

private suspend inline fun <reified T> ApplicationCall.respond(body: T) = respondText(
    text = Json.encodeToString(body), contentType = ContentType.Application.Json
)

private suspend fun ApplicationCall.respondError(
    exception: Throwable,
    message: String = "Error fetching data"
) {
    respondText(text = "$message: ${exception.message}", status = HttpStatusCode.InternalServerError)
}
