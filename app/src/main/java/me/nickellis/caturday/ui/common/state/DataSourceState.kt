package me.nickellis.caturday.ui.common.state

import me.nickellis.caturday.domain.common.AppError


sealed class DataSourceState {
  object LoadInitial: DataSourceState()
  object LoadAfter: DataSourceState()
  object Success: DataSourceState()
  class Error(val error: AppError): DataSourceState()
}