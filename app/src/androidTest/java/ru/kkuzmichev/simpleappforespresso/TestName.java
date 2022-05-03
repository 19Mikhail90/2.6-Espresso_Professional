package ru.kkuzmichev.simpleappforespresso;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class TestName {
    @Rule
    public IntentsTestRule intentsTestRule = new IntentsTestRule(MainActivity.class);

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
    public void testName() {
        ViewInteraction setting = onView(Matchers.allOf(withContentDescription("More options"), childAtPosition(childAtPosition(withId(R.id.toolbar), 2), 0), isDisplayed()));
        setting.perform(click());

        ViewInteraction settingsItem = onView(Matchers.allOf(withId(androidx.appcompat.R.id.title), withText("Settings"), childAtPosition(childAtPosition(withId(androidx.appcompat.R.id.content), 0), 0), isDisplayed()));
        settingsItem.perform(click());
        intended(allOf(
                hasData("https://google.com"),
                hasAction(Intent.ACTION_VIEW)));
    }
}
/**Ниже код как в лекции, он не сработал. Записал рекордером в тесте.*/
//        ViewInteraction setting = onView(withParent(isAssignableFrom(ActionMenuView.class)));
//        ViewInteraction settingsItem = onView(allOf(withId(R.id.item_title), withText("Settings")));
//
//        setting.check(matches(isDisplayed()));
//        setting.perform(click());
//        settingsItem.check(matches(isDisplayed()));
