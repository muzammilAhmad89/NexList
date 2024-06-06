package com.nine_tailstech.nexlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.todo_list.R

class Task : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val db = DataBase(this)
        val position = intent.getIntExtra("position", -1) // получаем порядковый номер элемента
        val task = db.getTaskById(position + 1) // получаем данные из базы данных

        val editText4 = findViewById<EditText>(R.id.editTextText4)
        val editText5 = findViewById<EditText>(R.id.editTextText5)
        val editText6 = findViewById<EditText>(R.id.editTextText6)

        editText4.setText(task?.name)
        editText5.setText(task?.dateAcc)
        editText6.setText(task?.description)


        val buttonCloseTask = findViewById<Button>(R.id.buttonCloseTask)
        buttonCloseTask.setOnClickListener {
            super.onResume()
            finish()
        }
       val buttonEdit = findViewById<Button>(R.id.buttonEdit)
        buttonEdit.setOnClickListener {
            db.deleteTaskById(position + 1)
            val dateAcc = editText5.text.toString()
            val descriptor = editText6.text.toString()
            val name = editText4.text.toString()
            db.addTask(
                name = name,
                description = descriptor,
                dateAcc = dateAcc,
                status = true/*статус еще не готов*/
            )
            super.onResume()
            db.close()
            finish()
        }
    }
}