package au.sjowl.app.base

import android.os.Environment
import io.reactivex.Observable

class MediaScanner(
    val extensions: Regex
) {

    fun scan(): Observable<String> {
        val rootFile = Environment.getExternalStorageDirectory()
        return Observable.fromIterable(
            rootFile.walkTopDown()
                .filter { it.isFile }
                .filter { it.absolutePath.contains(extensions) }
                .map { it.absolutePath }
                .asIterable()
        )
    }
}
