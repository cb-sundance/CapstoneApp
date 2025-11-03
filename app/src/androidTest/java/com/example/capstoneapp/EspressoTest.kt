package com.example.capstoneapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

class EspressoTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testFullUserFlow() {

        // ✅ Check Welcome screen text exists
        composeTestRule.onNodeWithText("Enter your name").assertIsDisplayed()

        // ✅ Type into TextField
        composeTestRule.onNodeWithText("Enter your name").performTextInput("John Doe")

        // ✅ Tap the Continue button
        composeTestRule.onNodeWithText("Continue").performClick()

        // ✅ Ensure About Me screen is shown
        composeTestRule.onNodeWithText("About Me").assertIsDisplayed()

        // ✅ Confirm that the user name is displayed on About Me screen
        composeTestRule.onNodeWithText("Hello, John Doe!").assertIsDisplayed()
    }
}
