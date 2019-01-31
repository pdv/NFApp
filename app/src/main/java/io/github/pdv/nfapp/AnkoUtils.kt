package io.github.pdv.nfapp

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewManager
import android.widget.ImageView
import org.jetbrains.anko.custom.ankoView

fun ViewManager.cardView(init: CardView.() -> Unit) = ankoView(::CardView, 0, init)

private fun Toolbar.withNavigationIcon(
    @DrawableRes drawableRes: Int,
    @ColorRes colorRes: Int,
    listener: () -> Unit
) {
    val drawable = resources.getDrawable(drawableRes, null)
    val color = ContextCompat.getColor(context, colorRes)
    drawable.mutate()
    drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    navigationIcon = drawable
    setNavigationOnClickListener { listener() }
}

fun Toolbar.withBackArrow(@ColorRes colorRes: Int, listener: () -> Unit = {}) =
    withNavigationIcon(R.drawable.ic_arrow_back_black_24dp, colorRes, listener)

var ImageView.tintColorResource: Int
    get() = throw UnsupportedOperationException()
    set(value) = setColorFilter(ContextCompat.getColor(context, value), PorterDuff.Mode.SRC_ATOP)

var View.rippleOnClick: Boolean
    get() = throw UnsupportedOperationException()
    set(value) {
        val attrs = intArrayOf(R.attr.selectableItemBackground)
        val typedArray = context.obtainStyledAttributes(attrs)
        val selectableBackground = typedArray.getDrawable(0)
        typedArray.recycle()
        foreground = if (value) selectableBackground else null
    }
