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
package org.codemucker.lang.matcher;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import org.codemucker.lang.matcher.AList;
import org.codemucker.lang.matcher.AnInstance;
import org.codemucker.lang.matcher.Matcher;
import org.junit.Test;

public class AListTest {
	
    /**
     * Given we have no matchers
     * and the item we pass in to match against is null
     * for each contains and order combination
     * we should fail
     */
    @Test
    public void test_no_matchers_null_list() {
    	assertFalse(AList.ofStrings().inAnyOrder().containing().matches(null));
    	assertFalse(AList.ofStrings().inAnyOrder().containingOnly().matches(null));
    	assertFalse(AList.ofStrings().inOrder().containing().matches(null));
    	assertFalse(AList.ofStrings().inOrder().containingOnly().matches(null));
    }

    /**
     * Given we have no matchers
     * and the list we pass in is empty
     * for each contains and order combination
     * we should pass
     */
    @Test
	public void test_no_matchers_no_items_always_passes() {
		for (final Collection<Object> list : getEmptyLists()) {
			assertTrue(AList.of(Object.class).inAnyOrder().containing().matches(list));
			assertTrue(AList.of(Object.class).inAnyOrder().containingOnly().matches(list));
			assertTrue(AList.of(Object.class).inOrder().containing().matches(list));
			assertTrue(AList.of(Object.class).inOrder().containingOnly().matches(list));
		}
	}

    /**
     * Given we have no matchers
     * and the list we pass in is non empty
     * and the contains enum is ONLY (need each item to have a matcher)
     * we should fail
     * as not each item has a matcher
     */
    @Test
    public void test_no_matchers_non_empty_list_always_fails() {
		for (final Collection<String> list : getEmptyStringLists()) {
	        list.add("myString");
	        assertFalse(AList.ofStrings().inAnyOrder().containingOnly().matches(list));
	    	assertFalse(AList.ofStrings().inOrder().containingOnly().matches(list));
		}
    }

    /**
     * Given we have no matchers
     * and the list we pass in is non empty
     * and the contains enum is ALL (need each matcher to match once)
     * we should pass
     * as we have nothing we need to match against
     */
    @Test
    public void test_no_matchers_non_empty_list_should_always_pass() {
    	for (final Collection<String> list : getEmptyStringLists()) {
    	    list.add("myString");
	        assertTrue(AList.ofStrings().inAnyOrder().containing().matches(list));
	        assertTrue(AList.ofStrings().inOrder().containing().matches(list));
    	}
    }

    /**
     * Given we have a matcher for each item in the list
     * and the list is in the same order to the matchers
     * then for any contains enum
     * and for any order enum
     * we should pass
     */
    @Test
    public void test_same_num_matchers_same_order_always_pass() {
         
        final Iterable<Matcher<String>> matchers = AnInstance.equalToAll("abc","123","xyz");
        final Collection<Collection<String>> lists = getEmptyOrderPreservingLists();
        for( final Collection<String> list:lists){
            //ensure each list contains the items
            list.addAll(asList("abc","123", "xyz"));
        	assertTrue(AList.ofStrings().inAnyOrder().containing().items(matchers).matches(list));
			assertTrue(AList.ofStrings().inAnyOrder().containingOnly().items(matchers).matches(list));
			assertTrue(AList.ofStrings().inOrder().containing().items(matchers).matches(list));
			assertTrue(AList.ofStrings().inOrder().containingOnly().items(matchers).matches(list));
        }
    }

    /**
     * Given we have a matcher for each item in the list
     * and the list is in a different order than the matchers
     * then for any contains flag
     * for ORDER.EXACT we should fail
     * for ORDER.ANY we should pass
     */
    @Test
    public void test_same_num_matchers_different_order() {
        final List<org.codemucker.lang.matcher.Matcher<String>> matchers = AnInstance.equalToAll("abc","123","xyz");
        final Collection<Collection<String>> lists = getEmptyOrderPreservingLists();
        for( final Collection<String> list:lists){
            //ensure each list contains the items
            list.addAll(asList("abc", "xyz", "123" ));

          	assertTrue(AList.ofStrings().inAnyOrder().containing().items(matchers).matches(list));
    		assertTrue(AList.ofStrings().inAnyOrder().containingOnly().items(matchers).matches(list));
    	
    		assertFalse(AList.ofStrings().inOrder().containing().items(matchers).matches(list));
    		assertFalse(AList.ofStrings().inOrder().containingOnly().items(matchers).matches(list));
        }
    }

    /**
     * Given we have more matchers than items in the list
     * and the list is in the same order as the matchers
     * then for any order flag
     * and any contains flag
     * we should fail
     * as there are not enough items to match
     */
    @Test
    public void test_more_matchers_same_order() {
        final List<Matcher<String>> matchers = AnInstance.equalToAll("abc","123","xyz");
        final Collection<Collection<String>> lists = getEmptyOrderPreservingLists();
        for( final Collection<String> list:lists){
            //ensure each list contains the items
            list.addAll(asList("abc", "123" ));
            
            assertFalse(AList.ofStrings().inAnyOrder().containing().items(matchers).matches(list));
          	assertFalse(AList.ofStrings().inAnyOrder().containingOnly().items(matchers).matches(list));
    		assertFalse(AList.ofStrings().inOrder().containing().items(matchers).matches(list));
    		assertFalse(AList.ofStrings().inOrder().containingOnly().items(matchers).matches(list));
        }
    }

    /**
     * Given we have more matchers than items in the list
     * and the list is in a different order to the matchers
     * then for contains and order combinations
     * we should always fail as we expect each matcher to be matched once
     */
    @Test
    public void test_more_matchers_different_order_always_fail() {
        final List<Matcher<String>> matchers = AnInstance.equalToAll("abc","123","xyz");
        final Collection<Collection<String>> lists = getEmptyOrderPreservingLists();
        for( final Collection<String> list:lists){
            //ensure each list contains the items
            list.addAll(asList( "123","abc" ));
            
            assertFalse(AList.ofStrings().inAnyOrder().containing().items(matchers).matches(list));
          	assertFalse(AList.ofStrings().inAnyOrder().containingOnly().items(matchers).matches(list));
    		assertFalse(AList.ofStrings().inOrder().containing().items(matchers).matches(list));
    		assertFalse(AList.ofStrings().inOrder().containingOnly().items(matchers).matches(list));
        }

    }

    /**
     * Given we have more items in the list than matchers
     * and the list is in a same order as the matchers
     * then for any order flag
     * for CONTAINS.ALL we should pass as each matcher is used once
     * for CONTAINS.ONLY we should fail as we can have no more items than matchers
     */
    @Test
    public void test_less_matchers_same_order() {
        final List<Matcher<String>> matchers = AnInstance.equalToAll("abc","123");
        final Collection<Collection<String>> lists = getEmptyOrderPreservingLists();
        for( final Collection<String> list:lists){
            //ensure each list contains the items
            list.addAll(asList("abc", "123", "xyz" ));

            assertTrue(AList.ofStrings().inAnyOrder().containing().items(matchers).matches(list));
    		assertTrue(AList.ofStrings().inOrder().containing().items(matchers).matches(list));
    		
    		assertFalse(AList.ofStrings().inAnyOrder().containingOnly().items(matchers).matches(list));
    		assertFalse(AList.ofStrings().inOrder().containingOnly().items(matchers).matches(list));
        }
    }

    /**
     * Given we have more items in the list than matchers
     * and the list is in a different order than the matchers
     * then for ORDER.ANY
     * for CONTAINS.ALL we should pass as each matcher is used once
     * for CONTAINS.ONLY we should fail as we can have no more items than matchers
     * then for ORDER.EXACT
     * should fail as the order of items must match
     */
    @Test
    public void test_less_matchers_different_order() {
        final List<Matcher<String>> matchers =AnInstance.equalToAll("abc","123");
        final Collection<Collection<String>> lists = getEmptyOrderPreservingLists();
        for( final Collection<String> list:lists){
            //ensure each list contains the items
            list.addAll(asList("123", "abc", "xyz" ));

            assertTrue(AList.ofStrings().inAnyOrder().containing().items(matchers).matches(list));
            
          	assertFalse(AList.ofStrings().inAnyOrder().containingOnly().items(matchers).matches(list));
    		assertFalse(AList.ofStrings().inOrder().containing().items(matchers).matches(list));
    		assertFalse(AList.ofStrings().inOrder().containingOnly().items(matchers).matches(list));
        }
    }

    private  Collection<Collection<String>> getEmptyStringLists(){
    	return getEmptyLists();
    }
    /**
     * Return various modifiable empty list implementations. Used to test various list types to ensure we
     * don't rely on any particular list type in comparisons
     *
     * @param <T>
     * @return
     */
    private <T> Collection<Collection<T>> getEmptyLists(){
        return asList((Collection<T>)new ArrayList<T>(), new LinkedList<T>(), new Vector<T>(), new TreeSet<T>(), new HashSet<T>() );
    }

    /**
     * Return various modifiable empty list implementations which preserve order of inserted elements. Used to test various list types to ensure we
     * don't rely on any particular list type in comparisons
     *
     * @param <T>
     * @return
     */
    private <T> Collection<Collection<T>> getEmptyOrderPreservingLists(){
        return asList((Collection<T>)new ArrayList<T>(), new LinkedList<T>(), new Vector<T>());
    }
}
