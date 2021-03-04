package com.example.myapplication

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat

/**
 *
 * @ClassName:      BitmapUtils
 * @Description:    类作用描述
 * @Author:         czg
 * @CreateDate:     2021/3/4 14:01
 */
class BitmapUtils {
    companion object {
        fun getBitmap(context: Context, vectorDrawableId: Int): Bitmap? {
            var bitmap: Bitmap? = null
            val vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableId)

            bitmap = Bitmap.createBitmap(
                vectorDrawable!!.intrinsicWidth,
                vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )

            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)

            val matrix = android.graphics.Matrix()
            matrix.setScale(1.0f, -1.0f)//垂直翻转
//        matrix.setScale(-1.0f, 1.0f)//水平翻转
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            return bitmap
        }
    }
}