package me.nickellis.caturday.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.nickellis.caturday.R
import me.nickellis.caturday.injector
import me.nickellis.caturday.repo.ErrorResponse
import me.nickellis.caturday.repo.SuccessResponse
import me.nickellis.caturday.repo.cat.CatRepository
import me.nickellis.caturday.repo.cat.CatImagesQuery
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }
}
