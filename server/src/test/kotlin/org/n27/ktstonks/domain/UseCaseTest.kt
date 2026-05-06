package org.n27.ktstonks.domain

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.n27.ktstonks.domain.model.Stocks.ValuationMeasures
import org.n27.ktstonks.test_data.getStock
import org.n27.ktstonks.test_data.getStocks
import kotlin.Result.Companion.success

class UseCaseTest {

    private lateinit var useCase: UseCase
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        repository = mock(Repository::class.java)
        useCase = UseCase(repository)
    }

    @Test
    fun `getStock should call repository`(): Unit = runBlocking {
        `when`(repository.getStock(anyString())).thenReturn(success(getStock()))

        val actual = useCase.getStock("AAPL")

        assertEquals(getStock(), actual.getOrNull())
    }

    @Test
    fun `getStocks should call repository`(): Unit = runBlocking {
        `when`(repository.getStocks(anyInt(), anyInt(),anyBoolean(), anyString())).thenReturn(success(getStocks()))

        val actual = useCase.getStocks(0, 1, false, "AAPL")

        assertEquals(getStocks(), actual.getOrNull())
    }

    @Test
    fun `getWatchlist should call repository`(): Unit = runBlocking {
        `when`(repository.getWatchlist(anyInt(), anyInt())).thenReturn(success(getStocks()))

        val actual = useCase.getWatchlist(0, 1)

        assertEquals(getStocks(), actual.getOrNull())
    }

    @Test
    fun `addToWatchlist should call repository`(): Unit = runBlocking {
        val symbol = "AAPL"

        useCase.addToWatchlist(symbol)

        verify(repository).addToWatchlist(symbol)
    }

    @Test
    fun `removeFromWatchlist should call repository`(): Unit = runBlocking {
        val symbol = "AAPL"

        useCase.removeFromWatchlist(symbol)

        verify(repository).removeFromWatchlist(symbol)
    }

    @Test
    fun `addCustomValuation should calculate intrinsic value`(): Unit = runBlocking {
        `when`(repository.getStock(anyString())).thenReturn(success(getStock()))
        `when`(repository.updateStock(any())).thenReturn(success(Unit))

        useCase.addCustomValuation("AAPL", 12.5)

        verify(repository).updateStock(
            getStock(valuationMeasures = ValuationMeasures(
                pe = 34.7215522245231,
                valuationFloor = 12.5,
                intrinsicValue = 93.37500000000016,
            ))
        )
    }

    @Test
    fun `addCustomValuation should return 0 intrinsic value when pe is null`(): Unit = runBlocking {
        `when`(repository.getStock(anyString())).thenReturn(success(getStock(valuationMeasures = ValuationMeasures(pe = null, valuationFloor = null, intrinsicValue = null))))
        `when`(repository.updateStock(any())).thenReturn(success(Unit))

        useCase.addCustomValuation("AAPL", 12.5)

        verify(repository).updateStock(
            getStock(valuationMeasures = ValuationMeasures(
                pe = null,
                valuationFloor = 12.5,
                intrinsicValue = 0.0,
            ))
        )
    }

    @Test
    fun `addCustomValuation should return 0 intrinsic value when price is null`(): Unit = runBlocking {
        `when`(repository.getStock(anyString())).thenReturn(success(getStock(price = null)))
        `when`(repository.updateStock(any())).thenReturn(success(Unit))

        useCase.addCustomValuation("AAPL", 12.5)

        verify(repository).updateStock(
            getStock(price = null, valuationMeasures = ValuationMeasures(
                pe = 34.7215522245231,
                valuationFloor = 12.5,
                intrinsicValue = 0.0,
            ))
        )
    }

    @Test
    fun `addCustomValuation should return 0 intrinsic value when pe is 0`(): Unit = runBlocking {
        `when`(repository.getStock(anyString())).thenReturn(success(getStock(valuationMeasures = ValuationMeasures(pe = 0.0, valuationFloor = null, intrinsicValue = null))))
        `when`(repository.updateStock(any())).thenReturn(success(Unit))

        useCase.addCustomValuation("AAPL", 12.5)

        verify(repository).updateStock(
            getStock(valuationMeasures = ValuationMeasures(
                pe = 0.0,
                valuationFloor = 12.5,
                intrinsicValue = 0.0,
            ))
        )
    }
}
