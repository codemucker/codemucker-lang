/*
 * Copyright 2011 Bert van Brakel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("x","value");
        assertNull(interpolator.interpolate(null,new HashMap<String, Object>()));
        assertNull(interpolator.interpolate(null,vars));
    }

    @Test
    public void ensure_returns_orginal_input_when_using_null_and_empty_vars(){
        final Interpolator interpolator = new Interpolator();
        final Map<String, Object> vars = new HashMap<String, Object>();

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
        final Map<String, Object> vars = new HashMap<String, Object>();
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
