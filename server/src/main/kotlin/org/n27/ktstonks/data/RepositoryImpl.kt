package org.n27.ktstonks.data

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.n27.ktstonks.data.alpha_vantage.AlphaVantageApi
import org.n27.ktstonks.data.alpha_vantage.mapping.toDomainEntity
import org.n27.ktstonks.data.alpha_vantage.mapping.toExpectedEpsGrowth
import org.n27.ktstonks.data.alpha_vantage.mapping.toPrice
import org.n27.ktstonks.domain.model.Stock
import org.n27.ktstonks.domain.Repository

class RepositoryImpl(private val api: AlphaVantageApi) : Repository {

    override suspend fun getStock(symbol: String): Result<Stock> = runCatching {
        coroutineScope {
            val stockDeferred = async { api.getStock(symbol) }
            val quoteDeferred = async { api.getGlobalQuote(symbol) }
            val epsDeferred = async { api.getEpsEstimate(symbol) }

            val stock = stockDeferred.await()
            val globalQuote = quoteDeferred.await()
            val estimates = epsDeferred.await()

            stock.toDomainEntity(globalQuote.toPrice(), estimates.toExpectedEpsGrowth())
        }
    }
}
