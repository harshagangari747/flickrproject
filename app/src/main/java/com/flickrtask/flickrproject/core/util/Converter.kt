package com.flickrtask.flickrproject.core.util

import android.util.Log

fun String.isValidateInput(): Boolean {
    if (this.isNullOrEmpty()) {
        return false
    }
    for (ch in this) {

        if (!ch.isLetter() && !ch.isWhitespace() && ch != ',') {
            Log.d("ch", "isValidateInput: $ch")
            return false
        }

    }

    return true


}