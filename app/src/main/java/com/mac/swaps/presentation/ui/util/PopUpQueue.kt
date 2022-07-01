package com.mac.swaps.presentation.ui.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mac.swaps.presentation.components.AlertPopUpInfo
import com.mac.swaps.presentation.components.PositiveAction
import java.util.*

class PopUpQueue {
    // Queue for "First-In-First-Out" behavior
    val queue: MutableState<Queue<AlertPopUpInfo>> = mutableStateOf(LinkedList())

    fun removeHeadMessage(){
        if (queue.value.isNotEmpty()) {
            val update = queue.value
            update.remove() // remove first (oldest message)
            queue.value = ArrayDeque()
            queue.value = update
        }
    }

    fun appendErrorMessage(title: String, description: String){
        queue.value.offer(
            AlertPopUpInfo.Builder()
                .title(title)
                .onDismiss(this::removeHeadMessage)
                .description(description)
                .positive(
                    PositiveAction(
                        positiveBtnTxt = "Ok",
                        onPositiveAction = this::removeHeadMessage,
                    )
                )
                .build()
        )
    }
}