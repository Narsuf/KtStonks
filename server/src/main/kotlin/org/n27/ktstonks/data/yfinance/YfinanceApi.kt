package org.n27.ktstonks.data.yfinance

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.n27.ktstonks.data.yfinance.model.StockRaw
import org.slf4j.LoggerFactory

private const val STOCK_URL = "/stock"
private const val STOCKS_URL = "/stocks"

class YfinanceApi(private val url: String, private val httpClient: HttpClient) {

    private val logger = LoggerFactory.getLogger(YfinanceApi::class.java)

    suspend fun getStock(symbol: String): StockRaw {
        logger.info("getStock request triggered")
        return httpClient.get("$url$STOCK_URL/$symbol").body()
    }

    suspend fun getStocks(symbols: String): List<StockRaw> {
        logger.info("getStocks request triggered")
        return httpClient.get("$url$STOCKS_URL") { parameter("symbols", symbols) }.body()
    }
}
