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

package hr.kravarscan.enchantedfortress;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import hr.kravarscan.enchantedfortress.logic.Difficulty;
import hr.kravarscan.enchantedfortress.logic.Game;
import hr.kravarscan.enchantedfortress.logic.Technology;
import hr.kravarscan.enchantedfortress.storage.HighScores;
import hr.kravarscan.enchantedfortress.storage.SaveLoad;

public class GameActivity extends AppCompatActivity {

    private static final String LOG_TAG = "GameActivity";

    public static final String ContinueGame = "ContinueGame";
    public static final String StartDifficulty = "StartDifficulty";

    private Game game = new Game(Difficulty.Medium);
    private final String[] techList = new String[5];

    private ArrayAdapter<String> techListAdapter;
    private TextView gameInfo;
    private TextView headlineText;
    private TextView otherNewsText;
    private TextView farmerInfo;
    private TextView builderInfo;
    private TextView soldierInfo;
    private TextView researchInfo;
    private Spinner researchSelector;
    private View farmerControls;
    private View builderControls;
    private View soldierControls;
    private Button endTurnButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        if (savedInstanceState != null) {
            SaveLoad.get().deserialize(this.game, savedInstanceState.get(SaveLoad.SaveKey));
        }
        else if (intent.getBooleanExtra(ContinueGame, false))
        {
            SaveLoad.get().deserialize(this.game, SaveLoad.get().load(this));
        }
        else
        {
            this.game = new Game(Difficulty.Levels[intent.getIntExtra(StartDifficulty, Difficulty.Medium.getIndex())]);
        }

        this.findViewById(R.id.newsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "newsButton click");
                onNews();
            }
        });

        this.findViewById(R.id.farmPlusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "farmPlusButton click");
                game.incFarmers();
                updateInfo();
            }
        });

        this.findViewById(R.id.farmMinusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "farmMinusButton click");
                game.decFarmers();
                updateInfo();
            }
        });

        this.findViewById(R.id.builderPlusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "builderPlusButton click");
                game.incBuilders();
                updateInfo();
            }
        });

        this.findViewById(R.id.builderMinusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "builderMinusButton click");
                game.decBuilders();
                updateInfo();
            }
        });

        this.findViewById(R.id.soldierPlusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "soldierPlusButton click");
                game.incSoldiers();
                updateInfo();
            }
        });

        this.findViewById(R.id.soldierMinusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "soldierMinusButton click");
                game.decSoldiers();
                updateInfo();
            }
        });

        this.farmerControls = this.findViewById(R.id.farmControls);
        this.builderControls = this.findViewById(R.id.builderControls);
        this.soldierControls = this.findViewById(R.id.soldierControls);

        this.endTurnButton = this.findViewById(R.id.endTurnButton);
        this.endTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(LOG_TAG, "endTurnButton click");
                endTurn();
            }
        });

        this.researchSelector = this.findViewById(R.id.researchSelection);
        this.updateTechList();
        this.techListAdapter = new ArrayAdapter<>(this, R.layout.research_item, this.techList);
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
        this.farmerInfo = this.findViewById(R.id.farmText);
        this.builderInfo = this.findViewById(R.id.builderText);
        this.gameInfo = this.findViewById(R.id.gameStatusText);
        this.headlineText = this.findViewById(R.id.newsHeadlineText);
        this.otherNewsText = this.findViewById(R.id.otherNewsText);
        this.soldierInfo = this.findViewById(R.id.soliderText);
        this.researchInfo = this.findViewById(R.id.researchText);

        this.updateInfo();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");

        if (!this.game.isOver())
            SaveLoad.get().save(this.game, this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");

        outState.putDoubleArray(SaveLoad.SaveKey, SaveLoad.get().serialize(this.game));
    }

    private void onNews() {
        Log.d(LOG_TAG, "onNews");

        Intent intent = new Intent(this, NewsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(NewsActivity.BanishCost, this.game.demonBanishCost);
        intent.putExtra(NewsActivity.DemonIndividualStrength, this.game.demonIndividualStrength());
        intent.putExtra(NewsActivity.FirstBanish, this.game.reportFirstBanish);
        intent.putExtra(NewsActivity.HellgatesClosed, this.game.reportHellgateClose);
        intent.putExtra(NewsActivity.BattleAttackers, this.game.reportAttackers);
        intent.putExtra(NewsActivity.BattleVictims, this.game.reportVictims);
        intent.putExtra(NewsActivity.ScoutedDemons, this.game.reportScoutedDemons);
        startActivity(intent);
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
        else if (banishDelta < this.game.demonBanishCost / 1000.0)
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
            HighScores.get().add(this.game, this);

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
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

        this.gameInfo.setText(getResources().getString(R.string.turn) + ": " + this.game.turn + "\n" +
                getResources().getString(R.string.population) + ": " + (int) this.game.roundedPop() + "\n" +
                getResources().getString(R.string.walls) + ": " + (int) this.game.walls
        );

        if (this.game.isOver()) {
            this.headlineText.setText(
                    getResources().getString(this.game.isPlayerAlive() ? R.string.victory : R.string.defeat)
            );

            this.otherNewsText.setVisibility(View.GONE);
            this.farmerControls.setVisibility(View.GONE);
            this.builderControls.setVisibility(View.GONE);
            this.soldierControls.setVisibility(View.GONE);
            this.researchInfo.setVisibility(View.GONE);
            this.researchSelector.setVisibility(View.GONE);

            this.endTurnButton.setText(R.string.gameOver);
        } else {
            boolean battleEvent = this.game.reportAttackers > 0;
            boolean goalEvent = this.game.reportHellgateClose > 0;

            this.headlineText.setText(battleEvent ?
                    getResources().getString(R.string.battleNotification) :
                    getResources().getString(R.string.scouted, Integer.toString(this.game.reportScoutedDemons))
            );
            this.headlineText.setTypeface(null, battleEvent ? Typeface.BOLD : Typeface.NORMAL);

            this.otherNewsText.setText(
                    (battleEvent || goalEvent ? getResources().getString(R.string.moreNews) + ": " : "") +
                            (battleEvent ? getResources().getString(R.string.scoutNotification) : "") +
                            (battleEvent && goalEvent ? ", " : "") +
                            (goalEvent ? getResources().getString(R.string.banishNotification) : "")
            );
        }
    }

    private String sliderText(int sliderTextId, int sliderValue, int descriptionTextId, int effectValue) {
        int percents = (int) (sliderValue * (100 / Game.SliderTicks));

        return getResources().getString(sliderTextId) + ": " + percents + "%\n" +
                getResources().getString(descriptionTextId) + ": " + effectValue;
    }
}
