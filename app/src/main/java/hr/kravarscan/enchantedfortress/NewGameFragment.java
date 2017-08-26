package hr.kravarscan.enchantedfortress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import hr.kravarscan.enchantedfortress.logic.Difficulty;

public class NewGameFragment extends AAttachableFragment {

    private NewGameFragment.OnFragmentInteractionListener listener;
    private Spinner difficultySelector;

    private TextView startPopText;
    private TextView demonSpawnText;

    public NewGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_game, container, false);

        this.startPopText = (TextView) view.findViewById(R.id.difficultyStartPop);
        this.demonSpawnText = (TextView) view.findViewById(R.id.difficultyDemonSpawn);

        this.difficultySelector = view.findViewById(R.id.difficultySelection);
        difficultySelector.setAdapter(ArrayAdapter.createFromResource(view.getContext(), R.array.difficultyLevels, R.layout.research_item));
        difficultySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Difficulty data = Difficulty.Levels[i];

                int visibility = (data == Difficulty.Medium) ? View.GONE : View.VISIBLE;
                startPopText.setVisibility(visibility);
                demonSpawnText.setVisibility(visibility);

                double startPop = data.getStartingPop() / Difficulty.Medium.getStartingPop();
                startPopText.setText(getResources().getString(R.string.difficultyStartingPop, bonusPercent(startPop)));
                demonSpawnText.setText(getResources().getString(R.string.difficultyDemonSpawn, bonusPercent(data.getDemonSpawnFactor())));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //No operation
            }
        });
        difficultySelector.setSelection(Difficulty.Medium.getIndex());

        view.findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onNewGameStart(difficultySelector.getSelectedItemPosition());
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void attach(Object listener) {
        if (listener instanceof OnFragmentInteractionListener) {
            this.listener = (OnFragmentInteractionListener) listener;
        } else {
            throw new RuntimeException(listener.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private static String bonusPercent(double factor)
    {
        String sign = factor >= 1 ? "+" : "";
        return sign + Integer.toString((int)(factor * 100 - 100)) + "%";
    }

    interface OnFragmentInteractionListener {
        void onNewGameStart(int difficulty);
    }
}
