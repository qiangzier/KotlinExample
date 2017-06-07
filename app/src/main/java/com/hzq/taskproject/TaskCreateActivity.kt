package com.hzq.taskproject

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.hzq.taskproject.coroutines.coroutine
import com.hzq.taskproject.data.insertTask
import com.hzq.taskproject.data.queryTask
import com.hzq.taskproject.data.updateTask
import com.hzq.taskproject.model.TaskModel
import com.hzq.taskproject.model.getNameByPriority
import com.hzq.taskproject.model.getPriorityIdByName
import kotlinx.android.synthetic.main.activity_task_create.*
import org.jetbrains.anko.selector
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class TaskCreateActivity : BaseActivity() {

    private var id: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_create)
        initToolBar()
        setTitle(R.string.add)

        id = intent.getIntExtra("id",0)
        priorityLayout.setOnClickListener {
            val countries = resources.getStringArray(R.array.task_priority_array)
            selectDialog(countries.asList())
        }

        remindLayout.setOnClickListener {
            startActivity<TaskRemindActivity>()
        }

        if(id != 0){
            //协程异步执行
            coroutine({ queryTask(applicationContext,id)}){
                if(it != null) {
                    with(it) {
                        edtTaskName.setText(name)
                        taskPriorityContent?.text = getNameByPriority(priority!!)
                        taskDesc.setText(desc)
                    }
                }
            }
        }
    }

    fun selectDialog(items: List<CharSequence>){
        selector(null,items) {
            _,i ->
            taskPriorityContent.text = items[i]
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.task_menu,menu)
        if(menu == null) return super.onCreateOptionsMenu(menu)
        menu.findItem(R.id.action_add).icon = null
        return super.onCreateOptionsMenu(menu)
    }


    fun insert(): Unit{
        coroutine({
            var taskModel = TaskModel(id,edtTaskName.text.toString(),
                    getPriorityIdByName(taskPriorityContent.text.toString()),
                    taskDesc.text.toString())
            if(id != 0)
                updateTask(applicationContext,taskModel)
            else
                insertTask(applicationContext,taskModel)
        }){
            if(it == -1L)
                toast("保存失败")
            else{
                if (id == 0) toast("保存成功") else toast("更新成功")
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null){
            insert()
        }
        return super.onOptionsItemSelected(item)
    }
}
