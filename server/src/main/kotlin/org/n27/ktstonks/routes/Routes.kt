package org.n27.ktstonks.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.n27.ktstonks.DEFAULT_PAGE_SIZE
import org.n27.ktstonks.domain.UseCase
import org.n27.ktstonks.extensions.respondError
import org.n27.ktstonks.extensions.respondSuccess

fun Route.stockRoutes(useCase: UseCase) {

    route("stock/{symbol}") {
        get {
            call.withSymbol { symbol ->
                useCase.getStock(symbol).fold(
                    onSuccess = { call.respondSuccess(it) },
                    onFailure = { call.respondError(it) }
                )
            }
        }

        post("/valuation") {
            call.withSymbol { symbol ->
                val valuationFloor = call.request.queryParameters["valuationFloor"]?.toDoubleOrNull()
                val epsGrowth = call.request.queryParameters["epsGrowth"]?.toDoubleOrNull()

                if (epsGrowth != null) {
                    useCase.addCustomValuation(symbol, valuationFloor, epsGrowth).fold(
                        onSuccess = { call.respondText("Valuation updated", status = HttpStatusCode.OK) },
                        onFailure = { call.respondError(it) }
                    )
                } else {
                    call.respondText("epsGrowth is required", status = HttpStatusCode.BadRequest)
                }
            }
        }
    }

    get("/stocks") {
        val (page, pageSize) = call.getPageAndSize()
        val filterWatchlist = call.request.queryParameters["filterWatchlist"]?.toBoolean() ?: false
        val symbol = call.request.queryParameters["symbol"]

        useCase.getStocks(page, pageSize, filterWatchlist, symbol).fold(
            onSuccess = { call.respondSuccess(it) },
            onFailure = { call.respondError(it, "Error searching for stock") }
        )
    }

    route("/watchlist") {
        get {
            val (page, pageSize) = call.getPageAndSize()
            useCase.getWatchlist(page, pageSize).fold(
                onSuccess = { call.respondSuccess(it) },
                onFailure = { call.respondError(it, "Error fetching watchlist") }
            )
        }

        post("/{symbol}") {
            call.withSymbol("No symbol provided to add to watchlist") { symbol ->
                useCase.addToWatchlist(symbol).fold(
                    onSuccess = { call.respondText("Stock $symbol added to watchlist", status = HttpStatusCode.OK) },
                    onFailure = { call.respondError(it, "Error adding stock to watchlist") }
                )
            }
        }

        delete("/{symbol}") {
            call.withSymbol("No symbol provided to remove from watchlist") { symbol ->
                useCase.removeFromWatchlist(symbol).fold(
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
