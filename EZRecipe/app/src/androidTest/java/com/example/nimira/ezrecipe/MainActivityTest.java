package com.example.nimira.ezrecipe;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

/**
 * Created by dkswe on 6/4/2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void initValidString() {
        //Specify a valid string
        mStringToBetyped = "apples";
    }

    @Test
    public void changeText_sameActivity() {
       /* //Type text then press the button.
        onView(withId(R.id.editTextUserInput)).perform(typeText(mStringToBetyped), closeSoftKeyboard());
        onView(withId(R.id.changeTextBt)).perform(click());
        //Check that hte text was changed.
        onView(withId(R.id.textToBeChanged)).check(matches(withText(mStringToBetyped)));*/
    }
    @Test
    public void onCreate() throws Exception {

    }

    @Test
    public void displayCheckBoxes() throws Exception {

    }

    @Test
    public void getCheckBoxes() throws Exception {

    }

    @Test
    public void selectItems() throws Exception {

    }

    @Test
    public void onCreate1() throws Exception {

    }

    @Test
    public void setTheme() throws Exception {

    }

    @Test
    public void onPostCreate() throws Exception {

    }

}