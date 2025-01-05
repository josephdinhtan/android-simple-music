package com.jddev.simplemusic.ui.navigation

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import androidx.navigation.toRoute
import com.jddev.simplemusic.AppContainer
import com.jddev.simplemusic.ui.MainActivity
import com.jddev.simplemusic.ui.components.FullScreenBottomSheet
import com.jddev.simplemusic.ui.components.HomeMenu
import com.jddev.simplemusic.ui.components.SmBottomSheet
import com.jddev.simplemusic.ui.library.LibraryRoute
import com.jddev.simplemusic.ui.library.LibraryViewModel
import com.jddev.simplemusic.ui.library.album.AlbumRoute
import com.jddev.simplemusic.ui.library.album.AlbumViewModel
import com.jddev.simplemusic.ui.library.artist.ArtistRoute
import com.jddev.simplemusic.ui.library.artist.ArtistViewModel
import com.jddev.simplemusic.ui.player.FullPlayerRoute
import com.jddev.simplemusic.ui.player.MiniPlayerBar
import com.jddev.simplemusic.ui.search.SearchRoute
import com.jddev.simplemusic.ui.settings.SettingsRoute
import com.jddev.simpletouch.ui.component.StDoubleBackPressToExit

@Composable
fun HomeNavGraph(
    modifier: Modifier = Modifier,
    appContainer: AppContainer,
    navController: NavHostController = rememberNavController(),
) {
    StDoubleBackPressToExit()

    val homeViewModel: LibraryViewModel = viewModel(LocalContext.current as MainActivity)
    val playerState = homeViewModel.playerState.collectAsState()
    val currentTrack = homeViewModel.currentTrack.collectAsState()

    val showPlayerScreen = homeViewModel.showFullTrackScreen.collectAsState()
    var showMenu by remember { mutableStateOf(false) }

    val fixMiniPlayerHeight = 54.dp
    Box {
        NavHost(
            navController = navController,
            startDestination = NvRoute.LibraryHome,
            modifier = modifier,
        ) {
            composable<NvRoute.LibraryHome> {
                LibraryRoute(
                    bottomPadding = currentTrack.value?.let { fixMiniPlayerHeight } ?: 0.dp,
                    homeViewModel = homeViewModel,
                    navigateToSettings = { navController.navigate(NvRoute.Settings) },
                    onArtistGroupSelected = { navController.navigate(NvRoute.LibraryArtist(it.id)) },
                    onAlbumGroupSelected = { navController.navigate(NvRoute.LibraryAlbum(it.id)) },
                )
            }

            composable<NvRoute.Search> {
                SearchRoute()
            }

            composable<NvRoute.Settings> {
                SettingsRoute(
                    bottomPadding = currentTrack.value?.let { fixMiniPlayerHeight } ?: 0.dp,
                    onBack = { navController.popBackStack() },
                )
            }

            composable<NvRoute.LibraryAlbum>(
                deepLinks = listOf(
                    navDeepLink {
                        action = Intent.ACTION_VIEW
                        uriPattern = "simplemusic/library/album/{albumId}"
                    },
                ),
            ) { backStackEntry ->
                val route: NvRoute.LibraryAlbum = backStackEntry.toRoute()
                val viewModel: AlbumViewModel = viewModel(LocalContext.current as MainActivity)
                AlbumRoute(
                    albumViewModel = viewModel,
                    albumId = route.albumId,
                    onBack = { navController.popBackStack() },
                )
            }

            composable<NvRoute.LibraryArtist>(
                deepLinks = listOf(
                    navDeepLink {
                        action = Intent.ACTION_VIEW
                        uriPattern = "simplemusic/library/artist/{artistId}"
                    },
                ),
            ) { backStackEntry ->
                val route: NvRoute.LibraryArtist = backStackEntry.toRoute()
                val viewModel: ArtistViewModel = viewModel(LocalContext.current as MainActivity)
                ArtistRoute(
                    artistViewModel = viewModel,
                    artistId = route.artistId,
                    onBack = { navController.popBackStack() },
                )
            }
        }

        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = currentTrack.value != null
        ) {
            currentTrack.value?.let { track ->
                MiniPlayerBar(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fixHeight = fixMiniPlayerHeight,
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

    if (showPlayerScreen.value) {
        FullScreenBottomSheet(
            onDismissRequest = { homeViewModel.onDismissTrackFullScreen() },
            content = {
                FullPlayerRoute(
                    onBack = { homeViewModel.onDismissTrackFullScreen() },
                    navigateToSettings = { navController.navigate(NvRoute.Settings) },
                )
            }
        )
    }

    if (showMenu) {
        SmBottomSheet(
            onDismissRequest = { showMenu = false },
        ) {
            HomeMenu(
                modifier = Modifier.fillMaxWidth(),
                track = currentTrack.value,
                getAlbumArt = homeViewModel::getAlbumArt,
                navigateToSettings = {
                    showMenu = false
                    navController.navigate(NvRoute.Settings)
                },
                onShowBottomSheetTrackInfo = {},
            )
        }
    }
}
