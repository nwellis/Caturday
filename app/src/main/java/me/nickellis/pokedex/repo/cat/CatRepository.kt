package me.nickellis.pokedex.repo.cat

import android.content.Context
import me.nickellis.pokedex.domain.CatImage
import me.nickellis.pokedex.ktx.ensureNonNullId
import me.nickellis.pokedex.repo.RepositoryRequest
import me.nickellis.pokedex.repo.asRepositoryRequest
import me.nickellis.pokedex.service.cat.CatImageSize
import me.nickellis.pokedex.service.cat.CatImagesQuery
import me.nickellis.pokedex.service.cat.CatService


interface CatRepository {
  /**
   * Get cat images right meow!
   */
  fun getCatImages(query: CatImagesQuery): RepositoryRequest<List<CatImage>>
}

class ApiCatRepository(
  private val context: Context,
  private val catService: CatService
): CatRepository {

  override fun getCatImages(query: CatImagesQuery): RepositoryRequest<List<CatImage>> {

    val size = when (query.imageSize) {
      CatImageSize.Max -> "full"
      CatImageSize.Medium -> "med"
      CatImageSize.Small -> "small"
      CatImageSize.Thumbnail -> "thumb"
    }

    val pageSize = query.pageSize.takeIf { it in 1..100 } ?: 1

    return catService
      .searchCatImages(size = size, page = query.page, limit = pageSize)
      .asRepositoryRequest(
        resources = context.resources,
        mapper = { apiCatImages ->
          apiCatImages.mapNotNull { apiCatImage ->
            CatImage(
              id = apiCatImage.id.ensureNonNullId("Found null ID from cat API get images"),
              url = apiCatImage.url
            )
          }
        }
      )
  }
}