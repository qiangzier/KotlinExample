package com.hzq.taskproject

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.hzq.taskproject.model.RemindModel
import com.hzq.taskproject.model.RemindTimeModel
import com.hzq.taskproject.model.getTextByType
import kotlinx.android.synthetic.main.activity_task_remind.*
import org.jetbrains.anko.find

class TaskRemindActivity : BaseActivity() {

    var remindModel = RemindModel()
    var taskId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_remind)
        initToolBar()
        setTitle(R.string.task_remind)

        layout2.setOnClickListener {
            checkbox2.toggle()
        }
        layout3.setOnClickListener {
            checkbox3.toggle()
        }

        add(RemindTimeModel(0L,4,3))
        add(RemindTimeModel(0L,5,1))
        add(RemindTimeModel(0L,6,10))

        setData()

    }

    fun add(model: RemindTimeModel){
        remindModel.reminds.add(model)
    }

    fun generatorRemindItemView(model: RemindTimeModel): View{
        val view = LayoutInflater.from(this).inflate(R.layout.task_remind_time_layout,task_remind_container,false)
        var tName = view.find<TextView>(R.id.textName)
        tName.text = getTextByType(this,model)
        var delImg = view.find<ImageView>(R.id.delete)
        delImg.setOnClickListener {
            startAnimator(view,model)
        }
        return view
    }

    fun startAnimator(view: View,model: RemindTimeModel){
        var animator = ObjectAnimator.ofFloat(view,"translationX",0f,-view.width.toFloat())
        animator.duration = 280

        animator.addListener(object : AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                setData(model,false)
            }
        })
        animator.start()

    }

    private fun setData(timeModel: RemindTimeModel? = null,add: Boolean = true){
        if(timeModel != null){
            if(add){
                remindModel.reminds.add(timeModel)
            }else{
                remindModel.reminds.remove(timeModel)
            }
        }
        buildView()
        task_remind_container.visibility =
                if(remindModel.reminds.size > 0) View.VISIBLE else View.GONE
        create_remind.visibility =
                if(remindModel.reminds.size >= 10) View.GONE else View.VISIBLE
    }

    private fun buildView(){
        task_remind_container.removeAllViews()
        remindModel.reminds
                .mapNotNull { generatorRemindItemView(it) }
                .forEach { task_remind_container.addView(it) }
    }
}
