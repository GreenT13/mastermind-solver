//package com.apon;
//
//import com.apon.pegs.ColorPegCombination;
//import com.apon.pegs.KeyPeg;
//import com.apon.pegs.PegUtil;
//import com.apon.solution.Guess;
//import com.google.common.collect.HashMultiset;
//import com.google.common.collect.Lists;
//import com.google.common.collect.Multiset;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//public class MasterMindGame {
//    private final int nrOfLocations;
//    private final int nrOfColors;
//
//    /** List containing all possible answers for a guess. Only needed to calculate this once. */
//    Set<Multiset<KeyPeg>> usefulAnswers;
//
//    /** List containing all the possible solutions that are still possible after each guess. */
//    List<ColorPegCombination> possibleSolutions;
//
//    public MasterMindGame(int nrOfLocations, int nrOfColors) {
//        this.nrOfLocations = nrOfLocations;
//        this.nrOfColors = nrOfColors;
//
//        usefulAnswers = calculateAllUsefulAnswers();
//        possibleSolutions = PegUtil.calculateAllColorPegCombinations(nrOfColors, nrOfLocations);
//    }
//
//    Set<Multiset<KeyPeg>> calculateAllUsefulAnswers() {
//        // Calculate all possible combinations of the possible answers using the cartesian product X^n, where
//        // X is the set of all possible pin answers and n is the number of locations.
//        // Some combinations are duplicate, as order doesn't matter. So we map everything into a set and we are done.
//        // Multiset magic does the rest.
//        List<List<KeyPeg>> cartesianProductSource = Collections.nCopies(nrOfColors, Arrays.asList(KeyPeg.values()));
//
//        // Some answers are not useful to calculate, as we always know the result:
//        // 1. All correct, this will always result in maximum elimination.
//        // 2. All correct, except one incorrect place but correct color. This is just impossible as an answer.
//        return Lists.cartesianProduct(cartesianProductSource).stream()
//                .map(HashMultiset::create)
//                .filter((keyPegCombination -> {
//                    if (keyPegCombination.count(KeyPeg.WHITE) == keyPegCombination.size()) {
//                        return false;
//                    }
//                    return keyPegCombination.count(KeyPeg.BLACK) != 1 || keyPegCombination.count(KeyPeg.WHITE) != keyPegCombination.size() - 1;
//                }))
//                .collect(Collectors.toSet());
//    }
//
//    public Guess getNextBestGuess() {
//        if (possibleSolutions.size() == 0) {
//            throw new RuntimeException("This must not happen!");
//        }
//
//        // For each guess for each possible pin answer, calculate how many elements you eliminate.
//        // The set of guesses is exactly the set of possible answers left.
//
//        ColorPegCombination maximalGuess = null;
//        int nrOfEliminations = 0;
//
//        for (ColorPegCombination guess : possibleSolutions) {
//            // Always initialize
//            int minimalNrOfEliminations = possibleSolutions.size();
//            for (Multiset<KeyPeg> keyPegCombination: usefulAnswers) {
//                int eliminations = calculateNumberOfEliminations(guess, keyPegCombination);
//                if (minimalNrOfEliminations > eliminations) {
//                    minimalNrOfEliminations = eliminations;
//                }
//            }
//
//            if (nrOfEliminations < minimalNrOfEliminations) {
//                nrOfEliminations = minimalNrOfEliminations;
//                maximalGuess = guess;
//            }
//
//            System.out.println("Finished guess " + guess.toString() + " with " + minimalNrOfEliminations + " nr of eliminations.");
//        }
//
//        return new Guess(maximalGuess, (float) (nrOfEliminations / possibleSolutions.size()));
//    }
//
//    private int calculateNumberOfEliminations(ColorPegCombination guess, Multiset<KeyPeg> keyPegCombination) {
//        int nrOfEliminations = 0;
//        // TODO: use lambda's.
//        for (ColorPegCombination solution : possibleSolutions) {
//            // Calculate the peg combination between the guess and the solution.
//            // If it is different, it means we eliminated that particular solution.
//            if (!PegUtil.determineKeyPegCombination(guess, solution, false).equals(keyPegCombination)) {
//                nrOfEliminations++;
//            }
//        }
//        return nrOfEliminations;
//    }
//}
