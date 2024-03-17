package com.example.deliverysystem;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.not;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.deliverysystem.Database.DBHelper;

import org.junit.Rule;
import org.junit.Test;

public class ApplicationUnitTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testLoginButton() {
        // Input username and password
        Espresso.onView(ViewMatchers.withId(R.id.editTextUserName))
                .perform(ViewActions.typeText("Test1"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.editTextPassword))
                .perform(ViewActions.typeText("1234"), ViewActions.closeSoftKeyboard());

        // Perform click action on the login button
        Espresso.onView(ViewMatchers.withId(R.id.buttonLogin))
                .perform(ViewActions.click());
    }

    @Test
    public void testSignUpButton() {
        // Perform click action on the login button
        Espresso.onView(ViewMatchers.withId(R.id.buttonSignUp))
                .perform(ViewActions.click());
    }

}
