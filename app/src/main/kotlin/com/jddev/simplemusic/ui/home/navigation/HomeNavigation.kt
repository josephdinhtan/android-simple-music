package com.jddev.simplemusic.ui.home.navigation

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.jddev.simplemusic.domain.model.Album
import com.jddev.simplemusic.domain.model.Artist
import java.net.URLDecoder
import java.net.URLEncoder

sealed class HomeNavigation(val route: String) {

    data object Home : HomeNavigation("home")
    data object Artist : HomeNavigation("artist/{artistId}") {
        val arguments = listOf(navArgument("artistId") { type = NavType.StringType })
        fun createRoute(artistId: Long): String {
            val encodeArtistInfo = URLEncoder.encode(artistId.toString(), "UTF-8")
            return "artist/$encodeArtistInfo"
        }
        fun getArtist(navBackStackEntry: NavBackStackEntry): Long {
            val encodedArtist = navBackStackEntry.arguments?.getString("artistId")
            return URLDecoder.decode(encodedArtist, "UTF-8").toLong()
        }
    }

    data object Album : HomeNavigation("album/{albumId}") {
        val arguments = listOf(navArgument("albumId") { type = NavType.StringType })
        fun createRoute(albumId: Long): String {
            val encodeAlbumInfo = URLEncoder.encode(albumId.toString(), "UTF-8")
            return "album/$encodeAlbumInfo"
        }
        fun getAlbumId(navBackStackEntry: NavBackStackEntry): Long {
            val encodedAlbum = navBackStackEntry.arguments?.getString("albumId")
            return URLDecoder.decode(encodedAlbum, "UTF-8").toLong()
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

    val navigateToArtist: (artist: Artist) -> Unit = {
        val artistInfo = HomeNavigation.Artist.createRoute(it.id)
        navController.navigate(artistInfo) {
            launchSingleTop = true
            restoreState = true
        }
    }

    val navigateToAlbum: (album: Album) -> Unit = {
        val albumInfo = HomeNavigation.Album.createRoute(it.id)
        navController.navigate(albumInfo) {
            launchSingleTop = true
            restoreState = true
        }
    }
}