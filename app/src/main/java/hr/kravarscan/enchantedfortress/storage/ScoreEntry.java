package hr.kravarscan.enchantedfortress.storage;

import java.util.List;

/**
 * Copyright 2018 Ivan Kravarščan
 *
 * This file is part of Enchanted Fortress.
 *
 * Enchanted Fortress is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Enchanted Fortress is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Enchanted Fortress.  If not, see <http://www.gnu.org/licenses/>.
 */

public class ScoreEntry {
    public static final int SaveLength = 2;

    private final int turn;
    private final int difficulty;

    public ScoreEntry(int turn, int difficulty) {
        this.turn = turn;
        this.difficulty = difficulty;
    }

    public boolean isBetter(ScoreEntry scoreEntry) {
        return this.turn < scoreEntry.turn;
    }

    public double[] saveData() {
        return new double[]
                {
                        this.turn,
                        this.difficulty
                };
    }

    public int getTurn() {
        return turn;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public static ScoreEntry Load(List<Double> data) {
        return new ScoreEntry(data.get(0).intValue(), data.get(1).intValue());
    }
}
