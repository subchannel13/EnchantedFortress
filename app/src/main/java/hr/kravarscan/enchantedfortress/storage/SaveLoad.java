package hr.kravarscan.enchantedfortress.storage;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import hr.kravarscan.enchantedfortress.BuildConfig;
import hr.kravarscan.enchantedfortress.logic.Difficulty;
import hr.kravarscan.enchantedfortress.logic.Game;

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

public final class SaveLoad {
    private static final String LOG_TAG = "SaveLoad";

    public static final String SaveKey = "GameState";
    private static final String SaveFileName = "autosave.dat";
    private static final int MaxSaveLength = LatestSaveKeys.KEY_COUNT.ordinal();
    private static SaveLoad instance = null;

    public static SaveLoad get() {
        if (instance == null)
            instance = new SaveLoad();

        return instance;
    }

    private SaveLoad() {
    }

    public boolean hasAutosave(Context context) {
        return context.getFileStreamPath(SaveFileName).exists();
    }

    public void save(Game game, Context context) {
        byte[] byteBuffer = new byte[Double.SIZE / Byte.SIZE];
        double[] data = this.serialize(game);

        Log.d(LOG_TAG, "Saving");
        try {
            FileOutputStream stream = context.openFileOutput(SaveFileName, Context.MODE_PRIVATE);

            for (double val : data) {
                ByteBuffer.wrap(byteBuffer).putDouble(val);
                stream.write(byteBuffer);
            }

            stream.close();
            Log.d(LOG_TAG, "Saved");
        } catch (Exception e) {
            Log.e(LOG_TAG, "Autosave failed", e);
        }
    }

    public double[] load(Context context) {
        byte[] byteBuffer = new byte[Double.SIZE / Byte.SIZE];
        List<Double> data = new ArrayList<>();

        Log.d(LOG_TAG, "Loading");
        try {
            FileInputStream stream = context.openFileInput(SaveFileName);

            while (stream.read(byteBuffer) == byteBuffer.length && data.size() < MaxSaveLength)
                data.add(ByteBuffer.wrap(byteBuffer).getDouble());

            stream.close();
            Log.d(LOG_TAG, "loaded");
            upgradeSave(data);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Loading autosave failed", e);
            return null;
        }

        double[] result = new double[data.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = data.get(i);

        return result;
    }

    private void upgradeSave(List<Double> data) {
        Log.d(LOG_TAG, "upgradeSave from " + data.get(LatestSaveKeys.VERSION.ordinal()));

        if (data.get(LatestSaveKeys.VERSION.ordinal()) <= 3) {
            Log.d(LOG_TAG, "upgradeSave to version 3");
            data.add(SaveKeysV7.DIFFICULTY.ordinal(), (double) Difficulty.Medium.getIndex());
        }

        if (data.get(LatestSaveKeys.VERSION.ordinal()) <= 7) {
            Log.d(LOG_TAG, "upgradeSave to version 7");

            data.add(LatestSaveKeys.DEMON_LEVEL.ordinal(), Math.floor(0.5 * data.get(LatestSaveKeys.TURN.ordinal())));
            data.add(LatestSaveKeys.REPORT_HELLGATE_CLOSE.ordinal(), 0.0);
            data.add(LatestSaveKeys.REPORT_HELLGATE_OPEN.ordinal(), 0.0);
            data.add(LatestSaveKeys.BANISH_COST_GROWTH.ordinal(), 0.0);
        }
    }

    public double[] serialize(Game game) {
        double[] result = new double[LatestSaveKeys.KEY_COUNT.ordinal()];

        result[0] = BuildConfig.VERSION_CODE;
        System.arraycopy(game.save(), 0, result, 1, result.length - 1);

        return result;
    }

    public void deserialize(Game game, double[] rawData) {
        if (rawData == null || rawData.length == 0) {
            Log.d(LOG_TAG, "deserialize");
            return;
        }

        int version = (int) rawData[0];
        if (rawData.length != LatestSaveKeys.KEY_COUNT.ordinal() || version > BuildConfig.VERSION_CODE)
            return;

        game.load(rawData);
    }
}
