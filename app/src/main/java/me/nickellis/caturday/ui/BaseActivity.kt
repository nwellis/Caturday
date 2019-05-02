package me.nickellis.caturday.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.nickellis.caturday.injector

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }
}
