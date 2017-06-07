package com.hzq.taskproject

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.hzq.taskproject.coroutines.coroutine
import com.hzq.taskproject.coroutines.firstCoroutine
import com.hzq.taskproject.data.queryAllTask
import com.hzq.taskproject.model.TaskModel
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity

class MainActivity : BaseActivity() {

    var adapter: TaskListAdapter? = null
    var data: List<TaskModel>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolBar()
        setTitle(R.string.task_title)

        recyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?
        adapter = TaskListAdapter(this)
        recyclerView.adapter = adapter

        //测试协程
        firstCoroutine()
    }

    override fun onStart() {
        super.onStart()
        refreshUi()
    }

    fun refreshUi(){
        coroutine({
            data = queryAllTask(applicationContext)
        }){
            if(data != null) {
                adapter?.setData(data as List<TaskModel>)
                adapter?.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.task_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null && item.itemId == R.id.action_add)
            startCreatePage()
        return super.onOptionsItemSelected(item)
    }

    fun startCreatePage(){
        startActivity<TaskCreateActivity>()
    }
}
