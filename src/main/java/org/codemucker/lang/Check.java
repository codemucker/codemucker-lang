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

import java.util.Collection;

public final class Check {
	
	public static void checkFailed(String msg){
        throw new IllegalArgumentException(msg);
	}
	
    /**
     * Assert the given string is not null or blank where blank means the
     * trimmed string is zero length
     *
     * @param attName
     * @param value
     *
     * @throws NullPointerException
     *             if the value is null
     * @throws IllegalArgumentException
     *             if the value is blank (when trimmed length is zero)
     */
    public static void checkNotBlank(final String attName, final String value)
            throws NullPointerException, IllegalArgumentException {
        if (value == null) {
            throw new NullPointerException(String.format(
                    "Check failed. Expected non null value for '%s'", attName));
        } else if (value.trim().length() == 0) {
            throw new IllegalArgumentException(String.format(
                    "Check failed. Expected non blank value for '%s'", attName));
        }
    }

    /**
     * Assert the given string is not null or zero length. Does not trim the
     * string, see {@link #checkNotBlank(String, String)} instead
     *
     * @param attName
     * @param value
     *
     * @throws NullPointerException
     *             if the value is null
     * @throws IllegalArgumentException
     *             if the value is empty (zero length)
     */
    public static void checkNotEmpty(final String attName, final String value)
            throws NullPointerException, IllegalArgumentException {
        if (value == null) {
            throw new NullPointerException(String.format(
                    "Check failed. Expected non null value for '%s'", attName));
        } else if (value.length() == 0) {
            throw new IllegalArgumentException(String.format(
                    "Check failed. Expected non empty value for '%s'", attName));
        }
    }

    /**
     * Assert that the attribute with the given name is not null
     *
     * @param attName
     * @param value
     *
     * @throws NullPointerException
     *             if the value is null
     */
    public static <T> T checkNotNull(final String attName, final T value)
            throws NullPointerException {
        if (value == null) {
            throw new NullPointerException(String.format(
                    "Check failed. Expected non null value for '%s'", attName));
        }
        return value;
    }

    /**
     * Assert the attribute with the given name and value is not null and that
     * it starts with the given string
     *
     * @param attName
     * @param value
     * @param expectStartsWith
     *
     * @throws NullPointerException
     *             if the value is null
     * @throws IllegalArgumentException
     *             if the value is
     */
    public static void checkStartsWith(final String attName,
            final String value, final String expectStartsWith)
            throws NullPointerException, IllegalArgumentException {
        checkNotNull(attName, value);
        if (!value.startsWith(expectStartsWith)) {
            throw new IllegalArgumentException(
                    String.format(
                            "Check failed. Expected '%s' to start with '%s' but was '%s'",
                            attName, expectStartsWith, value));
        }
    }

    /**
     * Assert the list with the given name is not null and that all items in the
     * list are non null
     *
     * @param attName
     * @param list
     *
     * @throws NullPointerException
     *             if the list is null
     * @throws IllegalArgumentException
     *             if any of the items are null. Message includes position of item which was null
     */
    public static void checkNoNullItems(final String attName,
            final Collection<?> list) throws NullPointerException,
            IllegalArgumentException {
        checkNotNull(attName, list);
        int position = 0;
        for (final Object item : list) {

            if (item == null) {
                throw new IllegalArgumentException(
                        String.format(
                                "Check failed. Expected '%s' to not have any null items, but item %d (zero-based) was null",
                                attName, position));
            }
            position++;
        }
    }

    /**
     * Assert the array with the given name is not null and that all items in the
     * array are non null
     *
     * @param attName
     * @param array
     *
     * @throws NullPointerException
     *             if the array is null
     * @throws IllegalArgumentException
     *             if any of the items are null. Message includes position of item which was null
     */
    public static void checkNoNullItems(final String attName,
            final Object[] array) throws NullPointerException,
            IllegalArgumentException {
        checkNotNull(attName, array);
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null) {
                throw new IllegalArgumentException(
                        String.format(
                                "Check failed. Expected '%s' to not have any null items, but item %d (zero-based) was null",
                                attName, i));
            }
        }
    }

    public static void checkTrue(final String attName, final Object attValue, final boolean expression,
            final String msg) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(
                    "Check failed. Expected '%s' to be %s but was '%s'",
                    attName, msg, attValue));
        }
    }

}
