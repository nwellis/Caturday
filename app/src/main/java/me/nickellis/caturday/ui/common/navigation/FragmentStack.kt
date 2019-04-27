package me.nickellis.caturday.ui.common.navigation

import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * A simple wrapper that makes using a [FragmentManager] easier. This wrapper is geared towards using the manager
 * as a navigation stack specifically.
 *
 * The stack is kept track of by the tags assigned to each fragment. When an actual reference to an actual
 * fragment is needed, the tag is used to get it from the [FragmentManager].
 */
class FragmentStack(
  private val manager: FragmentManager,
  @param:IdRes private val containerViewId: Int
) {
  
  companion object {
    private const val TAG_PATTERN_PREFIX = "frag_stack_"
    const val TAG = "FragmentStack"
  }

  /**
   * By only maintaining the tags, we can save them in a bundle and restore the fragments.
   */
  private val fragmentTagStack = mutableListOf<String>()

  /**
   * Checks the stack to see if its empty
   * @returns True if the stack is empty
   */
  val isEmpty: Boolean
    get() = fragmentTagStack.isEmpty()

  /**
   * Gets the number of fragments in the stack
   * @returns the count of fragments in the stack
   */
  val size: Int
    get() = fragmentTagStack.size

  /**
   * Push a fragment on the stack
   * @param nextFragment Fragment to push onto the stack.
   */
  fun push(nextFragment: Fragment) {
    val currentFragment = topFragmentByTagStack()

    val nextTag = "$TAG_PATTERN_PREFIX ${fragmentTagStack.size}"
      .also { fragmentTagStack.add(it) }

    executeTransaction {
      add(containerViewId, nextFragment, nextTag)
      currentFragment?.let { detach(it) }
    }
  }

  /**
   * Pop a fragment off the stack. If there are no fragments this is a no-op.
   */
  fun pop() {
    topFragmentByTagStack()?.let { currentFragment ->
      executeTransaction {
        remove(currentFragment)
        fragmentTagStack.removeAt(fragmentTagStack.lastIndex)

        topFragmentByTagStack()?.let { previousFragment ->
          attach(previousFragment)
        }
      }
    }
  }

  /**
   * Clear all the fragments. If there are no fragments this is a no-op.
   */
  fun clear() {
    if (isEmpty) return

    executeTransaction {
      fragmentTagStack
        .mapNotNull { tag -> fragmentAt(tag) }
        .forEach { fragment -> remove(fragment) }
    }

    fragmentTagStack.clear()
  }

  private fun topFragmentByTagStack(): Fragment? {
    return fragmentTagStack.lastOrNull()?.let { fragmentAt(it) }
  }

  private fun fragmentAt(tag: String): Fragment? {
    return manager.findFragmentByTag(tag)
  }

  private inline fun executeTransaction(block: FragmentTransaction.() -> Unit) {
    return manager.beginTransaction()
      .apply(block)
      .tryCommit()
  }

  private fun FragmentTransaction.tryCommit() {
    try {
      commit()
    } catch (ise: IllegalStateException) {
      try { 
        commitAllowingStateLoss()
        Log.e(TAG, "Transaction with state loss")
      } catch (ex: Exception) {
        Log.e(TAG, "Transaction commit error$ex")
      }
    } catch (ex: Exception) {
      Log.e(TAG, "Transaction commit error$ex")
    }
  }
}