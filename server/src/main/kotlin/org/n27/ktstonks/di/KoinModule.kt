package org.n27.ktstonks.di

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import org.n27.ktstonks.ALPHA_VANTAGE_BASE_URL
import org.n27.ktstonks.data.RepositoryImpl
import org.n27.ktstonks.data.alpha_vantage.AlphaVantageApi
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.domain.UseCase

val mainModule = module {
    single {
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single {
        val apiKey = System.getenv("ALPHAVANTAGE_API_KEY") ?: error("API key not configured")
        AlphaVantageApi(
            client = get(),
            apiKey = apiKey,
            baseUrl = ALPHA_VANTAGE_BASE_URL
        )
    }

    single<Repository> { RepositoryImpl(get()) }

    single { UseCase(get()) }
}
