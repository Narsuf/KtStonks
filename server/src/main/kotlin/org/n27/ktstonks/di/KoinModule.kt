package org.n27.ktstonks.di

import io.ktor.client.*
import io.ktor.client.engine.cio.*
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
import org.n27.ktstonks.data.yfinance.YfinanceApi
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.domain.UseCase

const val BASE_URL = "http://localhost:8000"

val mainModule = module {
    single {
        HttpClient(CIO) {
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
    single { UseCase(get()) }

    single<Repository> { RepositoryImpl(get(), get(), get()) }
}

private fun initDatabase(): Database {
    val db = Database.connect(
        url = "jdbc:h2:./data/stocks;AUTO_SERVER=TRUE",
        driver = "org.h2.Driver"
    )

    transaction(db) { SchemaUtils.create(StocksTable) }

    return db
}
