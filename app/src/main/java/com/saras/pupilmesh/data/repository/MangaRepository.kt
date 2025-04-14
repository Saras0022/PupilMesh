package com.saras.pupilmesh.data.repository

import com.saras.network.KtorClient
import com.saras.pupilmesh.data.local.dao.NetworkDataDao
import com.saras.pupilmesh.data.local.entity.NetworkDataEntity
import javax.inject.Inject


class MangaRepository @Inject constructor(
    private val client: KtorClient,
    private val networkDataDao: NetworkDataDao
) {
    suspend fun fetchMangaDataAndCache(
        page: Int = 1,
        genres: List<String> = listOf(),
        type: String = "all",
        nsfw: Boolean = false
    ) {
        val result = client.fetchMangaData(page, genres, type, nsfw).data!!
        println(result.toString())

    }

    suspend fun getMangaDataFromLocal() = networkDataDao.getAllData()

    suspend fun getMangaDataById(id: String) = networkDataDao.getById(id)

    suspend fun fetchData(connected: Boolean, page: Int = 1): List<NetworkDataEntity> {
        return if (connected) {
            val result = client.fetchMangaData(page).data!!
            result.forEach { mangaData ->
                val networkDataEntity = NetworkDataEntity(
                    id = mangaData.id,
                    imageUrl = mangaData.thumbnail,
                    title = mangaData.title,
                    subTitle = mangaData.subTitle,
                    status = mangaData.status,
                    summary = mangaData.summary
                )
                networkDataDao.insert(networkDataEntity)
            }
            getMangaDataFromLocal()
        } else {
            getMangaDataFromLocal()
        }
    }
}