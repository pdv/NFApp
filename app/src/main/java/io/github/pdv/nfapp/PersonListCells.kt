package io.github.pdv.nfapp

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.*

class HeaderCell : Cell<String> {

    private lateinit var textView: TextView

    override fun createView(ui: AnkoContext<ViewGroup>): View = ui.relativeLayout {
        lparams(width = matchParent, height = dip(40))
        backgroundColorResource = R.color.background
        textView = textView {
        }.lparams {
            horizontalMargin = dip(16)
            gravity = Gravity.BOTTOM
        }
    }

    override fun bind(item: String) {
        textView.text = item
    }

}

class PersonCell : Cell<Person> {

    private lateinit var nameTextView: TextView
    private lateinit var subtitleTextView: TextView

    override fun createView(ui: AnkoContext<ViewGroup>): View = ui.verticalLayout {
        lparams(width = matchParent)
        backgroundColorResource = R.color.background
        cardView {
            backgroundColorResource = R.color.white
            rippleOnClick = true
            setOnClickListener { this@verticalLayout.callOnClick() }
            elevation = 2f
            verticalLayout {
                padding = dip(16)
                nameTextView = textView {
                    textSize = 18f
                }
                subtitleTextView = textView ()
            }
        }.lparams(width = matchParent) {
            verticalMargin = dip(4)
            horizontalMargin = dip(8)
        }
    }

    override fun bind(item: Person) {
        nameTextView.text = item.name
        subtitleTextView.text = "${item.score}% - ${item.date.toString("MMM dd, yyyy - h:mm:ss")}"
    }

}
