package com.bertvanbrakel.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Converter {

    /**
     * Convert an array into a list. If the array is null return an empty
     * list
     *
     * @param <E>
     * @param array
     * @return
     */
    public static <E> List<E> toListEmptyIfNull(final E[] array) {
        if (array == null) {
            return new ArrayList<E>();
        } else {
            return Arrays.asList(array);
        }
    }
}
