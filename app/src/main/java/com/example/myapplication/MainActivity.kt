package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.lighting.LightingActivity


class MainActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_triangle).setOnClickListener {
            startActivity(Intent(this, SquareActivity::class.java))
        }

        findViewById<Button>(R.id.btn_light).setOnClickListener {
            startActivity(Intent(this, LightingActivity::class.java))
        }
    }


}