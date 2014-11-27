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
package org.codemucker.lang;

import org.codemucker.lang.Check;
import org.junit.Test;


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
