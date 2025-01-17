package com.jddev.simplemusic.ui.navigation

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
        containerColor = Color.White,
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
                            modifier = Modifier.size(28.dp),
                            imageVector = it.imageVector,
                            contentDescription = stringResource(it.label),
                        )
                    },
//                    label = {
//                        Text(
//                            text = stringResource(it.label),
//                            style = MaterialTheme.typography.bodySmall
//                        )
//                    },
                    alwaysShowLabel = false,
                )
            }
        },
    ) {
        content()
    }
}