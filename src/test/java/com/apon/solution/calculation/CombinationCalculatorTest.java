package com.apon.solution.calculation;

import com.apon.pegs.ColorPegCombination;
import com.apon.pegs.PegUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CombinationCalculatorTest {
    private CombinationCalculator createInstance(boolean useElvenarRules, int nrOfLocations, int nrOfColors) {
        return new CombinationCalculator(
                useElvenarRules,
                PegUtil.determineAllPossibleKeyPegCombinations(nrOfLocations),
                PegUtil.calculateAllColorPegCombinations(nrOfColors, nrOfLocations)
        );
    }

    @Test
    void optimalFirstCombinationIsCorrect() {
        // Given
        CombinationCalculator combinationCalculator = createInstance(false, 5, 3);

        // When
        ColorPegCombination colorPegCombination = combinationCalculator.calculateOptimalFirstCombination();

        // Then
        // The answer may be any of the one optimal solutions. The only common factor is that we know is the minimal number of eliminations.
        assertEquals((Integer) 207, combinationCalculator.calculateMinimalNumberOfEliminations(combinationCalculator.allColorPegCombinations, colorPegCombination));
    }
}
