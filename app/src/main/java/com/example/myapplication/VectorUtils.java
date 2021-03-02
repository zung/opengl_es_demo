package com.example.myapplication;

import android.renderscript.Float3;

/**
 * @ClassName: VectorUtils
 * @Description: 类作用描述
 * @Author: czg
 * @CreateDate: 2021/3/2 11:11
 */
public class VectorUtils {
    /**
     * Compute the cross product of two vectors
     *
     * @param v1
     *            The first vector
     * @param v2
     *            The second vector
     * @return  result
     *            Where to store the cross product
     **/
    public static Float3 cross(Float3 v1, Float3 v2) {
        Float3 result = new Float3();
        result.x = v1.y* v2.z - v2.y * v1.z;
        result.y = v1.z * v2.x - v2.z * v1.x;
        result.z = v1.x * v2.y - v2.x * v1.y;
        return result;
    }

    /**
     * Vector subtraction
     *
     * @param a
     * @param b
     * @return
     */
    public static Float3 sub(Float3 a, Float3 b) {
        Float3 res = new Float3();
        res.x = a.x - b.x;
        res.y = a.y - b.y;
        res.z = a.z - b.z;

        return res;
    }

    public static Float3 div(Float3 v, float value) {
        v.x /= value;
        v.y /= value;
        v.z /= value;
        return v;
    }

    public static Float3 negate(Float3 v) {
        v.x = -v.x;
        v.y = -v.y;
        v.z = -v.z;
        return v;
    }

    public static Float3 normalize(Float3 v) {
        double len = Math.sqrt(Math.pow(v.x, 2) + Math.pow(v.y, 2) + Math.pow(v.z, 2));
        return div(v, (float) len);
    }
}
