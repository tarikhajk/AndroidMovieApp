package com.outlook.movieappv2;


import android.os.SystemClock;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
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

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SarchandGenrePlusResetTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void sarchandGenrePlusResetTest() {
        SystemClock.sleep(500);
        ViewInteraction textView = onView(
                allOf(withId(R.id.posterTextView), withText("John Wick"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView.check(matches(withText("John Wick")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.posterTextView), withText("Left Behind"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView2.check(matches(withText("Left Behind")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.genreSpinner), withText("All genres"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        textView3.check(matches(withText("All genres")));

        ViewInteraction niceSpinner = onView(
                allOf(withId(R.id.genreSpinner), withText("All genres"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        niceSpinner.perform(click());

        DataInteraction appCompatTextView = onData(anything())
                .inAdapterView(allOf(withId(R.id.genreSpinner),
                        childAtPosition(
                                withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                                0)))
                .atPosition(3);
        appCompatTextView.perform(click());

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.posterTextView), withText("Alexander and the Terrible, Horrible, No Good, Very Bad Day"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView4.check(matches(withText("Alexander and the Terrible, Horrible, No Good, Very Bad Day")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.posterTextView), withText("Playing It Cool"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView5.check(matches(withText("Playing It Cool")));

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.genreSpinner), withText("Comedy"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        textView6.check(matches(withText("Comedy")));

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.searchBarEditText),
                        childAtPosition(
                                allOf(withId(R.id.searchLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                0),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("the"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.searchBarEditText), withText("the"),
                        childAtPosition(
                                allOf(withId(R.id.searchLayout),
                                        childAtPosition(
                                                withClassName(is("android.widget.RelativeLayout")),
                                                2)),
                                0),
                        isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction editText = onView(
                allOf(withId(R.id.searchBarEditText), withHint("Showing movies with 'the'"),
                        childAtPosition(
                                allOf(withId(R.id.searchLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                2)),
                                0),
                        isDisplayed()));
        editText.check(matches(withHint("Showing movies with 'the'")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.posterTextView), withText("Alexander and the Terrible, Horrible, No Good, Very Bad Day"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView7.check(matches(withText("Alexander and the Terrible, Horrible, No Good, Very Bad Day")));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.resetButton), withText("Show all movies"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatButton.perform(click());

        SystemClock.sleep(500);
        ViewInteraction textView8 = onView(
                allOf(withId(R.id.genreSpinner), withText("All genres"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        textView8.check(matches(withText("All genres")));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.posterTextView), withText("John Wick"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView9.check(matches(withText("John Wick")));

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.posterTextView), withText("Left Behind"),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                        0),
                                1),
                        isDisplayed()));
        textView10.check(matches(withText("Left Behind")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.searchBarEditText), withHint("Enter a movie title..."),
                        childAtPosition(
                                allOf(withId(R.id.searchLayout),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.RelativeLayout.class),
                                                2)),
                                0),
                        isDisplayed()));
        editText2.check(matches(withHint("Enter a movie title...")));
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
