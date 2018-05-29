package hr.kravarscan.enchantedfortress;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class NewsFragment extends AAttachableFragment {

    private static final String LOG_TAG = "NewsFragment";

    private Game game = null;

    public NewsFragment() {
        // Required empty public constructor
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");

        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void attach(Object listener) {
        Log.d(LOG_TAG, "attach");
    }
}
