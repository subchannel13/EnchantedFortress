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

import android.util.Log;

import java.nio.ByteBuffer;
import java.util.Random;

public class Game {
    private static final String LOG_TAG = "Game";

    private static final double WallCost = 100;
    private static final int DemonStrength = 5;
    private static final int CombatRounds = 10;
    private static final int MaxBanishCost = 10000000;
    private static final int MaxDemonGates = 100000000;
    private static final int MaxPopulation = 100000;
    private static final double NatalityPerFood = 1 / 12.0;
    private static final double Mortality = 1 / 50.0;

    private static final double FarmingBase = 3;
    private static final double BuilderBase = 10;
    private static final double CivilStrength = 1;
    private static final double WallCivilStrength = 4;
    private static final double SoldierStrength = 10;
    private static final double WallSoldierStrength = 20;
    private static final double ResearchBase = 0.1;
    private static final double ScholarResearch = 1 - ResearchBase;
    private static final double FarmingTechBonus = 1 / 12.0;
    private static final double BuildingTechBonus = 1;
    private static final double SoldieringTechBonus = 1 / 1.0;
    private static final double ResearchTechBonus = 1 / 8.0;

    private static final Random rand = new Random();

    public int turn = 1;
    private double population;
    public double walls = 0;
    private int demons = 0;
    private int demonLevel = 0;
    private int demonGates = 0;
    public int demonBanishCost = 10000;
    private Difficulty difficulty;

    public static final double SliderTicks = 20;
    public int farmerSlider = 10;
    public int builderSlider = 2;
    public int soldierSlider = 0;
    private int selectedTech = 0;

    public final Technology farming = new Technology();
    public final Technology building = new Technology();
    public final Technology soldiering = new Technology();
    public final Technology scholarship = new Technology();

    public int reportAttackers = 0;
    public int reportHellgateClose = 0;
    private int reportHellgateOpen = 0;
    public int reportVictims = 0;
    public int reportScoutedDemons = 0;
    public boolean reportFirstBanish = true;
    public int banishCostGrowth = Integer.MAX_VALUE;

    public Game(Difficulty difficulty)
    {
        this.difficulty = difficulty;
        this.population = difficulty.getStartingPop();
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    /*
        Sliders
     */
    public int getScholarSlider() {
        return (int) SliderTicks - this.farmerSlider - this.builderSlider - this.soldierSlider;
    }

    public void decBuilders() {
        this.builderSlider = moveSlider(this.builderSlider, -1, minBuilders());
    }

    public void incBuilders() {
        this.builderSlider = moveSlider(this.builderSlider, 1, 0);
    }

    public void decFarmers() {
        this.farmerSlider = moveSlider(this.farmerSlider, -1, minFarmers());
    }

    public void incFarmers() {
        this.farmerSlider = moveSlider(this.farmerSlider, 1, 0);
    }

    public void decSoldiers() {
        this.soldierSlider = moveSlider(this.soldierSlider, -1, 0);
    }

    public void incSoldiers() {
        this.soldierSlider = moveSlider(this.soldierSlider, 1, 0);
    }

    private int moveSlider(int sliderValue, int delta, double minimumPop) {
        int sum = this.builderSlider + this.farmerSlider + this.soldierSlider + delta;
        Log.d(LOG_TAG, "moveSlider, sum: " + sum + ", delta: " + delta + ", slider value: " + sliderValue + ", minimum population: " + minimumPop);

        if (sum < 0 || sum > SliderTicks) {
            Log.d(LOG_TAG, "moveSlider, sum out of bounds, no change");
            return sliderValue;
        }

        sliderValue += delta;
        int minSlider = (int) Math.ceil(SliderTicks * minimumPop / this.roundedPop());
        Log.d(LOG_TAG, "moveSlider, minimum slider value: " + minSlider + ", desired value: " + sliderValue);

        if (sliderValue < minSlider) {
            sliderValue = minSlider;
        }
        if (sliderValue > SliderTicks) {
            sliderValue = (int) SliderTicks;
        }

        return sliderValue;
    }

    private double minBuilders() {
        return this.roundedPop() / this.builderEfficiency();
    }

    private double minFarmers() {
        return this.roundedPop() * (1 + Mortality / NatalityPerFood) / this.farmerEfficiency();
    }

    public int getSelectedTech() {
        return this.selectedTech;
    }

    public void selectTech(int i) {
        if (i < 0 || i > 5)
            return;

        this.selectedTech = i;
    }

    /*
        Derivative values
     */
    public double roundedPop()
    {
        return Math.floor(this.population);
    }

    private int farmers() {
        return (int) (this.roundedPop() * this.farmerSlider / SliderTicks);
    }

    private int builders() {
        return (int) (this.roundedPop() * this.builderSlider / SliderTicks);
    }

    private int soldiers() {
        return (int) (this.roundedPop() * this.soldierSlider / SliderTicks);
    }

    private int scholars() {
        return (int)this.roundedPop() - this.farmers() - this.builders() - this.soldiers();
    }

    private double builderEfficiency() {
        return BuilderBase + this.building.level * BuildingTechBonus;
    }

    private double farmerEfficiency() {
        return FarmingBase + this.farming.level * FarmingTechBonus;
    }

    public int realDeltaPop() {
        return Utils.integerDelta(this.population, this.deltaPop());
    }

    public double deltaWalls() {
        return Math.max(
                (this.builders() * builderEfficiency() - this.roundedPop() - (int) this.walls) / WallCost,
                0);
    }

    public double militaryStrength() {
        int civilians = (int)this.roundedPop() - this.soldiers();

        int wallSoldiers = (int) Math.min(this.soldiers(), this.walls);
        int groundSoldiers = this.soldiers() - wallSoldiers;
        int wallCivils = Math.min((int) this.walls - wallSoldiers, civilians);
        int groundCivils = civilians - wallCivils;

        return groundCivils * CivilStrength +
                wallCivils * WallCivilStrength +
                (groundSoldiers * SoldierStrength +
                wallSoldiers * WallSoldierStrength) * (1 + this.soldiering.level * SoldieringTechBonus);
    }

    public double demonIndividualStrength()
    {
        return DemonStrength * Math.pow(this.difficulty.getDemonPowerBase(), this.demonLevel);
    }

    public double deltaResearch() {
        return (this.roundedPop() * ResearchBase + this.scholars() * ScholarResearch) * (1 + this.scholarship.level * ResearchTechBonus);
    }

    public boolean isOver() {
        return this.population < 1 || this.demonBanishCost <= 0;
    }

    public boolean isPlayerAlive() {
        return this.population >= 1;
    }

    private double deltaPop() {
        return (this.farmers() * farmerEfficiency() - this.roundedPop()) * NatalityPerFood - this.roundedPop() * Mortality;
    }
    /*
        Turn processing
     */
    public void endTurn() {
        Log.d(LOG_TAG, "endTurn, turn: " + this.turn);

        this.reportFirstBanish &= this.reportHellgateClose <= 0;
        this.reportAttackers = 0;
        this.reportHellgateClose = 0;
        this.reportHellgateOpen = 0;
        this.reportVictims = 0;
        this.turn++;

        Log.d(LOG_TAG, "endTurn, population: " + this.population + ", delta: " + this.deltaPop());
        this.population += this.deltaPop();
        this.population = Utils.clamp(this.population, 0, MaxPopulation);
        Log.d(LOG_TAG, "endTurn, postgrowth population: " + this.population);

        Log.d(LOG_TAG, "endTurn, walls: " + this.walls + ", delta: " + this.deltaWalls());
        this.walls += this.deltaWalls();
        if (this.walls > MaxPopulation)
            this.walls = MaxPopulation;
        Log.d(LOG_TAG, "endTurn, postbuild walls: " + this.walls);

        this.doResearch();
        this.doCombat();
        this.spawnDemons();
        this.doScouting();
        this.correctSliders();
    }

    private void doResearch() {
        double researchPoints = this.deltaResearch();
        Log.d(LOG_TAG, "doResearch, delta: " + researchPoints);

        switch (this.selectedTech) {
            case 0:
                Log.d(LOG_TAG, "doResearch invest in farming");
                this.farming.Invest(researchPoints);
                break;
            case 1:
                Log.d(LOG_TAG, "doResearch invest in building");
                this.building.Invest(researchPoints);
                break;
            case 2:
                Log.d(LOG_TAG, "doResearch invest in soldiering");
                this.soldiering.Invest(researchPoints);
                break;
            case 3:
                Log.d(LOG_TAG, "doResearch invest in scholarship");
                this.scholarship.Invest(researchPoints);
                break;
            case 4:
                Log.d(LOG_TAG, "doResearch invest in banishment, demonBanishCost: " + this.demonBanishCost);
                this.demonBanishCost -= (int) researchPoints;
                if (this.demonBanishCost < 0)
                    this.demonBanishCost = 0;
                Log.d(LOG_TAG, "doResearch postinvest demonBanishCost: " + this.demonBanishCost);

                int gateDelta = (int) (researchPoints / 100);
                Log.d(LOG_TAG, "doResearch gates: " + this.demonGates + ", delta: " + gateDelta);
                if (gateDelta > this.demonGates)
                    gateDelta = this.demonGates;
                this.reportHellgateClose = this.demonGates / 1000 - (this.demonGates - gateDelta) / 1000;
                this.demonGates -= gateDelta;
                Log.d(LOG_TAG, "doResearch final gates: " + this.demonGates + ", report: " + this.reportHellgateClose);

                break;
        }
    }

    private void doCombat() {
        if (rand.nextDouble() * this.roundedPop() > this.demons) {
            this.demonLevel++;

            Log.d(LOG_TAG, "doCombat no attack, demonLevel: " + this.demonLevel);
            return;
        }

        int attackers = rand.nextInt(this.demons + 1);
        double defenderStr = this.militaryStrength();
        double peopleStr = defenderStr / this.roundedPop();
        Log.d(LOG_TAG, "doCombat attack!, attackers: " + attackers + " out of " + this.demons + ", defenderStr: " + defenderStr + ", peopleStr: " + peopleStr);

        this.demons -= attackers;
        this.reportAttackers = attackers;
        double demonStrBonus = Math.pow(this.difficulty.getDemonPowerBase(), this.demonLevel);
        double attackerStr = attackers * DemonStrength * demonStrBonus;
        Log.d(LOG_TAG, "doCombat demonLevel: " + this.demonLevel + ", demonStrBonus: " + demonStrBonus + ", attackerStr: " + attackerStr);

        for (int i = 0; attackerStr > 0 && defenderStr > 0 && i < CombatRounds; i++) {
            if (rand.nextDouble() > 0.5) {
                defenderStr -= attackerStr * rand.nextDouble();
                Log.d(LOG_TAG, "doCombat round: " + i + ", demons first, defenderStr down to " + defenderStr);
                if (defenderStr < 0)
                    break;
                attackerStr -= defenderStr * rand.nextDouble();
                Log.d(LOG_TAG, "doCombat humans retaliate, attackerStr down to " + attackerStr);
            } else {
                attackerStr -= defenderStr * rand.nextDouble();
                Log.d(LOG_TAG, "doCombat round: " + i + ", humans first, attackerStr down to " + attackerStr);
                if (attackerStr < 0)
                    break;
                defenderStr -= attackerStr * rand.nextDouble();
                Log.d(LOG_TAG, "doCombat demons retaliate, defenderStr down to " + defenderStr);
            }
        }

        if (attackerStr < 0)
            attackerStr = 0;
        if (defenderStr < 0)
            defenderStr = 0;

        this.demons += (int)(attackerStr / DemonStrength / demonStrBonus);
        if (this.demons < 0)
            this.demons = 0;

        this.reportVictims = (int) ((this.militaryStrength() - defenderStr) / peopleStr);
        this.population -= this.reportVictims;

        this.reportScoutedDemons -= this.reportAttackers;
        if (this.reportScoutedDemons < 0)
            this.reportScoutedDemons = 0;

        Log.d(LOG_TAG, "doCombat combat ended, attackerStr: " + attackerStr + ", defenderStr: " + defenderStr + ", demons left: " + this.demons + ", victims: " + this.reportVictims + ", population left: " + this.population);
    }

    private void spawnDemons() {
        if (this.demonBanishCost > 0) {
            double gatesDelta = this.roundedPop() + this.demonBanishCost / 100.0;
            Log.d(LOG_TAG, "spawnDemons, gatesDelta: " + gatesDelta + ", previous gates: " + this.demonGates + ", old banish cost: " + this.demonBanishCost);

            if (this.demonGates + gatesDelta > MaxDemonGates)
                gatesDelta = MaxDemonGates - this.demonGates;
            this.demonGates += gatesDelta;
            this.reportHellgateOpen = (int) (gatesDelta / 1000);

            this.banishCostGrowth = Math.max(this.demonGates / 100, (int)this.roundedPop());
            this.demonBanishCost += this.banishCostGrowth;
            if (this.demonBanishCost > MaxBanishCost)
                this.demonBanishCost = MaxBanishCost;

            Log.d(LOG_TAG, "spawnDemons, gates: " + this.demonGates + ", banish cost delta: " + this.banishCostGrowth + ", new banish cost: " + this.demonBanishCost);
        }
        else
            Log.d(LOG_TAG, "spawnDemons, banished, no new gates");

        if (this.demonGates > 0) {
            Log.d(LOG_TAG, "spawnDemons, gates: " + this.demonGates + ", spawn factor: " + this.difficulty.getDemonSpawnFactor() + ", old demon count: " + this.demons);

            this.demons += this.demonGates * this.difficulty.getDemonSpawnFactor() / 1000;
            if (this.demons > MaxPopulation)
                this.demons = MaxPopulation;

            Log.d(LOG_TAG, "spawnDemons, new demon count: " + this.demons);
        }
        else
            Log.d(LOG_TAG, "spawnDemons, no gates, no new demons");
    }

    private void doScouting() {
        if (this.demons > this.reportScoutedDemons) {
            Log.d(LOG_TAG, "doScouting, scouting up to " + (this.demons - this.reportScoutedDemons));
            this.reportScoutedDemons += rand.nextInt(this.demons - this.reportScoutedDemons);
        }
        else {
            Log.d(LOG_TAG, "doScouting, scouts spotted all demons");
            this.reportScoutedDemons = this.demons;
        }
    }

    private void correctSliders() {
        Log.d(LOG_TAG, "correctSliders, adjusting minimum farmers");
        this.farmerSlider = moveSlider(this.farmerSlider, 0, this.minFarmers());

        Log.d(LOG_TAG, "correctSliders, adjusting minimum builders");
        this.builderSlider = moveSlider(this.builderSlider, 0, this.minBuilders());

        Log.d(LOG_TAG, "correctSliders, adjusting overflow on farmers, overflow: " + this.sliderOverflow());
        while (this.sliderOverflow() > 0 && this.moveSlider(this.farmerSlider, -1, this.minFarmers()) != this.farmerSlider)
            this.farmerSlider--;

        Log.d(LOG_TAG, "correctSliders, adjusting overflow on soldiers, overflow: " + this.sliderOverflow());
        while (this.sliderOverflow() > 0 && this.soldierSlider > 0)
            this.soldierSlider--;

        Log.d(LOG_TAG, "correctSliders, adjusting overflow on builders, overflow: " + this.sliderOverflow());
        while (this.sliderOverflow() > 0 && this.moveSlider(this.builderSlider, -1, minBuilders()) != this.builderSlider)
            this.builderSlider--;
    }

    private int sliderOverflow() {
        return this.farmerSlider + this.builderSlider + this.soldierSlider - (int) SliderTicks;
    }

    /*
        Saving and loading
     */
    public double[] save() {
        return new double[]
                {
                        this.difficulty.getIndex(),
                        this.turn,
                        this.population,
                        this.walls,
                        this.demons,
                        this.demonLevel,
                        this.demonGates,
                        this.demonBanishCost,

                        this.farmerSlider,
                        this.builderSlider,
                        this.soldierSlider,
                        this.selectedTech,

                        this.farming.level,
                        this.farming.points,
                        this.building.level,
                        this.building.points,
                        this.soldiering.level,
                        this.soldiering.points,
                        this.scholarship.level,
                        this.scholarship.points,

                        this.reportAttackers,
                        this.reportHellgateClose,
                        this.reportHellgateOpen,
                        this.reportVictims,
                        this.reportScoutedDemons,
                        this.banishCostGrowth,
                };
    }

    public void load(ByteBuffer data) {
        this.difficulty = Difficulty.Levels[(int) data.getDouble()];
        this.turn = (int) data.getDouble();
        this.population = (int) data.getDouble();
        this.walls = data.getDouble();
        this.demons = (int) data.getDouble();
        this.demonLevel = (int) data.getDouble();
        this.demonGates = (int) data.getDouble();
        this.demonBanishCost = (int) data.getDouble();

        this.farmerSlider = (int) data.getDouble();
        this.builderSlider = (int) data.getDouble();
        this.soldierSlider = (int) data.getDouble();
        this.selectedTech = (int) data.getDouble();

        this.farming.level = (int) data.getDouble();
        this.farming.points = data.getDouble();
        this.building.level = (int) data.getDouble();
        this.building.points = data.getDouble();
        this.soldiering.level = (int) data.getDouble();
        this.soldiering.points = data.getDouble();
        this.scholarship.level = (int) data.getDouble();
        this.scholarship.points = data.getDouble();

        this.reportAttackers = (int) data.getDouble();
        this.reportHellgateClose = (int) data.getDouble();
        this.reportHellgateOpen = (int) data.getDouble();
        this.reportVictims = (int) data.getDouble();
        this.reportScoutedDemons = (int) data.getDouble();
        this.banishCostGrowth = (int) data.getDouble();
    }
}
