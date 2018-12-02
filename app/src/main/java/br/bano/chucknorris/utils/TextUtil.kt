package br.bano.chucknorris.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("textOrGone")
fun TextView.setTextOrGone(text: String?) {
    this.text = text
    this.goneIfMissing()
}

fun TextView.goneIfMissing() = this.apply {
    if (text == null || text.isEmpty()) gone() else visible()
}