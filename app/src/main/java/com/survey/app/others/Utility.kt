package com.survey.app.others

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore

class Utility {

    companion object {
        val PICK_IMAGE: Int = 101
        val TAKE_PHOTOS: Int = 102
        val IMAGE_MIME_TYPES = arrayOf("image/jpeg", "image/png")
        val QUESTION_TYPE_TEXT: Int = 201
        val QUESTION_TYPE_TEXTAREA: Int = 202
        val QUESTION_TYPE_CHECKBOX: Int = 203
        val QUESTION_TYPE_RADIO: Int = 204
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
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    fun decodeSampledBitmapFromResource(path: String, reqWidth: Int, reqHeight: Int): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, this)
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)
            inJustDecodeBounds = false
            BitmapFactory.decodeFile(path, this)
        }
    }

}