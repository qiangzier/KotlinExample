package com.hzq.taskproject.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

/**
 * @author: hezhiqiang
 * @date: 2017/5/25
 * @version:
 * @description:
 */
class TaskDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx,"task_database",null,1){

    val tableName: String = "task_table"
    /**
     * 伴生对象:类内部的对象,声明时需要用关键字 "companion"标记,
     * * 伴生对象可以只是用类名来调用
     * * TaskDdatabasOpenHelper.getInstance(...)
     * * 伴生对象的名称可以省略
     *       companion object{
     *            //.....
     *       }
     * * 省略名称时将使用默认名称 Companion
     *      TaskDatabaseOpenHelper.Companion
     */
    companion object {
        private var instance: TaskDatabaseOpenHelper? = null
        @Synchronized
        fun getInstance(ctx: Context) : TaskDatabaseOpenHelper{
            if(instance == null)
                instance = TaskDatabaseOpenHelper(ctx.applicationContext)
            return instance!!
        }
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(tableName,true,
                "id" to INTEGER + PRIMARY_KEY + UNIQUE + AUTOINCREMENT ,
                "name" to TEXT,
                "priority" to INTEGER,
                "desc" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(tableName,true)
    }

}

val Context.database: TaskDatabaseOpenHelper
    get() = TaskDatabaseOpenHelper.getInstance(this)