package org.n27.ktstonks

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.engine.cio.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    routing {
        get("/stock/{symbol}") {
            val symbol = call.parameters["symbol"] ?: return@get call.respondText("No symbol")
            val apiKey = System.getenv("ALPHAVANTAGE_API_KEY")
                ?: return@get call.respondText("API key not configured")
            val url = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=$symbol&apikey=$apiKey"

            try {
                val responseString: String = client.get(url) {
                    headers { append("Accept", "*/*") }
                }.body()

                call.respondText(responseString, contentType = io.ktor.http.ContentType.Application.Json)
            } catch (e: Exception) {
                call.respondText("Error fetching data: ${e.message}")
            }
        }
    }
}
