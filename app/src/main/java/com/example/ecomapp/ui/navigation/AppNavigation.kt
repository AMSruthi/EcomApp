package com.example.ecomapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ecomapp.ui.category.CategoryScreen
import com.example.ecomapp.ui.category.CategoryViewModel
import com.example.ecomapp.ui.detail.ProductDetailScreen
import com.example.ecomapp.ui.detail.ProductDetailViewModel
import com.example.ecomapp.ui.home.HomeScreen
import com.example.ecomapp.ui.home.HomeViewModel
import com.example.ecomapp.ui.search.SearchScreen
import com.example.ecomapp.ui.search.SearchViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Category : Screen("category/{categoryName}") {
        fun createRoute(categoryName: String) = "category/$categoryName"
    }
    object Search : Screen("search")
    object Detail : Screen("detail/{productId}") {
        fun createRoute(productId: Int) = "detail/$productId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val homeViewModel: HomeViewModel = hiltViewModel()
    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val searchViewModel: SearchViewModel = hiltViewModel()
    val detailViewModel: ProductDetailViewModel = hiltViewModel()

    val homeState by homeViewModel.homeState.collectAsState()
    val categoryState by categoryViewModel.categoryState.collectAsState()
    val searchState by searchViewModel.searchState.collectAsState()
    val detailState by detailViewModel.detailState.collectAsState()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                uiState = homeState,
                onCategoryClick = { category ->
                    categoryViewModel.selectCategory(category)
                    navController.navigate(Screen.Category.createRoute(category))
                },
                onProductClick = { productId ->
                    detailViewModel.loadProductDetail(productId)
                    navController.navigate(Screen.Detail.createRoute(productId))
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onRefresh = { homeViewModel.loadHomeData() }
            )
        }

        composable(
            route = Screen.Category.route,
            arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
        ) { backStackEntry ->
            val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""
            CategoryScreen(
                categoryName = categoryName,
                uiState = categoryState,
                onProductClick = { productId ->
                    detailViewModel.loadProductDetail(productId)
                    navController.navigate(Screen.Detail.createRoute(productId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Search.route) {
            SearchScreen(
                uiState = searchState,
                onQueryChange = { query -> searchViewModel.updateSearchQuery(query) },
                onSearchSubmit = { query -> searchViewModel.performSearch(query) },
                onProductClick = { productId ->
                    detailViewModel.loadProductDetail(productId)
                    navController.navigate(Screen.Detail.createRoute(productId))
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("productId") { type = NavType.IntType })
        ) {
            ProductDetailScreen(
                uiState = detailState,
                onSubmitEnquiry = { name, email, message ->
                    detailViewModel.submitEnquiry(name, email, message)
                },
                onResetEnquiry = { detailViewModel.resetEnquiryStatus() },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
