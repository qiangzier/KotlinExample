package com.hzq.taskproject.data

import android.content.Context
import com.hzq.taskproject.model.TaskModel
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.db.update

/**
 * @author: hezhiqiang
 * @date: 2017/5/25
 * @version:
 * @description:
 */
fun insertTask(context: Context,task: TaskModel): Long{
    var result: Long = 0L
    context.database.use {
         result = insert(context.database.tableName,
                "name" to task.name,
                "priority" to task.priority,
                "desc" to task.desc)
    }
    return result
}

fun updateTask(context: Context,task: TaskModel): Long{
    var result: Long = 0
    context.database.use {
        result = update(context.database.tableName,
                "name" to task.name,
                "priority" to task.priority,
                "desc" to task.desc)
                .where("id = {tid}","tid" to task.id)
                .exec()
                .toLong()
    }
    return result
}

fun queryTask(context: Context,id: Int) : TaskModel? {
    var task: TaskModel? = null
    context.database.use {
        select(context.database.tableName).whereArgs("id = {id}","id" to id).exec {
            while (moveToNext()) {
                task = TaskModel()
                task!!.id = id
                task!!.name = getString(getColumnIndex("name"))
                task!!.priority = getInt(getColumnIndex("priority"))
                task!!.desc = getString(getColumnIndex("desc"))
                return@exec
            }
        }
    }
    return task
}

fun queryAllTask(context: Context): List<TaskModel>{
    var result = arrayListOf<TaskModel>()
    context.database.use {
        select(context.database.tableName).exec {
            //Cursor
            while (moveToNext()) {
                var task = TaskModel()
                task.id = getInt(getColumnIndex("id"))
                task.name = getString(getColumnIndex("name"))
                task.priority = getInt(getColumnIndex("priority"))
                task.desc = getString(getColumnIndex("desc"))
                result.add(task)
            }
        }
    }
    return result
}