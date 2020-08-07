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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import hr.kravarscan.enchantedfortress.storage.SaveLoad;

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

    static  double interpolate(double x, double min, double max) {
        return x * (max - min) + min;
    }

    public static void readStream(InputStream stream, byte[] buffer) throws IOException {
        int readLength = stream.read(buffer);

        if (readLength != buffer.length){
            throw new IOException("Read " + readLength + " bytes instead of expected " + buffer.length);
        }
    }

    public static List<Byte> toBytes(int num)
    {
        byte[] bytes = new byte[Integer.SIZE / Byte.SIZE];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.putInt(num);

        return toList(bytes);
    }

    public static List<Byte> toBytes(double num)
    {
        byte[] bytes = new byte[Double.SIZE / Byte.SIZE];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        byteBuffer.putDouble(num);

        return toList(bytes);
    }

    public static List<Byte> toBytes(String text) throws UnsupportedEncodingException {
        byte[] textBytes = text.getBytes(SaveLoad.Encoding);
        byte[] bytes = new byte[Integer.SIZE / Byte.SIZE + textBytes.length];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);

        byteBuffer.putInt(textBytes.length);
        byteBuffer.put(textBytes);

        return toList(bytes);
    }

    private static List<Byte> toList(byte[] bytes) {
        ArrayList<Byte> result = new ArrayList<>(bytes.length);

        for (byte b : bytes) {
            result.add(b);
        }

        return result;
    }
}
