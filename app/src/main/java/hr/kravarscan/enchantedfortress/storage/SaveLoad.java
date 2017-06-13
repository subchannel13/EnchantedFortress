package hr.kravarscan.enchantedfortress.storage;

import hr.kravarscan.enchantedfortress.BuildConfig;
import hr.kravarscan.enchantedfortress.logic.Game;

/**
 * Copyright 2017 Ivan Kravarščan
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

public final class SaveLoad {
    private static SaveLoad instance = null;

    public static SaveLoad get() {
        if (instance == null)
            instance = new SaveLoad();

        return instance;
    }

    private SaveLoad() {
    }

    public double[] serialize(Game game) {
        double[] result = new double[LatestSaveKeys.KEY_COUNT.ordinal()];

        result[0] = BuildConfig.VERSION_CODE;
        System.arraycopy(game.save(), 0, result, 1, result.length - 1);

        return result;
    }

    public boolean load(Game game, double[] rawData) {
        if (rawData.length == 0)
            return false;

        int version = (int) rawData[0];
        if (rawData.length != LatestSaveKeys.KEY_COUNT.ordinal() || version > BuildConfig.VERSION_CODE)
            return false;

        game.load(rawData);
        return true;
    }
}
