package com.apon.pegs;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import one.util.streamex.StreamEx;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PegUtil {
    public static Set<Multiset<KeyPeg>> determineAllPossibleKeyPegCombinations(int nrOfLocations) {
        // Calculate all possible combinations of the possible answers using the cartesian product X^n, where
        // X is the set of all possible pin answers and n is the number of locations.
        // Some combinations are duplicate, as order doesn't matter. So we map everything into a set and we are done.
        List<List<KeyPeg>> cartesianProductSource = Collections.nCopies(nrOfLocations, Arrays.asList(KeyPeg.values()));

        return StreamEx.cartesianProduct(cartesianProductSource)
                .map(HashMultiset::create)
                .collect(Collectors.toSet());
    }

    /**
     * Calculates all the possible color peg combinations.
     * @param nrOfColors    The number of colors used
     * @param nrOfLocations The number of locations used
     */
    public static Set<ColorPegCombination> calculateAllColorPegCombinations(int nrOfLocations, int nrOfColors) {
        // Create a list from 1 to N where N is the number of colors.
        List<Integer> allColors = IntStream.rangeClosed(1, nrOfColors).boxed().collect(Collectors.toList());

        // Calculate all possible combinations of the possible answers using the cartesian product X^n, where
        // X is the set of all possible colors and n is the number of locations.
        List<List<Integer>> cartesianProductSource = Collections.nCopies(nrOfLocations, allColors);

        return StreamEx.cartesianProduct(cartesianProductSource)
                .map(ColorPegCombination::new)
                .toSet();
    }

    public static Multiset<KeyPeg> determineKeyPegCombination(ColorPegCombination guess, ColorPegCombination solution, boolean useElvenarRules) {
        // TODO: implement Elvenar ruleset here.

        // The number of white pegs must be equal to the exact number of matches.
        int nrOfWhitePegs = 0;
        for (int i = 0; i < guess.size(); i++) {
            if (guess.get(i).equals(solution.get(i))) {
                nrOfWhitePegs++;
            }
        }

        // The total number of pegs equals the number of matching colors.
        // So the number of black pegs can be calculated.
        int totalNrOfPegs = 0;
        // Clone the list to not touch the given object.
        ColorPegCombination removable = (ColorPegCombination) solution.clone();
        for (Integer color : guess) {
            if (removable.contains(color)) {
                removable.remove(color);
                totalNrOfPegs++;
            }
        }

        Multiset<KeyPeg> keyPegCombination = HashMultiset.create();
        keyPegCombination.add(KeyPeg.WHITE, nrOfWhitePegs);
        keyPegCombination.add(KeyPeg.BLACK, totalNrOfPegs - nrOfWhitePegs);
        keyPegCombination.add(KeyPeg.NONE, guess.size() - totalNrOfPegs);

        return keyPegCombination;
    }

    /**
     * Returns all the solutions that satisfy the given color peg combination with key pegs.
     * @param useElvenarRules      Determines the rule set
     * @param possibleCombinations All the possible combinations to filter
     * @param colorPegCombination  The color peg combination
     * @param keyPegCombination    The key peg combination
     */
    public static Set<ColorPegCombination> selectCombinationsThatSatisfyGuess(boolean useElvenarRules, Set<ColorPegCombination> possibleCombinations,
                                                                              ColorPegCombination colorPegCombination, Multiset<KeyPeg> keyPegCombination) {
        return StreamEx.of(possibleCombinations)
                .filter((solution) -> PegUtil.determineKeyPegCombination(colorPegCombination, solution, useElvenarRules).equals(keyPegCombination))
                .toSet();
    }
}
