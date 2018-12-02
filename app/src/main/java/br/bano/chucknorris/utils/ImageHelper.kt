package br.bano.chucknorris.utils

import android.app.Activity
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide

fun ImageView.loadImageUrl(url: String?) {
    val urlTmp: String = url ?: ""
    val context = this.context
    if (context is FragmentActivity && context.isDestroyed)
        return
    else if (context is Activity && context.isDestroyed)
        return

    Glide.with(context)
            .load(urlTmp)
            .into(this)
}

