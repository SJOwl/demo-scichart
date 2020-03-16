@file:Suppress("SpellCheckingInspection")

package au.sjowl.scicharts.demo.base

import timber.log.Timber

internal typealias MessageListener = (message: String) -> Unit

class ErrorHandler {

    fun proceed(error: Throwable, messageListener: MessageListener) {
        Timber.e(error)
    }
}
