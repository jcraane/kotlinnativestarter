package nl.jamiecraane.nativestarter.api

import com.soywiz.klock.DateTime
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.readText
import io.ktor.http.Url
import io.ktor.http.isSuccess
import kotlinx.io.charsets.Charset
import kotlinx.serialization.list
import nl.jamiecraane.nativestarter.domain.Person
import nl.jamiecraane.nativestarter.domain.Task
import nl.jamiecraane.nativestarter.log.info
import nl.jamiecraane.nativestarter.log.ktor.logging.LogLevel
import nl.jamiecraane.nativestarter.log.ktor.logging.Logger
import nl.jamiecraane.nativestarter.log.ktor.logging.Logging
import nl.jamiecraane.nativestarter.log.ktor.logging.SIMPLE

//Mockoon does not have good response times at the moment, see: https://github.com/mockoon/mockoon/issues/48
class RealApi : Api {
    private val client = HttpClient {
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }
    override suspend fun retrievePersons(): ApiResponse<List<Person>> {
        info("Retrieve persons from common")
        return withinTryCatch<List<Person>> {
            val start = DateTime.nowUnixLong()
            val response = client.get<HttpResponse> {
                url(Url("http://localhost:2500/persons"))
//                url(Url("http://10.0.2.2:2500/persons"))
            }
            val end = DateTime.nowUnixLong()
            println("End persons service call = ${end - start}")

        try {
            val response = client.get<HttpResponse> {
                url(Url("http://192.168.1.48:2500/persons"))
            }

            return if (response.status.isSuccess()) {
                Success(
                    jsonParser.parse(
                        Person.serializer().list,
                        response.readText(Charset.forName("UTF-8"))
                    )
                )
            } else {
                println("is Failure")
                Failure(response.status.value, "Error")
            }
        } catch (e: Throwable) {
            println("is Exception!!!, $e")
            return if (e.message?.contains("Code=-1009") == true) { //Is Network down, code..
                Failure(-1009, "Error")
            } else {
                Failure(0, "Error") //Unknown failure
            }
        }
    }
}

suspend fun <T> withinTryCatch(block: suspend () -> ApiResponse<T>): ApiResponse<T> {
    try {
        return block()
    } catch (exception: Exception) {
        return Failure(500, exception.message)
    }
}