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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.bertvanbrakel.lang.annotation.ThreadSafe;

/**
 * Matcher to match on a collections of objects. Configurable to take into account element order
 * and/or whether we ignore additional list items or not.
 * 
 * Typical usage:
 * 
 * AList.of(String.class).inAnyOrder().containingOnly().a(myMatcher).a(myMatcher2)
 * 
 * Thread safe if no modifications are made to the matcher once it's been handed off to the various threads
 *
 * @author Bert van Brakel
 *
 * @param <T> the type of item in the list
 */
public class AList<T> {

	private AList(){
		//prevent instantiation
	}
	
	public static AList<Object> ofObjects(){
		return of(Object.class);
	}
	
	public static AList<String> ofStrings(){
		return of(String.class);
	}
	
	public static <T> AList<T> of(Class<T> itemType){
		return new AList<T>();
	}
	
	/**
	 * A matcher which expects a list with only a single matching item
	 * @return
	 */
	public static <T> Matcher<Iterable<T>> ofOnly(Matcher<T> matcher){
		return new AList<T>().inOrder().containingOnly().item(matcher);
	}

	/**
	 * A matcher which expects an empty list
	 * @return
	 */
	public Matcher<Iterable<T>> nothing(){
		return new EmptyMatcher<T>();
	}
	
	/**
	 * A list match builder for a list where item order is important
	 * @return
	 */
	public InOrder<T> inOrder(){
		return new InOrder<T>();
	}
	
	/**
	 * A list match builder for a list where item order is not important
	 * @return
	 */
	public AnyOrder<T> inAnyOrder(){
		return new AnyOrder<T>();
	}
	
	public static class InOrder<T> {
		
		/**
		 * All matchers must match but additional items are ignored
		 * @return
		 */
		public ListMatcher<T> containing(){
			return new ListMatcher<T>(ListMatcher.CONTAINS.ALL,ListMatcher.ORDER.EXACT);
		}
		
		/**
		 * All items must be matches. Additional non matched items will cause a fail
		 * @return
		 */
		public ListMatcher<T> containingOnly(){
			return new ListMatcher<T>(ListMatcher.CONTAINS.ONLY,ListMatcher.ORDER.EXACT);
		}
	}
	
	public static class AnyOrder<T>{
		
		/**
		 * All matchers must match but additional items are ignored
		 * @return
		 */
		public ListMatcher<T> containing(){
			return new ListMatcher<T>(ListMatcher.CONTAINS.ALL,ListMatcher.ORDER.ANY);
		}
		
		/**
		 * All items must be matches. Additional non matched items will cause a fail
		 * @return
		 */
		public ListMatcher<T> containingOnly(){
			return new ListMatcher<T>(ListMatcher.CONTAINS.ONLY,ListMatcher.ORDER.ANY);
		}
	}
	
	private static class EmptyMatcher<T> extends TypeSafeMatcher<Iterable<T>>
	{
		@Override
		public void describeTo(Description desc) {
	        desc.appendText("is empty");
		}

		@Override
		public boolean matchesSafely(Iterable<T> actual) {
			if( actual instanceof Collection){
				return ((Collection<T>)actual).size() == 0;
			}
			return !actual.iterator().hasNext();
		}
	}
	
	@ThreadSafe(caveats="if no matchers are added once it's been handed off to the various threads")
	public static class ListMatcher<T> extends TypeSafeMatcher<Iterable<T>> 
	{
	    private final Collection<Matcher<T>> matchers = new ArrayList<Matcher<T>>();
	    private final ORDER order;
	    private final CONTAINS contains;
	
	    private static enum ORDER {
	        /**
	         * Exact order. Elements must be in the same order as the matcher
	         */
	        EXACT,
	        /**
	         * Any order. Elements can be in any order
	         */
	        ANY;
	    }
	
	    private static enum CONTAINS {
	        /**
	         * All matchers have to match. Additional elements are allowed. One matcher per item
	         */
	        ALL,
	        /**
	         * All matchers have to match, and no additional elements are allowed. One matcher per item
	         */
	        ONLY;
	    }
	
	    private ListMatcher(final CONTAINS contains, final ORDER order) {
	        if (contains == null) {
	            throw new IllegalArgumentException("Must supply non null strictness. One of " + Arrays.toString(CONTAINS.values()) );
	        }
	        if (order == null) {
	            throw new IllegalArgumentException("Must supply non null order. One of " + Arrays.toString(ORDER.values()) );
	        }
	        this.contains = contains;
	        this.order = order;
	    }
	    
	    public ListMatcher<T> items(final Iterable<Matcher<T>> matchers) {
	        for( Matcher<T>matcher:matchers){
	        	this.matchers.add(matcher);
	        }
	        return this;
	    }
	    
	    /**
	     * Add a matcher. Synonym for {@link #add(Matcher)}
	     *
	     * @param matcher
	     */
	    public ListMatcher<T> item(final Matcher<T> matcher) {
	        add(matcher);
	        return this;
	    }
	    
	    /**
	     * Add a matcher
	     *
	     * @param matcher
	     */
	    public ListMatcher<T> add(final Matcher<T> matcher) {
	        this.matchers.add(matcher);
	        return this;
	    }
	
	    @Override
	    public boolean matchesSafely(final Iterable<T> actual) {
	        // loop though each item in the actual list, and find a matching
	        // matcher. If all the matchers
	        // have passed once, then we have no more checks to perform
	        final List<Matcher<T>> matchersLeft = new ArrayList<Matcher<T>>(matchers);
	        int actualCount=0;
	        switch (order) {
	        case ANY:
	            // in any order
	            for (final T actualItem : actual) {
	                actualCount++;
	                // since order is not important, lets find the first matcher
	                // which matches
	                matcher:for (final Iterator<Matcher<T>> iter = matchersLeft.iterator(); iter
	                        .hasNext();) {
	                    final Matcher<T> matcher = iter.next();
	                    if (matcher.matches(actualItem)) {
	                        // we've used the matcher, lets not use it again
	                        iter.remove();
	                        break matcher;
	                    }
	                }
	            }
	            break;
	        case EXACT:
	            // in the same order as the matchers
	            for (final T actualItem : actual) {
	                actualCount++;
	                // ensure we have matchers left
	                if (matchersLeft.size() == 0) {
	                    break;
	                }
	                // always use the first matcher as this should match first.
	                if (matchersLeft.get(0).matches(actualItem)) {
	                    matchersLeft.remove(0);
	                }
	            }
	            break;
	        }
	        //don't expect any more items than matchers for CONTAINS.ONLY
	        if( CONTAINS.ONLY == contains && matchers.size() != actualCount){
	            return false;
	        }
	
	        // if zero means each matcher has matched once
	        return matchersLeft.size() == 0;
	    }
	    
	    @Override
	    public void describeTo(final Description desc) {
	        desc.appendText("contains " );
	        desc.appendText( contains.toString().toLowerCase());
	        desc.appendText( " in " );
	        desc.appendText( order.toString().toLowerCase());
	        desc.appendText( " order " );
	        desc.appendList("[", ",", "]", matchers);
	    }
	}
}
