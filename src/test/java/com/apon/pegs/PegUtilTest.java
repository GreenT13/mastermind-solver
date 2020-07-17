package com.apon.pegs;

import com.google.common.collect.Multiset;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.apon.pegs.KeyPeg.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class PegUtilTest {
    @Nested
    class CombinationCalculator {
        @Test
        void keyPegsForThreeLocations() {
            // Given
            int nrOfLocations = 3;

            // When
            Set<Multiset<KeyPeg>> allKeyPegCombinations = PegUtil.determineAllPossibleKeyPegCombinations(nrOfLocations);

            // Then
            assertThat(allKeyPegCombinations, containsInAnyOrder(
                    KeyPegTestUtil.of(NONE, NONE, NONE),
                    KeyPegTestUtil.of(NONE, NONE, BLACK),
                    KeyPegTestUtil.of(NONE, BLACK, BLACK),
                    KeyPegTestUtil.of(BLACK, BLACK, BLACK),
                    KeyPegTestUtil.of(NONE, NONE, WHITE),
                    KeyPegTestUtil.of(NONE, WHITE, WHITE),
                    KeyPegTestUtil.of(WHITE, WHITE, WHITE),
                    KeyPegTestUtil.of(BLACK, BLACK, WHITE),
                    KeyPegTestUtil.of(BLACK, WHITE, WHITE),
                    KeyPegTestUtil.of(NONE, BLACK, WHITE)));
        }

        @Test
        void colorPegsForThreeLocationsTwoColors() {
            // Given
            int nrOfLocations = 2;
            int nrOfColors = 3;

            // When
            Set<ColorPegCombination> allColorCombinations = PegUtil.calculateAllColorPegCombinations(nrOfLocations, nrOfColors);

            // Then
            assertThat(allColorCombinations, containsInAnyOrder(
                    ColorPegCombination.of(1, 1),
                    ColorPegCombination.of(1, 2),
                    ColorPegCombination.of(1, 3),
                    ColorPegCombination.of(2, 1),
                    ColorPegCombination.of(2, 2),
                    ColorPegCombination.of(2, 3),
                    ColorPegCombination.of(3, 1),
                    ColorPegCombination.of(3, 2),
                    ColorPegCombination.of(3, 3)));
        }
    }

    @Nested
    class GuessSolutionChecker {
        @Test
        void singleColorUsage() {
            // Given
            boolean useElvenarRules = false;
            int nrOfLocations = 3;
            ColorPegCombination guess = ColorPegCombination.of(1, 2, 3);
            ColorPegCombination solution = ColorPegCombination.of(3, 4, 5);

            // When
            Multiset<KeyPeg> keyPegCombination = PegUtil.determineKeyPegCombination(guess, solution, useElvenarRules);

            // Then
            assertThat(keyPegCombination, equalTo(KeyPegTestUtil.of(BLACK, NONE, NONE)));
        }

        @Test
        void multipleColorUsage() {
            // Given
            boolean useElvenarRules = false;
            int nrOfLocations = 3;
            ColorPegCombination guess = ColorPegCombination.of(2, 1, 1, 1);
            ColorPegCombination solution = ColorPegCombination.of(1, 1, 3, 4);

            // When
            Multiset<KeyPeg> keyPegCombination = PegUtil.determineKeyPegCombination(guess, solution, useElvenarRules);

            // Then
            assertThat(keyPegCombination, equalTo(KeyPegTestUtil.of(BLACK, WHITE, NONE, NONE)));
        }
    }

    @Nested
    class FilterSolutions {
        @Test
        void filterAllSolutions() {
            // Given
            boolean useElvenarRules = false;
            int nrOfLocations = 2;
            int nrOfColors = 3;
            Set<ColorPegCombination> solutions = PegUtil.calculateAllColorPegCombinations(nrOfLocations, nrOfColors);
            ColorPegCombination colorPegCombination = ColorPegCombination.of(1, 2);
            Multiset<KeyPeg> keyPegCombination = KeyPegTestUtil.of(WHITE, NONE);

            // Then
            Set<ColorPegCombination> filteredCombinations = PegUtil.selectCombinationsThatSatisfyGuess(useElvenarRules, solutions, colorPegCombination, keyPegCombination);

            // Then
            assertThat(filteredCombinations, containsInAnyOrder(
                    ColorPegCombination.of(1, 1),
                    ColorPegCombination.of(1, 3),
                    ColorPegCombination.of(2, 2),
                    ColorPegCombination.of(3, 2)));
        }
    }
}
