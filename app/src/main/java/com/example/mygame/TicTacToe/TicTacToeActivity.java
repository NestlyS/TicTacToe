package com.example.mygame.TicTacToe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mygame.AnimatedBackground;
import com.example.mygame.BaseActivity;
import com.example.mygame.Bot.Bot;
import com.example.mygame.R;
import com.example.mygame.MenuActivity;
import com.example.mygame.TicTacToe.Game.BasicGame;
import com.example.mygame.TicTacToe.Game.Game;
import com.example.mygame.TicTacToe.Game.GameBuilderPack.Factory;
import com.example.mygame.TicTacToe.Game.Messangers.MessageSenderPlus;
import com.example.mygame.TicTacToe.Game.Square;
import com.example.mygame.TicTacToe.Listeners.ListenerBuilder;
import com.example.mygame.TicTacToe.Listeners.ListenerInterface;
import com.example.mygame.TicTacToe.multi.MultiplayerActivity;

import java.util.Vector;

public class TicTacToeActivity extends BaseActivity implements TicTacToeInterface{
    private int HEIGHT_OF_THE_GAME = 5;
    private int WIDTH_OF_THE_GAME = 5;
    private int NUMBER_OF_PLAYERS = 2;
    private int NUMBER_SYM_TO_WIN = 3;
    private int DIFFICULTY = 1;
    private final int DURATION_OF_LIGHT_ANIMATION = 3000;

    private boolean disconnected = false;
    private String numberOfDiscPlayer = "";

    private String DIFFICULTY_STATE;
    private String NUMBER_OF_SQUARES_SETTINGS;
    private String NUMBER_OF_PLAYER_SETTINGS;

    AnimatedBackground animatedBackground;
    private LinearLayout tableLayout;
    //private MultiplayerGame gameM;
    private BasicGame game;
    //private MultiplayerGameClient gameC;
    private Bot bot = null;

    private Drawable imagesButton[];
    private ImageButton[][] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InterfaceManager.getInstance().setActivity(this);

        setContentView(R.layout.activity_tic_tac_toe);
        tableLayout = (LinearLayout) findViewById(R.id.main_l);
        String APP_SETTINGS = getString(R.string.APP_SETTINGS);
        DIFFICULTY_STATE = getString(R.string.DIFFICULTY_STATE);
        NUMBER_OF_SQUARES_SETTINGS = getString(R.string.NUMBER_OF_SQUARES_SETTINGS);
        NUMBER_OF_PLAYER_SETTINGS = getString(R.string.NUMBER_OF_PLAYER_SETTINGS);

        SharedPreferences sharedPreferences = getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(NUMBER_OF_PLAYER_SETTINGS)){
            NUMBER_OF_PLAYERS = sharedPreferences.getInt(NUMBER_OF_PLAYER_SETTINGS, 0);
        }
        if(sharedPreferences.contains(NUMBER_OF_SQUARES_SETTINGS)){
            WIDTH_OF_THE_GAME = sharedPreferences.getInt(NUMBER_OF_SQUARES_SETTINGS, 0);
            HEIGHT_OF_THE_GAME = WIDTH_OF_THE_GAME;
        }
        if(sharedPreferences.contains(DIFFICULTY_STATE)){
            DIFFICULTY = sharedPreferences.getInt(DIFFICULTY_STATE, 1);
        }

        NUMBER_SYM_TO_WIN = HEIGHT_OF_THE_GAME;
        if(HEIGHT_OF_THE_GAME > 3) NUMBER_SYM_TO_WIN--;

        Bundle bundle = getIntent().getExtras();
        game = Factory.getBasic(bundle, HEIGHT_OF_THE_GAME, WIDTH_OF_THE_GAME, NUMBER_SYM_TO_WIN, DIFFICULTY, NUMBER_OF_PLAYERS);
        buildGameField();
        Factory.afterBuild(game, buttons);
    }

    @Override
    protected void onStart() {
        super.onStart();
        InterfaceManager.getInstance().setActivity(this);
        isDisconnected();
    }


    public void disconnected(String nameOfDiscPlayer){
        disconnected = true;
        numberOfDiscPlayer = nameOfDiscPlayer;
    }


    public void isDisconnected(){
        if(disconnected){
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(InterfaceManager
                    .getInstance()
                    .getActivity())
                    .setMessage(R.string.disconnected)
                    .setTitle(InterfaceManager
                            .getInstance()
                            .getActivity()
                            .getString(R.string.message_first_part)
                            + " "
                            + numberOfDiscPlayer
                            + " "
                            + InterfaceManager
                            .getInstance()
                            .getActivity()
                            .getString(R.string.message_second_part))
                    .setPositiveButton(InterfaceManager.getInstance().getActivity().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            InterfaceManager
                                    .getInstance()
                                    .getActivity()
                                    .startActivity(
                                            new Intent(InterfaceManager
                                                    .getInstance()
                                                    .getActivity(), MultiplayerActivity.class));
                            InterfaceManager
                                    .getInstance()
                                    .getActivity()
                                    .finish();
                        }
                    });
            alertDialog.show();
            //alertDialog.setOnDismissListener();
            //startActivity(new Intent(TicTacToeActivity.this, MultiplayerActivity.class));
            //finish();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        game.onExit();
        InterfaceManager.getInstance().resetActivity();
    }


    private void buildGameField(){
        HEIGHT_OF_THE_GAME = game.getField().length;
        WIDTH_OF_THE_GAME = game.getField()[0].length;
        buttons = new ImageButton[HEIGHT_OF_THE_GAME][WIDTH_OF_THE_GAME];
        tableLayout.setWeightSum(HEIGHT_OF_THE_GAME);
        for(int i = 0; i < HEIGHT_OF_THE_GAME; i++){
            LinearLayout tableRow = new LinearLayout(this);
            tableRow.setOrientation(LinearLayout.HORIZONTAL);
            tableRow.setWeightSum(WIDTH_OF_THE_GAME);
            //Space space = new Space(this);
            //space.setMinimumWidth(20);
            //tableRow.addView(space);
            for(int j = 0; j < WIDTH_OF_THE_GAME; j++){
                final ImageButton button = new ImageButton(getApplicationContext());
                buttons[i][j] = button;
                button.setImageResource(R.drawable.square);
                button.setScaleType(ImageView.ScaleType.FIT_CENTER);
                FrameLayout cardView = new FrameLayout(getApplicationContext());
                cardView.addView(button, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        FrameLayout.LayoutParams.MATCH_PARENT));
                button.setBackground(getDrawable(R.drawable.shadow_1395));
                cardView.setPadding(0,0,5,5);
                int padding = 32*3/WIDTH_OF_THE_GAME;
                button.setPadding(padding,padding,padding,padding);
                /*cardView.setContentPadding(0,0,0,0);
                ;
                cardView.setPreventCornerOverlap(false);*/
                //cardView.setBackgroundColor(Color.argb(0, 0, 0, 0));
                /*button.setTranslationZ(3);
                button.setElevation(3);
                button.setStateListAnimator(null);

                button.setBackground(getDrawable(R.drawable.shadow_14019));
                button.setOutlineProvider(new ViewOutlineProvider() {
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setRect(15,15, 150, 150);
                        view.setClipToOutline(true);
                    }
                });*/
                //space = new Space(this);
                //space.setMinimumWidth(20);

                tableRow.addView(cardView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
                tableRow.setPadding(10, 0, 10, 0);
                ListenerInterface lisInt = ListenerBuilder.build(i, j, game);
                button.setOnClickListener(lisInt);
                //button.setBackgroundColor(Color.TRANSPARENT);
            }
            tableLayout.addView(tableRow, new TableLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        }
        tableLayout.setPadding(0,20,0,20);
    }

    public void gameOver(Pair<Player, Vector<Pair<Integer, Integer>>> bundle){
        String result = "Player '" + bundle.first.getSymbol() + "' won!";
        game.playerXWon(bundle.first.getSymbol());
        lightResult(bundle.second);
        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        //game.reset and refresh in lightResult
    }

    public void gameOver(){
        String result = "Draw!";
        Toast.makeText(getApplicationContext() , result, Toast.LENGTH_LONG).show();
        lightResult();
    }

    public void refresh(){
        Square[][] field = game.getField();

        for (int i = 0, len = field.length; i < len; i++) {
            for (int j = 0, len2 = field[i].length; j < len2; j++) {
                if (field[i][j].getPlayer().getSymbol().equals("")) {
                    buttons[i][j].setImageResource(R.drawable.square);
                } else {
                    buttons[i][j].setImageResource((
                            (field[i][j].getPlayer().getSymbol()=="X")
                                    ?
                                    R.drawable.cross_new_anim_57
                                    :
                                    R.drawable.circle_new_anim_47));
                }
            }
        }
        String resultMessage = game.getScoreOfXPlayer(0) + " : " + game.getScoreOfXPlayer(1);
        TextView result = (TextView)findViewById(R.id.scores);
        result.setText(resultMessage);
    }

    private void lightResult(Vector<Pair<Integer, Integer>> vectorOfSquares){
        int duration = 0;
        for(Pair<Integer,Integer> onePair:vectorOfSquares){
            ImageButton button = buttons[onePair.first][onePair.second];
            if(game.getField()[onePair.first][onePair.second].getPlayer().getSymbol().equals(game.getPlayers()[0].getSymbol())){
                button.setImageResource(R.drawable.anim_light_cross);
            } else {
                button.setImageResource(R.drawable.anim_light_circle);
            }
            AnimationDrawable animationDrawable = (AnimationDrawable)buttons[onePair.first][onePair.second].getDrawable();
            animationDrawable.start();
            duration = animationDrawable.getDuration(0)*animationDrawable.getNumberOfFrames();
        }
        new CountDownTimer(duration+DURATION_OF_LIGHT_ANIMATION, duration) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            public  void onFinish() {
                game.reset();
                refresh();//Аккуратней с приведением типов снизу
                if(game instanceof Game)((Game)game).makeTurnAsBot();
            }
        }.start();
    }

    private void lightResult() {
        int duration = 0;
        for(int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if(game.getField()[i][j].getPlayer().getSymbol().equals(game.getPlayers()[0].getSymbol())){
                    buttons[i][j].setImageResource(R.drawable.anim_light_cross);
                } else {
                    buttons[i][j].setImageResource(R.drawable.anim_light_circle);
                }
                AnimationDrawable animationDrawable = (AnimationDrawable) buttons[i][j].getDrawable();
                animationDrawable.start();
                duration = animationDrawable.getDuration(0) * animationDrawable.getNumberOfFrames();
            }
        }
        new CountDownTimer(duration+DURATION_OF_LIGHT_ANIMATION, duration) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            public  void onFinish() {
                game.reset();
                refresh();
                if(game instanceof Game)((Game)game).makeTurnAsBot();
            }
        }.start();
    }

    public void onDrawButtonPush(View view){
        startActivity(new Intent(TicTacToeActivity.this, MenuActivity.class));
        //animatedBackground.stop();
        TicTacToeActivity.this.finish();
    }


}
