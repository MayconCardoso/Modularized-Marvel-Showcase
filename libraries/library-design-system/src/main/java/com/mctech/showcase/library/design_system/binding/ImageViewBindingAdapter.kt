package com.mctech.showcase.library.design_system.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.mctech.showcase.library.design_system.R
import com.squareup.picasso.Picasso

@BindingAdapter("loadImage", requireAll = false)
fun ImageView.loadImage(url: String) {
    Picasso.get()
        .load(url)
        .error(R.drawable.image_not_available)
        .into(this)
}

@BindingAdapter("loadImageWithLoader", requireAll = false)
fun ImageView.loadImageWithLoader(url: String) {
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(R.drawable.shape_loading)
        .centerInside()
        .error(R.drawable.image_not_available)
        .into(this)
}