package io.github.pdv.nfapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

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

    private val disposable = CompositeDisposable()

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
        persons()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { persons -> personAdapter.rows = rowify(persons) },
                { error -> toast(error.localizedMessage) }
            )
            .addTo(disposable)
    }

    override fun onPause() {
        disposable.clear()
        super.onPause()
    }

}