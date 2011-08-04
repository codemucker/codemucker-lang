package com.bertvanbrakel.lang;

import org.junit.Test;

import com.bertvanbrakel.lang.Check;

public class CheckTest {

    @Test(expected = NullPointerException.class)
    public void test_isNotNull_throws_npe_on_null() {
        Check.checkNotNull("myattname", null);
    }

    @Test
    public void test_isNotNull_happy_path() {
        Check.checkNotNull("myattname", new Object());
    }

    @Test(expected = NullPointerException.class)
    public void test_startsWith_throws_npe_on_null() {
        Check.checkStartsWith("myattname", null, "/");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_startsWith_throws_iae_on_no_match() {
        Check.checkStartsWith("myattname", "abc", "/");
    }

    @Test
    public void test_startsWith_happy_path() {
        Check.checkStartsWith("myattname", "/abc", "/");
    }

}
