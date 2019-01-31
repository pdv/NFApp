package io.github.pdv.nfapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.text.TextUtils
import android.view.Gravity
import android.view.ViewManager
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import java.util.*

fun ViewManager.personDetailHeader(name: String) = relativeLayout {
    backgroundColorResource = R.color.slate
    textView {
        ellipsize = TextUtils.TruncateAt.END
        textColorResource = R.color.white
        textSize = 24f
        text = name
    }.lparams {
        gravity = Gravity.BOTTOM
        margin = dip(16)
    }
}

fun ViewManager.personDetailRow(
    @DrawableRes iconRes: Int,
    value: String
) = linearLayout {
    gravity = Gravity.CENTER_VERTICAL
    imageView(iconRes) {
        tintColorResource = R.color.slate
    }
    space().lparams(width = dip(12))
    textView(value) {
        textSize = 16f
        textColorResource = R.color.slate
    }
}

fun ViewManager.personDetailView(
    name: String,
    score: Int,
    date: Date
) = verticalLayout {
    backgroundColorResource = R.color.background
    personDetailHeader(name).lparams(width = matchParent, height = dip(144))
    cardView {
        backgroundColorResource = R.color.white
        elevation = 2f
        verticalLayout {
            padding = dip(16)
            personDetailRow(R.drawable.ic_star_black_24dp, "$score%")
            space().lparams(height = dip(12))
            personDetailRow(R.drawable.ic_event_black_24dp, date.toString("M/dd/yyyy h:mm:ss"))
        }
    }.lparams(width = matchParent) {
        verticalMargin = dip(16)
        horizontalMargin = dip(8)
    }
}

class PersonDetailActivity : AppCompatActivity() {

    companion object {
        const val KEY_PERSON = "KEY_PERSON"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val person = intent.extras?.getParcelable(KEY_PERSON)
            ?: Person("Bob Costas", 48, Date(), Gender.Male)
        frameLayout {
            personDetailView(person.name, person.score, person.date)
            toolbar {
                withBackArrow(R.color.white, ::finish)
            }
        }
    }

}
