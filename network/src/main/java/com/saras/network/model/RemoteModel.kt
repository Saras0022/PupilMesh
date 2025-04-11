package com.saras.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Result(
    val code: Int,
    val data: List<MangaData>? = null,
)

@Serializable
data class MangaData(
    val id: String,
    val title: String,
    @SerialName("sub_title") val subTitle: String,
    val status: String,
    @SerialName("thumb") val thumbnail: String,
    val summary: String,
    val genres: List<String>,
    val nsfw: Boolean,
    val type: String,
    @SerialName("total_chapter") val totalChapter: Int,
)