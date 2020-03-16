package au.sjowl.scicharts.demo.service

import au.sjowl.scicharts.demo.data.PricePoint
import kotlinx.coroutines.channels.Channel

interface IPriceService {
    fun start()
    fun stop()
    fun observePrice(): Channel<PricePoint>
    suspend fun getHistory(from: Long, to: Long): List<PricePoint>
}
