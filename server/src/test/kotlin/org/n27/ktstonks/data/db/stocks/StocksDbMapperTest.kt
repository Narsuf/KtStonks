package org.n27.ktstonks.data.db.stocks

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.n27.ktstonks.test_data.data.getStockEntity
import org.n27.ktstonks.test_data.data.getStocksEntity

class StocksDbMapperTest {

    private lateinit var dao: StocksDao

    @Before
    fun setUp() {
        Database.connect("jdbc:h2:mem:dbmapper;DB_CLOSE_DELAY=-1", driver = "org.h2.Driver")
        transaction { SchemaUtils.create(StocksTable) }
        dao = StocksDao()
    }

    @After
    fun tearDown() {
        transaction { SchemaUtils.drop(StocksTable) }
    }

    @Test
    fun `toStockEntity should map ResultRow to StockEntity correctly`() = runBlocking {
        val stock = getStockEntity()
        dao.saveStock(stock)

        val result = newSuspendedTransaction {
            StocksTable.selectAll().single().toStockEntity()
        }

        assertEquals(stock, result)
    }

    @Test
    fun `fromStockEntity should persist all fields correctly`() = runBlocking {
        val stock = getStockEntity()
        dao.saveStock(stock)

        val result = dao.getStock(stock.symbol)

        assertEquals(stock, result)
    }

    @Test
    fun `toStockEntities should paginate correctly`() = runBlocking {
        val stock1 = getStockEntity(symbol = "AAPL", isWatchlisted = true)
        val stock2 = getStockEntity(symbol = "GOOG", isWatchlisted = true)
        dao.saveStock(stock1)
        dao.saveStock(stock2)

        val page0 = dao.getWatchlist(0, 1)
        val page1 = dao.getWatchlist(1, 1)

        assertEquals(1, page0.items.size)
        assertEquals(1, page0.nextPage)
        assertEquals(1, page1.items.size)
        assertEquals(null, page1.nextPage)
    }
}
