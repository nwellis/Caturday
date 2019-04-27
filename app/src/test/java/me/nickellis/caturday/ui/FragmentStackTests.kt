package me.nickellis.caturday.ui

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
import org.mockito.junit.MockitoJUnitRunner

/**
 * TODO: Isn't very "unit testy" as I'm performing ops to begin initial state. Change once resume state is implemented
 */
@RunWith(MockitoJUnitRunner::class)
class FragmentStackTests {

  @IdRes private val containerId: Int = 10000
  @Mock private lateinit var mockFragmentManager: FragmentManager
  @Mock private lateinit var mockTransaction: FragmentTransaction

  private fun newMockFragment(): Fragment {
    return mock(Fragment::class.java)
  }

  private lateinit var fragmentStack: FragmentStack

  @Before
  fun setUp() {
    fragmentStack = FragmentStack(mockFragmentManager, containerId)

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

    // If the fragment stack is non empty, it will find an existing fragment
    `when`(mockFragmentManager.findFragmentByTag(anyString()))
      .thenReturn(existingFragment)

    fragmentStack.push(existingFragment)

    // Act
    fragmentStack.push(newFragment)

    // Assert
    verifyBeginTransactionWithCommit(2)

    verify(mockTransaction, times(1))
      .detach(existingFragment)
    verify(mockTransaction, times(2))
      .add(anyInt(), any(Fragment::class.java), anyString())
  }

  @Test
  fun `pop on non-empty fragment stack`() {
    // Arrange
    val fragment = newMockFragment()
    fragmentStack.push(fragment)

    // If the fragment stack is non empty, it will find an existing fragment
    `when`(mockFragmentManager.findFragmentByTag(anyString()))
      .thenReturn(fragment)

    // Act
    fragmentStack.pop()

    // Assert
    verifyBeginTransactionWithCommit(2)

    verify(mockTransaction, times(1))
      .remove(fragment)
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
  fun `clear on non-empty fragment stack`() {
    // Arrange
    val fragment1 = newMockFragment()
    val fragment2 = newMockFragment()
    val fragment3 = newMockFragment()

    // If the fragment stack is non empty, it will find an existing fragment
    `when`(mockFragmentManager.findFragmentByTag(anyString()))
      .thenReturn(null)
      .thenReturn(fragment1)
      .thenReturn(fragment2)
      .thenReturn(fragment3)
      .thenReturn(fragment1)
      .thenReturn(fragment2)
      .thenReturn(fragment3)

    fragmentStack.apply {
      push(fragment1)
      push(fragment2)
      push(fragment3)
    }

    // Act
    fragmentStack.clear()

    // Assert
    verifyBeginTransactionWithCommit(4)

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
}