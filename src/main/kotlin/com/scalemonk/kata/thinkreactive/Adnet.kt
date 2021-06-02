package com.scalemonk.kata.thinkreactive

import io.reactivex.Completable
import io.reactivex.Single

interface Adnet {

    val adnetId: String

    /**
     * initializes the adnet for the given [appId]
     * @return a Completable when the initialization is done. If anything goes wrong (network error, internal errors, etc) it emits an error
     */
    fun initialize(appId: String): Completable

    fun isInitialized(): Boolean


    /**
     * Request a bid for the given [deviceId]
     * @return a Single emitting the [BidResult]. If anything goes wrong (network error, internal errors, etc) it emits an error
     */
    fun requestBid(deviceId: String): Single<BidResult>
}