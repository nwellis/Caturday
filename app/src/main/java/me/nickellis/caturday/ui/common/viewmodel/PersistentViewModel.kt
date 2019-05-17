package me.nickellis.caturday.ui.common.viewmodel

import android.os.Bundle

interface PersistentViewModel {
  fun saveTo(bundle: Bundle)
  fun restoreFrom(bundle: Bundle?)
}

interface PersistentViewModelManager {
  fun managePersistenceOf(viewModel: PersistentViewModel)
}