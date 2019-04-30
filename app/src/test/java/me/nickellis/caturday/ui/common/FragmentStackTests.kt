package me.nickellis.caturday.ui.common

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import me.nickellis.caturday.ui.common.navigation.FragmentStack
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.internal.util.reflection.FieldSetter
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FragmentStackTests {

  @IdRes private val containerId: Int = 10000
  @Mock private lateinit var mockFragmentManager: FragmentManager
  @Mock private lateinit var mockTransaction: FragmentTransaction
  @Mock private lateinit var mockBundle: Bundle

  private fun newMockFragment(): Fragment {
    return mock(Fragment::class.java)
  }

  private lateinit var fragmentStack: FragmentStack

  @Before
  fun setUp() {
    fragmentStack = FragmentStack(mockFragmentManager, containerId, mockBundle)

    `when`(mockFragmentManager.beginTransaction())
      .thenReturn(mockTransaction)

    `when`(mockTransaction.commit())
      .thenReturn(-1)
  }

  @After
  fun tearDown() {
  }

  @Test
  fun `push on empty fragment stack`() {
    // Arrange
    val fragment = newMockFragment()

    // Act
    fragmentStack.push(fragment)

    // Assert
    verifyBeginTransactionWithCommit(1)

    verify(mockTransaction, never())
      .detach(any(Fragment::class.java))
    verify(mockTransaction, times(1))
      .add(anyInt(), any(Fragment::class.java), anyString())

    assertTrue("Stack shouldn't be empty", !fragmentStack.isEmpty)
    assertEquals("Stack should have size of 1", 1, fragmentStack.size)
  }

  @Test
  fun `push on non-empty fragment stack`() {
    // Arrange
    val existingFragment = newMockFragment()
    val newFragment = newMockFragment()
    fragmentStack.setInitialFragments(existingFragment)

    // Act
    fragmentStack.push(newFragment)

    // Assert
    verifyBeginTransactionWithCommit(1)

    verify(mockTransaction, times(1))
      .detach(existingFragment)
    verify(mockTransaction, times(1))
      .add(anyInt(), any(Fragment::class.java), anyString())
  }

  @Test
  fun `pop on empty fragment stack`() {
    // Arrange

    // Act
    fragmentStack.pop()

    // Assert
    verifyBeginTransactionWithCommit(0)

    verify(mockTransaction, never())
      .remove(any(Fragment::class.java))
  }

  @Test
  fun `pop on non-empty fragment stack`() {
    // Arrange
    val fragment = newMockFragment()
    fragmentStack.setInitialFragments(fragment)

    // Act
    fragmentStack.pop()

    // Assert
    verifyBeginTransactionWithCommit(1)

    verify(mockTransaction, times(1))
      .remove(fragment)
  }

  @Test
  fun `clear on non-empty fragment stack`() {
    // Arrange
    val fragment1 = newMockFragment()
    val fragment2 = newMockFragment()
    val fragment3 = newMockFragment()
    fragmentStack.setInitialFragments(fragment1, fragment2, fragment3)

    // Act
    fragmentStack.clear()

    // Assert
    verifyBeginTransactionWithCommit(1)

    verify(mockTransaction, times(1))
      .remove(fragment1)
    verify(mockTransaction, times(1))
      .remove(fragment2)
    verify(mockTransaction, times(1))
      .remove(fragment3)

    assertTrue("Stack should be empty", fragmentStack.isEmpty)
    assertEquals("Stack should have size of 0", 0, fragmentStack.size)
  }

  @Test
  fun `clear on empty fragment stack`() {
    // Arrange

    // Act
    fragmentStack.clear()

    // Assert
    verifyBeginTransactionWithCommit(0)

    assertTrue("Stack should be empty", fragmentStack.isEmpty)
    assertEquals("Stack should have size of 0", 0, fragmentStack.size)
  }

  /**
   * A very common pairing. I want to ensure that if a transaction is began, then it will commit that transaction.
   */
  private fun verifyBeginTransactionWithCommit(wantedNumberOfInvocations: Int = 1) {
    verify(mockFragmentManager, times(wantedNumberOfInvocations))
      .beginTransaction()
    verify(mockTransaction, times(wantedNumberOfInvocations))
      .commit()
  }

  private fun FragmentStack.setInitialFragments(vararg fragments: Fragment) {
    val fragmentsWithTag = fragments.mapIndexed { index, fragment ->
      Pair(fragment, FragmentStack.TAG_PATTERN_PREFIX + index)
    }

    FieldSetter.setField(this, javaClass.getDeclaredField("fragmentTagStack"),
      fragmentsWithTag.map { (_, tag) -> tag }.toMutableList())

    fragmentsWithTag.forEach { (fragment, tag) ->
      `when`(mockFragmentManager.findFragmentByTag(tag))
        .thenReturn(fragment)
    }
  }
}