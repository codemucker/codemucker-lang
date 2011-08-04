package com.bertvanbrakel.lang.matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.generator.qdox.model.util.OrderedMap;

/**
 * Matcher to match on a collections of objects. Configurable to take into account element order
 * and/or whether we ignore additional list items or not
 *
 * @author Bert van Brakel
 *
 * @param <T> the type of item in the list
 */
public class IsCollectionOf<T> extends
        TypeSafeMatcher<Iterable<T>> {
    private final Collection<Matcher<T>> matchers;
    private final ORDER order;
    private final CONTAINS contains;

    public static enum ORDER {
        /**
         * Exact order. Elements must be in the same order as the matcher
         */
        EXACT,
        /**
         * Any order. Elements can be in any order
         */
        ANY;
    }

    public static enum CONTAINS {
        /**
         * All matchers have to match. Additional elements are allowed. One matcher per item
         */
        ALL,
        /**
         * All matchers have to match, and no additional elements are allowed. One matcher per item
         */
        ONLY;
    }

    public IsCollectionOf(final CONTAINS contains, final ORDER order) {
        this(new ArrayList<Matcher<T>>(), contains, order);
    }
    
    public IsCollectionOf(final Iterable<Matcher<T>> matchers,
            final CONTAINS contains, final ORDER order) {
        if (matchers == null) {
            throw new IllegalArgumentException("Must supply non null matchers");
        }
        if (contains == null) {
            throw new IllegalArgumentException("Must supply non null strictness. One of " + Arrays.toString(CONTAINS.values()) );
        }
        if (order == null) {
            throw new IllegalArgumentException("Must supply non null order. One of " + Arrays.toString(ORDER.values()) );
        }
        final Collection<Matcher<T>> col = new ArrayList<Matcher<T>>();
        for( final Matcher<T> m:matchers){
            col.add(m);
        }
        this.matchers = col;//new ArrayList<Matcher<T>>(matchers);
        this.contains = contains;
        this.order = order;
    }

    /**
     * Add a matcher to match items on
     *
     * @param matcher
     */
    public void add(final Matcher<T> matcher) {
        this.matchers.add(matcher);
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

    /**
     * Matcher to assert a collection contains items which match the given matchers. Order is irrelevant, extra items are allowed
     *
     * @param <T>
     * @param matchers
     * @return
     */
    public static <T> Matcher<Iterable<T>> containsItemsInAnyOrder(final Iterable<Matcher<T>> expect) {
        return new IsCollectionOf<T>(expect,CONTAINS.ALL,ORDER.ANY);
    }

    /**
     * Matcher to assert a collection contains only items which match the given matchers. Order is irrelevant, extra items are not allowed
     *
     * @param <T>
     * @param matchers
     * @return
     */
    public static <T> Matcher<Iterable<T>> containsOnlyItemsInAnyOrder(final Iterable<Matcher<T>> matchers) {
        return new IsCollectionOf<T>(matchers,CONTAINS.ONLY,ORDER.ANY);
    }

    /**
     * Matcher to assert a collection contains only items which match the given matchers in the order the matchers are given. Extra items are not allowed
     *
     * @param <T>
     * @param matchers
     * @return
     */
    public static <T> Matcher<Iterable<T>> containsOnlyItemsInOrder(final Iterable<Matcher<T>> matchers) {
        return new IsCollectionOf<T>(matchers,CONTAINS.ONLY,ORDER.EXACT);
    }

    /**
     * Matcher to assert a collection contains an item which matches the given matcher. Extra items are allowed
     *
     * @param <T>
     * @param matcher
     * @return
     */
    public static <T> Matcher<Iterable<T>> containsItem(final Matcher<T> matcher) {
        final ArrayList<Matcher<T>> matchers = new ArrayList<Matcher<T>>();
        matchers.add(matcher);
        return new IsCollectionOf<T>(matchers,CONTAINS.ALL,ORDER.EXACT);
    }

    /**
     * Matcher to assert a collection contains only item which matches the given matcher. Extra items are not allowed
     *
     * @param <T>
     * @param matcher
     * @return
     */
    public static <T> Matcher<Iterable<T>> containsOnlyItem(final Matcher<T> matcher) {
        final ArrayList<Matcher<T>> matchers = new ArrayList<Matcher<T>>();
        matchers.add(matcher);
        return new IsCollectionOf<T>(matchers,CONTAINS.ONLY,ORDER.EXACT);
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

    public ORDER getOrder() {
        return order;
    }

    public CONTAINS getContains() {
        return contains;
    }
    
    public static <T> IsCollectionBuilder<T> builder(T item){
    	return new IsCollectionBuilder<T>().match(item);
    }
    
    public static <T> IsCollectionBuilder<T> builder(T item, String[] excludeFields){
    	return new IsCollectionBuilder<T>().matchExcludeFields(item, excludeFields);
    }
    
    public static <T> IsCollectionBuilder<T> builder(Iterable<T> items, String[] excludeFields){
    	return new IsCollectionBuilder<T>().matchExcludeFields(items, excludeFields);
    }
    
    
    public static class IsCollectionBuilder<T> {
    	private ORDER order = ORDER.EXACT;
    	private CONTAINS contains = CONTAINS.ONLY;
    	private Collection<Matcher<T>> matchers = new ArrayList<Matcher<T>>();
    	
    	public IsCollectionBuilder<T> order(ORDER order){
    		this.order = order;
    		return this;
    	}
    	
    	public IsCollectionBuilder<T> contains(CONTAINS contains){
    		this.contains = contains;
    		return this;
    	}
    	
    	public IsCollectionBuilder<T> matcher(Matcher<T> matcher){
    		this.matchers.add(matcher);
    		return this;
    	}
    	
    	public IsCollectionBuilder<T> match(T item){
    		this.matchers.add(new EqualsMatcher<T>(item));
    		return this;
    	}
    	
    	public IsCollectionBuilder<T> matchExcludeFields(T item, String[] excludeFields){
    		this.matchers.add(new EqualsMatcher<T>(item,excludeFields));
    		return this;
    	}
    	
		public IsCollectionBuilder<T> matchExcludeFields(Iterable<T> items, String[] excludeFields) {
			for (T item : items) {
				this.matchers.add(new EqualsMatcher<T>(item, excludeFields));
			}
			return this;
		}
    	
    	public Matcher<Iterable<T>> create(){
    		return new IsCollectionOf<T>(matchers, contains, order);
    	}
    	 
    }
}
