package com.bertvanbrakel.lang.interpolator;

import static  com.bertvanbrakel.lang.Check.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.*;
import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.junit.matchers.JUnitMatchers.*;

public class InterpolatorTest {

    @Test
    public void ensure_can_handle_null_input_and_null_map(){
        final Interpolator interpolator = new Interpolator();
        assertNull(interpolator.interpolate(null,null));
    }

    @Test
    public void ensure_returns_null_with_null_input(){
        final Interpolator interpolator = new Interpolator();
        final Map<String, String> vars = new HashMap<String, String>();
        vars.put("x","value");
        assertNull(interpolator.interpolate(null,new HashMap<String, String>()));
        assertNull(interpolator.interpolate(null,vars));
    }

    @Test
    public void ensure_returns_orginal_input_when_using_null_and_empty_vars(){
        final Interpolator interpolator = new Interpolator();
        final Map<String, String> vars = new HashMap<String, String>();

        assertEquals("", interpolator.interpolate("",null));
        assertEquals("", interpolator.interpolate("",vars));

        assertEquals("abc", interpolator.interpolate("abc",null));
        assertEquals("abc", interpolator.interpolate("abc",vars));


        assertEquals("alice likes ${food}", interpolator.interpolate("alice likes ${food}",null));
        assertEquals("alice likes ${food}", interpolator.interpolate("alice likes ${food}",vars));
    }

    @Test
    public void ensure_correctly_interpolates_input(){
        final Interpolator interpolator = new Interpolator();
        final Map<String, String> vars = new HashMap<String, String>();
        vars.put("food","apples");
        vars.put("and","oranges");

        assertEquals("", interpolator.interpolate("",vars));
        assertEquals("alice likes apples", interpolator.interpolate("alice likes ${food}",vars));
        assertEquals("alice likes apples and oranges", interpolator.interpolate("alice likes ${food} and ${and}",vars));
        assertEquals("alice likes apples and oranges and ${undefined}", interpolator.interpolate("alice likes ${food} and ${and} and ${undefined}",vars));
        assertEquals("alice likes ${food", interpolator.interpolate("alice likes ${food",vars));
        assertEquals("alice likes $apples", interpolator.interpolate("alice likes $${food}",vars));
        assertEquals("alice likes ${apples}", interpolator.interpolate("alice likes ${${food}}",vars));
        assertEquals("alice likes ${apples$}", interpolator.interpolate("alice likes ${${food}$}",vars));
    }


}
