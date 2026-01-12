package org.n27.ktstonks.data.yfinance.mapping

import org.junit.Assert.assertEquals
import org.junit.Test
import org.n27.ktstonks.test_data.getStock
import org.n27.ktstonks.test_data.data.getStockRaw

class StockMapperTest {

    @Test
    fun `toDomainEntity should map StockRaw to Stock correctly`() {
        val result = getStockRaw().toDomainEntity(logo = "/9j/2wCEAAEBAQEBAQEBAQEBAQEB")

        assertEquals(getStock(lastUpdated = result.lastUpdated), result)
    }
}
