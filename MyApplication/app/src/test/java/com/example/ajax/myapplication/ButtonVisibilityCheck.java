package com.example.ajax.myapplication;


import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ButtonVisibilityCheck {
    @InjectMocks
    private MainActivity mActivity;
    @Mock
    private MyObject mockObject;

    @Before
    public void initialize() {
        mockObject = Mockito.spy(new MyObject());
        mActivity = Robolectric.buildActivity(MainActivity.class).create().get();
        Whitebox.setInternalState(mActivity, "object", mockObject);
    }

    @Test
    public void testVisibility() {
        Mockito.when(mockObject.isVisible()).thenReturn(View.VISIBLE);
        mActivity.updateVisibility();
        assertEquals(View.VISIBLE, mActivity.findViewById(R.id.btn_test).getVisibility());
    }

    @Test
    public void testInvisibilityVisibility() {
        Mockito.when(mockObject.isVisible()).thenReturn(View.INVISIBLE);
        mActivity.updateVisibility();
        assertEquals(View.INVISIBLE, mActivity.findViewById(R.id.btn_test).getVisibility());
    }
}
