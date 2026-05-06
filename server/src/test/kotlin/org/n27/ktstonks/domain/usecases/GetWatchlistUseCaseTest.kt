package org.n27.ktstonks.domain.usecases

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.n27.ktstonks.domain.Repository
import org.n27.ktstonks.test_data.getStocks
import kotlin.Result.Companion.success

class GetWatchlistUseCaseTest {

    private lateinit var useCase: GetWatchlistUseCase
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        repository = mock(Repository::class.java)
        useCase = GetWatchlistUseCase(repository)
    }

    @Test
    fun `getWatchlist should call repository`(): Unit = runBlocking {
        `when`(repository.getWatchlist(anyInt(), anyInt())).thenReturn(success(getStocks()))

        val actual = useCase(0, 1)

        assertEquals(getStocks(), actual.getOrNull())
    }
}
