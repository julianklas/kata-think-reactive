package micho

import com.scalemonk.kata.thinkreactive.Adnet
import com.scalemonk.kata.thinkreactive.Bid
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable

class Auction(val adnets: List<Adnet>) {

    fun run(): Single<AuctionResult> {
        val deviceId = "fake-device-id"

        return adnets.toObservable()
            .filter { adnet ->
                adnet.isInitialized()
            }
            .flatMapSingle { adnet ->
                adnet.requestBid(deviceId).map { bidResult ->
                    Pair(adnet, bidResult)
                }
            }
            .toList()
            .map { listOfPairs ->
                if(listOfPairs.isEmpty()) return@map AuctionResultWithoutWinner()

                val adnetAndBidResult = listOfPairs.maxBy { pair ->
                    (pair.second as Bid).bidPrice
                }
                AuctionResultWithWinner(adnetAndBidResult!!.first)
            }
    }
}
