package com.jddev.simplemusic.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun SmBottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val destination = navBackStackEntry?.destination
    val topLevelDestination = TopLevelDestination.fromNavDestination(destination)

    StBottomNavigation(
        modifier = modifier,
        navigationItems = {
            TopLevelDestination.entries.forEach {
                val isSelected = it == topLevelDestination
                item(
                    selected = isSelected,
                    onClick = {
                        if (!isSelected) {
                            navController.navigate(it.route) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = it.imageVector,
                            contentDescription = stringResource(it.label),
                        )
                    },
                    label = {
                        Text(text = stringResource(it.label), style = MaterialTheme.typography.labelSmall)
                    },
                    alwaysShowLabel = false,
                )
            }
        },
    ) {
        content()
    }
}