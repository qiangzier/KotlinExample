package com.hzq.taskproject.extensions

/**
 * @author: hezhiqiang
 * @date: 2017/5/26
 * @version:
 * @description:
 */

//扩展方法
fun <T> MutableList<T>.swap(index1: Int,index2: Int){
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

//扩展属性,扩展属性不能直接赋值,必须定义getter/setter方法,
// 因为扩展属性并实际上并未将属性插入到类中。
val <T> List<T>.lastIndex: Int
    get() = size - 1

/**
 * 查找
 */
inline fun <T> Iterable<T>.find(predicate: (T) -> Boolean): T?{
    for(element in this) if(predicate(element)) return element
    return null
}

inline fun <T> Iterable<T>.findLast(predicate: (T) -> Boolean): T?{
    var last: T? = null
    for (element in this){
        if(predicate(element))
            last = element
    }
    return last
}

/**
 * 过滤
 */
inline fun <T> Iterable<T>.filter(predicate: (T) -> Boolean): List<T>{
    var l = arrayListOf<T>()
    for (element in this) if(predicate(element)) l.add(element)
    return l
}

/**
 * 反转
 */
inline fun <T> MutableList<T>.reverse(): Unit{
    java.util.Collections.reverse(this)
}

