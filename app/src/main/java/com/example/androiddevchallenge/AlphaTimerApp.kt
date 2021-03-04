/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.util.Locale

@ExperimentalAnimationApi
@Composable
fun AlphaTimerApp(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.primaryVariant.copy(alpha = 0.96f))
    ) {
        Card(
            modifier = modifier
                .align(Alignment.Center)
                .size(width = 300.dp, height = 500.dp)
                .padding(vertical = 32.dp, horizontal = 16.dp),
            backgroundColor = MaterialTheme.colors.primaryVariant,
            shape = RoundedCornerShape(32.dp),
            elevation = 4.dp
        ) {
            CountTimer()
        }
    }
}

private enum class TimerState {
    Collapsed,
    Expanded
}

@ExperimentalAnimationApi
@Composable
fun CountTimer(viewModel: TimerViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    BoxWithConstraints {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {

            val tick by viewModel.liveTimer.observeAsState()

            var timerTransitionState by remember { mutableStateOf(TimerState.Collapsed) }

            val timerTransition = updateTransition(timerTransitionState)

            val height by timerTransition.animateDp(
                transitionSpec = {
                    tween(
                        durationMillis = (tick?.toInt() ?: 0) * 1000,
                        delayMillis = 450,
                        easing = LinearEasing
                    )
                }
            ) { state ->
                when (state) {
                    TimerState.Expanded -> 0.dp
                    TimerState.Collapsed -> this@BoxWithConstraints.maxHeight
                }
            }

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(
                        color = MaterialTheme.colors.primary
                    )
            )

            Box(Modifier.align(Alignment.Center)) {
                Text(text = "00 : 0$tick", style = MaterialTheme.typography.h3)
            }

            AnimatedVisibility(visible = tick ?: 0 == TimerViewModel.INIT_DISPLAY_VALUE) {
                TextButton(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        timerTransitionState = if (timerTransitionState == TimerState.Collapsed) {
                            TimerState.Expanded
                        } else {
                            TimerState.Collapsed
                        }
                        viewModel.startTimer()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.start).toUpperCase(Locale.getDefault()),
                        color = contentColorFor(backgroundColor = MaterialTheme.colors.primary)
                    )
                }
            }
        }
    }
}
