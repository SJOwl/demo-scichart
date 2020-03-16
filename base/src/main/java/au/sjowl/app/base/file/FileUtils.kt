package au.sjowl.scicharts.demo.ui

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import au.sjowl.app.base.logd
import au.sjowl.app.base.loge
import java.io.File
import java.io.FileFilter
import java.text.DecimalFormat
import java.util.Comparator
import java.util.Locale

object FileUtils {

    const val MIME_TYPE_IMAGE = "image/*"

    const val HIDDEN_PREFIX = "."

    /**
     * File and folder comparator.
     *
     * @author paulburke
     */
    var sComparator: Comparator<File?> = Comparator<File?> { f1, f2 ->
        f1?.name.orEmpty().toLowerCase(locale).compareTo(
            f2?.name.orEmpty().toLowerCase(locale)
        )
    }

    /**
     * File (not directories) filter.
     *
     * @author paulburke
     */
    var sFileFilter = FileFilter { file ->
        val fileName: String = file.name
        // Return files only (not directories) and skip hidden files

        file.isFile && !fileName.startsWith(HIDDEN_PREFIX)
    }

    /**
     * Folder (directories) filter.
     *
     * @author paulburke
     */
    var sDirFilter = FileFilter { file ->
        val fileName: String = file.name
        // Return directories only and skip hidden directories

        file.isDirectory && !fileName.startsWith(HIDDEN_PREFIX)
    }

    private val locale = Locale.getDefault()

    private const val DEBUG = false // Set to true to enable logging

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @param uri
     * @return Extension including the dot("."); "" if there is no extension;
     * null if uri was null.
     */
    fun getExtension(uri: String?): String? {
        if (uri == null) {
            return null
        }
        val dot = uri.lastIndexOf(".")
        return if (dot >= 0) {
            uri.substring(dot)
        } else {
            // No extension.
            ""
        }
    }

    /**
     * @return Whether the URI is a local one.
     */
    fun isLocal(url: String?): Boolean {
        return url != null && !url.startsWith("http://") && !url.startsWith("https://")
    }

    /**
     * @return True if Uri is a MediaStore Uri.
     * @author paulburke
     */
    fun isMediaUri(uri: Uri?): Boolean {
        return "media".equals(uri!!.authority, ignoreCase = true)
    }

    /**
     * Convert File into Uri.
     *
     * @param file
     * @return uri
     */
    fun getUri(file: File?): Uri? {
        return if (file != null) {
            Uri.fromFile(file)
        } else null
    }

    /**
     * Returns the path only (without file name).
     *
     * @param file
     * @return
     */
    fun getPathWithoutFilename(file: File?): File? {
        return if (file != null) {
            if (file.isDirectory) {
                // no file to be split off. Return everything

                file
            } else {
                val filename: String = file.name
                val filepath: String = file.absolutePath

                // Construct path without file name.

                var pathwithoutname = filepath.substring(
                    0,
                    filepath.length - filename.length
                )
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length - 1)
                }
                File(pathwithoutname)
            }
        } else null
    }

    /**
     * @return The MIME type for the given file.
     */
    fun getMimeType(file: File): String? {
        val extension = getExtension(file.name)
        return if (extension!!.isNotEmpty()) MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(extension.substring(1)) else "application/octet-stream"
    }

    /**
     * @return The MIME type for the give Uri.
     */
    fun getMimeType(context: Context, uri: Uri): String? {
        val file = File(getPath(context, uri))
        return getMimeType(file)
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is [LocalStorageProvider].
     * @author paulburke
     */
    fun isLocalStorageDocument(uri: Uri): Boolean {
        return LocalStorageProvider.AUTHORITY.equals(uri.authority)
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                if (DEBUG) DatabaseUtils.dumpCursor(cursor)
                val columnIndex = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br></br>
     * <br></br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @see .isLocal
     * @see .getFile
     * @author paulburke
     */
    fun getPath(context: Context, uri: Uri): String? {
        logd("Authority: ${uri.authority}, Fragment: ${uri.fragment}, Port: ${uri.port}, Query: ${uri.query}, Scheme: ${uri.scheme}, Host: ${uri.host}, Segments: ${uri.pathSegments}")
        // DocumentProvider

        if (DocumentsContract.isDocumentUri(context, uri)) {
            // LocalStorageProvider

            if (isLocalStorageDocument(uri)) {
                // The path is the id

                return DocumentsContract.getDocumentId(uri)
            }
            // ExternalStorageProvider
            else if (isExternalStorageDocument(uri)) {
                val docId: String = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                val id: String? = DocumentsContract.getDocumentId(uri)
                val contentUri: Uri? = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), id?.toLong() ?: 0L
                )
                return getDataColumn(context, contentUri, null, null)
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                val docId: String = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        }
        // MediaStore (and general)
        else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address

            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        }
        // File
        else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     * Uri is unsupported or pointed to a remote resource.
     * @see .getPath
     * @author paulburke
     */
    fun getFile(context: Context, uri: Uri?): File? {
        if (uri != null) {
            val path = getPath(context, uri)
            if (path != null && isLocal(path)) {
                return File(path)
            }
        }
        return null
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @param size
     * @return
     * @author paulburke
     */
    fun getReadableFileSize(size: Int): String {
        val bytesInKilobytes = 1024
        val dec = DecimalFormat("###.#")
        val kilobytes = " KB"
        val megabytes = " MB"
        val gigabytes = " GB"
        var fileSize = 0f
        var suffix = kilobytes
        if (size > bytesInKilobytes) {
            fileSize = size / bytesInKilobytes.toFloat()
            if (fileSize > bytesInKilobytes) {
                fileSize /= bytesInKilobytes
                if (fileSize > bytesInKilobytes) {
                    fileSize /= bytesInKilobytes
                    suffix = gigabytes
                } else {
                    suffix = megabytes
                }
            }
        }
        return dec.format(fileSize.toDouble()) + suffix
    }

    /**
     * Attempt to retrieve the thumbnail of given File from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param file
     * @return
     * @author paulburke
     */
    fun getThumbnail(context: Context, file: File): Bitmap? {
        val mime = getMimeType(file)
        return if (mime == null) null else getThumbnail(context, getUri(file), mime)
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @return
     * @author paulburke
     */
    fun getThumbnail(context: Context, uri: Uri): Bitmap? {
        val mime = getMimeType(context, uri)
        return if (mime != null) getThumbnail(context, uri, mime) else null
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @param context
     * @param uri
     * @param mimeType
     * @return
     * @author paulburke
     */
    fun getThumbnail(context: Context, uri: Uri?, mimeType: String): Bitmap? {
        if (!isMediaUri(uri)) {
            loge("You can only retrieve thumbnails for images and videos.")
            return null
        }
        var bm: Bitmap? = null
        if (uri != null) {
            val resolver: ContentResolver = context.contentResolver
            var cursor: Cursor? = null
            try {
                cursor = resolver.query(uri, null, null, null, null)
                if (cursor.moveToFirst()) {
                    val id = cursor.getInt(0)
                    logd("Got thumb ID: $id")
                    if (mimeType.contains("video")) {
                        bm = MediaStore.Video.Thumbnails.getThumbnail(
                            resolver,
                            id.toLong(),
                            MediaStore.Video.Thumbnails.MINI_KIND,
                            null
                        )
                    } else if (mimeType.contains(MIME_TYPE_IMAGE)) {
                        bm = MediaStore.Images.Thumbnails.getThumbnail(
                            resolver,
                            id.toLong(),
                            MediaStore.Images.Thumbnails.MINI_KIND,
                            null
                        )
                    }
                }
            } catch (e: Exception) {
                loge(e)
            } finally {
                cursor?.close()
            }
        }
        return bm
    }

    /**
     * Get the Intent for selecting content to be used in an Intent Chooser.
     *
     * @return The intent for opening a file with Intent.createChooser()
     * @author paulburke
     */
    fun createGetContentIntent(): Intent {
        // Implicitly allow the user to select a particular kind of data

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        // The MIME data type filter

        intent.type = "*/*"
        // Only return URIs that can be opened with ContentResolver

        intent.addCategory(Intent.CATEGORY_OPENABLE)
        return intent
    }
}
