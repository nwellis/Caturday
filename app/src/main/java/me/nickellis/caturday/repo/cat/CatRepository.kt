package me.nickellis.caturday.repo.cat


import android.content.Context
import me.nickellis.caturday.domain.CatBreed
import me.nickellis.caturday.domain.CatImage
import me.nickellis.caturday.repo.RepositoryRequest
import me.nickellis.caturday.repo.asRepositoryRequest
import me.nickellis.caturday.service.ErrorHandler
import me.nickellis.caturday.service.cat.CatService
import retrofit2.Response

/**
 * Repository for querying all cat data.
 */
interface CatRepository {
  /**
   * Get cat images right meow!
   * @param query Used to specify criteria and request which page to load.
   * @return a repository request with the specified page of results. [CatImage.details] will not be populated.
   */
  fun getRandomCatImages(query: CatImagesQuery): RepositoryRequest<List<CatImage>>

  /**
   * Get a more "detailed" view of a cat image.
   * @param imageId image ID found from [CatImage.id]
   * @return a repository request with the specified image. [CatImage.details] will be populated.
   */
  fun getCatImage(imageId: String): RepositoryRequest<CatImage>

  /**
   * Need some cat facts? Use this to get some info on each cat breed.
   * @param query Used to specify criteria and request which page to load.
   * @return a repository request with the specified page of results.
   */
  fun getCatBreeds(query: CatBreedsQuery): RepositoryRequest<List<CatBreed>>
}

class ApiCatRepository(
  private val errorHandler: ErrorHandler<Response<*>>,
  private val catService: CatService,
  private val context: Context
): CatRepository {

  override fun getRandomCatImages(query: CatImagesQuery): RepositoryRequest<List<CatImage>> {

    val size = when(query.imageSize) {
      CatImageSize.Max -> "full"
      CatImageSize.Medium -> "med"
      CatImageSize.Small -> "small"
      CatImageSize.Thumbnail -> "thumb"
    }

    val pageSize = query.pageSize.takeIf { it in 1..100 } ?: 1

    return catService
      .getRandomCatImages(size = size, page = query.page, limit = pageSize)
      .asRepositoryRequest(
        errorHandler = errorHandler,
        mapper = { apiCatImages -> apiCatImages.map { it.toCatImage(context.resources) } }
      )
  }

  override fun getCatImage(imageId: String): RepositoryRequest<CatImage> {
    return catService
      .getCatImage(imageId)
      .asRepositoryRequest(
        errorHandler = errorHandler,
        mapper = { apiCatImageDetail -> apiCatImageDetail.toCatImage(context.resources) }
      )
  }

  override fun getCatBreeds(query: CatBreedsQuery): RepositoryRequest<List<CatBreed>> {
    return catService
      .getCatBreeds(1, page = query.page, limit = query.pageSize)
      .asRepositoryRequest(
        errorHandler = errorHandler,
        mapper = { apiCatBreeds -> apiCatBreeds.map { it.toCatBreed(context.resources) } }
      )
  }
}