package com.apon.solution;

import com.apon.pegs.ColorPegCombination;
import com.apon.pegs.KeyPeg;
import com.apon.pegs.PegUtil;
import com.google.common.collect.Multiset;

import java.util.Map;
import java.util.Set;

/**
 * Interface for solution classes of the mastermind game.
 */
public interface Solution {

    /**
     * Returns how many color pegs are used for each color peg combination.
     */
    int getNrOfLocations();

    /**
     * Returns how many colors are used in the game.
     */
    int getNrOfColors();

    /**
     * Returns {@code true} if the Elvenar rules are used. Returns {@code false} if the original Mastermind rules are used.
     */
    boolean useElvenarRules();

    /**
     * Returns the first combination and the winning chance.
     */
    Guess getFirstGuess();

    /**
     * Returns a map to determine the second combination and the winning chance, based on the key pegs of the first combination.
     */
    Map<Multiset<KeyPeg>, Guess> getSecondGuessMap();

    /**
     * Determine the final guess based on the key pegs of the first and second combinations.
     * @param firstKeyPegCombination  The key peg combination of the first color peg combination
     * @param secondKeyPegCombination The key peg combination of the second color peg combination
     * @return A random possible solution with the chance to win the game
     */
    default Guess determineFinalGuess(Multiset<KeyPeg> firstKeyPegCombination, Multiset<KeyPeg> secondKeyPegCombination) {
        ColorPegCombination firstCombination = getFirstGuess().getGuess();
        ColorPegCombination secondCombination = getSecondGuessMap().get(secondKeyPegCombination).getGuess();

        Set<ColorPegCombination> allSolutions = PegUtil.calculateAllColorPegCombinations(getNrOfColors(), getNrOfLocations());

        // Filter out all the possible solutions based on the first and second combination.
        Set<ColorPegCombination> solutionsAfterFirstGuess = PegUtil.selectCombinationsThatSatisfyGuess(useElvenarRules(), allSolutions, firstCombination, firstKeyPegCombination);
        Set<ColorPegCombination> solutionsAfterSecondGuess = PegUtil.selectCombinationsThatSatisfyGuess(useElvenarRules(), solutionsAfterFirstGuess, secondCombination, secondKeyPegCombination);

        return new Guess(solutionsAfterSecondGuess.iterator().next(), solutionsAfterSecondGuess.size() / (float) allSolutions.size());
    }
}
