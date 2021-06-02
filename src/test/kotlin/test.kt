package micho

import com.scalemonk.kata.thinkreactive.Adnet
import com.scalemonk.kata.thinkreactive.Bid
import io.mockk.mockk
import io.mockk.every
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert
import org.junit.Test


class MyTest {

    @Test
    fun `An auction can be run on an initialized adnet`() {
        // given: an always bidding adnet
        val anAdnet = mockk<Adnet> {
            every { adnetId } returns "lichaNet"
            every { isInitialized() } returns true
            every { requestBid(any()) } returns Single.just(Bid(payload = "some-payload", bidPrice = 3))
        }

        val auction = Auction(listOf(anAdnet))

        // when: the auction is run
        val auctionResult = auction.run().blockingGet()

        // then: the winner is lichaNet
        Assert.assertTrue(auctionResult is AuctionResultWithWinner);
    }

    // test that an auction can't be run if the adnet is not initialized
    @Test
    fun `An auction CANNOT be run on an non-initialized adnet`() {
        // given: an adnet which is not initialized
        val anAdnet = mockk<Adnet> {
            every { adnetId } returns "lichaNet"
            every { isInitialized() } returns false
        }

        val auction = Auction(listOf(anAdnet))

        // when: the auction is run
        val auctionResult = auction.run().blockingGet()

        // then: there is no winner
        Assert.assertTrue(auctionResult is AuctionResultWithoutWinner);
    }

    // test that higher bidder wins
    @Test
    fun `higher bidder wins the auction`() {
        // given: an adnet which is not initialized
        val loserAdnet = mockk<Adnet> {
            every { adnetId } returns "loserAdnet"
            every { isInitialized() } returns true
            every { requestBid(any()) } returns Single.just(Bid(payload = "some-payload", bidPrice = 3))
        }

        val winnerAdnet = mockk<Adnet> {
            every { adnetId } returns "winnerAdnet"
            every { isInitialized() } returns true
            every { requestBid(any()) } returns Single.just(Bid(payload = "some-payload-2", bidPrice = 4))
        }

        val auction = Auction(listOf(loserAdnet, winnerAdnet))

        // when: the auction is run
        auction.run().test().assertValue {
            // then: there is no winner
            it is AuctionResultWithWinner && winnerAdnet.adnetId == it.winner.adnetId
        }
    }

    // test that an auction CAN be run if 1 adnet -out of many- fails

}
