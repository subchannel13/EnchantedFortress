package hr.kravarscan.enchantedfortress;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        TextView goalReport = view.findViewById(R.id.goalReport);
        if (this.game.reportHellgateClose <= 0)
            goalReport.setVisibility(View.GONE);
        else if (this.game.reportFirstBanish)
            goalReport.setText(getResources().getString(R.string.banishProgress, this.game.demonBanishCost / 100));
        else
            goalReport.setText(getResources().getString(R.string.banishProgressShort, this.game.demonBanishCost / 100));

        TextView battleReport = view.findViewById(R.id.battleReport);
        if (this.game.reportAttackers <= 0)
            battleReport.setVisibility(View.GONE);
        if (this.game.reportVictims > 0)
            battleReport.setText(getResources().getString(R.string.attacked) + Integer.toString(this.game.reportAttackers) + getResources().getString(R.string.victims) + Integer.toString(this.game.reportVictims));
        else
            battleReport.setText(getResources().getString(R.string.noVictims));

        TextView scoutReport = view.findViewById(R.id.scoutReport);
        scoutReport.setText(getResources().getString(R.string.scouted) + ": " + Integer.toString(this.game.reportScoutedDemons) + " " + getResources().getString(R.string.demons));

        TextView popReport = view.findViewById(R.id.populationReport);
        popReport.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void attach(Object listener) {
        Log.d(LOG_TAG, "attach");
    }
}
