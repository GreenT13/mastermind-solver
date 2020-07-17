package com.apon.pegs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Class representing a combination of color pegs, where colors are represented as integers from 1 to N.
 * Order matters.
 */
public class ColorPegCombination extends ArrayList<Integer> {
    public static final ColorPegCombination EMPTY = new ColorPegCombination(Collections.emptyList());

    public static ColorPegCombination of(Integer... elements) {
        return new ColorPegCombination(Arrays.asList(elements));
    }

    public ColorPegCombination(Collection<? extends Integer> c) {
        super(c);
    }
}
