package hr.kravarscan.enchantedfortress;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import hr.kravarscan.enchantedfortress.logic.Difficulty;
import hr.kravarscan.enchantedfortress.logic.Game;
import hr.kravarscan.enchantedfortress.logic.Technology;
import hr.kravarscan.enchantedfortress.storage.HighScores;
import hr.kravarscan.enchantedfortress.storage.SaveLoad;

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

public class GameFragment extends AAttachableFragment {

    private static final String LOG_TAG = "GameFragment";

    private Game game = new Game(Difficulty.Medium);
    private final String[] techList = new String[5];
    private int longBanishProgressTurn = Integer.MAX_VALUE;

    private ArrayAdapter<String> techListAdapter;
    private TextView gameInfo;
    private TextView farmerInfo;
    private TextView builderInfo;
    private TextView soldierInfo;
    private TextView researchInfo;
    private Spinner researchSelector;
    private View farmerControls;
    private View builderControls;
    private View soldierControls;
    private Button endTurnButton;

    private OnFragmentInteractionListener listener;

    interface OnFragmentInteractionListener {
        void onGameOver();
    }

    public GameFragment() {
        // Required empty public constructor
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView, has saved instance: " + (savedInstanceState != null));
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        if (savedInstanceState != null) {
            SaveLoad.get().deserialize(this.game, savedInstanceState.getDoubleArray(SaveLoad.SaveKey));
        }

        view.findViewById(R.id.farmPlusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "farmPlusButton click");
                game.incFarmers();
                updateInfo();
            }
        });

        view.findViewById(R.id.farmMinusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "farmMinusButton click");
                game.decFarmers();
                updateInfo();
            }
        });

        view.findViewById(R.id.builderPlusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "builderPlusButton click");
                game.incBuilders();
                updateInfo();
            }
        });

        view.findViewById(R.id.builderMinusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "builderMinusButton click");
                game.decBuilders();
                updateInfo();
            }
        });

        view.findViewById(R.id.soldierPlusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "soldierPlusButton click");
                game.incSoldiers();
                updateInfo();
            }
        });

        view.findViewById(R.id.soldierMinusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "soldierMinusButton click");
                game.decSoldiers();
                updateInfo();
            }
        });

        this.farmerControls = view.findViewById(R.id.farmControls);
        this.builderControls = view.findViewById(R.id.builderControls);
        this.soldierControls = view.findViewById(R.id.soldierControls);

        this.endTurnButton = view.findViewById(R.id.endTurnButton);
        this.endTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "endTurnButton click");
                endTurn();
            }
        });

        this.researchSelector = view.findViewById(R.id.researchSelection);
        this.updateTechList();
        this.techListAdapter = new ArrayAdapter<>(view.getContext(), R.layout.research_item, this.techList);
        this.researchSelector.setAdapter(this.techListAdapter);
        this.researchSelector.setSelection(this.game.getSelectedTech());
        this.researchSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(LOG_TAG, "researchSelector, selected " + i);
                game.selectTech(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d(LOG_TAG, "researchSelector, no selection");
            }
        });
        this.farmerInfo = view.findViewById(R.id.farmText);
        this.builderInfo = view.findViewById(R.id.builderText);
        this.gameInfo = view.findViewById(R.id.gameStatusText);
        this.soldierInfo = view.findViewById(R.id.soliderText);
        this.researchInfo = view.findViewById(R.id.researchText);
        this.longBanishProgressTurn = Integer.MAX_VALUE;

        this.updateInfo();

        return view;
    }

    @Override
    public void attach(Object listener) {
        Log.d(LOG_TAG, "attach");

        if (listener instanceof OnFragmentInteractionListener) {
            this.listener = (OnFragmentInteractionListener) listener;
        } else {
            throw new RuntimeException(listener.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(LOG_TAG, "onDetach");

        listener = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");

        if (!this.game.isOver())
            SaveLoad.get().save(this.game, this.getActivity());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");

        outState.putDoubleArray(SaveLoad.SaveKey, SaveLoad.get().serialize(this.game));
    }

    private void updateTechList() {
        Log.d(LOG_TAG, "updateTechList");
        this.techList[0] = getResources().getString(R.string.farmingTech) + techDescription(this.game.farming);
        this.techList[1] = getResources().getString(R.string.buildTech) + techDescription(this.game.building);
        this.techList[2] = getResources().getString(R.string.soldierTech) + techDescription(this.game.soldiering);
        this.techList[3] = getResources().getString(R.string.scholarTech) + techDescription(this.game.scholarship);

        double rp = this.game.deltaResearch();
        double banishDelta = rp - this.game.banishCostGrowth;
        String banishEta = "";

        if (banishDelta <= 0)
            banishEta = " - " + getResources().getString(R.string.banishNever);
        else if (banishDelta < this.game.demonBanishCost / 1000)
            banishEta = " - " + getResources().getString(R.string.banishSlow);
        else if (this.game.demonBanishCost > 0)
            banishEta = " - " + (int) Math.ceil(this.game.demonBanishCost / banishDelta) + " " + getResources().getString(R.string.turns);

        this.techList[4] = getResources().getString(R.string.banishTech) + banishEta;
    }

    private String techDescription(Technology tech) {
        return " " + (tech.level + 1) + techEta(tech.cost() - tech.points);
    }

    private String techEta(double cost) {
        double rp = this.game.deltaResearch();

        if (rp > cost / 1000)
            return " - " + (int) Math.ceil(cost / rp) + " " + getResources().getString(R.string.turns);

        return "";
    }

    private void endTurn() {
        Log.d(LOG_TAG, "endTurn, is game over: " + this.game.isOver());

        if (this.game.isOver()) {
            this.listener.onGameOver();
            HighScores.get().add(this.game, this.getActivity());
            return;
        }

        this.game.endTurn();
        this.updateInfo();
    }

    private void updateInfo() {
        Log.d(LOG_TAG, "updateInfo");
        this.farmerInfo.setText(sliderText(R.string.farmers, game.farmerSlider, R.string.popDelta, game.realDeltaPop()));
        this.builderInfo.setText(sliderText(R.string.builders, game.builderSlider, R.string.wallDelta, (int) game.deltaWalls()));
        this.soldierInfo.setText(sliderText(R.string.soldiers, game.soldierSlider, R.string.militaryStrength, (int) game.militaryStrength()));
        this.researchInfo.setText(sliderText(R.string.scholars, game.getScholarSlider(), R.string.researchDelta, (int) game.deltaResearch()));

        this.updateTechList();
        this.techListAdapter.notifyDataSetChanged();

        String status = getResources().getString(R.string.turn) + ": " + Integer.toString(this.game.turn) + "\n" +
                getResources().getString(R.string.population) + ": " + Integer.toString((int) this.game.roundedPop()) + "\n" +
                getResources().getString(R.string.walls) + ": " + Integer.toString((int) this.game.walls) + "\n";

        if (this.game.isOver()) {
            this.gameInfo.setText(status + "\n" +
                    getResources().getString(this.game.isPlayerAlive() ? R.string.victory : R.string.defeat) +
                    "\n"
            );

            this.farmerControls.setVisibility(View.GONE);
            this.builderControls.setVisibility(View.GONE);
            this.soldierControls.setVisibility(View.GONE);
            this.researchInfo.setVisibility(View.GONE);
            this.researchSelector.setVisibility(View.GONE);

            this.endTurnButton.setText(R.string.gameOver);
        } else
            this.gameInfo.setText(status +
                    getResources().getString(R.string.scouted) + ": " + Integer.toString(this.game.reportScoutedDemons) + " " + getResources().getString(R.string.demons) +
                    this.battleInfo() +
                    this.banishInfo()
            );
    }

    private String sliderText(int sliderTextId, int sliderValue, int descriptionTextId, int effectValue) {
        Integer percents = (int) (sliderValue * (100 / Game.SliderTicks));
        Integer effect = effectValue;

        return getResources().getString(sliderTextId) + ": " + percents.toString() + "%\n" +
                getResources().getString(descriptionTextId) + ": " + effect.toString();
    }

    private String battleInfo() {
        Log.d(LOG_TAG, "battleInfo, attackers: " + this.game.reportAttackers + ", victims: " + this.game.reportVictims);
        if (this.game.reportAttackers <= 0)
            return "";

        if (this.game.reportVictims > 0)
            return "\n" + getResources().getString(R.string.attacked) + Integer.toString(this.game.reportAttackers) + getResources().getString(R.string.victims) + Integer.toString(this.game.reportVictims);
        else
            return "\n" + getResources().getString(R.string.noVictims);
    }

    private String banishInfo() {
        Log.d(LOG_TAG, "banishInfo, hellgateClose: " + this.game.reportHellgateClose + ", turn: " + this.game.turn);
        if (this.game.reportHellgateClose <= 0)
            return "";

        if (this.game.turn <= this.longBanishProgressTurn) {
            this.longBanishProgressTurn = this.game.turn;
            return "\n" + getResources().getString(R.string.banishProgress, this.game.demonBanishCost / 100);
        }
        else
            return "\n" + getResources().getString(R.string.banishProgressShort, this.game.demonBanishCost / 100);
    }
}