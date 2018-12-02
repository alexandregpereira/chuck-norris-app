package br.bano.chucknorris.utils

import android.view.View

fun View.gone(): View {
    this.visibility = View.GONE
    return this
}

fun View.visible(): View {
    this.visibility = View.VISIBLE
    return this
}