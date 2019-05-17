package me.nickellis.caturday.ui.common.viewmodel

import android.os.Bundle
import android.os.TransactionTooLargeException

/**
 * View model that can save its data to a [Bundle] so that it can try restore itself later. Note that this should
 * not be used for storing any significant amount of data as we want to avoid a [TransactionTooLargeException]. When
 * a large amount of data needs to be stored considering saving its identifier and fetching that resource with
 * the ID on restoration.
 */
interface PersistentViewModel {

  /**
   * Called when a view model should save its data to the [Bundle]. Note again that this should not hold large amounts
   * of data.
   *
   * @param bundle The bundle to save to
   */
  fun saveTo(bundle: Bundle)

  /**
   * Called when the view model should try restore its state from the [Bundle].
   *
   * @param bundle The bundle to restore from (if applicable)
   */
  fun restoreFrom(bundle: Bundle?)
}