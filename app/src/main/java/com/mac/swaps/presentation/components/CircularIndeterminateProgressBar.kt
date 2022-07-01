package com.mac.swaps.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.constraintlayout.compose.ConstraintLayout


@ExperimentalComposeUiApi
@Composable
fun CircularIndeterminateProgressBar(show: Boolean){
    if (show){
        ConstraintLayout(
            modifier = Modifier.fillMaxSize(),
        ) {
            val (progressBar) = createRefs()
            val topBias = createGuidelineFromTop(0.3f)
            CircularProgressIndicator(
                modifier = Modifier.constrainAs(progressBar)
                {
                    top.linkTo(topBias)
                    end.linkTo(parent.end)
                    start.linkTo(parent.start)
                },
                color = MaterialTheme.colors.primary
            )
        }
    }
}