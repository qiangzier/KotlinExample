package com.hzq.taskproject.model

import android.content.Context
import android.support.annotation.IntDef
import java.text.SimpleDateFormat

/**
 * @author: hezhiqiang
 * @date: 2017/5/27
 * @version:
 * @description:
 */
data class RemindModel(
        var id: Long = 0,
        var reminds: ArrayList<RemindTimeModel> = arrayListOf(),
        var remindPrincipal: Boolean = true,
        var remindCreator: Boolean = false,
        var remindScope: Boolean = false)


object RType{
    @IntDef(DAY,HOUR,MINUTE,ABS_TIME)
    @Retention(AnnotationRetention.SOURCE)
    annotation class RemindType
    const val DAY: Long = 4
    const val HOUR: Long = 5
    const val MINUTE: Long = 6
    const val ABS_TIME: Long = 7
}

data class RemindTimeModel(
        var id: Long = 0L,
        var type: Int = 0,
        var ahead: Int = 0,
        var remindTime: Long = 0L)


fun getTextByType(context: Context, time: RemindTimeModel): String{
    var typeStr = ""
    when (time.type.toLong()){
        RType.DAY -> typeStr = "提前${ time.ahead }天提醒"
        RType.HOUR -> typeStr = "提前${ time.ahead }小时提醒"
        RType.MINUTE -> typeStr = "提前${ time.ahead }分钟提醒"
        RType.ABS_TIME -> typeStr = "${ getDateMMDDHM(time.remindTime) } 提醒"
    }
    return typeStr
}

fun getDateMMDDHM(time: Long): String{
    var format = SimpleDateFormat("M月D日 HH:mm")
    return format.format(time)
}

fun getTypeByStr(str: String): Int{
    if("天".equals(str)){
        return RType.DAY as Int
    }else if("小时".equals(str)){
        return RType.HOUR as Int
    }else if("分钟".equals(str)){
        return RType.MINUTE as Int
    }
    return -1
}




