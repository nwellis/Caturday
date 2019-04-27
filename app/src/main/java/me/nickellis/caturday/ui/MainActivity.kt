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

class MainActivity : AppCompatActivity() {

    @Inject lateinit var catRepository: CatRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
        setContentView(R.layout.activity_main)

        catRepository.getCatImages(
          CatImagesQuery(
            page = 0,
            pageSize = 25
          )
        ).enqueueWith(lifecycle) { response ->
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
