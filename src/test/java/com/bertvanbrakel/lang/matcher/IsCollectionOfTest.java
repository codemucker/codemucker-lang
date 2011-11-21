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
package com.bertvanbrakel.lang.matcher;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.Vector;

import org.hamcrest.Matcher;
import org.junit.Test;

import com.bertvanbrakel.lang.matcher.IsCollectionOf;
import com.bertvanbrakel.lang.matcher.IsCollectionOf.CONTAINS;
import com.bertvanbrakel.lang.matcher.IsCollectionOf.ORDER;

public class IsCollectionOfTest {

    private static final Collection<Matcher<Object>> EMPTY_MATCHERS = Collections.unmodifiableCollection(new ArrayList<Matcher<Object>>());

    // basic checks (would be nice to have these auto generated)
    /**
     * Expect constructor args to be validated
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ctor_contains_is_null_throws_exception() {
        new IsCollectionOf<Object>(CONTAINS.ONLY, null);
    }

    /**
     * Expect constructor args to be validated
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ctor_order_is_null_throws_exception() {
        new IsCollectionOf<Object>(null, ORDER.EXACT);
    }

    /**
     * Expect constructor args to be validated
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_ctor_matcher_is_null_throws_exception() {
        new IsCollectionOf<Object>(null, CONTAINS.ONLY, ORDER.EXACT);
    }

    /**
     * Ensure we use the constructor args we pass in
     */
    @Test
    public void test_ctor_args_passed_in_are_used() {
        for(final ORDER order:ORDER.values()){
            for( final CONTAINS contains:CONTAINS.values()){
                final IsCollectionOf<Object> col = new IsCollectionOf<Object>(EMPTY_MATCHERS,contains, order);
                assertEquals(order, col.getOrder());
                assertEquals(contains, col.getContains());
            }
        }
    }

    /**
     * Given we have no matchers
     * and the item we pass in to match against is null
     * for each contains and order combination
     * we should fail
     */
    @Test
    public void test_no_matchers_null_list() {
        for(final ORDER order:ORDER.values()){
            for( final CONTAINS contains:CONTAINS.values()){
                assertFalse(new IsCollectionOf<Object>(EMPTY_MATCHERS,contains, order).matches(null));
            }
        }
    }

    /**
     * Given we have no matchers
     * and the item we pass in to match against is not a list
     * for each contains and order combination
     * we should fail
     */
    @Test
    public void test_no_matchers_not_a_collection() {
        for(final ORDER order:ORDER.values()){
            for( final CONTAINS contains:CONTAINS.values()){
                assertFalse(new IsCollectionOf<Object>(contains, order).matches(new Object()));
            }
        }
    }

    /**
     * Given we have no matchers
     * and the list we pass in is empty
     * for each contains and order combination
     * we should pass
     */
    @Test
    public void test_no_matchers_no_items_always_passes() {
        for( final Object emptyCol:getEmptyLists()){
            for(final ORDER order:ORDER.values()){
                for( final CONTAINS contains:CONTAINS.values()){
                    assertTrue(new IsCollectionOf<Object>(EMPTY_MATCHERS,contains, order).matches(emptyCol));
                }
            }
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
        for( final Collection<Object> col:getEmptyLists()){
            col.add("some string to make collection non empty");
            for(final ORDER order:ORDER.values()){
                 assertNoMatch(new IsCollectionOf<Object>(EMPTY_MATCHERS, CONTAINS.ONLY, order), col);
            }
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
        for( final Collection<Object> col:getEmptyLists()){
            col.add("some string to make collection non empty");
            for(final ORDER order:ORDER.values()){
                assertMatch(new IsCollectionOf<Object>(EMPTY_MATCHERS, CONTAINS.ALL, order), col);
            }
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
         @SuppressWarnings("unchecked")
        final List<Matcher<String>> matchers = Arrays.asList(equalTo("abc"),equalTo("123"),equalTo("xyz"));
        final Collection<Collection<String>> lists = getEmptyOrderPreservingLists();
        for( final Collection<String> list:lists){
            //ensure each list contains the items
            list.addAll(Arrays.asList("abc","123", "xyz"));
            for(final ORDER order:ORDER.values()){
                for( final CONTAINS contains:CONTAINS.values()){
                    assertMatch(new IsCollectionOf<String>(matchers, contains, order), list);
                }
            }
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
         @SuppressWarnings("unchecked")
        final List<Matcher<String>> matchers = Arrays.asList(equalTo("abc"),equalTo("123"),equalTo("xyz"));
        final Collection<Collection<String>> lists = getEmptyOrderPreservingLists();
        for( final Collection<String> list:lists){
            //ensure each list contains the items
            list.addAll(Arrays.asList("abc", "xyz", "123" ));
            for( final CONTAINS contains:CONTAINS.values()){
                assertNoMatch(new IsCollectionOf<String>(matchers, contains, ORDER.EXACT), list);
                assertMatch(new IsCollectionOf<String>(matchers, contains, ORDER.ANY), list);
            }
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
         @SuppressWarnings("unchecked")
        final List<Matcher<String>> matchers = Arrays.asList(equalTo("abc"),equalTo("123"),equalTo("xyz"));
        final Collection<Collection<String>> lists = getEmptyOrderPreservingLists();
        for( final Collection<String> list:lists){
            //ensure each list contains the items
            list.addAll(Arrays.asList("abc", "123" ));
            for(final ORDER order:ORDER.values()){
                for( final CONTAINS contains:CONTAINS.values()){
                    assertNoMatch(new IsCollectionOf<String>(matchers, contains, order), list);
                }
            }
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
         @SuppressWarnings("unchecked")
        final List<Matcher<String>> matchers = Arrays.asList(equalTo("abc"),equalTo("123"),equalTo("xyz"));
        final Collection<Collection<String>> lists = getEmptyOrderPreservingLists();
        for( final Collection<String> list:lists){
            //ensure each list contains the items
            list.addAll(Arrays.asList( "123","abc" ));
            for(final ORDER order:ORDER.values()){
                for( final CONTAINS contains:CONTAINS.values()){
                    assertNoMatch(new IsCollectionOf<String>(matchers, contains, order), list);
                }
            }
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
         @SuppressWarnings("unchecked")
        final List<Matcher<String>> matchers = Arrays.asList(equalTo("abc"),equalTo("123"));
        final Collection<Collection<String>> lists = getEmptyOrderPreservingLists();
        for( final Collection<String> list:lists){
            //ensure each list contains the items
            list.addAll(Arrays.asList("abc", "123", "xyz" ));
            for(final ORDER order:ORDER.values()){
                assertMatch(new IsCollectionOf<String>(matchers, CONTAINS.ALL, order), list);
                assertNoMatch(new IsCollectionOf<String>(matchers, CONTAINS.ONLY, order), list);
            }
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
         @SuppressWarnings("unchecked")
        final List<Matcher<String>> matchers = Arrays.asList(equalTo("abc"),equalTo("123"));
        final Collection<Collection<String>> lists = getEmptyOrderPreservingLists();
        for( final Collection<String> list:lists){
            //ensure each list contains the items
            list.addAll(Arrays.asList("123", "abc", "xyz" ));

            assertMatch(new IsCollectionOf<String>(matchers, CONTAINS.ALL, ORDER.ANY), list);

            assertNoMatch(new IsCollectionOf<String>(matchers, CONTAINS.ONLY, ORDER.ANY), list);
            assertNoMatch(new IsCollectionOf<String>(matchers, CONTAINS.ALL, ORDER.EXACT), list);
            assertNoMatch(new IsCollectionOf<String>(matchers, CONTAINS.ONLY, ORDER.EXACT), list);
        }
    }

    @Test
    public void test_containsItemsInAnyOrder() {
        assertOrderAndContains(IsCollectionOf.containsItemsInAnyOrder(EMPTY_MATCHERS),CONTAINS.ALL,ORDER.ANY);
    }

    @Test
    public void test_containsOnlyItemsInAnyOrder() {
        assertOrderAndContains(IsCollectionOf.containsOnlyItemsInAnyOrder(EMPTY_MATCHERS),CONTAINS.ONLY,ORDER.ANY);
    }

    @Test
    public void test_containsOnlyItemsInOrder() {
        assertOrderAndContains(IsCollectionOf.containsOnlyItemsInOrder(EMPTY_MATCHERS),CONTAINS.ONLY,ORDER.EXACT);
    }

    @Test
    public void test_containsItem() {
        assertOrderAndContains(IsCollectionOf.containsItem(equalTo("abc")),CONTAINS.ALL,ORDER.EXACT);
    }

    @Test
    public void test_containsOnlyItem() {
        assertOrderAndContains(IsCollectionOf.containsOnlyItem(equalTo("abc")),CONTAINS.ONLY,ORDER.EXACT);
    }

    private <T> void assertOrderAndContains(final Matcher<Iterable<T>> isCollectionOfMatcher, final CONTAINS contains, final ORDER order){
        final IsCollectionOf<T> test = (IsCollectionOf<T>) isCollectionOfMatcher;
        assertEquals(contains,test.getContains());
        assertEquals(order,test.getOrder());
    }
    
    @Test
    public void test_equals_matchers(){
    	
    	//expect
    	Collection<TstBean> expect = new ArrayList<TstBean>();
    	{
			TstBean bean1 = new TstBean();
			bean1.setFieldA("a");
			bean1.setFieldB("b");
			bean1.setFieldC("c");
			
			expect.add(bean1);

			TstBean bean2 = new TstBean();
			bean2.setFieldA("d");
			bean2.setFieldB("e");
			bean2.setFieldC("f");

			expect.add(bean2);
    	}
    	Matcher<Iterable<TstBean>> matcher = IsCollectionOf.builder(expect, new String[]{ "fieldC" }).create();
    	
    	//actual
    	Collection<TstBean> actual = new ArrayList<TstBean>();
    	{
			TstBean bean1 = new TstBean();
			bean1.setFieldA("a");
			bean1.setFieldB("b");
			bean1.setFieldC("c");
			
			actual.add(bean1);

			TstBean bean2 = new TstBean();
			bean2.setFieldA("d");
			bean2.setFieldB("e");
			bean2.setFieldC("XXX");

			actual.add(bean2);
    	
			assertTrue("matcher does not ignore ignore fields", matcher.matches(actual));
	    	
			bean2.setFieldA("different");
			assertFalse("matcher does not pick up field change", matcher.matches(actual));
    	}
    }
    
    private static class TstBean {
		private String fieldA;
		private String fieldB;
		private String fieldC;

		public String getFieldA() {
			return fieldA;
		}

		public void setFieldA(String fieldA) {
			this.fieldA = fieldA;
		}

		public String getFieldB() {
			return fieldB;
		}

		public void setFieldB(String fieldB) {
			this.fieldB = fieldB;
		}

		public String getFieldC() {
			return fieldC;
		}

		public void setFieldC(String fieldC) {
			this.fieldC = fieldC;
		}
    }

//      /**
//       * Matcher to assert a collection contains an item which matches the given matcher. Extra items are allowed
//       *
//       * @param <T>
//       * @param matcher
//       * @return
//       */
//      public static <T> Matcher<Collection<T>> containsItem(final Matcher<T> matcher) {
//          final ArrayList<Matcher<T>> matchers = new ArrayList<Matcher<T>>();
//          matchers.add(matcher);
//          return new IsCollectionOf<T>(matchers,CONTAINS.ALL,ORDER.ANY);
//      }
  //
//      /**
//       * Matcher to assert a collection contains only item which matches the given matcher. Extra items are not allowed
//       *
//       * @param <T>
//       * @param matcher
//       * @return
//       */
//      public static <T> Matcher<Collection<T>> containsOnlyItem(final Matcher<T> matcher) {
//          final ArrayList<Matcher<T>> matchers = new ArrayList<Matcher<T>>();
//          matchers.add(matcher);
//          return new IsCollectionOf<T>(matchers,CONTAINS.ONLY,ORDER.EXACT);
//      }
  //
  // */

    /**
     * Return various modifiable empty list implementations. Used to test various list types to ensure we
     * don't rely on any particular list type in comparisons
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> Collection<Collection<T>> getEmptyLists(){
        return Arrays.asList((Collection<T>)new ArrayList<T>(), new LinkedList<T>(), new Vector<T>(), new TreeSet<T>(), new HashSet<T>() );
    }

    /**
     * Return various modifiable empty list implementations which preserve order of inserted elements. Used to test various list types to ensure we
     * don't rely on any particular list type in comparisons
     *
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    private <T> Collection<Collection<T>> getEmptyOrderPreservingLists(){
        return Arrays.asList((Collection<T>)new ArrayList<T>(), new LinkedList<T>(), new Vector<T>());
    }

    private <T> void assertNoMatch(final IsCollectionOf<T> test, final Collection<T> colToTest) {
        assertFalse("Expected no match for contains:" + test.getContains() + ", order:" + test.getOrder() + ", col type " + colToTest.getClass().getName(), test.matches(colToTest));
    }

    private <T> void assertMatch(final IsCollectionOf<T> test, final Collection<T> colToTest){
        assertTrue("Expected match for contains:" + test.getContains() + ", order:" + test.getOrder() + ", col type " + colToTest.getClass().getName(), test.matches(colToTest));
    }
}
