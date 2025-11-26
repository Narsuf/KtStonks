package org.n27.ktstonks.routes

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.n27.ktstonks.models.AlphaStock
import org.n27.ktstonks.service.StockService

fun Routing.stockRoutes(stockService: StockService) {

    get("/stock/{symbol}") {
        val symbol = call.parameters["symbol"] ?: return@get call.respondText("No symbol", status = HttpStatusCode.BadRequest)
        runCatching {
            val stock = stockService.fetchStock(symbol)
            call.respondText(
                text = Json.encodeToString(AlphaStock.serializer(), stock),
                contentType = ContentType.Application.Json
            )
        }.onFailure { e ->
            call.respondText("Error fetching data: ${e.message}", status = HttpStatusCode.InternalServerError)
        }
    }
}
