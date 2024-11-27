package com.jddev.simplemusic.ui.home.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.jddev.simplemusic.ui.home.album.AlbumTrackGroup
import com.jddev.simplemusic.ui.home.artist.ArtistTrackGroup
import java.net.URLDecoder
import java.net.URLEncoder

sealed class HomeNavigation(val route: String) {

    data object Home : HomeNavigation("home")
    data object Artist : HomeNavigation("artist/{artistInfo}") {
        val arguments = listOf(navArgument("artistInfo") { type = NavType.StringType })
        fun createRoute(artistInfo: String): String {
            val encodeArtistInfo = URLEncoder.encode(artistInfo, "UTF-8")
            return "artist/$encodeArtistInfo"
        }
        fun getArtist(navBackStackEntry: NavBackStackEntry): String {
            val encodedArtist = navBackStackEntry.arguments?.getString("artistInfo")
            return URLDecoder.decode(encodedArtist, "UTF-8")
        }
    }

    data object Album : HomeNavigation("album/{albumInfo}") {
        val arguments = listOf(navArgument("albumInfo") { type = NavType.StringType })
        fun createRoute(albumInfo: String): String {
            val encodeAlbumInfo = URLEncoder.encode(albumInfo, "UTF-8")
            return "album/$encodeAlbumInfo"
        }
        fun getAlbum(navBackStackEntry: NavBackStackEntry): String {
            val encodedAlbum = navBackStackEntry.arguments?.getString("albumInfo")
            return URLDecoder.decode(encodedAlbum, "UTF-8")
        }
    }
}

class HomeNavigationActions(navController: NavHostController) {
    val navigateToHome: () -> Unit = {
        navController.navigate(HomeNavigation.Home.route) {
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToArtist: (artistTrackGroup: ArtistTrackGroup) -> Unit = {
        val artistInfo = HomeNavigation.Artist.createRoute(it.artist)
        navController.navigate(artistInfo) {
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToAlbum: (albumTrackGroup: AlbumTrackGroup) -> Unit = {
        val albumInfo = HomeNavigation.Album.createRoute(it.album)
        navController.navigate(albumInfo) {
            launchSingleTop = true
            restoreState = true
        }
    }
}