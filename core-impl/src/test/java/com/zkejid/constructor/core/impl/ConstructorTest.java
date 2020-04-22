package com.zkejid.constructor.core.impl;

import com.zkejid.constructor.core.api.v1.EntryPoint;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicReference;

class ConstructorTest {

    /**
     * Check if it is possible to pass null arguments.
     * <p>
     * Constructor doesn't verify input arguments. It just pass arguments as is from caller to entry point.
     */
    @Test
    void main_nullArgs_success() {
        final AtomicReference<String[]> actualArgs = new AtomicReference<>(new String[]{});
        final Constructor constructor = new Constructor(
                () -> Collections.singletonList(
                        new CustomPart(
                                Collections.emptySet(),
                                Collections.singleton(EntryPoint.class),
                                Collections.singletonMap(EntryPoint.class, (EntryPoint) actualArgs::set)
                        )
                )
        );

        constructor.main(null);

        Assertions.assertNull(actualArgs.get());
    }

    /**
     * Check if it is possible to pass arguments to the application.
     * <p>
     * Constructor doesn't verify input arguments. It just pass arguments as is from caller to entry point.
     */
    @Test
    void main_notNullArgs_success() {
        final String[] expected = {"a"};
        final AtomicReference<String[]> actualArgs = new AtomicReference<>(null);
        final Constructor constructor = new Constructor(
                () -> Collections.singletonList(
                        new CustomPart(
                                Collections.emptySet(),
                                Collections.singleton(EntryPoint.class),
                                Collections.singletonMap(EntryPoint.class, (EntryPoint) actualArgs::set)
                        )
                )
        );

        constructor.main(expected);

        Assertions.assertSame(expected, actualArgs.get());
    }

    @Test
    void main_providesTwoParts_success() {
        final Constructor constructor = new Constructor(
                () -> Arrays.asList(
                        new CustomPart(
                                Collections.emptySet(),
                                Collections.emptySet(),
                                Collections.emptyMap()
                        ),
                        new CustomPart(
                                Collections.emptySet(),
                                Collections.emptySet(),
                                Collections.emptyMap()
                        )
                )
        );
        constructor.main(new String[] {});
    }
}