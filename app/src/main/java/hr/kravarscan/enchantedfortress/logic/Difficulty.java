package hr.kravarscan.enchantedfortress.logic;

import android.util.Log;

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

public class Difficulty {
    public static final Difficulty Medium = new Difficulty(100, 1, 1.02);
    public static final Difficulty[] Levels = new Difficulty[]
            {
                    new Difficulty(120, 0.75, 1),
                    Medium,
                    new Difficulty(80, 2, 1.05),
            };

    private final double startingPop;
    private final double demonSpawnFactor;
    private final double demonPowerBase;

    private Difficulty(double startingPop, double demonSpawnFactor, double demonPowerBase) {
        this.startingPop = startingPop;
        this.demonSpawnFactor = demonSpawnFactor;
        this.demonPowerBase = demonPowerBase;
    }

    public double getStartingPop() {
        return startingPop;
    }

    public double getDemonSpawnFactor() {
        return demonSpawnFactor;
    }

    public double getDemonPowerBase() {
        return demonPowerBase;
    }

    public int getIndex(){
        for (int i = 0; i < Levels.length; i++)
            if (Levels[i] == this)
                return i;

        Log.e("Difficulty", "Failed to find index of a difficulty level, population" + this.startingPop + ", spawnFactor: " + this.demonSpawnFactor + ", powerBase: " + this.demonPowerBase);
        return 1;
    }
}
