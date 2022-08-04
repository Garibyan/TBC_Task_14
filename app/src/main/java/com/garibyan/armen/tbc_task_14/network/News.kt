package com.garibyan.armen.tbc_task_14.network

import com.squareup.moshi.Json

data class News(
    val id: String,
    val descriptionKA: String,
    val titleKA: String,
    val cover: String,
    @Json(name = "publish_date")
    val publishDate: String
)

data class NewsList(
    val content: List<News>
)
