package hr.kravarscan.enchantedfortress.storage;

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

@SuppressWarnings("unused")
public enum LatestSaveKeys {
    VERSION,

    DIFFICULTY,
    TURN,
    POPULATION,
    WALLS,
    DEMONS,
    DEMON_LEVEL,
    DEMON_GATES,
    DEMON_BANISH_COST,

    FARMER_SLIDER,
    BUILDER_SLIDER,
    SOLDIER_SLIDER,
    SELECTED_TECH,

    FARMING_LEVEL,
    FARMING_POINTS,
    BUILDING_LEVEL,
    BUILDING_POINTS,
    SOLDIERING_LEVEL,
    SOLDIERING_POINTS,
    SCHOLARSHIP_LEVEL,
    SCHOLARSHIP_POINTS,

    REPORT_ATTACKERS,
    REPORT_HELLGATE_CLOSE,
    REPORT_HELLGATE_OPEN,
    REPORT_VICTIMS,
    REPORT_SCOUTED_DEMONS,
    BANISH_COST_GROWTH,

    KEY_COUNT
}