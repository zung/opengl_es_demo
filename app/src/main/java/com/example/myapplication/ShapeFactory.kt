package com.example.myapplication

import android.content.Context
import com.example.myapplication.lighting.TestLighting
import com.example.myapplication.lighting.TestLighting1
import java.lang.reflect.Type

/**
 *
 * @ClassName:      ShapeFactory
 * @Description:    类作用描述
 * @Author:         czg
 * @CreateDate:     2021/3/10 10:45
 */
class ShapeFactory {
    var mContext: Context? = null

    constructor(context: Context) {
        mContext = context
    }

    fun createShape(type: Int): Shape {
        return when(type) {
            1 -> Square3(mContext)
            2 -> TestLighting(mContext)
            else -> TestLighting1(mContext)
        }
    }
}