package com.hzq.taskproject

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.hzq.taskproject.model.TaskModel
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity

/**
 * @author: hezhiqiang
 * @date: 2017/5/25
 * @version:
 * @description:
 */
class TaskListAdapter(ctx: Context) : RecyclerView.Adapter<TaskViewHolder>(){
    var mContext = ctx
    var mData: List<TaskModel> = arrayListOf()

    fun setData(list: List<TaskModel>?){
        if(list != null)
            mData = list
    }

    override fun onBindViewHolder(holder: TaskViewHolder?, position: Int) {
        with(mData[position]) {
            holder?.name?.text = name
            if (priority == 1) {
                holder?.priorityView?.backgroundColor = mContext.resources.getColor(R.color.color_yellow)
            } else if (priority == 2) {
                holder?.priorityView?.backgroundColor = mContext.resources.getColor(R.color.color_red)
            }

            holder?.itemView?.setOnClickListener {
                mContext.startActivity<TaskCreateActivity>("id" to id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(mContext).inflate(R.layout.activity_item,parent,false))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

}

class TaskViewHolder(view: View) : ViewHolder(view){
    var name: TextView? = null
    var priorityView: View? = null

    init {
        name = view.find<TextView>(R.id.taskName)
        priorityView = view.find(R.id.taskPriority)
    }
}

