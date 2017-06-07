package com.hzq.taskproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.app_bar.*

/**
 * @author: hezhiqiang
 * @date: 2017/5/24
 * @version:
 * @description:
 */
open class BaseActivity : AppCompatActivity(){
    open protected fun initToolBar(){
        if(toolBar == null) throw NullPointerException("任务标题栏不存在,请添加对象的xml布局中引用app_bar.xml")
        setSupportActionBar(toolBar)
        toolBar.setNavigationOnClickListener {
            onBackPressed()
        }
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun startActivityForResult(intent: Intent?, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
        super.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out)
    }

    override fun finish() {
        super.finish()
        super.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out)
    }

    override fun finishActivity(requestCode: Int) {
        super.finishActivity(requestCode)
        super.overridePendingTransition(R.anim.push_right_in,R.anim.push_right_out)
    }
}