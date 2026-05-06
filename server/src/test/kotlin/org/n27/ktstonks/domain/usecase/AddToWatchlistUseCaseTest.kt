package org.n27.ktstonks.domain.usecase

import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.n27.ktstonks.domain.Repository

class AddToWatchlistUseCaseTest {

    private lateinit var useCase: AddToWatchlistUseCase
    private lateinit var repository: Repository

    @Before
    fun setUp() {
        repository = mock(Repository::class.java)
        useCase = AddToWatchlistUseCase(repository)
    }

    @Test
    fun `addToWatchlist should call repository`(): Unit = runBlocking {
        useCase("AAPL")

        verify(repository).addToWatchlist("AAPL")
    }
}
