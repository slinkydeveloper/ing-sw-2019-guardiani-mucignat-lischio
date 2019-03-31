package com.adrenalinici.adrenaline.model;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class MyAssertions {

    public static <T> void assertAbsent(Optional<T> optional) {
        assertFalse(optional.isPresent());
    }

    public static <T> void assertPresent(Optional<T> optional) {
        assertTrue(optional.isPresent());
    }

    public static void assertInstanceOf(Class<?> expected, Object actual) {
        assertEquals(expected, actual.getClass());
    }

    public static <T> void assertListEqualsWithoutOrdering(List<T> expected, List<T> actual) {
        assertEquals(new HashSet<>(expected), new HashSet<>(actual));
    }

    public static <T> void assertContainsExactly(T expectedColor, int numberOfOccourences, List<T> colors) {
        long count = colors.stream().filter(expectedColor::equals).count();
        assertEquals(numberOfOccourences, count);
    }

    public static <T extends Throwable> void assertThatCodeThrowsExceptionOfType(Runnable code, Class<T> exceptionType) {
        try {
            code.run();
        } catch (Throwable e) {
            assertEquals(exceptionType, e.getClass());
            return;
        }
        fail("Exception of type " + exceptionType.getName() + " must be thrown");
    }

}
