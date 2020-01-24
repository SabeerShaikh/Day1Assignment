package com.day1assignment;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction textView = onView(
                allOf(withId(R.id.ts_cat_1), withText("Bengaluru"),
                        childAtPosition(
                                allOf(withId(R.id.catogries),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        textView.check(matches(withText("Bengaluru")));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.image),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.recycler_view),
                                        0),
                                0),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withText("Bruhat Bengaluru Mahanagara Palike looks up to Mumbai"),
                        childAtPosition(
                                allOf(withId(R.id.ts_title),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        textView2.check(matches(withText("Bruhat Bengaluru Mahanagara Palike looks up to Mumbai")));

        ViewInteraction textView3 = onView(
                allOf(withText("https://www.thehindu.com/news/cities/bangalore/bruhat-bengaluru-mahanagara-palike-looks-up-to-mumbai/article30640470.ece"),
                        childAtPosition(
                                allOf(withId(R.id.ts_description),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        textView3.check(matches(withText("https://www.thehindu.com/news/cities/bangalore/bruhat-bengaluru-mahanagara-palike-looks-up-to-mumbai/article30640470.ece")));

        ViewInteraction textView4 = onView(
                allOf(withText("Fri, 24 Jan 2020 07:58:25 +0530"),
                        childAtPosition(
                                allOf(withId(R.id.ts_clock),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        textView4.check(matches(withText("Fri, 24 Jan 2020 07:58:25 +0530")));

        ViewInteraction textView5 = onView(
                allOf(withText("Fri, 24 Jan 2020 07:58:25 +0530"),
                        childAtPosition(
                                allOf(withId(R.id.ts_clock),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class),
                                                1)),
                                0),
                        isDisplayed()));
        textView5.check(matches(withText("Fri, 24 Jan 2020 07:58:25 +0530")));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
