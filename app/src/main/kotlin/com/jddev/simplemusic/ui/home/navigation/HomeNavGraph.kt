package com.jddev.simplemusic.ui.home.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jddev.simplemusic.ui.MainActivity
import com.jddev.simplemusic.ui.components.FullScreenBottomSheet
import com.jddev.simplemusic.ui.components.HomeMenu
import com.jddev.simplemusic.ui.components.SmBottomSheet
import com.jddev.simplemusic.ui.home.HomeRoute
import com.jddev.simplemusic.ui.home.HomeViewModel
import com.jddev.simplemusic.ui.home.album.AlbumRoute
import com.jddev.simplemusic.ui.home.album.AlbumViewModel
import com.jddev.simplemusic.ui.home.artist.ArtistRoute
import com.jddev.simplemusic.ui.home.artist.ArtistViewModel
import com.jddev.simplemusic.ui.player.FullPlayerRoute
import com.jddev.simplemusic.ui.player.MiniPlayerBar

@Composable
fun HomeNavGraph(
    startDestination: String = HomeNavigation.Home.route,
    navigateToSettings: () -> Unit
) {
    val nestedHomeNavController = rememberNavController()
    val nestedHomeNavigationActions =
        remember(nestedHomeNavController) { HomeNavigationActions(nestedHomeNavController) }

    val homeViewModel: HomeViewModel = viewModel(LocalContext.current as MainActivity)

    val playerState = homeViewModel.playerState.collectAsState()
    val currentTrack = homeViewModel.currentTrack.collectAsState()
    val showTrackFullScreen = homeViewModel.showFullTrackScreen.collectAsState()
    var showBottomSheetMenu by remember { mutableStateOf(false) }
    Box {
        NavHost(
            navController = nestedHomeNavController,
            startDestination = startDestination,
        ) {
            composableSlideTransition2(
                route = HomeNavigation.Home.route,
            ) {
                HomeRoute(
                    bottomPadding = currentTrack.value?.let { 64.dp } ?: 0.dp,
                    homeViewModel = homeViewModel,
                    navigateToSettings = navigateToSettings,
                    onArtistGroupSelected = {
                        nestedHomeNavigationActions.navigateToArtist(it)
                    },
                    onAlbumGroupSelected = {
                        nestedHomeNavigationActions.navigateToAlbum(it)
                    },
                )
            }

            composableSlideTransition2(
                route = HomeNavigation.Artist.route,
                arguments = HomeNavigation.Artist.arguments
            ) { navBackStackEntry ->
                val artist = HomeNavigation.Artist.getArtist(navBackStackEntry)
                val viewModel: ArtistViewModel = viewModel(LocalContext.current as MainActivity)
                ArtistRoute(
                    artistViewModel = viewModel,
                    artistId = artist,
                    onBack = { nestedHomeNavController.popBackStack() },
                )
            }

            composableSlideTransition2(
                route = HomeNavigation.Album.route,
                arguments = HomeNavigation.Album.arguments
            ) { navBackStackEntry ->
                val albumId = HomeNavigation.Album.getAlbumId(navBackStackEntry)
                val viewModel: AlbumViewModel = viewModel(LocalContext.current as MainActivity)
                AlbumRoute(
                    albumViewModel = viewModel,
                    albumId = albumId,
                    onBack = { nestedHomeNavController.popBackStack() },
                )
            }
        }
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = currentTrack.value != null) {
            currentTrack.value?.let { track ->
                MiniPlayerBar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    track = track,
                    getAlbumArt = homeViewModel::getAlbumArt,
                    onTrackEvent = { event -> homeViewModel.onTrackEvent(event) },
                    playerState = playerState.value,
                    onBarClick = {
                        homeViewModel.onShowTrackFullScreen()
                    }
                )
            }
        }
    }

    if (showTrackFullScreen.value) {
        FullScreenBottomSheet(
            onDismissRequest = { homeViewModel.onDismissTrackFullScreen() },
            content = {
                FullPlayerRoute(
                    onBack = { homeViewModel.onDismissTrackFullScreen() },
                    navigateToSettings = navigateToSettings,
                )
            }
        )
    }
    if (showBottomSheetMenu) {
        SmBottomSheet(
            onDismissRequest = { showBottomSheetMenu = false },
        ) {
            HomeMenu(
                modifier = Modifier.fillMaxWidth(),
                track = currentTrack.value,
                getAlbumArt = homeViewModel::getAlbumArt,
                navigateToSettings = {
                    showBottomSheetMenu = false
                    navigateToSettings()
                },
                onShowBottomSheetTrackInfo = {},
            )
        }
    }
}

private fun NavGraphBuilder.composableSlideTransition2(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit,
) {
    val tweenDuration = 400
    composable(
        route = route,
        arguments = arguments,
        content = content,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(tweenDuration),
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(tweenDuration),
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(tweenDuration),
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(tweenDuration),
            )
        },
    )
}