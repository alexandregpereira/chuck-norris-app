package br.bano.chucknorris.utils

import android.widget.TextView


fun TextView.setTextOrGone(text: String?) {
    this.text = text
    this.goneIfMissing()
}

fun TextView.goneIfMissing() {
    this.apply {
        if (text == null || text.isEmpty()) gone() else visible()
    }
}