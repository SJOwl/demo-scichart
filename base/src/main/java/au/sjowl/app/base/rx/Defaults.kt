@file:Suppress("unused")

package au.sjowl.app.base.rx

import au.sjowl.app.base.loge
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

val errorHandler = Consumer<Throwable> { error ->
    loge(error.message)
    error.printStackTrace()
}

fun <T> Flowable<T>.defaults(schedulers: AppSchedulers): Flowable<T> {
    return compose {
        it.subscribeOn(schedulers.io())
            .observeOn(schedulers.main())
            .doOnError(errorHandler)
    }
}

fun <T> Maybe<T>.defaults(schedulers: AppSchedulers): Maybe<T> {
    return compose {
        it.subscribeOn(schedulers.io())
            .observeOn(schedulers.main())
            .doOnError(errorHandler)
    }
}

fun <T> Single<T>.defaults(schedulers: AppSchedulers): Single<T> {
    return compose {
        it.subscribeOn(schedulers.io())
            .observeOn(schedulers.main())
            .doOnError(errorHandler)
    }
}

fun <T> Observable<T>.defaults(schedulers: AppSchedulers): Observable<T> {
    return compose {
        it.subscribeOn(schedulers.io())
            .observeOn(schedulers.main())
            .doOnError(errorHandler)
    }
}

fun Completable.defaults(schedulers: AppSchedulers): Completable {
    return compose {
        it.subscribeOn(schedulers.io())
            .observeOn(schedulers.main())
            .doOnError(errorHandler)
    }
}

fun <T> Single<T>.main(schedulers: AppSchedulers): Single<T> {
    return compose {
        it.subscribeOn(schedulers.main())
            .observeOn(schedulers.main())
            .doOnError(errorHandler)
    }
}

fun <T> Observable<T>.main(schedulers: AppSchedulers): Observable<T> {
    return compose {
        it.subscribeOn(schedulers.main())
            .observeOn(schedulers.main())
            .doOnError(errorHandler)
    }
}

fun Completable.main(schedulers: AppSchedulers): Completable {
    return compose {
        it.subscribeOn(schedulers.main())
            .observeOn(schedulers.main())
            .doOnError(errorHandler)
    }
}

fun <T> Single<T>.bg(schedulers: AppSchedulers): Single<T> {
    return compose {
        it.subscribeOn(schedulers.io())
            .observeOn(schedulers.io())
            .doOnError(errorHandler)
    }
}

fun <T> Observable<T>.bg(schedulers: AppSchedulers): Observable<T> {
    return compose {
        it.subscribeOn(schedulers.io())
            .observeOn(schedulers.io())
            .doOnError(errorHandler)
    }
}

fun Completable.bg(schedulers: AppSchedulers): Completable {
    return compose {
        it.subscribeOn(schedulers.io())
            .observeOn(schedulers.io())
            .doOnError(errorHandler)
    }
}

fun <T> Single<T>.onMain(schedulers: AppSchedulers, onSuccess: (t: T) -> Unit, onError: (e: Throwable) -> Unit = {}): Disposable {
    return main(schedulers).subscribe(onSuccess, onError)
}

fun <T> Observable<T>.onMain(schedulers: AppSchedulers, onSuccess: (t: T) -> Unit, onError: (e: Throwable) -> Unit = {}): Disposable {
    return main(schedulers).subscribe(onSuccess, onError)
}

fun Completable.onMain(
    schedulers: AppSchedulers,
    onSuccess: () -> Unit,
    onError: (e: Throwable) -> Unit = {}
): Disposable {
    return main(schedulers).subscribe(onSuccess, onError)
}
