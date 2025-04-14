package com.saras.pupilmesh.navigation

enum class Screen(val route: String) {
    HOME("home"),
    FACEDETECTION("facedetection"),
    DETAIL("detail/{mangaId}"),
    LOGIN("login");

    fun withArgs(mangaId: String): String {
        return "detail/$mangaId"
    }
}