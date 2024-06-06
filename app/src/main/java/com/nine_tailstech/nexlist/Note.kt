package com.nine_tailstech.nexlist

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.todo_list.R
import java.util.Calendar

class Note : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note)

        // Создаем объект класса MyDatabase
        val myDatabase = DataBase(this)
        val db = myDatabase.writableDatabase

        // Получить ссылку на EditText, который будет использоваться для выбора даты
        val dateEditText = findViewById<EditText>(R.id.editTextDate)

        // Установить слушатель нажатия для EditText
        dateEditText.setOnClickListener {
            // Создать DatePickerDialog для выбора даты
            val datePickerDialog = DatePickerDialog(this, { _, year, monthOfYear, dayOfMonth ->
                    // Получить выбранную дату и установить ее в EditText
                    val selectedDate = String.format("%02d-%02d-%d", dayOfMonth, monthOfYear+1, year)
                    dateEditText.setText(selectedDate)
                },
                // Установить текущую дату как начальную дату в DatePickerDialog
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )

            // Показать DatePickerDialog
            datePickerDialog.show()
        }

        val dateEditText2 = findViewById<EditText>(R.id.editTextText3)
        val dateEditText3 = findViewById<EditText>(R.id.editTextText)

        val button = findViewById<Button>(R.id.buttonCreate)
        // Вызываем функцию addTask, передавая ей необходимые параметры
        button.setOnClickListener {
            val dateAcc = dateEditText.text.toString()
            val descriptor = dateEditText2.text.toString()
            val name = dateEditText3.text.toString()

            val yes = areStringsNotNull(name)

            if (!yes) {
                Toast.makeText(this, "Enter Note Title", Toast.LENGTH_SHORT).show()
            }
            else{
                myDatabase.addTask(name = name , description = descriptor , dateAcc = dateAcc, status = true/*статус еще не готов*/)
                super.onResume()
                db.close()
                this.recreate();
                finish()
            }

        }

        val but1 = findViewById<Button>(R.id.buttonClose)
        but1.setOnClickListener {
            super.onResume()
            db.close()
            finish()//добавил закрытие layout
            onPause()
        }
    }
    fun areStringsNotNull(vararg strings: String?): Boolean {
        for (str in strings) {
            if (str == null || str.isEmpty()) {
                return false
            }
        }
        return true
    }
}
