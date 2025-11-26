package org.n27.ktstonks.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.n27.ktstonks.DEFAULT_PAGE_SIZE
import org.n27.ktstonks.domain.UseCase

fun Route.stockRoutes(useCase: UseCase) {
    get("/stock/{symbol}") {
        val symbol = call.parameters["symbol"]
            ?: return@get call.respondText(
                text = "No symbol",
                status = HttpStatusCode.BadRequest,
            )

        useCase.getStock(symbol).fold(
            onSuccess = { call.respondText(Json.encodeToString(it), ContentType.Application.Json) },
            onFailure = { call.respondText("Error fetching data: ${it.message}", status = HttpStatusCode.InternalServerError) }
        )
    }

    get("/stocks") {
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
        val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: DEFAULT_PAGE_SIZE

        useCase.getStocks(page, pageSize).fold(
            onSuccess = { call.respondText(Json.encodeToString(it), ContentType.Application.Json) },
            onFailure = { call.respondText("Error fetching data: ${it.message}", status = HttpStatusCode.InternalServerError) }
        )
    }

    get("/search/stocks/{symbol}") {
        val symbol = call.parameters["symbol"]
            ?: return@get call.respondText(
                text = "No symbol provided for search",
                status = HttpStatusCode.BadRequest,
            )
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
        val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: DEFAULT_PAGE_SIZE

        useCase.searchStock(symbol, page, pageSize).fold(
            onSuccess = { call.respondText(Json.encodeToString(it), ContentType.Application.Json) },
            onFailure = { call.respondText("Error searching for stock: ${it.message}", status = HttpStatusCode.InternalServerError) }
        )
    }

    get("/watchlist") {
        val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 0
        val pageSize = call.request.queryParameters["pageSize"]?.toIntOrNull() ?: DEFAULT_PAGE_SIZE

        useCase.getWatchlist(page, pageSize).fold(
            onSuccess = { call.respondText(Json.encodeToString(it), ContentType.Application.Json) },
            onFailure = { call.respondText("Error fetching watchlist: ${it.message}", status = HttpStatusCode.InternalServerError) }
        )
    }

    post("/watchlist/{symbol}") {
        val symbol = call.parameters["symbol"]
            ?: return@post call.respondText(
                text = "No symbol provided to add to watchlist",
                status = HttpStatusCode.BadRequest,
            )

        useCase.addStockToWatchlist(symbol).fold(
            onSuccess = { call.respondText("Stock $symbol added to watchlist", status = HttpStatusCode.OK) },
            onFailure = { call.respondText("Error adding stock to watchlist: ${it.message}", status = HttpStatusCode.InternalServerError) }
        )
    }

    delete("/watchlist/{symbol}") {
        val symbol = call.parameters["symbol"]
            ?: return@delete call.respondText(
                text = "No symbol provided to remove from watchlist",
                status = HttpStatusCode.BadRequest,
            )

        useCase.removeStockFromWatchlist(symbol).fold(
            onSuccess = { call.respondText("Stock $symbol removed from watchlist", status = HttpStatusCode.OK) },
            onFailure = { call.respondText("Error removing stock from watchlist: ${it.message}", status = HttpStatusCode.InternalServerError) }
        )
    }
}
