package com.example.composetrendingmovies.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.serialization.NavKeySerializer
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer

@Composable
fun rememberNavigationState(
    startRoute: NavKey,
    topLevelRoutes: Set<NavKey>
): NavigationState {
    val topLevelRouteState = rememberSerializable(
        startRoute, topLevelRoutes,
        serializer = MutableStateSerializer(NavKeySerializer())
    ) {
        mutableStateOf(startRoute)
    }

    val backStacks = remember(topLevelRoutes) { mutableMapOf<NavKey, NavBackStack<NavKey>>() }
    for (route in topLevelRoutes) {
        key("backstack", route) {
            backStacks[route] = rememberNavBackStack(route)
        }
    }

    return remember(startRoute, topLevelRoutes, backStacks) {
        NavigationState(
            startRoute = startRoute,
            topLevelRouteState = topLevelRouteState,
            backStacks = backStacks
        )
    }
}

@Stable
class NavigationState(
    val startRoute: NavKey,
    topLevelRouteState: MutableState<NavKey>,
    val backStacks: Map<NavKey, NavBackStack<NavKey>>
) {
    var topLevelRoute: NavKey by topLevelRouteState

    val stacksInUse by derivedStateOf {
        if (topLevelRoute == startRoute) {
            listOf(startRoute)
        } else {
            listOf(startRoute, topLevelRoute)
        }
    }
}

@Composable
fun NavigationState.toEntries(
    entryProvider: (NavKey) -> NavEntry<NavKey>
): List<NavEntry<NavKey>> {
    val decorator = rememberSaveableStateHolderNavEntryDecorator<NavKey>()
    val decorators = remember(decorator) { listOf(decorator) }

    val allEntries = mutableListOf<NavEntry<NavKey>>()
    for (stackKey in stacksInUse) {
        val stack = backStacks[stackKey] ?: continue
        val entries = key(stackKey) {
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = decorators,
                entryProvider = entryProvider
            )
        }
        allEntries.addAll(entries)
    }
    return allEntries
}
