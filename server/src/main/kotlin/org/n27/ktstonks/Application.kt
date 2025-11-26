package org.n27.ktstonks

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.n27.ktstonks.client.AlphaVantageClient
import org.n27.ktstonks.routes.stockRoutes
import org.n27.ktstonks.service.StockService

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val apiKey = System.getenv("ALPHAVANTAGE_API_KEY") ?: error("API key not configured")
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json { ignoreUnknownKeys = true; prettyPrint = true; isLenient = true }
            )
        }
    }

    val alphaClient = AlphaVantageClient(client, apiKey)
    val stockService = StockService(alphaClient)

    routing {
        stockRoutes(stockService)
    }
}
