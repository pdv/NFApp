package io.github.pdv.nfapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.verticalLayout
import java.util.*

fun rowify(persons: List<Person>) = persons
    .sortedBy { it.date }
    .groupBy { it.gender }
    .mapKeys { (gender, _) ->
        when (gender) {
            Gender.Male -> "Male"
            Gender.Female -> "Female"
            is Gender.Other -> gender.name
        }.let { SectionedAdapter.Row.Header<Person>(it) }
    }
    .mapValues { (genderRow, persons) ->
        listOf(genderRow) + persons.map { SectionedAdapter.Row.Item(it) }
    }
    .values
    .flatten()

class PersonListActivity : AppCompatActivity() {

    private val persons = listOf(
        Person("Ryan", 63, Date(1546341851000), Gender.Male),
        Person("Melissa", 91, Date(1540341851000), Gender.Female),
        Person("Jess", 93, Date(1540341751000), Gender.Female),
        Person("Sam", 86, Date(1536442851000), Gender.Male),
        Person("Carly", 89, Date(1540341651000), Gender.Female),
        Person("Joey", 78, Date(1546442992000), Gender.Male)
    )

    private val personAdapter = SectionedAdapter(::HeaderCell, ::PersonCell) { person ->
        startActivity(intentFor<PersonDetailActivity>(PersonDetailActivity.KEY_PERSON to person))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            backgroundColorResource = R.color.background
            recyclerView {
                layoutManager = LinearLayoutManager(context)
                adapter = personAdapter
            }.lparams(width = matchParent, height = matchParent)
        }
    }

    override fun onResume() {
        super.onResume()
        personAdapter.rows = rowify(persons)
    }

}