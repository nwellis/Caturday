package me.nickellis.pokedex.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import me.nickellis.pokedex.R
import me.nickellis.pokedex.injector
import me.nickellis.pokedex.repo.ErrorResponse
import me.nickellis.pokedex.repo.SuccessResponse
import me.nickellis.pokedex.repo.cat.CatRepository
import me.nickellis.pokedex.service.cat.CatImagesQuery
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var catRepository: CatRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
        setContentView(R.layout.activity_main)

        catRepository.getCatImages(CatImagesQuery(page = 0, pageSize = 25)).enqueueWith(lifecycle) { response ->
          when (response) {
            is SuccessResponse -> {
              main_text.text = response.data.toString()
            }
            is ErrorResponse -> {
              main_text.text = response.error.message
            }
          }
        }
    }
}
