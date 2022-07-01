package com.mac.swaps.presentation.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun ToolBar(
    title: String,
    canNavigateBack: Boolean,
    isChecked: Boolean,
    filterCallback: (value: Boolean) -> Unit,
    navController: NavHostController
) {
    if (canNavigateBack) {
        TopAppBar(
            title = { Text(text = title) },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigateUp()
                }) {
                    Icon(Icons.Rounded.ArrowBack, "")
                }
            },
            backgroundColor = MaterialTheme.colors.secondary
        )
    } else {
        TopAppBar(
            title = { Text(text = title) },
            backgroundColor = MaterialTheme.colors.secondary,
            actions = {
                FilterButton(isChecked = isChecked, onClick =  filterCallback)
            }
        )
    }
}