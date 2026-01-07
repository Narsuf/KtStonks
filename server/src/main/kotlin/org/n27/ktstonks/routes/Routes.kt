package org.n27.ktstonks.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.n27.ktstonks.DEFAULT_PAGE_SIZE
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.extensions.respondError
import org.n27.ktstonks.extensions.respondSuccess

fun Route.stockRoutes(repository: Repository) {

    get("stock/{symbol}") {
        call.withSymbol { symbol ->
            repository.getStock(symbol).fold(
                onSuccess = { call.respondSuccess(it) },
                onFailure = { call.respondError(it) }
            )
        }
    }

    get("/stocks") {
        val (page, pageSize) = call.getPageAndSize()
        val filterWatchlist = call.request.queryParameters["filterWatchlist"]?.toBoolean() ?: false
        val symbol = call.request.queryParameters["symbol"]

        repository.getStocks(page, pageSize, filterWatchlist, symbol).fold(
            onSuccess = { call.respondSuccess(it) },
            onFailure = { call.respondError(it, "Error searching for stock") }
        )
    }

    route("/watchlist") {
        get {
            val (page, pageSize) = call.getPageAndSize()
            repository.getWatchlist(page, pageSize).fold(
                onSuccess = { call.respondSuccess(it) },
                onFailure = { call.respondError(it, "Error fetching watchlist") }
            )
        }

        post("/{symbol}") {
            call.withSymbol("No symbol provided to add to watchlist") { symbol ->
                repository.addToWatchlist(symbol).fold(
                    onSuccess = { call.respondText("Stock $symbol added to watchlist", status = HttpStatusCode.OK) },
                    onFailure = { call.respondError(it, "Error adding stock to watchlist") }
                )
            }
        }

        delete("/{symbol}") {
            call.withSymbol("No symbol provided to remove from watchlist") { symbol ->
                repository.removeFromWatchlist(symbol).fold(
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
