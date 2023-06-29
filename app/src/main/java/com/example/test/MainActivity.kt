package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.test.ui.theme.TestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppScreen()
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MyAppScreen() {
        val navController = rememberNavController() // NavControllerを定義

        TestTheme {
            // 画面
            Scaffold(
                bottomBar = {
                    BottomNavigation(navController)
                }
            ) { padding ->
                NavHost(
                    navController = navController,
                    startDestination = Route.FIRST.name, // 初期表示する画面
                    modifier = Modifier.padding(padding)
                ) {
                    // 画面1
                    composable(route = Route.FIRST.name) {
                        FirstScreen()
                    }

                    // 画面2
                    composable(route = Route.SECOND.name) {
                        SecondScreen()
                    }

                    // 画面3
                    composable(
                        route = "${Route.THIRD.name}/{isFirstOpen}", // 渡したい値がある場合は、routeに引数プレースホルダーを追加する
                        arguments = listOf(
                            navArgument("isFirstOpen") {
                                // 渡したい値の設定
                                type = NavType.BoolType
                                nullable = false
                                defaultValue = true
                            }
                        )
                    ) { entry ->
                        // NavBackStackEntryから値を取得して、次の画面に渡す。
                        val isFirstOpen = entry.arguments?.getBoolean("isFirstOpen")
                        ThirdScreen(isFirstOpen ?: true)
                    }
                }
            }
        }
    }
    @Composable
    private fun BottomNavigation(navController:NavHostController) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        Route.values().forEach { screen ->
            val selected =
                currentDestination?.hierarchy?.any { it.route == screen.name } == true

            BottomNavigationItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                label = { Text(screen.name) },
                selected = selected,
                onClick = {
                    if (screen == Route.THIRD) {
                        navController.navigate("${screen.name}/$isThirdScreenFirstOpen")
                        isThirdScreenFirstOpen = false
                    } else {
                        navController.navigate(screen.name)
                    }
                }
            )
        }
    }

    //画面1
    private @Composable
    fun FirstScreen() {
        Text(
            text = Route.FIRST.name,
            modifier = Modifier.fillMaxSize()
        )
    }
    //画面2
    private @Composable
    fun SecondScreen() {
        Text(text = Route.SECOND.name)
    }
    //画面3
    private @Composable
    fun ThirdScreen(isFirstOpen: Boolean) {
        Text(
            text = if (isFirstOpen) {
                "Hello World!!"
            } else {
                Route.THIRD.name
            }
        )
    }
    private enum class Route {
        FIRST,
        SECOND,
        THIRD;
    }
}