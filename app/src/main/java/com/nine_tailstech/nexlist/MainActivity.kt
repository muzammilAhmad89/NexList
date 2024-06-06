package com.nine_tailstech.nexlist

import android.content.Intent
import android.graphics.drawable.PictureDrawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caverock.androidsvg.SVG
import com.example.todo_list.R
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        // Заполняем основное окно
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = DataBase(this)
        db.initialize()

        // Инициализируем список под названием events типа RecycleView, заполняя его с помощью адаптера EventsAdapter
        val events = findViewById<View>(R.id.mainList) as RecyclerView
        val adapter = EventsAdapter(db)
        events.adapter = adapter
        events.layoutManager = LinearLayoutManager(this)

        val p2 = findViewById<Button>(R.id.button6)
        val myNotification = Notification(this)
        myNotification.createNotificationChannel("channel_id_example_01")
        p2.setOnClickListener {
            myNotification.sendNotification("channel_id_example_01", 101)
        }

        val but1 = findViewById<Button>(R.id.button5)
        but1.setOnClickListener {
            val intent = Intent(this, Note::class.java)
            startActivity(intent)
        }
        linearLayout = findViewById(R.id.my_linear_layout)

        loadSvgBackground()
    }
    override fun onResume() {
        super.onResume()
        val events = findViewById<View>(R.id.mainList) as RecyclerView
        val adapter = events.adapter as EventsAdapter
        adapter.notifyDataSetChanged()
    }
    private fun loadSvgBackground() {
        try {
            val inputStream: InputStream = resources.openRawResource(R.raw.wave)
            val svg: SVG = SVG.getFromInputStream(inputStream)
            val drawable = PictureDrawable(svg.renderToPicture())
            Glide.with(this)
                .`as`(PictureDrawable::class.java)
                .load(drawable)
                .into(SvgDrawableTranscoder(linearLayout))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
