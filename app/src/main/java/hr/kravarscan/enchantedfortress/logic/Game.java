package hr.kravarscan.enchantedfortress.logic;

import java.util.Random;

/**
 * Copyright 2017 Ivan Kravarščan
 * <p>
 * This file is part of Enchanted Fortress.
 * <p>
 * Enchanted Fortress is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * Enchanted Fortess is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with Enchanted Fortress.  If not, see <http://www.gnu.org/licenses/>.
 */

public class Game {
    private static final double WallCost = 100;
    private static final int DemonStrength = 5;
    private static final int CombatRounds = 10;
    private static final int MaxBanishCost = 10000000;
    private static final int MaxDemonGates = 100000000;
    private static final int MaxPopulation = 100000;
    private static Random rand = new Random();

    public int turn = 0;
    public int population = 100;
    public double walls = 0;
    private int demons = 0;
    private int demonGates = 0;
    private int demonBanishCost = 10000;

    public static final double SliderTicks = 10;
    public int farmerSlider = 5;
    public int builderSlider = 0;
    public int soldierSlider = 1;
    private int selectedTech = 0;

    Technology farming = new Technology();
    Technology building = new Technology();
    Technology soldiering = new Technology();
    Technology scholarship = new Technology();

    public int reportAttackers = 0;
    public int reportVictims = 0;
    public int reportScoutedDemons = 0;

    /*
        Sliders
     */
    public int getScholarSlider() {
        return (int)SliderTicks - this.farmerSlider - this.builderSlider - this.soldierSlider;
    }

    public void decBuilders() {
        this.builderSlider = moveSlider(this.builderSlider, -1);
    }

    public void incBuilders() {
        this.builderSlider = moveSlider(this.builderSlider, 1);
    }

    public void decFarmers() {
        this.farmerSlider = moveSlider(this.farmerSlider, -1);
    }

    public void incFarmers() {
        this.farmerSlider = moveSlider(this.farmerSlider, 1);
    }

    public void decSoldiers() {
        this.soldierSlider = moveSlider(this.soldierSlider, -1);
    }

    public void incSoldiers() {
        this.soldierSlider = moveSlider(this.soldierSlider, 1);
    }

    private int moveSlider(int sliderValue, int delta) {
        int sum = this.builderSlider + this.farmerSlider + this.soldierSlider + delta;
        if (sum < 0 || sum > SliderTicks)
            return sliderValue;

        sliderValue += delta;

        if (sliderValue < 0) {
            sliderValue = 0;
        }
        if (sliderValue > SliderTicks) {
            sliderValue = (int) SliderTicks;
        }

        return  sliderValue;
    }

    public void selectTech(int i) {
        if (i < 0 || i > 5)
            return;

        this.selectedTech = i;
    }

    /*
        Derivate values
     */
    private int farmers()
    {
        return (int) (this.population * this.farmerSlider / SliderTicks);
    }

    private int builders()
    {
        return (int) (this.population * this.builderSlider / SliderTicks);
    }

    private int soldiers()
    {
        return (int) (this.population * this.soldierSlider / SliderTicks);
    }

    private int scholars()
    {
        return this.population - this.farmers() - this.builders() - this.soldiers();
    }

    public int deltaPop()
    {
        return (int)((this.farmers() * (3 + this.farming.level / 12.0) - this.population) / 20.0 - this.population / 50.0);
    }

    public double deltaWalls() {
        return (this.builders() * (10 + this.building.level) - this.population - (int)this.walls) / WallCost;
    }

    public int militaryStrength() {
        int civilians = this.population - this.soldiers();

        int wallSoldiers = (int)Math.min(this.soldiers(), this.walls);
        int groundSoldiers = this.soldiers() - wallSoldiers;
        int wallCivils = Math.min((int)this.walls - wallSoldiers, civilians);
        int groundCivils = civilians - wallCivils;

        return groundCivils + wallCivils * 4 + (groundSoldiers + wallSoldiers * 2) * (10 + this.soldiering.level);
    }

    public double deltaResearch() {
        return (this.population / 10.0 + this.scholars() * 0.9) * (1 + this.scholarship.level / 8.0);
    }

    /*
        Turn processing
     */
    public void endTurn() {
        this.reportAttackers = 0;
        this.reportVictims = 0;
        this.turn++;

        this.population += this.deltaPop();
        if (this.population > MaxPopulation)
            this.population = MaxPopulation;

        this.walls += this.deltaWalls();
        if (this.walls > MaxPopulation)
            this.walls = MaxPopulation;

        this.doResearch();
        this.doCombat();
        this.spawnDemons();
        this.doScouting();

        /*float limitf;
        float ide;
        int i, limit;
        char tmps[200];

        if (this.populacija <= 0)
        {
            sprintf(tmps, "Demoni su pobili svo stanovnistvo u %d godini vladavine, izgubio si!", this.izvjestaj_godina);
            MessageBox(hwndGlobal, tmps, "Poraz", MB_OK);
            EndDialog(hwndGlobal, 0);
            return;
        }

        if (this.populacija >= 1000000 && this.demon_kraj <= 0 && this.demonGates <= 0)
        {
            sprintf(tmps, "Demoni su protjerani i tvrđava je dom milijun ljudi, pobjedio si u %d godini!", this.izvjestaj_godina);
            MessageBox(hwndGlobal, tmps, "Pobjeda", MB_OK);
            EndDialog(hwndGlobal, 0);
            return;
        }*/
    }

    private void doResearch() {
        double researchPoints = this.deltaResearch();

        switch (this.selectedTech)
        {
            case 0:
                this.farming.Invest(researchPoints);
                break;
            case 1:
                this.building.Invest(researchPoints);
                break;
            case 2:
                this.soldiering.Invest(researchPoints);
                break;
            case 3:
                this.scholarship.Invest(researchPoints);
                break;
            case 4:
                this.demonBanishCost -= (int) researchPoints;
                this.demonGates -= (int) (researchPoints / 100);
                if (this.demonGates<0)
                    this.demonGates = 0;
                break;
        }
        /*while(this.poeni_teh>0)
        {
            switch(this.tren_teh)
            {
                ...

                case 4:
                    if (this.demon_kraj>0) this.demon_kraj -= this.poeni_teh;
                    if (this.demonGates>0) this.demonGates -= this.poeni_teh;
                    if (this.demonGates<0) this.demonGates = 0;
                    this.poeni_teh = 0;
                    if (this.demon_kraj <= 0 && this.demonGates <= 0)
                        MessageBox(hwndGlobal, "Veze s demonskim svijetom su prekinute, demoni više nemogu dolaziti u naš svijet.", "Istjerivanje demona", MB_OK);
                    break;
            }
        }*/
    }

    private void doCombat() {
        if (rand.nextDouble() * this.population > this.demons)
            return;

        int attackers = rand.nextInt(this.demons + 1);
        int defenderStr = this.militaryStrength();
        float peopleStr = (float) defenderStr / this.population;

        this.demons -= attackers;
        this.reportAttackers = attackers;
        attackers *= DemonStrength;

        for (int i = 0; attackers > 0 && defenderStr > 0 && i < CombatRounds; i++) {
            if (rand.nextDouble() > 0.5) {
                defenderStr -= (int) (attackers * rand.nextDouble());
                if (defenderStr < 0)
                    continue;
                attackers -= (int) (defenderStr * rand.nextDouble());
            } else {
                attackers -= (int) (defenderStr * rand.nextDouble());
                if (attackers < 0)
                    continue;
                defenderStr -= (int) (attackers * rand.nextDouble());
            }
        }

        if (attackers < 0)
            attackers = 0;
        if (defenderStr < 0)
            defenderStr = 0;

        attackers /= DemonStrength;
        this.reportScoutedDemons = (int) (this.reportScoutedDemons * this.demons / (double)(this.demons + attackers) + attackers);
        this.demons += attackers / DemonStrength;
        if (this.demons < 0)
            this.demons = 0;

        this.reportVictims = (int) ((this.militaryStrength() - defenderStr) / peopleStr);
        this.population -= this.reportVictims;
    }

    private void spawnDemons() {
        if (this.demonBanishCost > 0)
        {
            this.demonGates += this.population + this.demonBanishCost / 100;
            if (this.demonGates > MaxDemonGates)
                this.demonGates = MaxDemonGates;

            int deltaCost = this.demonGates / 100;
            this.demonBanishCost += deltaCost < this.population ? this.population : deltaCost;
            if (this.demonBanishCost > MaxBanishCost)
                this.demonBanishCost = MaxBanishCost;
        }

        if (this.demonGates > 0)
        {
            this.demons += this.demonGates / 1000;
            if (this.demons > MaxPopulation) this.demons = MaxPopulation;
        }
    }

    private void doScouting() {
        if (this.demons > this.reportScoutedDemons)
            this.reportScoutedDemons += rand.nextInt(this.demons - this.reportScoutedDemons);
        else {
            this.reportScoutedDemons = this.demons;
        }
    }

    public boolean isOver() {
        return this.population <= 0 || this.demonBanishCost <= 0;
    }
}
