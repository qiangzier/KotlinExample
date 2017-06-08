package com.hzq.taskproject.coroutines

import android.util.Log
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.android.UI
import kotlin.system.measureTimeMillis

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

/**
 * runBlocking{...}启动一个顶级协程
 */
fun secondCoroutines() = runBlocking {
    launch(CommonPool){
        delay(1000L)
        Log.d(Tag.TAG,"Word!")
    }
    Log.d(Tag.TAG,"Hello,")
    delay(2000L)
}

fun coroutines3() = runBlocking {
    var job = launch(CommonPool){ doWork() }
    Log.d(Tag.TAG,"Hello,")
    job.join()
}

/**
 * 自定义协程函数，必须使用suspend修饰，才能在coroutines中使用
 */
suspend fun doWork(){
    delay(1000L)
    Log.d(Tag.TAG,"Word!")
}

fun coroutines4() = runBlocking {
    launch(CommonPool){
        repeat(100){ i ->
            Log.d(Tag.TAG,"I am Sleeping $i...")
            delay(500L)
        }
    }
    delay(1300L)
}

fun coroutines5() = runBlocking {
    var job = launch(CommonPool){
        repeat(100){ i ->
            Log.d(Tag.TAG,"I am Sleeping $i...")
            delay(500L)
        }
    }
    delay(1300L)
    Log.d(Tag.TAG,"I tired...")
    job.cancel() //延迟，确保被取消
    delay(1300L)
    Log.d(Tag.TAG,"我可以退出了")
}

/**
 * 协程与系统循环混合使用时调用cancel只能取消协程，而不能终止系统代码。
 */
fun coroutines6() = runBlocking {
    val job = launch(CommonPool) {
        var nextPrintTime = 0L
        var i = 0
        while (i < 10) { // computation loop
            val currentTime = System.currentTimeMillis()
            if (currentTime >= nextPrintTime) {
                Log.d(Tag.TAG,"I'm sleeping ${i++} ...")
                nextPrintTime = currentTime + 500L
            }
        }
    }
    delay(1300L) // delay a bit
    Log.d(Tag.TAG,"main: I'm tired of waiting!")
    job.cancel() // cancels the job
    delay(1300L) // delay a bit to see if it was cancelled....
    Log.d(Tag.TAG,"main: Now I can quit.")
}

/**
 * 同时取消系统代码合协程代码
 */
fun coroutines7() = runBlocking {
    val job = launch(CommonPool) {
        var nextPrintTime = 0L
        var i = 0
        while (isActive && i < 10) { // computation loop
            val currentTime = System.currentTimeMillis()
            if (currentTime >= nextPrintTime) {
                Log.d(Tag.TAG,"I'm sleeping ${i++} ...")
                nextPrintTime = currentTime + 500L
            }
        }
    }
    delay(1300L) // delay a bit
    Log.d(Tag.TAG,"main: I'm tired of waiting!")
    job.cancel() // cancels the job
    delay(1300L) // delay a bit to see if it was cancelled....
    Log.d(Tag.TAG,"main: Now I can quit.")
}

/**
 * close resource with finally(try{...} finally{...})
 */
fun coroutines8() = runBlocking {
    var job = launch(CommonPool){
        try {
            repeat(100){ i ->
                Log.d(Tag.TAG,"I am Sleeping $i...")
                delay(500L)
            }
        } finally {
            Log.d(Tag.TAG,"I am run finally...")
        }
    }
    delay(1300L)
    Log.d(Tag.TAG,"I tired...")
    job.cancel() //延迟，确保被取消
    delay(1300L)
    Log.d(Tag.TAG,"我可以退出了")
}

/**
 * 在finally中执行协程函数
 */
fun coroutines9() = runBlocking {
    var job = launch(CommonPool){
        try {
            repeat(100){ i ->
                Log.d(Tag.TAG,"I am Sleeping $i...")
                delay(500L)
            }
        } finally {
            run(NonCancellable) {
                Log.d(Tag.TAG,"I'm running finally")
                delay(1000L)
                Log.d(Tag.TAG,"And I've just delayed for 1 sec because I'm non-cancellable")
            }
        }
    }
    delay(2300L)
    Log.d(Tag.TAG,"I tired...")
    job.cancel() //延迟，确保被取消
    delay(1300L)
    Log.d(Tag.TAG,"我可以退出了")
}

/**
 * 超时
 */
fun coroutines10() = runBlocking {
    try {
        withTimeout(1300L) {
            repeat(1000) { i ->
                Log.d(Tag.TAG,"I'm sleeping $i ...")
                delay(500L)
            }
        }
    } catch(e: Exception) {
    }
}

suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // pretend we are doing something useful here
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1000L) // pretend we are doing something useful here, too
    return 29
}

/**
 * 同步执行时间
 */
fun coroutines11() = runBlocking {
    val time = measureTimeMillis {
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()
        Log.d(Tag.TAG,"The answer is ${one + two}")
    }
    Log.d(Tag.TAG,"Completed in $time ms")
}

/**
 * 测量异步执行时间
 */
fun coroutines12() = runBlocking {
    val time = measureTimeMillis {
        val one = async(CommonPool) { doSomethingUsefulOne() }
        val two = async(CommonPool) { doSomethingUsefulTwo() }
        Log.d(Tag.TAG,"The answer is ${one.await() + two.await()}")
    }
    Log.d(Tag.TAG,"Completed in $time ms")
}

/**
 * 延迟执行
 */
fun coroutines13() = runBlocking {
    val time = measureTimeMillis {
        val one = async(CommonPool,CoroutineStart.LAZY) { doSomethingUsefulOne() }
        val two = async(CommonPool,CoroutineStart.LAZY) { doSomethingUsefulTwo() }
        Log.d(Tag.TAG,"The answer is ${one.await() + two.await()}")
    }
    Log.d(Tag.TAG,"Completed in $time ms")
}
// The result type of asyncSomethingUsefulOne is Deferred<Int>
fun asyncSomethingUsefulOne() = async(CommonPool) {
    doSomethingUsefulOne()
}

// The result type of asyncSomethingUsefulTwo is Deferred<Int>
fun asyncSomethingUsefulTwo() = async(CommonPool)  {
    doSomethingUsefulTwo()
}

fun coroutines14() = runBlocking {
    val time = measureTimeMillis {
        val one = asyncSomethingUsefulOne()
        val two = asyncSomethingUsefulTwo()
        // but waiting for a result must involve either suspending or blocking.
        // here we use `runBlocking { ... }` to block the main thread while waiting for the result
        runBlocking {
            Log.d(Tag.TAG, "The answer is ${one.await() + two.await()}")
        }
    }
    Log.d(Tag.TAG,"Completed in $time ms")
}


/**协程上下文*/
fun coroutines15() = runBlocking {
    val jobs = arrayListOf<Job>()
    jobs += launch(Unconfined) { // not confined -- will work with main thread
        Log.d(Tag.TAG," 'Unconfined': I'm working in thread ${Thread.currentThread().name}")
    }
    jobs += launch(context) { // context of the parent, runBlocking coroutine
        Log.d(Tag.TAG,"    'context': I'm working in thread ${Thread.currentThread().name}")
    }
    jobs += launch(CommonPool) { // will get dispatched to ForkJoinPool.commonPool (or equivalent)
        Log.d(Tag.TAG," 'CommonPool': I'm working in thread ${Thread.currentThread().name}")
    }
    jobs += launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
        Log.d(Tag.TAG,"     'newSTC': I'm working in thread ${Thread.currentThread().name}")
    }
    jobs.forEach { it.join() }
}

fun coroutines16() = runBlocking {
    val jobs = arrayListOf<Job>()
    jobs += launch(Unconfined) { // not confined -- will work with main thread
        Log.d(Tag.TAG," 'Unconfined': I'm working in thread ${Thread.currentThread().name}")
        delay(500)
        Log.d(Tag.TAG," 'Unconfined': After delay in thread ${Thread.currentThread().name}")
    }
    jobs += launch(context) { // context of the parent, runBlocking coroutine
        Log.d(Tag.TAG,"    'context': I'm working in thread ${Thread.currentThread().name}")
        delay(1000)
        Log.d(Tag.TAG,"    'context': After delay in thread ${Thread.currentThread().name}")
    }
    jobs.forEach { it.join() }
}

fun log(msg: String) = Log.d(Tag.TAG,"[${Thread.currentThread().name}] $msg")

fun coroutines17() = runBlocking {
    val a = async(context) {
        log("I'm computing a piece of the answer")
        6
    }
    val b = async(context) {
        log("I'm computing another piece of the answer")
        7
    }
    log("The answer is ${a.await() * b.await()}")
}

/**
 * 不同线程之间执行任务
 */
fun coroutines18() = runBlocking {
    val ctx1 = newSingleThreadContext("Ctx1")
    val ctx2 = newSingleThreadContext("Ctx2")
    runBlocking(ctx1) {
        log("Started in ctx1")
        run(ctx2) {
            log("Working in ctx2")
        }
        log("Back to ctx1")
    }
}

fun coroutines19() = runBlocking {
    // start a coroutine to process some kind of incoming request
    val request = launch(CommonPool) {
        // it spawns two other jobs, one with its separate context
        val job1 = launch(CommonPool) {
            log("job1: I have my own context and execute independently!")
            delay(1000)
            log("job1: I am not affected by cancellation of the request")
        }
        // and the other inherits the parent context
        val job2 = launch(context) {
            log("job2: I am a child of the request coroutine")
            delay(1000)
            log("job2: I will not execute this line if my parent request is cancelled")
        }
        // request completes when both its sub-jobs complete:
        job1.join()
        job2.join()
    }
    delay(500)
    request.cancel() // cancel processing of the request
    delay(1000) // delay a second to see what happens
    log("main: Who has survived request cancellation?")
}

