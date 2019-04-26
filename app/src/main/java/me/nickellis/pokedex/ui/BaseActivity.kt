package me.nickellis.pokedex.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.nickellis.pokedex.R
import me.nickellis.pokedex.injector
import me.nickellis.pokedex.repo.ErrorResponse
import me.nickellis.pokedex.repo.SuccessResponse
import me.nickellis.pokedex.repo.cat.CatRepository
import me.nickellis.pokedex.repo.cat.CatImagesQuery
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }
}
