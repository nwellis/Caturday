package me.nickellis.caturday.ui.common.state

import me.nickellis.caturday.repo.RepositoryError


sealed class NetworkState {
  object Loading : NetworkState()
  object Success : NetworkState()
  class Error(val error: RepositoryError) : NetworkState()
}