package org.n27.ktstonks.data.db.stocks

import org.junit.Assert.assertEquals
import org.junit.Test
import org.n27.ktstonks.data.db.stocks.StocksEntity.StockEntity
import org.n27.ktstonks.domain.model.Stocks
import org.n27.ktstonks.test_data.data.getStockEntity
import org.n27.ktstonks.test_data.data.getStocksEntity
import org.n27.ktstonks.test_data.getStock
import org.n27.ktstonks.test_data.getStocks
import java.util.*

class StocksMapperTest {

    @Test
    fun `toStocks should map StocksEntity to Stocks correctly`() {
        val expected = getStocks(
            items = listOf(getStock(logo = "aHR0cHM6Ly9pbWcubG9nby5kZXYvYXBwbGUuY29t"))
        )

        val result = getStocksEntity().toStocks()

        assertEquals(expected, result)
    }

    @Test
    fun `toStock should map StockEntity to Stock correctly`() {
        val expected = getStock(logo = "aHR0cHM6Ly9pbWcubG9nby5kZXYvYXBwbGUuY29t")

        val result = getStockEntity().toStock()

        assertEquals(expected, result)
    }

    @Test
    fun `toEntity should map Stock to StockEntity correctly`() {
        val result = getStock().toEntity()

        assertEquals(getStockEntity(), result)
    }
}
