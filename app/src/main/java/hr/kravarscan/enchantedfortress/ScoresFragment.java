package hr.kravarscan.enchantedfortress;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import hr.kravarscan.enchantedfortress.storage.HighScores;
import hr.kravarscan.enchantedfortress.storage.ScoreEntry;


public class ScoresFragment extends AAttachableFragment {

    public ScoresFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scores, container, false);

        CharSequence[] difficultyNames = this.getActivity().getResources().getTextArray(R.array.difficultyLevels);
        ViewGroup listView = view.findViewById(R.id.scoreList);

        for (ScoreEntry score : HighScores.get().getAll()) {
            View itemView = LayoutInflater.from(this.getActivity()).inflate(R.layout.score_entry_view, listView, false);

            ((TextView)itemView.findViewById(R.id.turnsText)).setText(Integer.toString(score.getTurn()));
            ((TextView)itemView.findViewById(R.id.difficultyText)).setText(difficultyNames[score.getDifficulty()]);

            listView.addView(itemView);
        }

        return view;
    }

    @Override
    public void attach(Object listener) {
    }
}
