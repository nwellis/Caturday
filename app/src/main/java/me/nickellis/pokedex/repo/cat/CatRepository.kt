package me.nickellis.pokedex.repo.cat

import me.nickellis.pokedex.repo.RepositoryRequest


interface CatRepository {
  fun getCatImages(): RepositoryRequest<Unit>
}

class ApiCatRepository(

): CatRepository {
  override fun getCatImages(): RepositoryRequest<Unit> {
    throw NotImplementedError()
  }
}