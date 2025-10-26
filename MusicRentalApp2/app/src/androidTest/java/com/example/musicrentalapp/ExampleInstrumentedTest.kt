package com.example.musicrentalapp

import androidx.compose.ui.test.hasProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.test.performSemanticsAction

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainFlowTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    /**
     * The original test to ensure the borrow and cancel flow works correctly.
     */
    @Test
    fun borrow_and_cancel_flow() {
        // Assert that initial item name is visible
        composeTestRule.onNodeWithText("Scorpion AGS430").assertIsDisplayed()

        // Click the Borrow button
        composeTestRule.onNodeWithText("Borrow").performClick()

        // We are now in DetailActivity — check item title
        composeTestRule.onNodeWithText("Borrow Scorpion AGS430").assertExists()

        // Type a note
        composeTestRule.onNodeWithText("Your note (required)")
            .performTextInput("Great instrument")

        // Cancel booking
        composeTestRule.onNodeWithText("Cancel").performClick()

        // Verify we are back on the main screen and no note chip is present
        composeTestRule.onNodeWithText("Scorpion AGS430").assertExists()
        composeTestRule.onNode(hasText("Saved:", substring = true)).assertDoesNotExist()
    }

    // --- NEW TESTS TO ADD ---

    /**
     * Tests that the 'Next' button correctly cycles through the available instruments.
     */
    @Test
    fun nextButton_cyclesThroughItems() {
        // Initial item
        composeTestRule.onNodeWithText("Scorpion AGS430").assertIsDisplayed()

        // Click "Next" to see the second item
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("Nord Stage 3").assertIsDisplayed()

        // Click "Next" to see the third item
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("Yamaha YAS-280").assertIsDisplayed()

        // Click "Next" again to loop back to the first item
        composeTestRule.onNodeWithText("Next").performClick()
        composeTestRule.onNodeWithText("Scorpion AGS430").assertIsDisplayed()
    }

    /**
     * Tests the complete end-to-end flow of successfully borrowing an item and
     * verifying that the saved note appears on the main screen.
     */
    @Test
    fun borrow_and_save_flow_isSuccessful() {
        // Go to the detail screen
        composeTestRule.onNodeWithText("Borrow").performClick()

        // Fill out the required note
        val noteText = "For my concert"
        composeTestRule.onNodeWithText("Your note (required)").performTextInput(noteText)

        // Click Save
        composeTestRule.onNodeWithText("Save").performClick()

        // Verify we are back on the main screen
        composeTestRule.onNodeWithText("Scorpion AGS430").assertExists()

        // Crucially, verify the saved note now appears as an AssistChip
        composeTestRule.onNodeWithText("Saved: $noteText").assertIsDisplayed()
    }

    /**
     * Tests the validation logic in DetailActivity.
     * It ensures the "Save" button is disabled when the note is empty.
     */
    @Test
    fun detailScreen_saveButtonIsDisabledWhenNoteIsEmpty() {
        // Go to the detail screen
        composeTestRule.onNodeWithText("Borrow").performClick()

        // Verify the "Save" button is initially disabled because the note is empty
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()

        // Verify the error message for the text field is shown
        composeTestRule.onNodeWithText("Note is required").assertIsDisplayed()

        // Type something into the note field
        composeTestRule.onNodeWithText("Your note (required)").performTextInput("A valid note")

        // Now, the "Save" button should be enabled
        composeTestRule.onNodeWithText("Save").assertIsEnabled()
    }

    /**
     * Tests the validation logic in DetailActivity.
     * It ensures the "Save" button is disabled when the total cost exceeds the credit limit.
     */
    @Test
    fun detailScreen_saveButtonIsDisabledWhenCreditExceeded() {
        composeTestRule.onNodeWithText("Borrow").performClick()
        composeTestRule.onNodeWithText("Your note (required)")
            .performTextInput("Testing credit limit")

        composeTestRule.onNodeWithText("Save").assertIsEnabled()

        // Find the Slider by its range and set months to 3
        val slider = composeTestRule.onNodeWithTag("durationSlider")
        slider.performSemanticsAction(SemanticsActions.SetProgress) { it(3f) }
        composeTestRule.onNode(hasText("(exceeds 100!)", substring = true)).assertIsDisplayed()
        composeTestRule.onNodeWithText("Save").assertIsNotEnabled()
    }
}
