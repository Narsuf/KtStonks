package org.n27.ktstonks.domain.usecases

import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.domain.model.Stocks.ValuationMeasures
import org.n27.ktstonks.test_data.getStock
import kotlin.Result.Companion.success

class AddCustomValuationUseCaseTest {

    private lateinit var useCase: AddCustomValuationUseCase
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        repository = mock(Repository::class.java)
        useCase = AddCustomValuationUseCase(repository)
    }

    @Test
    fun `addCustomValuation should calculate intrinsic value`(): Unit = runBlocking {
        `when`(repository.getStock(anyString())).thenReturn(success(getStock()))
        `when`(repository.updateStock(any())).thenReturn(success(Unit))

        useCase("AAPL", 12.5)

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

        useCase("AAPL", 12.5)

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

        useCase("AAPL", 12.5)

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

        useCase("AAPL", 12.5)

        verify(repository).updateStock(
            getStock(valuationMeasures = ValuationMeasures(
                pe = 0.0,
                valuationFloor = 12.5,
                intrinsicValue = 0.0,
            ))
        )
    }
}
