package com.example.mealjsonexample

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil3.compose.AsyncImage

@Composable
fun Navigation(
    modifier: Modifier,
    navigationController: NavHostController,
){
    val viewModel: MealsViewModel = viewModel()
    NavHost(
        modifier = modifier,
        navController = navigationController,
        startDestination = Graph.mainScreen.route
    ){
        composable(route = "${Graph.secondScreen.route}"){
            SecondScreen(viewModel)
        }
        composable(route = Graph.mainScreen.route){
            MainScreen(viewModel, navigationController)
        }
    }
}

@Composable
fun SecondScreen(viewModel: MealsViewModel) {
    val categoryName = viewModel.chosenCategoryName.collectAsState()
    val dishesState = viewModel.mealsState.collectAsState()
    viewModel.getAllDishesByCategoryName(categoryName.value)
    Column{
        if (dishesState.value.isLoading){
            LoadingScreen()
        }
        if (dishesState.value.isError){
            ErrorScreen(dishesState.value.error!!)
        }
        if (dishesState.value.result.isNotEmpty()){
            DishesScreen(dishesState.value.result)
        }
    }
}

@Composable
fun DishesScreen(result: List<Meal>) {
    LazyColumn{
        items(result){
            DishItem(it)
        }
    }
}

@Composable
fun DishItem(meal: Meal) {
    Box(
        modifier = Modifier.background(color = Color.DarkGray)
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                modifier = Modifier.height(150.dp),
                model = meal.strMealThumb,
                contentDescription = null
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = meal.mealName
            )
        }
    }
}

@Composable
fun MainScreen(viewModel: MealsViewModel, navigationController: NavHostController){


    val categoriesState = viewModel.categoriesState.collectAsState()

    if (categoriesState.value.isLoading){
        LoadingScreen()
    }
    if (categoriesState.value.isError){
        ErrorScreen(categoriesState.value.error!!)
    }
    if (categoriesState.value.result.isNotEmpty()){
        CategoriesScreen(viewModel, categoriesState.value.result, navigationController)
    }

}

@Composable
fun CategoriesScreen(viewModel: MealsViewModel, result: List<Category>, navigationController: NavHostController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(result){
            CategoryItem(viewModel, it, navigationController)
        }
    }
}

@Composable
fun CategoryItem(viewModel: MealsViewModel, category: Category, navigationController: NavHostController) {
    Box(
        modifier = Modifier.height(200.dp).background(color = Color.DarkGray)
            .clickable {
                viewModel.setChosenCategory(category.strCategory)
                navigationController.navigate("${Graph.secondScreen.route}")
            }
    ){
       Column(
           modifier = Modifier.fillMaxSize(),
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center
       ) {
           AsyncImage(
               model = category.strCategoryThumb,
               contentDescription = null
           )
           Spacer(Modifier.height(5.dp))
           Text(
               text = category.strCategory
           )
       }
    }
}

@Composable
fun ErrorScreen(error: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = error
        )
    }
}

@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}
