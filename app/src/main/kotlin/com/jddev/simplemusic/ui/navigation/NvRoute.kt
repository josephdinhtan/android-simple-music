package com.jddev.simplemusic.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.jddev.simplemusic.R
import kotlinx.serialization.Serializable

sealed interface NvRoute {
    @Serializable
    data object Home: NvRoute

    @Serializable
    data object Search: NvRoute

    @Serializable
    data object Settings: NvRoute

    @Serializable
    data object LibraryHome: NvRoute

    @Serializable
    data class LibraryAlbum(val albumId: Long): NvRoute

    @Serializable
    data class LibraryArtist(val artistId: Long): NvRoute
}

enum class TopLevelDestination(
    val route: NvRoute,
    @StringRes val label: Int,
    val imageVector: ImageVector,
) {
    Timeline(
        route = NvRoute.LibraryHome,
        label = R.string.library,
        imageVector = Icons.Filled.LibraryMusic,
    ),
    ChatsList(
        route = NvRoute.Search,
        label = R.string.search,
        imageVector = Icons.Filled.Search,
    ),
    Settings(
        route = NvRoute.Settings,
        label = R.string.settings,
        imageVector = Icons.Filled.Settings,
    ),
    ;

    companion object {
        val START_DESTINATION = ChatsList

        fun fromNavDestination(destination: NavDestination?): TopLevelDestination {
            return entries.find { dest ->
                destination?.hierarchy?.any {
                    it.hasRoute(dest.route::class)
                } == true
            } ?: START_DESTINATION
        }

        fun NavDestination.isTopLevel(): Boolean {
            return entries.any {
                hasRoute(it.route::class)
            }
        }
    }
}