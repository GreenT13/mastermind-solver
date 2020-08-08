package com.apon.solution.calculation;

import com.apon.pegs.ColorPegCombination;
import com.apon.pegs.KeyPeg;
import com.apon.pegs.PegUtil;
import com.google.common.collect.Multiset;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChanceCalculator {
    private static final Logger log = LogManager.getLogger(ChanceCalculator.class);

    private final boolean useElvenarRules;
    private final Set<Multiset<KeyPeg>> allKeyPegCombinations;
    private final Set<ColorPegCombination> allColorPegCombinations;

    public ChanceCalculator(boolean useElvenarRules, Set<Multiset<KeyPeg>> allKeyPegCombinations, Set<ColorPegCombination> allColorPegCombinations) {
        this.useElvenarRules = useElvenarRules;
        this.allKeyPegCombinations = allKeyPegCombinations;
        this.allColorPegCombinations = allColorPegCombinations;
    }

    public Map<Multiset<KeyPeg>, Double> calculateSecondWinningChanceMap(ColorPegCombination firstCombination, Map<Multiset<KeyPeg>, ColorPegCombination> secondCombinationMap) {
        // Create a map where the keys are the key pegs of the first combination and the values are the calculated winning chances if the key would be the result of the game.
        return StreamEx.of(allKeyPegCombinations)
                .mapToEntry((keyPegCombination) -> calculateFinalWinningChance(firstCombination, secondCombinationMap, keyPegCombination))
                .toMap();
    }

    private Double calculateFinalWinningChance(ColorPegCombination firstCombination, Map<Multiset<KeyPeg>, ColorPegCombination> secondCombinationMap, Multiset<KeyPeg> keyPegCombination) {
        // Filter all the solutions to a list of possible solutions.
        Set<ColorPegCombination> filteredSolutions = PegUtil.selectCombinationsThatSatisfyGuess(useElvenarRules, allColorPegCombinations, firstCombination, keyPegCombination);

        // Calculate the chance of each key peg combination occurring
        Map<Multiset<KeyPeg>, Double> chanceOfSecondKeyPegCombinationOccurring =
                calculateChanceOfSecondKeyPegCombinationOccurring(filteredSolutions, secondCombinationMap.get(keyPegCombination));

        Map<Multiset<KeyPeg>, Integer> numberOfSolutionsLeftMap = calculateNumberOfSolutionsLeft(filteredSolutions, secondCombinationMap.get(keyPegCombination));

        // Return these two maps multiplied and then summed.
        return EntryStream.of(chanceOfSecondKeyPegCombinationOccurring)
                .map((entry) -> entry.getValue() * numberOfSolutionsLeftMap.get(entry.getKey()))
                .reduce((double) 0, Double::sum);
    }

    private Map<Multiset<KeyPeg>, Double> calculateChanceOfSecondKeyPegCombinationOccurring(Set<ColorPegCombination> filteredSolutions, ColorPegCombination secondCombination) {
        // Count how many solutions map to each key peg combination.
        Map<Multiset<KeyPeg>, Long> countMap = StreamEx.of(filteredSolutions)
                .map((solution) -> PegUtil.determineKeyPegCombination(secondCombination, solution, useElvenarRules))
                .groupingBy(Function.identity(), Collectors.counting());

        // Convert the counts to chances by dividing by the total number of solutions.
        return EntryStream.of(countMap)
                .mapValues((value) -> value / (double) filteredSolutions.size())
                .toMap();
    }

    private Map<Multiset<KeyPeg>, Integer> calculateNumberOfSolutionsLeft(Set<ColorPegCombination> filteredSolutions, ColorPegCombination secondCombination) {
        return StreamEx.of(allKeyPegCombinations)
                .mapToEntry((keyPegCombination) -> PegUtil.selectCombinationsThatSatisfyGuess(useElvenarRules, filteredSolutions, secondCombination, keyPegCombination).size())
                .toMap();
    }
}
