package impl;

import java.util.Calendar;
import java.util.List;
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

    public static void validateBeforeToday(Calendar date) {
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
}
