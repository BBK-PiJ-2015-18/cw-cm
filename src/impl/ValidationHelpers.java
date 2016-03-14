package impl;

import interfaces.Contact;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ValidationHelpers {
    public static void validateId(int id) {
        if(id <= 0) {
            throw new IllegalArgumentException("Id is 0 or negative");
        }
    }

    public static void validateNotNull(Object... params) {
        for (Object item : params) {
            if(item == null) {
                throw new NullPointerException();
            }
        }
    }

    public static void validateHasItem(Set set) {
        if (set == null || set.isEmpty()) {
            throw new IllegalArgumentException("Set contains no elements");
        }
    }

    public static void validateHasItem(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array contains no elements");
        }
    }

    public static void validateNotBeforeNow(Calendar date) {
        Calendar today = Calendar.getInstance();
        if(date.before(today)) {
            throw new IllegalArgumentException("Date is not in the future.");
        }
    }

    public static void validateContainsAll(List expected, Set actual) {
        if(expected.isEmpty() || actual.isEmpty()) {
            throw new IllegalArgumentException("Set is empty");
        }

        for (Object item : actual) {
            if(!expected.contains(item)) {
                throw new IllegalArgumentException("Item not found in expected set");
            }
        }
    }

    public static void validateContains(List list, Object obj) {
        if(!list.contains(obj)) {
            throw new IllegalArgumentException("List does not contain object");
        }
    }

    public static void validateBeforeNow(Calendar date) {
        Calendar today = Calendar.getInstance();
        if(!date.before(today)) {
            throw new IllegalArgumentException("Date is in the future.");
        }
    }
}
