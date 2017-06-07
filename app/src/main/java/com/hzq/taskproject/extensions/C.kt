package com.hzq.taskproject.extensions

/**
 * @author: hezhiqiang
 * @date: 2017/5/26
 * @version:
 * @description:
 */
open class C

class D : C()

fun C.foo() = "c"
fun D.foo() = "d"

fun printFoo(c: C){
    println(c.foo())
}