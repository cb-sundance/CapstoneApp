package com.example.capstoneapp

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.assertion.ViewAssertions.matches
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExampleEspressoTest {

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun appLaunches_andMainTextIsVisible() {
        // Replace R.id.main_text and the expected text with your app's actual UI IDs/text
        onView(withText("Welcome")).check(matches(withText("Welcome")))
    }

    @Test
    fun clickButton_opensNextScreen() {
        // Example: click a button and assert a view on the next screen is visible
        // onView(withId(R.id.some_button)).perform(click())
        // onView(withId(R.id.next_screen_label)).check(matches(withText("Next screen")))
    }
}
