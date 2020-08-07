/*
 * Copyright 2020 Ivan Kravarščan
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

package hr.kravarscan.enchantedfortress.logic;

import java.util.Random;

class CombatSide {
    private double numbers;
    private double strength;

    public CombatSide(double numbers, double strength) {
        this.numbers = numbers;
        this.strength = strength;
    }

    public double getNumbers() {
        return numbers;
    }

    public CombatSide split(double nextDouble) {
        double splitNumber = numbers * nextDouble;
        this.numbers -= splitNumber;

        return new CombatSide(splitNumber, strength);
    }

    public void join(CombatSide others) {
        this.numbers += others.numbers;
        others.numbers = 0;
    }

    public void attack(Random rand, CombatSide... targets) {
        double damage = this.numbers * this.strength * Utils.interpolate(rand.nextDouble(), 0.1, 0.5);

        for (CombatSide target: targets) {
            double hitPoints = target.numbers * target.strength;

            if (damage > hitPoints) {
                target.numbers = 0;
                damage -= hitPoints;
            }
            else {
                target.numbers -= damage / target.strength;
                break;
            }
        }
    }
}
