package com.mac.swaps.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.CoilImage
import com.mac.swaps.Config
import com.mac.swaps.model.Movie
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun MovieCard(
    movie: Movie,
    isFavorite: Boolean,
    isFavCallback: (value: Boolean) -> Unit,
    onClick: () -> Unit,
){
    Card(
        shape = MaterialTheme.shapes.small,
        modifier = Modifier
            .padding(bottom = 6.dp, top = 6.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 8.dp
    ) {
        Column {
            Box(contentAlignment = Alignment.TopEnd) {
                CoilImage(
                    data = Config.IMAGE_URL + movie.poster_path,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(225.dp),
                    contentScale = ContentScale.Crop,
                )
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(6.dp)
                        .size(32.dp),
                    color = Color(0x77ffffff)
                ) {
                    FavoriteButton(
                        isChecked = isFavorite,
                        onClick = isFavCallback
                    )
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp, start = 8.dp, end = 8.dp)) {
                Text(text = movie.title, modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .wrapContentWidth(Alignment.Start),
                    style = MaterialTheme.typography.h3)
            }

        }
    }
}