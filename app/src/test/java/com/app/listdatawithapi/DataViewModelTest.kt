package com.app.listdatawithapi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@ExperimentalCoroutinesApi
class DataViewModelTest {
    // Required for LiveData to be tested synchronously
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // Rule for Mockito initialization
    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    // Use TestCoroutineDispatcher for controlling coroutines
    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    lateinit var dataRepository: DataRepository

    @Mock
    lateinit var dataObserver: Observer<Result<ArrayList<DataList>>>

    private lateinit var dataViewModel: DataViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Set main dispatcher to testDispatcher
        dataViewModel = DataViewModel(dataRepository)
        dataViewModel.dataResult.observeForever(dataObserver)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset main dispatcher
        testDispatcher.cleanupTestCoroutines() // Clean up test coroutines
    }

    @Test
    fun `fetchData should update liveData with data`() = runBlockingTest {
        // Arrange
        val data = DataList(1,
            "1",
            "accusamus beatae ad facilis cum similique qui sunt",
            "https://via.placeholder.com/600/92c952",
            "https://via.placeholder.com/150/92c952")
        val mockUser = Result.success(arrayListOf(data))

        `when`(dataRepository.data()).thenReturn(mockUser)

        // Act
        dataViewModel.data()

        // Assert
        verify(dataObserver).onChanged(mockUser) // Verifies LiveData was updated
        verify(dataRepository).data()   // Verifies repository was called
    }


}