package com.android.verticalprogressbar2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mVerticalStepViewFragment = VerticalStepViewFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, mVerticalStepViewFragment)
            .commit()
    }
}