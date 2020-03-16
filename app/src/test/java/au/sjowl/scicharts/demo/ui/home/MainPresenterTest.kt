package au.sjowl.scicharts.demo.ui.home

import au.sjowl.scicharts.demo.CoroutineTestRule
import au.sjowl.scicharts.demo.testAppComponent
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.timeout
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.mockito.ArgumentMatchers

@RunWith(JUnit4::class)
class MainPresenterTest : KoinTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var presenter: MainPresenter

    private val view: IMainView = mock()

    private val viewState: `IMainView$$State` = mock()

    @Before
    fun before() {
        startKoin { modules(testAppComponent) }
        attachDefaultPresenter()
    }

    @After
    fun after() {
        presenter.detachView(view)
        presenter.destroyView(view)
        stopKoin()
    }

    @Test
    fun `show initial chart`() {
        verify(viewState).restartChart(ArgumentMatchers.anyList())
    }

    @Test
    fun `update chart after 1 sec`() {
        verify(viewState, timeout(2000)).updateChartAndScrollToStart(any())
    }

    @Test
    fun `add chart data after pause and update chart after 1 sec`() {
        presenter.detachView(view)
        presenter.setViewState(viewState)
        presenter.attachView(view)
        verify(viewState, timeout(2000).atLeast(1)).updateChartAndScrollToStart(any())
    }

    private fun attachPresenter(presenter: MainPresenter) {
        this.presenter = presenter
        presenter.detachView(view)
        presenter.setViewState(viewState)
        presenter.attachView(view)
    }

    private fun attachDefaultPresenter() {
        attachPresenter(
            MainPresenter(
                coroutineDispatchers = getKoin().get(),
                errorHandler = getKoin().get(),
                priceServer = getKoin().get()
            )
        )
    }
}
