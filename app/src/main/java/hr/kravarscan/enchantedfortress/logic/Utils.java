package hr.kravarscan.enchantedfortress.logic;

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

public final class Utils {
    private Utils()
    {}

    public static double clamp(double x, double min, double max) {
        if (x < min)
            return min;
        else if (x > max)
            return max;

        return x;
    }

    public static int integerDelta(double x, double delta) {
        return (int)(Math.floor(x + delta) - Math.floor(x));
    }
}
