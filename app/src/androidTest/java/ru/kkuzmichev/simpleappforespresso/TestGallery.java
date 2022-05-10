package ru.kkuzmichev.simpleappforespresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;
import static ru.kkuzmichev.simpleappforespresso.CustomViewAssertions.isRecyclerView;
import static ru.kkuzmichev.simpleappforespresso.CustomViewMatcher.recyclerViewSizeMatcher;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class TestGallery {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    ViewInteraction recycleList = onView(withId(R.id.recycle_view));

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


    @Test
    public void testGallery() {
        ViewInteraction menu = onView(Matchers.allOf(withContentDescription("Open navigation drawer"),
                childAtPosition(Matchers.allOf(withId(R.id.toolbar), childAtPosition(withClassName(is("com.google.android.material.appbar.AppBarLayout")), 0)), 1),
                isDisplayed()));
        menu.perform(click());

        ViewInteraction gallery = onView(Matchers.allOf(withId(R.id.nav_gallery),
                childAtPosition(Matchers.allOf(withId(R.id.design_navigation_view), childAtPosition(withId(R.id.nav_view), 0)), 2),
                isDisplayed()));
        gallery.perform(click());
        recycleList.check(isRecyclerView());
        recycleList.check(matches(recyclerViewSizeMatcher(10)));
    }


}

