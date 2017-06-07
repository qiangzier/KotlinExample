package com.hzq.taskproject.model

/**
 * @author: hezhiqiang
 * @date: 2017/5/25
 * @version:
 * @description:
 */
data class TaskModel(var id: Int = 0,var name: String = "",var priority: Int = 0,var desc: String = "")

fun getPriorityIdByName(p: String): Int{
    if("非常紧急".equals(p))
        return 2
    else if ("紧急".equals(p))
        return 1
    else
        return 0
}

fun getNameByPriority(p: Int): String{
    if(p == 2)
        return "非常紧急"
    else if (p == 1)
        return "紧急"
    else
        return "普通"
}