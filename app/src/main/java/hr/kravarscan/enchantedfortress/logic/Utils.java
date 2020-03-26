/*
 * Copyright 2019 Ivan Kravarščan
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

import java.io.IOException;
import java.io.InputStream;

public class Utils {
    private Utils()
    {}

    static double clamp(double x, double min, double max) {
        if (x < min)
            return min;
        else if (x > max)
            return max;

        return x;
    }

    static int integerDelta(double x, double delta) {
        return (int)(Math.floor(x + delta) - Math.floor(x));
    }

    public static void readStream(InputStream stream, byte[] buffer) throws IOException {
        int readLength = stream.read(buffer);

        if (readLength != buffer.length){
            throw new IOException("Read " + readLength + " bytes instead of expected " + buffer.length);
        }
    }
}
