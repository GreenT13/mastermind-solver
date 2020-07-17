package com.apon.solution.calculation;

import com.apon.pegs.ColorPegCombination;
import com.apon.pegs.KeyPeg;
import com.apon.pegs.PegUtil;
import com.google.common.collect.Multiset;
import one.util.streamex.StreamEx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class CombinationCalculator {
    private static final Logger log = LogManager.getLogger(CombinationCalculator.class);

    private final boolean useElvenarRules;
    final Set<Multiset<KeyPeg>> allKeyPegCombinations;
    final Set<ColorPegCombination> allColorPegCombinations;

    /** Result of {@link #calculateOptimalFirstCombination()} stored for easy reuse. */
    private ColorPegCombination optimalFirstCombination;

    /**
     * Creates an instance, where you need to supply pre-calculated sets.
     * @param useElvenarRules         If Elvenar rules should be used
     * @param allKeyPegCombinations   A set containing all the possible key peg combinations
     * @param allColorPegCombinations A set containing all the possible color peg combinations
     */
    public CombinationCalculator(boolean useElvenarRules, Set<Multiset<KeyPeg>> allKeyPegCombinations, Set<ColorPegCombination> allColorPegCombinations) {
        this.useElvenarRules = useElvenarRules;
        this.allKeyPegCombinations = allKeyPegCombinations;
        this.allColorPegCombinations = allColorPegCombinations;
    }

    /**
     * Calculates the optimal first combination.
     */
    public ColorPegCombination calculateOptimalFirstCombination() {
        // Use the generic calculation of the optimal solution on the complete solution set.
        optimalFirstCombination = calculateOptimalCombination(allColorPegCombinations);
        return optimalFirstCombination;
    }

    /**
     * Calculates a map with optimal combinations for each possible key keg combination for the first combination.
     */
    public Map<Multiset<KeyPeg>, ColorPegCombination> calculateGuessCombinationMap() {
        // Create a map where the keys are the key pegs and the values are the calculated optimal solutions using the
        // fact that the key is the result of the first combination.
        return StreamEx.of(allKeyPegCombinations)
                .mapToEntry(this::calculateOptimalSecondCombination)
                .toMap();
    }

    /**
     * Calculates the optimal second combination based on the information of the first combination with key pegs.
     * @param keyPegCombination The key pegs of the first combination
     */
    private ColorPegCombination calculateOptimalSecondCombination(Multiset<KeyPeg> keyPegCombination) {
        // Calculate the optimal combination, just on a smaller set of solutions.
        Set<ColorPegCombination> filteredSolutions = PegUtil.selectCombinationsThatSatisfyGuess(useElvenarRules, allColorPegCombinations, optimalFirstCombination, keyPegCombination);

        // In case there are no solutions (if the key pegs are impossible), just return the empty combination.
        if (filteredSolutions.size() == 0) {
            return ColorPegCombination.EMPTY;
        }

        return calculateOptimalCombination(filteredSolutions);
    }

    /**
     * Calculates the optimal combination based on the solutions that are possible.
     */
    private ColorPegCombination calculateOptimalCombination(Set<ColorPegCombination> solutions) {
        // For each guess for each possible pin answer, calculate how many elements you eliminate.
        // The set of guesses is exactly the set of possible answers left.
        log.debug("Start picking optimal combination.");

        //noinspection OptionalGetWithoutIsPresent
        Map.Entry<ColorPegCombination, Integer> optimalGuess = StreamEx.of(solutions)
                .parallel()
                // Map each combination to the minimal number of eliminations.
                .mapToEntry(combination -> calculateMinimalNumberOfEliminations(solutions, combination))
                // Pick the combination with the maximal number of eliminations.
                .max(Comparator.comparingInt(Map.Entry::getValue))
                // We know at least one solution is present, so just call get.
                .get();

        log.debug("Picked optimal combination {} with {} eliminations.", optimalGuess.getKey(), optimalGuess.getValue());
        return optimalGuess.getKey();
    }

    /**
     * Calculates the minimal number of eliminations from the solution set assuming the given combination.
     * @param solutions       The list of all solutions
     * @param combination     The combination
     */
    Integer calculateMinimalNumberOfEliminations(Set<ColorPegCombination> solutions, ColorPegCombination combination) {
        return StreamEx.of(allKeyPegCombinations)
                .parallel()
                // The number of eliminations equals the total number of solutions minus the combinations that are still possible.
                .mapToInt(keyPegCombination -> solutions.size() - PegUtil.selectCombinationsThatSatisfyGuess(useElvenarRules, solutions, combination, keyPegCombination).size())
                .min().orElse(solutions.size());
    }
}
