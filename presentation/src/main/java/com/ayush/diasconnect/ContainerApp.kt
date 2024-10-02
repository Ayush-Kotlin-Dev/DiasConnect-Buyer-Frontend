package com.ayush.diasconnect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.ayush.diasconnect.tabs.CartTab
import com.ayush.diasconnect.tabs.HomeTab
import com.ayush.diasconnect.tabs.ProfileTab
import com.ayush.diasconnect.tabs.MoreTab
import com.ayush.domain.model.User

 class ContainerApp: Screen {
    @Composable
    override fun Content() {
        TabNavigator(HomeTab){
            Scaffold(
                content = { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    NavigationBar {
                        TabNavigationItem(HomeTab)
                        TabNavigationItem(MoreTab)
                        TabNavigationItem(CartTab)
                        TabNavigationItem(ProfileTab)
                    }
                }
            )
        }
    }
}




@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        icon = {
            tab.options.icon?.let { painter ->
                Icon(
                    painter = painter,
                    contentDescription = tab.options.title
                )
            }
        },
        label = {
            Text(
                text = tab.options.title,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color.Red.copy(alpha = 0.7f),
            unselectedIconColor = Color.Black.copy(alpha = 0.7f),
            indicatorColor = Color.Transparent,

            ),
        alwaysShowLabel = false
    )
}