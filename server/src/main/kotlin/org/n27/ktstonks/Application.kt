package org.n27.ktstonks

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get
import org.koin.ktor.plugin.Koin
import org.n27.ktstonks.di.mainModule
import org.n27.ktstonks.routes.stockRoutes

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(Koin) {
        modules(mainModule)
    }

    routing { stockRoutes(get()) }
}
