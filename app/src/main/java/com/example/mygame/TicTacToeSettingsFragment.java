package com.example.mygame;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;


public class TicTacToeSettingsFragment extends Fragment{

    View root;
    private RadioGroup radioGroupForFields, radioGroupForPlayers, radioGroupForDiff;
    private RadioButton field3x3, field4x4, field5x5, field6x6, multi, pvE, pvP, diffLow, diffMed, diffHARD;
    int numberOfPlayers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_tic_tac_toe_settings, container, false);
        radioGroupForFields = (RadioGroup) root.findViewById(R.id.groupFieldSizes);
        radioGroupForPlayers = (RadioGroup) root.findViewById(R.id.groupPlayTypes);
        radioGroupForDiff = (RadioGroup) root.findViewById(R.id.groupDiff);

        field3x3 = (RadioButton) root.findViewById(R.id.smallfield);
        field4x4 = (RadioButton) root.findViewById(R.id.mediumfield);
        field5x5 = (RadioButton) root.findViewById(R.id.largefield);
        field6x6 = (RadioButton) root.findViewById(R.id.ultralargefield);
        pvE = (RadioButton) root.findViewById(R.id.pve);
        pvP = (RadioButton) root.findViewById(R.id.pvp);
        multi = (RadioButton) root.findViewById(R.id.multi);
        diffLow = (RadioButton) root.findViewById(R.id.diffLow);
        diffMed = (RadioButton) root.findViewById(R.id.diffMed);
        diffHARD = (RadioButton) root.findViewById(R.id.diffMAX);

        pvP.setOnClickListener(radioButtionClickListener);
        pvE.setOnClickListener(radioButtionClickListener);
        multi.setOnClickListener(radioButtionClickListener);
        diffLow.setOnClickListener(radioButtionClickListener);
        diffMed.setOnClickListener(radioButtionClickListener);
        diffHARD.setOnClickListener(radioButtionClickListener);



        return root;
    }

    View.OnClickListener radioButtionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            switch (rb.getId()){
                case R.id.smallfield:
                    break;
                case R.id.mediumfield:
                    break;
                case R.id.largefield:
                    break;
                case R.id.ultralargefield:
                    break;
                case R.id.pve:
                    diffLow.setClickable(true);
                    diffMed.setClickable(true);
                    diffHARD.setClickable(true);
                    break;
                case R.id.multi:
                case R.id.pvp:
                    diffLow.setClickable(false);
                    diffMed.setClickable(false);
                    diffHARD.setClickable(false);
                    break;

            }
            onPause();
        }
    };

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);

        bundle.putBoolean("field3x3", field3x3.isChecked());
        bundle.putBoolean("field4x4", field4x4.isChecked());
        bundle.putBoolean("field5x5", field5x5.isChecked());
        bundle.putBoolean("field6x6", field6x6.isChecked());
        bundle.putBoolean("pvE", pvE.isChecked());
        bundle.putBoolean("pvP", pvP.isChecked());
        bundle.putBoolean("multi", multi.isChecked());
        bundle.putBoolean("HARD", diffHARD.isChecked());
        bundle.putBoolean("Med", diffMed.isChecked());
        bundle.putBoolean("Low", diffLow.isChecked());
        //Toast.makeText(root.getContext(), "onSave", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onViewStateRestored(Bundle bundle){
        super.onViewStateRestored(bundle);
        if(bundle != null) {
            field3x3.setChecked(bundle.getBoolean("field3x3", false));
            field4x4.setChecked(bundle.getBoolean("field4x4", false));
            field5x5.setChecked(bundle.getBoolean("field5x5", false));
            field6x6.setChecked(bundle.getBoolean("field6x6", false));
            pvE.setChecked(bundle.getBoolean("pvE", false));
            pvP.setChecked(bundle.getBoolean("pvP", false));
            multi.setChecked(bundle.getBoolean("multi", false));
            diffHARD.setChecked(bundle.getBoolean("HARD", false));
            diffMed.setChecked(bundle.getBoolean("Med", false));
            diffLow.setChecked(bundle.getBoolean("Low", false));
        }
        //Toast.makeText(root.getContext(), "onView", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause(){
        super.onPause();
        //Toast.makeText(root.getContext(), "onPause", Toast.LENGTH_LONG).show();
        Bundle bundle = new Bundle();
        //Сохраняем лишь размер поля и количество игроков
        int fieldSize = 3;
        if(field4x4.isChecked()) fieldSize = 4;
        if(field5x5.isChecked()) fieldSize = 5;
        if(field6x6.isChecked()) fieldSize = 6;
        bundle.putInt("fieldSize", fieldSize);

        int numPlayers = 2;
        if(pvE.isChecked()) numPlayers = 1;
        if(multi.isChecked()) numPlayers = -1;
        bundle.putInt("numPlayers", numPlayers);

        int difficulty = 1;
        if(diffMed.isChecked()) difficulty = 2;
        if(diffHARD.isChecked()) difficulty = 3;
        bundle.putInt("diff", difficulty);

        ((MenuActivity)getActivity()).saveSettingsFromFragment(bundle);
    }

    @Override
    public void onResume(){
        super.onResume();
        //Toast.makeText(root.getContext(), "onResume", Toast.LENGTH_LONG).show();
        Bundle bundle = ((MenuActivity)getActivity()).getSettingsFromFragment();
        int numberOfSquares = bundle.getInt("fieldSize");
        switch (numberOfSquares){
            case 4:
                field4x4.setChecked(true);
                break;
            case 5:
                field5x5.setChecked(true);
                break;
            case 6:
                field6x6.setChecked(true);
                break;
            default:
                field3x3.setChecked(true);
        }
        int numberOfPlayers = bundle.getInt("numPlayers");
        switch (numberOfPlayers){
            case -1:
                multi.setChecked(true);
                diffLow.setClickable(false);
                diffMed.setClickable(false);
                diffHARD.setClickable(false);
                break;
            case 1:
                pvE.setChecked(true);
                diffLow.setClickable(true);
                diffMed.setClickable(true);
                diffHARD.setClickable(true);
                break;
            default:
                pvP.setChecked(true);
                diffLow.setClickable(false);
                diffMed.setClickable(false);
                diffHARD.setClickable(false);
                break;
        }
        int difficulty = bundle.getInt("diff");
        switch (difficulty){
            case 3:
                diffHARD.setChecked(true);
                break;
            case 2:
                diffMed.setChecked(true);
                break;
            default:
                diffLow.setChecked(true);
        }
    }


}
