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

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    companion object {
        private const val SEC = 1000L
        private const val INIT_VALUE = 6L * SEC
        const val INIT_DISPLAY_VALUE = INIT_VALUE / SEC
    }

    private val _liveTimer = MutableLiveData(INIT_DISPLAY_VALUE)
    val liveTimer: LiveData<Long> get() = _liveTimer

    private val countDownTimer = object : CountDownTimer(INIT_VALUE, SEC) {
        override fun onTick(tick: Long) {
            _liveTimer.value = tick / SEC
        }

        override fun onFinish() {
            _liveTimer.value = INIT_DISPLAY_VALUE
        }
    }

    private fun onTimerStop() {
        countDownTimer.cancel()
    }

    fun startTimer() {
        onTimerStop()
        countDownTimer.start()
    }

    override fun onCleared() {
        countDownTimer.cancel()
        super.onCleared()
    }
}
