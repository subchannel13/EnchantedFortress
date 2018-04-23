package hr.kravarscan.enchantedfortress.logic;

import android.util.Log;

/**
 * Copyright 2018 Ivan Kravarščan
 *
 * This file is part of Enchanted Fortress.
 *
 *  Enchanted Fortress is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Enchanted Fortress is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Enchanted Fortress.  If not, see <http://www.gnu.org/licenses/>.
 */

public class Technology {
    private static final double LevelExp = 4 / 3.0;
    private static final double Level0Cost = 100;

    public int level = 0;
    public double points = 0;

    public void Invest(double researchPoints) {
        Log.d("Technology", "Invest, points" + this.points + ", investment: " + researchPoints);
        this.points += researchPoints;
        double levelCost = this.cost();

        while (this.points > levelCost) {
            this.points -= levelCost;
            this.level++;
            levelCost *= LevelExp;
        }
    }

    public double cost() {
        return Level0Cost * Math.pow(LevelExp, this.level);
    }
}
