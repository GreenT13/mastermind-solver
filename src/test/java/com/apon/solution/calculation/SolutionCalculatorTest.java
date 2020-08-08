package com.apon.solution.calculation;

import com.apon.pegs.ColorPegCombination;
import com.apon.pegs.KeyPeg;
import com.apon.pegs.KeyPegTestUtil;
import com.apon.pegs.PegUtil;
import com.google.common.collect.Multiset;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class SolutionCalculatorTest {
    private ChanceCalculator createInstance(boolean useElvenarRules, int nrOfLocations, int nrOfColors) {
        return new ChanceCalculator(
                useElvenarRules,
                PegUtil.determineAllPossibleKeyPegCombinations(nrOfLocations),
                PegUtil.calculateAllColorPegCombinations(nrOfColors, nrOfLocations)
        );
    }

    @Test
    public void test() {
        // Given
        int nrOfLocations = 2;
        int nrOfColors = 4;
        ChanceCalculator chanceCalculator = createInstance(false, nrOfLocations, nrOfColors);
        ColorPegCombination firstCombination = ColorPegCombination.of(2, 1);

        Map<Multiset<KeyPeg>, ColorPegCombination> secondCombinationMap = new HashMap<>();
        secondCombinationMap.put(KeyPegTestUtil.of(KeyPeg.WHITE, KeyPeg.WHITE), ColorPegCombination.of(2, 1));
        secondCombinationMap.put(KeyPegTestUtil.of(KeyPeg.WHITE, KeyPeg.NONE), ColorPegCombination.of(2, 3));
        secondCombinationMap.put(KeyPegTestUtil.of(KeyPeg.WHITE, KeyPeg.BLACK), ColorPegCombination.EMPTY);
        secondCombinationMap.put(KeyPegTestUtil.of(KeyPeg.BLACK, KeyPeg.BLACK), ColorPegCombination.of(1, 2));
        secondCombinationMap.put(KeyPegTestUtil.of(KeyPeg.NONE, KeyPeg.NONE), ColorPegCombination.of(4, 3));
        secondCombinationMap.put(KeyPegTestUtil.of(KeyPeg.BLACK, KeyPeg.NONE), ColorPegCombination.of(3, 2));

        // When
        System.out.println(chanceCalculator.calculateSecondWinningChanceMap(firstCombination, secondCombinationMap));
    }
}
