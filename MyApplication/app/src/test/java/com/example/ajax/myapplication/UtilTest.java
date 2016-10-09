package com.example.ajax.myapplication;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.regex.PatternSyntaxException;

import static org.junit.Assert.assertEquals;

public class UtilTest {
    @Mock
    Util util;

    @Before
    public void init() {
        util = Mockito.spy(new Util());
    }

    @Test
    public void matchTest() {
        assertEquals(false, util.match("Hello", "\\W"));
        assertEquals(true, util.match("Hello", "\\w+"));
    }

    @Test
    public void macthCountTest() {
        assertEquals(2, util.matchCount("Hello", "l"));
    }

    @Test(expected = PatternSyntaxException.class)
    public void patternErrorTest() {
        assertEquals(2, util.matchCount("Hello", "[]"));
    }
}
