package com.mac.swaps.presentation.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mac.swaps.R

@Composable
fun FilterButton(
    isChecked: Boolean,
    onClick: (isChecked: Boolean) -> Unit
) {
    IconToggleButton(
        checked = isChecked,
        onCheckedChange = { onClick(!isChecked) }
    ) {
        val transition = updateTransition(isChecked, label = "Checked indicator")
        val size by transition.animateDp(
            transitionSpec = {
                if (false isTransitioningTo true) {
                    keyframes {
                        durationMillis = 250
                        30.dp at 0 with LinearOutSlowInEasing // for 0-15 ms
                        35.dp at 15 with FastOutLinearInEasing // for 15-75 ms
                        40.dp at 75 // ms
                        35.dp at 150 // ms
                    }
                } else {
                    spring(stiffness = Spring.StiffnessVeryLow)
                }
            },
            label = "Size"
        ) { 30.dp }

        Icon(
            painter = if (isChecked)
                painterResource(id = R.drawable.ic_baseline_filter_alt_24)
            else
                painterResource(id = R.drawable.ic_outline_filter_alt_24),
            contentDescription = null,
            modifier = Modifier.size(size)
        )
    }

}