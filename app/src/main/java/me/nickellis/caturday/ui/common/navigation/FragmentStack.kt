package me.nickellis.caturday.ui.common.navigation

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * A simple wrapper that makes using a [FragmentManager] easier. This wrapper is geared towards using the manager
 * as a navigation stack specifically.
 *
 * The stack is kept track of by the tags assigned to each fragment. When an actual reference to an actual
 * fragment is needed, the tag is used to get it from the [FragmentManager].
 *
 * @param manager Fragment manager to wrap from [AppCompatActivity.getSupportFragmentManager]
 * @param containerViewId Layout ID of the container for the stack
 * @param inState to resume the state of the fragment stack
 */
class FragmentStack(
  private val manager: FragmentManager,
  @param:IdRes private val containerViewId: Int,
  inState: Bundle?
) {

  companion object {
    const val TAG_PATTERN_PREFIX = "frag_stack_"
    const val BUNDLE_KEY = "frag_stack_tags"
    const val TAG = "FragmentStack"
  }

  /**
   * By only maintaining the tags, we can save them in a bundle and restore the fragments.
   */
  private val fragmentTagStack: MutableList<String>

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
   * Resume the previous tag array and ensure it is sorted correctly.
   */
  init {
    fragmentTagStack = inState?.getStringArray(BUNDLE_KEY)
      ?.sortedBy { tag -> tag.removePrefix(TAG_PATTERN_PREFIX).toIntOrNull() }
      ?.toMutableList()
      ?: mutableListOf()
  }

  /**
   * In order for the fragment stack to resume it's previous state you must save it here on
   * [AppCompatActivity.onSaveInstanceState]. When the stack is recreated it will use the bundle to resume
   * the stack automatically.
   */
  fun save(outState: Bundle?) {
    outState?.putStringArray(BUNDLE_KEY, fragmentTagStack.toTypedArray())
  }

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