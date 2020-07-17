package com.apon.pegs;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.Arrays;

public class KeyPegTestUtil {

    public static Multiset<KeyPeg> of(KeyPeg... keyPegs) {
        return HashMultiset.create(Arrays.asList(keyPegs));
    }
}
