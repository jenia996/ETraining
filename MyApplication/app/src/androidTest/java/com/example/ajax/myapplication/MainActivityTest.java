package com.example.ajax.myapplication;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity
            .class);

    @Test
    public void mainActivityTest() {
        ViewInteraction editText = onView(allOf(withId(R.id.edit_text), childAtPosition(allOf
                (withId(R.id.content_main), childAtPosition(IsInstanceOf.<View>instanceOf(android
                        .view.ViewGroup.class), 1)), 1), isDisplayed()));
        editText.check(matches(isDisplayed()));

        ViewInteraction appCompatEditText = onView(allOf(withId(R.id.edit_text), withParent
                (withId(R.id.content_main)), isDisplayed()));
        appCompatEditText.perform(replaceText("Hello world!"), closeSoftKeyboard());

        ViewInteraction editText2 = onView(allOf(withId(R.id.edit_text), withText("Hello world!")
                , childAtPosition(allOf(withId(R.id.content_main), childAtPosition(IsInstanceOf
                        .<View>instanceOf(android.view.ViewGroup.class), 1)), 1), isDisplayed()));
        editText2.check(matches(withText("Hello world!")));

        ViewInteraction button = onView(allOf(withId(R.id.btn_temp), childAtPosition(allOf(withId
                (R.id.content_main), childAtPosition(IsInstanceOf.<View>instanceOf(android.view
                .ViewGroup.class), 1)), 0), isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(allOf(withId(R.id.btn_temp), withText("Show " +
                "Visibility"), withParent(withId(R.id.content_main)), isDisplayed()));
        appCompatButton.perform(click());

    }

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int
            position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent) && view
                        .equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
