package au.sjowl.app.base.rx

import au.sjowl.app.base.SLog
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.Disposable

fun <T> Single<T>.defaults(schedulers: AppSchedulers): Single<T> {
    return compose {
        it.subscribeOn(schedulers.io())
            .observeOn(schedulers.main())
            .doOnError { error -> SLog.e(error.message, error) }
    }
}

fun <T> Observable<T>.defaults(schedulers: AppSchedulers): Observable<T> {
    return compose {
        it.subscribeOn(schedulers.io())
            .observeOn(schedulers.main())
            .doOnError { error -> SLog.e(error.message, error) }
    }
}

fun Completable.defaults(schedulers: AppSchedulers): Completable {
    return compose {
        it.subscribeOn(schedulers.io())
            .observeOn(schedulers.main())
            .doOnError { error -> SLog.e(error.message, error) }
    }
}

fun <T> Single<T>.main(schedulers: AppSchedulers): Single<T> {
    return compose {
        it.subscribeOn(schedulers.main())
            .observeOn(schedulers.main())
            .doOnError { error -> SLog.e(error.message, error) }
    }
}

fun <T> Observable<T>.main(schedulers: AppSchedulers): Observable<T> {
    return compose {
        it.subscribeOn(schedulers.main())
            .observeOn(schedulers.main())
            .doOnError { error -> SLog.e(error.message, error) }
    }
}

fun <T> Single<T>.bg(schedulers: AppSchedulers): Single<T> {
    return compose {
        it.subscribeOn(schedulers.main())
            .observeOn(schedulers.main())
            .doOnError { error -> SLog.e(error.message, error) }
    }
}

fun <T> Observable<T>.bg(schedulers: AppSchedulers): Observable<T> {
    return compose {
        it.subscribeOn(schedulers.main())
            .observeOn(schedulers.main())
            .doOnError { error -> SLog.e(error.message, error) }
    }
}

fun Completable.bg(schedulers: AppSchedulers): Completable {
    return compose {
        it.subscribeOn(schedulers.io())
            .observeOn(schedulers.io())
            .doOnError { error -> SLog.e(error.message, error) }
    }
}

fun <T> Single<T>.onMain(schedulers: AppSchedulers, onSuccess: (t: T) -> Unit, onError: (e: Throwable) -> Unit = {}): Disposable {
    return main(schedulers).subscribe(onSuccess, onError)
}

fun <T> Observable<T>.onMain(schedulers: AppSchedulers, onSuccess: (t: T) -> Unit, onError: (e: Throwable) -> Unit = {}): Disposable {
    return main(schedulers).subscribe(onSuccess, onError)
}

fun <T> Single<T>.withDefaults(schedulers: AppSchedulers, onSuccess: (t: T) -> Unit, onError: (e: Throwable) -> Unit = {}): Disposable {
    return defaults(schedulers).subscribe(onSuccess, onError)
}

fun <T> Observable<T>.withDefaults(schedulers: AppSchedulers, onSuccess: (t: T) -> Unit, onError: (e: Throwable) -> Unit = {}): Disposable {
    return defaults(schedulers).subscribe(onSuccess, onError)
}

fun Completable.withDefaults(schedulers: AppSchedulers, onSuccess: () -> Unit = {}, onError: (e: Throwable) -> Unit = {}): Disposable {
    return defaults(schedulers).subscribe(onSuccess, onError)
}

fun <T> Single<T>.onSameThread(onSuccess: (t: T) -> Unit, onError: (e: Throwable) -> Unit = {}): Disposable {
    return subscribe(onSuccess, onError)
}

fun <T> Single<T>.onBackground(schedulers: AppSchedulers, onSuccess: (t: T) -> Unit, onError: (e: Throwable) -> Unit = {}): Disposable {
    return defaults(schedulers).subscribe(onSuccess, onError)
}

fun <T> Observable<T>.onBackground(schedulers: AppSchedulers, onSuccess: (t: T) -> Unit, onError: (e: Throwable) -> Unit = {}): Disposable {
    return defaults(schedulers).subscribe(onSuccess, onError)
}

fun Completable.onBackground(schedulers: AppSchedulers, onSuccess: () -> Unit = {}, onError: (e: Throwable) -> Unit = {}): Disposable {
    return bg(schedulers).subscribe(onSuccess, onError)
}