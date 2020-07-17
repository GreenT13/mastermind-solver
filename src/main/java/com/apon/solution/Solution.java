package com.apon.solution;

import com.apon.pegs.ColorPegCombination;
import com.apon.pegs.KeyPeg;
import com.apon.pegs.PegUtil;
import com.google.common.collect.Multiset;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * @param firstGuessKeyPegs  The key pegs of the first guess.
     * @param secondGuessKeyPegs The key pegs of the second guess.
     * @return A random possible solution with the chance to win the game.
     */
    default Guess determineFinalGuess(Multiset<KeyPeg> firstGuessKeyPegs, Multiset<KeyPeg> secondGuessKeyPegs) {
        ColorPegCombination firstGuessCombination = getFirstGuess().getGuess();
        ColorPegCombination secondGuessCombination = getSecondGuessMap().get(secondGuessKeyPegs).getGuess();

        List<ColorPegCombination> allSolutions = PegUtil.calculateAllPossibleSolutions(getNrOfColors(), getNrOfLocations());

        // Filter out all the possible solutions based on the first and second combination.
        List<ColorPegCombination> possibleSolutions = allSolutions.stream()
                .filter(PegUtil.selectValidSolutionsBasedOn(firstGuessCombination, firstGuessKeyPegs, useElvenarRules()))
                .filter(PegUtil.selectValidSolutionsBasedOn(secondGuessCombination, secondGuessKeyPegs, useElvenarRules()))
                .collect(Collectors.toList());

        return new Guess(possibleSolutions.get(0), possibleSolutions.size() / (float) allSolutions.size());
    }
}
