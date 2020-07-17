package com.apon.solution;

import com.apon.pegs.ColorPegCombination;

public class Guess {
    private final ColorPegCombination guess;
    private final double winningChance;

    public Guess(ColorPegCombination guess, float winningChance) {
        this.guess = guess;
        this.winningChance = winningChance;
    }

    public ColorPegCombination getGuess() {
        return guess;
    }

    public double getWinningChance() {
        return winningChance;
    }

    @Override
    public String toString() {
        return "BestGuess{" +
                "guess=" + guess +
                ", percentage=" + winningChance +
                '}';
    }
}
