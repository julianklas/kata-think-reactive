package micho

import com.scalemonk.kata.thinkreactive.Adnet

interface AuctionResult

class AuctionResultWithoutWinner: AuctionResult
data class AuctionResultWithWinner(val winner: Adnet): AuctionResult
