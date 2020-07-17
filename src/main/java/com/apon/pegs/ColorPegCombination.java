package com.apon.pegs;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Class representing a combination of color pegs, where colors are represented as integers from 1 to N.
 * Order matters.
 */
public class ColorPegCombination extends ArrayList<Integer> {
    public ColorPegCombination(Collection<? extends Integer> c) {
        super(c);
    }
}
