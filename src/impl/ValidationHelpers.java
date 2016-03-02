package impl;

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
}
