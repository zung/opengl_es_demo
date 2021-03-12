package com.example.myapplication

import android.content.Context
import com.example.myapplication.lighting.TestLighting
import com.example.myapplication.lighting.MultipleLighting
import com.example.myapplication.testdepth.TestDepthShape

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

    fun createShape(type: Any): Shape? {
        return when(type) {
            Square3::class.java -> Square3(mContext)
            TestLighting::class.java -> TestLighting(mContext)
            MultipleLighting::class.java -> MultipleLighting(mContext)
            TestDepthShape::class.java -> TestDepthShape(mContext)
            else -> null
        }
    }
}