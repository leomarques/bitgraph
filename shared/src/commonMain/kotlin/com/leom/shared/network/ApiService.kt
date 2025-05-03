package com.leom.shared.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

/**
 * Example API service for making HTTP requests
 */
class ApiService(private val httpClient: HttpClient) {

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com"
    }

    /**
     * Fetches sample data from the API
     * @return Flow emitting the API response
     */
    fun fetchSampleData(): Flow<Result<SampleResponse>> = flow {
        try {
            val response = httpClient.get("$BASE_URL/posts") {
                contentType(ContentType.Application.Json)
            }
            // Convert the response to our model
            val posts: List<Post> = response.body()
            val sampleItems = posts.map { post ->
                SampleItem(title = post.title, description = post.body)
            }
            emit(
                Result.success(
                    SampleResponse(
                        id = "posts-response",
                        results = sampleItems,
                        totalCount = sampleItems.size
                    )
                )
            )
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    /**
     * Posts data to the API
     * @param request data to send
     * @return Flow emitting the API response
     */
    fun postData(request: SampleRequest): Flow<Result<SampleResponse>> = flow {
        try {
            val postRequest = Post(
                userId = 1,
                id = 0, // will be assigned by the server
                title = request.query,
                body = "Sample post with count: ${request.count}"
            )

            val response = httpClient.post("$BASE_URL/posts") {
                contentType(ContentType.Application.Json)
                setBody(postRequest)
            }

            val createdPost: Post = response.body()
            val sampleItem = SampleItem(
                title = createdPost.title,
                description = createdPost.body
            )

            emit(
                Result.success(
                    SampleResponse(
                        id = createdPost.id.toString(),
                        results = listOf(sampleItem),
                        totalCount = 1
                    )
                )
            )
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}

/**
 * Sample data models for API requests/responses
 */
@Serializable
data class SampleRequest(
    val query: String,
    val count: Int = 10
)

@Serializable
data class SampleResponse(
    val id: String,
    val results: List<SampleItem> = emptyList(),
    val totalCount: Int = 0
)

@Serializable
data class SampleItem(
    val title: String,
    val description: String? = null
)

/**
 * JSONPlaceholder API model
 */
@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)