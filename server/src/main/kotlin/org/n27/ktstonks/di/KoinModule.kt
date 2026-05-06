package org.n27.ktstonks.di

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import org.n27.ktstonks.data.RepositoryImpl
import org.n27.ktstonks.data.db.stocks.StocksDao
import org.n27.ktstonks.data.db.stocks.StocksTable
import org.n27.ktstonks.data.json.JsonReader
import org.n27.ktstonks.data.json.SymbolReader
import org.n27.ktstonks.data.LogoApi
import org.n27.ktstonks.data.yfinance.YfinanceApi
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.domain.usecases.AddCustomValuationUseCase
import org.n27.ktstonks.domain.usecases.AddToWatchlistUseCase
import org.n27.ktstonks.domain.usecases.GetStockUseCase
import org.n27.ktstonks.domain.usecases.GetStocksUseCase
import org.n27.ktstonks.domain.usecases.GetWatchlistUseCase
import org.n27.ktstonks.domain.usecases.RemoveFromWatchlistUseCase

const val BASE_URL = "http://localhost:8000"

val mainModule = module {
    single {
        HttpClient(CIO) {
            install(HttpTimeout) {
                connectTimeoutMillis = 5_000
                socketTimeoutMillis = 20_000
                requestTimeoutMillis = 20_000
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    single { initDatabase() }
    single { StocksDao() }
    single { JsonReader() }
    single { SymbolReader(get()) }
    single { YfinanceApi(BASE_URL, get()) }
    single { LogoApi(get()) }
    single { GetStockUseCase(get()) }
    single { GetStocksUseCase(get()) }
    single { GetWatchlistUseCase(get()) }
    single { AddToWatchlistUseCase(get()) }
    single { RemoveFromWatchlistUseCase(get()) }
    single { AddCustomValuationUseCase(get()) }

    single<Repository> { RepositoryImpl(get(), get(), get(), get()) }
}

private fun initDatabase(): Database {
    val db = Database.connect(
        url = "jdbc:h2:./data/stocks;AUTO_SERVER=TRUE",
        driver = "org.h2.Driver"
    )

    transaction(db) { SchemaUtils.createMissingTablesAndColumns(StocksTable) }

    return db
}
