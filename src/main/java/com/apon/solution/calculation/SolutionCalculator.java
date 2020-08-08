package com.apon.solution.calculation;

import com.apon.pegs.ColorPegCombination;
import com.apon.pegs.KeyPeg;
import com.apon.pegs.PegUtil;
import com.apon.solution.Guess;
import com.google.common.collect.Multiset;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;

/**
 * Calculate all the values needed for creating an implementation of {@link com.apon.solution.Solution}.
 */
public class SolutionCalculator {
    private final static Logger log = LogManager.getLogger(SolutionCalculator.class);

    /** The set of all possible key peg combinations, calculated once so for easy reuse. */
    private final Set<Multiset<KeyPeg>> allKeyPegCombinations;

    /** The set of all possible color peg combinations, calculated once for easy reuse. */
    private final Set<ColorPegCombination> allColorPegCombinations;

    // Start solution fields.
    private final int nrOfLocations;
    private final int nrOfColors;
    private final boolean useElvenarRules;
    private Guess firstGuess;
    private Map<Multiset<KeyPeg>, Guess> secondGuessMap;
    // End solution fields.

    public SolutionCalculator(int nrOfLocations, int nrOfColors, boolean useElvenarRules) {
        this.nrOfLocations = nrOfLocations;
        this.nrOfColors = nrOfColors;
        this.useElvenarRules = useElvenarRules;

        allKeyPegCombinations = PegUtil.determineAllPossibleKeyPegCombinations(nrOfLocations);
        allColorPegCombinations = PegUtil.calculateAllColorPegCombinations(nrOfColors, nrOfLocations);
    }

    /**
     * Calculate all the needed values for a solution.
     */
    public void calculateSolution() {
        // It has been mathematically proven that the best combination is the combination for which the worst case
        // key pegs to receive is the highest value for all combinations to choice. It is a lot more unreadable
        // if we would determine the chances while calculating the best solution. Since we don't need the chance to
        // determine to best combinations to pick, we just calculate all of these at the end.

        CombinationCalculator combinationCalculator = new CombinationCalculator(useElvenarRules, allKeyPegCombinations, allColorPegCombinations);
        ColorPegCombination firstCombination = combinationCalculator.calculateOptimalFirstCombination();
        Map<Multiset<KeyPeg>, ColorPegCombination> secondCombinationMap = combinationCalculator.calculateGuessCombinationMap();

        ChanceCalculator chanceCalculator = new ChanceCalculator(useElvenarRules, allKeyPegCombinations, allColorPegCombinations);
        Map<Multiset<KeyPeg>, Double> secondWinningChanceMap = chanceCalculator.calculateSecondWinningChanceMap(firstCombination, secondCombinationMap);

    }



    public String solutionAsString() {
        //noinspection StringBufferReplaceableByString
        StringBuilder stringBuilder = new StringBuilder();

        // Write the input fields.
        stringBuilder
                .append("Number of locations: ").append(nrOfLocations)
                .append("Number of colors   : ").append(nrOfColors)
                .append("Use elvenar rules  : ").append(useElvenarRules)
                .append("First guess: ").append(firstGuess)
                .append("Second guess map: ").append(secondGuessMap);

        // TODO: see if the guess map is displayed properly, otherwise I should do something fancy.
        return stringBuilder.toString();
    }
}
