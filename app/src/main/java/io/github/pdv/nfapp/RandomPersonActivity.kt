package io.github.pdv.nfapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import org.jetbrains.anko.button
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.textResource
import java.util.Date
import kotlin.random.Random

val names = listOf("John", "Paul", "George", "Ringo")

fun randomPerson() = Person(
    name = names.random(),
    score = Random.nextInt(100),
    date = Date(Random.nextLong(Date().time))
)

class RandomPersonActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        relativeLayout {
            button {
                textResource = R.string.random_person
                setOnClickListener {
                    startActivity(intentFor<PersonDetailActivity>(PersonDetailActivity.KEY_PERSON to randomPerson()))
                }
            }.lparams { gravity = Gravity.CENTER }
        }
    }

}