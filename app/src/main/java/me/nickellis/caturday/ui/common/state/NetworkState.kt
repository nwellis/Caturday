package me.nickellis.caturday.ui.common.state

import me.nickellis.caturday.domain.common.AppError


sealed class NetworkState {
  object Loading : NetworkState()
  object Success : NetworkState()
  class Error(val error: AppError) : NetworkState()
}