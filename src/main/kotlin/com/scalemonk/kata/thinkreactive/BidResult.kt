package com.scalemonk.kata.thinkreactive

interface BidResult

// result per adnet
data class NoFill(val reason:String): BidResult
data class Bid(val payload:String, val bidPrice: Int): BidResult

// result for many adnets
