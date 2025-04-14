package com.saras.network

import com.saras.network.model.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class KtorClient {

    private val hostUrl = BuildConfig.URL
    private val apiKey = BuildConfig.API_KEY

    private val defaultDispatcher = Dispatchers.IO

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = hostUrl
                header("x-rapidapi-key", apiKey)
                header("x-rapidapi-host", hostUrl)
            }
        }
    }

    suspend fun fetchMangaData(
        page: Int = 1,
        genres: List<String> = listOf(),
        type: String = "all",
        nsfw: Boolean = false
    ): Result {
        return withContext(defaultDispatcher) {
            client.get("manga/fetch") {
                url {
                    parameters.append("page", page.toString())
                    parameters.appendAll("genres", genres)
                    parameters.append("type", type)
                    parameters.append("nsfw", nsfw.toString())
                }
            }
                .body<Result>()
        }
    }
}