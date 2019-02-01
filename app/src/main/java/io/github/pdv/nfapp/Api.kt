package io.github.pdv.nfapp

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.*

data class JsonPerson(
    @SerializedName("name") val name: String,
    @SerializedName("score") val score: Int,
    @SerializedName("date_created") val date: Long
)

typealias PersonsResponse = List<Map<String, List<JsonPerson>>>

interface PersonApi {
    @GET("ryanneuroflow/370d19311602c091928300edd7a40f66/raw/1865ae6004142553d8a6c6ba79ccb511028a2cba/names.json")
    fun personsByGender(): Single<PersonsResponse>
}

private fun parse(response: PersonsResponse): List<Person> = response
    .map { genderMap ->
        genderMap.entries.map { (genderString, jsonPersons) ->
            val gender = when (genderString) {
                "males" -> Gender.Male
                "females" -> Gender.Female
                else -> Gender.Other(genderString)
            }
            jsonPersons.map { Person(
                name = it.name,
                score = it.score,
                date = Date(it.date),
                gender = gender
            )}
        }.flatten()
    }.flatten()

fun persons(): Single<List<Person>> {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://gist.githubusercontent.com/")
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val personApi = retrofit.create(PersonApi::class.java)
    return personApi.personsByGender().map(::parse)
}