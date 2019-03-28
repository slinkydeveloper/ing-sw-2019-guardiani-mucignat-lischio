package com.adrenalinici.adrenaline.model;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class MyAssertions {

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
