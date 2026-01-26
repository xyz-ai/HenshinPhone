package com.example.henshinphone.ui

import com.example.henshinphone.feature.BeltType

sealed class Screen {
    object Selector : Screen()
    data class Device(val belt: BeltType) : Screen()
    object Finished : Screen()
    object Settings : Screen()
    object UserCodes : Screen()
    object AddUserCode : Screen()
}
