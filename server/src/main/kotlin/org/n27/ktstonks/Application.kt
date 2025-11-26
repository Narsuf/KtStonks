package org.n27.ktstonks

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.n27.ktstonks.data.RepositoryImpl
import org.n27.ktstonks.routes.stockRoutes
import org.n27.ktstonks.data.alpha_vantage.AlphaVantageApi
import org.n27.ktstonks.domain.UseCase

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val apiKey = System.getenv("ALPHAVANTAGE_API_KEY") ?: error("API key not configured")
    val api = AlphaVantageApi.create(apiKey)
    val repository = RepositoryImpl(api)
    val useCase = UseCase(repository)

    routing { stockRoutes(useCase) }
}
