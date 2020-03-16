package au.sjowl.scicharts.demo.service

import au.sjowl.app.base.android.coroutines.ICoroutineDispatchersProvider
import au.sjowl.scicharts.demo.data.PricePoint
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Implemented as Thread
 * Also could be implemented as Bound Service
 * */
class PriceService(
    dispatchers: ICoroutineDispatchersProvider,
    private val stepMs: Long = 100
) : IPriceService, CoroutineScope {

    override val coroutineContext: CoroutineContext = dispatchers.io

    private val channel = Channel<PricePoint>()

    private var job: Job? = null

    private var latestPoint: PricePoint = PricePoint(System.currentTimeMillis(), 0.0)

    override fun start() {
        job = launch {
            while (isActive) {
                latestPoint = PricePoint(
                    time = System.currentTimeMillis(),
                    price = nextPrice(latestPoint.price)
                )
                channel.send(latestPoint)
                delay(stepMs)
            }
        }
    }

    override suspend fun getHistory(from: Long, to: Long): List<PricePoint> {
        var price = latestPoint.price
        val n = (to - from) / stepMs - 1

        return (n downTo 0).mapIndexed { i, _ ->
            price = nextPrice(price)
            PricePoint(from + (i + 1) * stepMs, price)
        }
    }

    override fun stop() {
        job?.cancel()
    }

    override fun observePrice(): Channel<PricePoint> = channel

    private fun nextPrice(prevPrice: Double): Double = prevPrice + (Random.nextFloat() * 2 - 1)
}
