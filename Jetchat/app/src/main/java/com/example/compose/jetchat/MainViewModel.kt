/*
 * Copyright 2020 The Android Open Source Project
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

package com.example.compose.jetchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * Used to communicate between screens.
 */
class MainViewModel : ViewModel() {
    sealed class Event {
        data class CheckPermission(val block: () -> Unit) : Event()
    }

    private val _drawerShouldBeOpened = MutableStateFlow(false)
    val drawerShouldBeOpened: StateFlow<Boolean> = _drawerShouldBeOpened

    private val eventChannel = Channel<Event>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()


    fun openDrawer() {
        _drawerShouldBeOpened.value = true
    }

    fun resetOpenDrawerAction() {
        _drawerShouldBeOpened.value = false
    }

    private var permissionBlock: (() -> Unit)? = null
    fun checkPermission(block: () -> Unit) {
        permissionBlock = block
        viewModelScope.launch {
            eventChannel.send(Event.CheckPermission(block))
        }
    }

    fun permissionGranted(granted: Boolean) {
        if (granted) {
            permissionBlock?.invoke()
            permissionBlock = null
        }
    }
}

