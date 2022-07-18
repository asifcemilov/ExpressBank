package com.expressbank.task.util

import android.content.res.TypedArray

inline fun TypedArray.toList(): List<Int> {
    val list = ArrayList<Int>()
    this.length()
    for (i in 0 until this.length()) {
        list.add(this.getResourceId(i, 0))
    }
    this.recycle()
    return list
}