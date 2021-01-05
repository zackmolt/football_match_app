package com.example.kade

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.kade.activities.MainActivity
import com.example.kade.R.id.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SearchTest {
    @Rule
    @JvmField var activityRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testSearch(){
        onView(withId(m_search))
            .check(matches(isDisplayed()))
        onView(withId(m_search)).perform(click())
        val searchAutoComplete = onView(
            Matchers.allOf(
                withId(search_src_text),
                childAtPosition(
                    Matchers.allOf(
                        withId(search_plate),
                        childAtPosition(
                            withId(search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(replaceText("manchester"), closeSoftKeyboard())

        val searchAutoComplete2 = onView(
            Matchers.allOf(
                withId(search_src_text), withText("manchester"),
                childAtPosition(
                    Matchers.allOf(
                        withId(search_plate),
                        childAtPosition(
                            withId(search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        searchAutoComplete2.perform(pressImeActionButton())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

}