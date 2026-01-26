package com.example.henshinphone.ui

import com.example.henshinphone.feature.BeltType

sealed class Screen {

    /** 腰带选择界面 */
    object Selector : Screen()

    /** 某一个腰带设备界面 */
    data class Device(val belt: BeltType) : Screen()

    /** 变身完成展示 */
    object Finished : Screen()

    /** 设置页 */
    object Settings : Screen()

    /** 用户密码列表（当前阶段：固定 FAIZ） */
    object UserCodes : Screen()

    /** 添加用户密码 */
    data class AddUserCode(val belt: BeltType) : Screen()
}
