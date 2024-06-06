package com.nine_tailstech.nexlist

import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.widget.LinearLayout
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.caverock.androidsvg.SVG

class SvgDrawableTranscoder(private val layout: LinearLayout) : CustomTarget<PictureDrawable>() {
    override fun onResourceReady(resource: PictureDrawable, transition: Transition<in PictureDrawable>?) {
        layout.background = resource
    }

    override fun onLoadCleared(placeholder: Drawable?) {
        // Handle cleanup if needed
    }
}
