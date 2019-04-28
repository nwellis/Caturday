package me.nickellis.caturday.repo.cat


import me.nickellis.caturday.data.CatImage
import me.nickellis.caturday.ktx.ensureNonNullId
import me.nickellis.caturday.repo.RepositoryRequest
import me.nickellis.caturday.repo.asRepositoryRequest
import me.nickellis.caturday.service.ErrorHandler
import me.nickellis.caturday.service.cat.CatService
import retrofit2.Response

/**
 * Repository for querying all cat data.
 *
 * @see [Docs](https://docs.thecatapi.com/)
 */
interface CatRepository {
  /**
   * Get cat images right meow!
   * @param query Used to specify criteria and request which page to load.
   * @return a repository request with the specified page of results.
   */
  fun getCatImages(query: CatImagesQuery): RepositoryRequest<List<CatImage>>
}

class ApiCatRepository(
  private val errorHandler: ErrorHandler<Response<*>>,
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
        errorHandler = errorHandler,
        mapper = { apiCatImages ->
          apiCatImages.map { apiCatImage ->
            CatImage(
              id = apiCatImage.id.ensureNonNullId("Found null ID from cat API get images"),
              url = apiCatImage.url
            )
          }
        }
      )
  }
}