package hr.kravarscan.enchantedfortress;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import hr.kravarscan.enchantedfortress.logic.Game;

public class GameFragment extends Fragment {

    private Game game = new Game();
    private String[] techList = new String[5];
    private ArrayAdapter<String> techListAdapter;
    private TextView popInfo;
    private TextView farmerInfo;
    private TextView builderInfo;
    private TextView soldierInfo;
    private TextView researchInfo;
    private Button endTurnButton;

    private OnFragmentInteractionListener listener;

    interface OnFragmentInteractionListener {
        void onGameOver();
    }

    public GameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        view.findViewById(R.id.farmPlusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.incFarmers();
                updateInfo();
            }
        });

        view.findViewById(R.id.farmMinusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.decFarmers();
                updateInfo();
            }
        });

        view.findViewById(R.id.builderPlusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.incBuilders();
                updateInfo();
            }
        });

        view.findViewById(R.id.builderMinusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.decBuilders();
                updateInfo();
            }
        });

        view.findViewById(R.id.soldierPlusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.incSoldiers();
                updateInfo();
            }
        });

        view.findViewById(R.id.soldierMinusButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game.decSoldiers();
                updateInfo();
            }
        });

        this.endTurnButton = (Button) view.findViewById(R.id.endTurnButton);
        this.endTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endTurn();
            }
        });

        Spinner researchSelector = (Spinner) view.findViewById(R.id.researchSelection);
        this.updateTechList();
        this.techListAdapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, this.techList);
        researchSelector.setAdapter(this.techListAdapter);
        researchSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                game.selectTech(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //No operation
            }
        });
        this.farmerInfo = (TextView) view.findViewById(R.id.farmText);
        this.builderInfo = (TextView) view.findViewById(R.id.builderText);
        this.popInfo = (TextView) view.findViewById(R.id.popText);
        this.soldierInfo = (TextView) view.findViewById(R.id.soliderText);
        this.researchInfo = (TextView) view.findViewById(R.id.researchText);

        this.updateInfo();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void updateTechList() {
        this.techList[0] = getResources().getString(R.string.farmingTech);
        this.techList[1] = getResources().getString(R.string.buildTech);
        this.techList[2] = getResources().getString(R.string.soldierTech);
        this.techList[3] = getResources().getString(R.string.scholarTech);
        this.techList[4] = getResources().getString(R.string.banishTech);
    }

    private void endTurn() {
        if (this.game.isOver()) {
            this.listener.onGameOver();
            return;
        }

        this.game.endTurn();
        this.updateInfo();
    }

    private void updateInfo()
    {
        this.farmerInfo.setText(sliderText(R.string.farmers, game.farmerSlider, R.string.popDelta, game.deltaPop()));
        this.builderInfo.setText( sliderText(R.string.builders, game.builderSlider, R.string.wallDelta, (int)game.deltaWalls()));
        this.soldierInfo.setText(sliderText(R.string.soldiers, game.soldierSlider, R.string.militaryStrength, game.militaryStrength()));
        this.researchInfo.setText(sliderText(R.string.scholars, game.getScholarSlider(), R.string.researchDelta, (int)game.deltaResearch()));

        this.updateTechList();
        this.techListAdapter.notifyDataSetChanged();

        if (this.game.isOver()) {
            this.popInfo.setText(this.game.population <= 0 ? R.string.defeat : R.string.victory);
            this.endTurnButton.setText(R.string.gameOver);
        }
        else
            this.popInfo.setText(getResources().getString(R.string.population) + ": " + Integer.toString(this.game.population) + "\n" +
                    getResources().getString(R.string.walls) + ": " + Integer.toString((int)this.game.walls) + "\n" +
                    getResources().getString(R.string.scouted) + ": " + Integer.toString(this.game.reportScoutedDemons) + " " + getResources().getString(R.string.demons) +
                    this.battleInfo()
            );
    }

    private String sliderText(int sliderTextId, int sliderValue, int descriptionTextId, int effectValue)
    {
        Integer percents = (int) (sliderValue * (100 / Game.SliderTicks));
        Integer effect = effectValue;

        return getResources().getString(sliderTextId) + ": " + percents.toString() + "%\n" +
                getResources().getString(descriptionTextId) + ": " + effect.toString();
    }

    private String battleInfo() {
        if (this.game.reportAttackers <= 0)
            return "";

        if (this.game.reportVictims > 0)
            return "\n" + getResources().getString(R.string.attacked) + Integer.toString(this.game.reportAttackers) + getResources().getString(R.string.victims) + Integer.toString(this.game.reportVictims);
        else
            return "\n" + getResources().getString(R.string.noVictims);
    }
}
