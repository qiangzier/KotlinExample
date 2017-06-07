package com.hzq.taskproject.coroutines

import android.util.Log
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI

/**
 * @author: hezhiqiang
 * @date: 2017/6/2
 * @version:
 * @description:
 */
object Tag{
    public val TAG = "CoroutinesTag"
}
fun <T> coroutine(block: suspend CoroutineScope.() -> T,
                  uiBlock: suspend (T) -> Unit): Deferred<T>{
    val deferred = async(CommonPool,start = CoroutineStart.LAZY,block = block)
    launch(UI){
        uiBlock(deferred.await())
    }
    return deferred
}

/**
 * 第一个协程例子
 */
fun firstCoroutine(){
    launch(CommonPool){ //在通用线程中
        delay(1000L)    //非阻塞延迟1s
        Log.d(Tag.TAG,"Word!")
    }
    Log.d(Tag.TAG,"Hello,")
    Thread.sleep(2000)
}

fun secondCoroutines() = runBlocking {
    launch(CommonPool){
        delay(1000L)
        Log.d(Tag.TAG,"Word!")
    }
    Log.d(Tag.TAG,"Hello,")
    delay(2000L)
}