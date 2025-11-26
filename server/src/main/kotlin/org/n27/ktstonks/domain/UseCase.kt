package org.n27.ktstonks.domain

import org.n27.ktstonks.data.json.JsonReader
import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.model.Stocks
import java.time.Instant
import java.time.ZoneId
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class UseCase(
    private val repository: Repository,
    private val jsonReader: JsonReader,
) {
    suspend fun getStock(symbol: String): Result<Stock> {
        val dbStock = repository.getDbStock(symbol).getOrNull()
        val isStockUpdated = dbStock?.lastUpdated?.isToday() ?: false

        return if (dbStock != null && isStockUpdated)
            success(dbStock)
        else
            repository.getStock(symbol).onSuccess { repository.saveStock(it) }
    }

    suspend fun getStocks(): Result<Stocks> = repository.getStocks()

    suspend fun searchStock(symbol: String): Result<Stocks> {
        val dbStocks = repository.searchStocks(symbol).getOrNull()?.items

        if (!dbStocks.isNullOrEmpty()) return success(Stocks(dbStocks))

        val symbols = jsonReader.getSymbols()
        val matchingSymbol = symbols.firstOrNull { it.equals(symbol, ignoreCase = true) }

        return if (matchingSymbol != null) {
            repository.getStock(matchingSymbol)
                .onSuccess { repository.saveStock(it) }
                .fold(
                    onSuccess = { success(Stocks(listOf(it))) },
                    onFailure = { failure(it) }
                )
        } else {
            failure(NoSuchElementException("Stock with symbol $symbol not found"))
        }
    }
}

private fun Long.isToday(): Boolean {
    val lastUpdatedDate = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
    val today = Instant.now().atZone(ZoneId.systemDefault()).toLocalDate()
    return lastUpdatedDate.isEqual(today)
}
