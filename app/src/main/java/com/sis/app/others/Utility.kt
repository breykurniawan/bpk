package com.sis.app.others

import android.content.ContentResolver
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import com.bumptech.glide.load.engine.Resource

class Utility {

    companion object {
        val IMAGE_MIME_TYPES = arrayOf("image/jpeg", "image/png")

        const val PICK_IMAGE: Int = 101
        const val TAKE_PHOTOS: Int = 102

        const val QUESTION_TYPE_TEXT: Int = 201
        const val QUESTION_TYPE_TEXTAREA: Int = 202
        const val QUESTION_TYPE_CHECKBOX: Int = 203
        const val QUESTION_TYPE_RADIO: Int = 204
        const val QUESTION_TYPE_SCALE: Int = 205
        const val QUESTION_TYPE_INSERT_PHOTOS: Int = 206
        const val QUESTION_TYPE_SECTION: Int = 300

        const val QUESTION_TEXT: String = "text"
        const val QUESTION_TEXTAREA: String = "textarea"
        const val QUESTION_CHECKBOX: String = "checkbox"
        const val QUESTION_RADIO: String = "radio"
        const val QUESTION_SCALE: String = "scale"
        const val QUESTION_INSERT_PHOTOS: String = "photo"
        const val QUESTION_SECTION: String = "section"

        const val WIDE_POTRAIT: Int = 401
        const val WIDE_LANDSCAPE: Int = 402
        const val STANDARD_POTRAIT: Int = 403
        const val STANDARD_LANDSCAPE: Int = 404

        const val WRITE_EXTERNAL = 501
        const val ACCESS_CAMERA = 502
        const val REQUEST_PERMISSION = 500
    }

    fun getImageFromUri(contentResolver: ContentResolver, uri: Uri?): Bitmap =
        MediaStore.Images.Media.getBitmap(contentResolver, uri)

    /**
     * source https://developer.android.com/topic/performance/graphics/load-bitmap
     */
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2 //960
            val halfWidth: Int = width / 2 //720

            while (height / inSampleSize >= reqHeight && width / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    // First decode with inJustDecodeBounds=true to check dimensions
    fun decodeSampledBitmapFromPath(path: String, reqWidth: Int, reqHeight: Int)
            : Bitmap = BitmapFactory.Options().run {
        inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, this)
        inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
        inJustDecodeBounds = false
        BitmapFactory.decodeFile(path, this)
    }

    fun decodeSampledBitmapFromResource(
        resource: Resources, resId: Int, reqWidth: Int, reqHeight: Int
    ): Bitmap = BitmapFactory.Options().run {
        inJustDecodeBounds = true
        BitmapFactory.decodeResource(resource, resId, this)
        inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
        inJustDecodeBounds = false
        BitmapFactory.decodeResource(resource, resId, this)
    }

    fun checkRatio(width: Float, height: Float): String {
        val r: Float = width / height
        return if (r > 0) {
            if (r > 1.6) "1920 1080"
            else "1920 1440"
        } else {
            if (r > 0.7) "1080 1920"
            else "1440 1920"
        }
    }
}