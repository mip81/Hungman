package mip.nz.hangman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by mikhailpastushkov on 9/29/16.
 */

public class GameActivity extends AppCompatActivity {

    private final boolean DEBUG = true; // define if DEBUG on / off
    private final int TIMERTIME = 30;
    private LinearLayout llAnswBtns ; // Layout for buttons
    private LinearLayout llWOB ; // Layout word on the board

    private String theWord;    // The word in the game
    private TextView tvWortOnTheBoard , tvTimer, tvChances;
    private ImageView ivPic;

    private StringBuilder theLetters = new StringBuilder();

    private int pxWidth; // Width of screen in px (to define width for btns)
    private int chances = 5;
    private int timer = 30;
    private boolean isStop = false;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getSupportActionBar().hide();

        DBHelper db = new DBHelper(getApplication());
      //  db.loadBase();

        tvWortOnTheBoard = (TextView) findViewById(R.id.tvWordOnTheBoard);
        llWOB = (LinearLayout) findViewById(R.id.llWordOnTheBoard);

        tvTimer = (TextView)findViewById(R.id.tvTimer);
        tvChances = (TextView)findViewById(R.id.tvChances);
        ivPic = (ImageView)findViewById(R.id.ivPic);

        // get the word
        theWord =  getIntent().getExtras().getString("word");
        if(DEBUG) Log.i("DEBUG onCreate WORD : ", theWord);

        // Initialize the
        for(int i = 0 ; i < theWord.length() ; i++) theLetters.append("_");


        // get width for device
        pxWidth = getResources().getDisplayMetrics().widthPixels;
        // get layouts for btns
        llAnswBtns = (LinearLayout)findViewById(R.id.llLetters);

        showTheWord();
        showAnswerBtn();
        startTimer(TIMERTIME);




    }


    /**
     * REFRESH THE WORD ON THE BOARD
     */
    private void showTheWord(){


        //tvWortOnTheBoard.setText(theLetters);
        llWOB.removeAllViews();
        for (int i = 0 ; i < theLetters.length(); i++){


            TextView tv = new TextView(this);
            tv.setTextSize(50);
            tv.setText( String.valueOf(theLetters.charAt(i)) );

            LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lastTxtParams.setMargins( 0, 0, 10, 0 );
            tv.setLayoutParams(lastTxtParams);
            llWOB.addView(tv);
            llWOB.setGravity(Gravity.CENTER);



        }


    }

    /**
     * Change chance with picture
     */
    public void changeChance(int chance){
        if(chance != -1) tvChances.setText(""+chance);
        // change picture
        switch (chance){
            case 4:
                ivPic.setImageResource(R.drawable.p5);
                break;
            case 3:
                ivPic.setImageResource(R.drawable.p4);
                break;
            case 2:
                ivPic.setImageResource(R.drawable.p3);
                break;
            case 1:
                ivPic.setImageResource(R.drawable.p2);
                break;
            case 0:
                ivPic.setImageResource(R.drawable.p1);
                break;
            case -1:
                ivPic.setImageResource(R.drawable.p0);
                break;
        }

    }


    /**
     * Check the letters and return true if
     * if the all letters in the word
     */
    private void checkLetter(char letter){

        ArrayList<Integer> alIdx = new ArrayList<Integer>();

        if ( theWord.contains( String.valueOf(letter)) ) {

            // Find all indexes of letter
            for (int i = 0; i < theWord.length(); i++) {
                if (theWord.charAt(i) == letter) alIdx.add(i);
            }

            // Change letter at found indexes
            for (int idx : alIdx){
                theLetters.setCharAt(idx , letter);
                if(DEBUG) Log.d("DEBUG : checkLetter : ", "Change letter "+letter+" at index "+idx);
            }

            // refresh the word
            showTheWord();

                // Check the word if not full start timer again
                if(theWord.equalsIgnoreCase(theLetters.toString())){
                    //// TODO: 9/29/16 show message offer new game
                    Toast.makeText(this,"Congratulations !!!", Toast.LENGTH_SHORT).show();

                }else{
                     isStop = false;
                    startTimer(TIMERTIME);
                }


        } // END IF CONTAINS TRUE
        else {
            changeChance(--chances);
            if(chances == -1){
                isStop = true;

                exitDialog(getResources().getString(R.string.GameOver));

                Toast.makeText(this, "GAME OVER! ", Toast.LENGTH_LONG).show();
                //goBack(null);

            }else{
                isStop = false;
                startTimer(TIMERTIME);
            }

        }



    }// END checkLetter()



    private void exitDialog(String msg){

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage(msg)

                .setNegativeButton("OK",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isStop = true;
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
//                .setPositiveButton("Yes, why not!",new DialogInterface.OnClickListener(){
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        isStop = true;
//                        DBHelper db = new DBHelper(getApplicationContext());
//                        theWord = db.randomWord();
//                        for(int i = 0 ; i < theWord.length() ; i++) theLetters.append("_");
//                        showTheWord();
//                        isStop = false;
//                        new GameTimer().execute();
//                        timer = TIMERTIME;
//
//
//
//                    }
//                });
        dialog.show();


    }



    //GO TO MAIN ACTIVITY
    public void goBack(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    // GENERATE DYNAMICALY BUTTIONS FOR ANSWER

    /**
     *  SHOW BTNS TO ANSWER
     */
    private void showAnswerBtn(){

        int count = 1;

        LinearLayout llRow = new LinearLayout(getApplicationContext());
        for(int i = 65; i < 91; i++){


            if(i == 65){
                llRow = new LinearLayout(getApplicationContext());
                llRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                llRow.setOrientation(LinearLayout.HORIZONTAL);
                llRow.setGravity(Gravity.CENTER);

            }

            Button btn = new Button(getApplicationContext());
            btn.setText(String.valueOf( (char)i ));
            btn.setLayoutParams(new LinearLayout.LayoutParams(pxWidth / 8, pxWidth / 8));
            btn.setOnClickListener(new ReadAnswerListerner());
            llRow.addView(btn);

            if( (count % 8)  == 0 || i >= 90){
                llAnswBtns.addView(llRow);
                llRow = new LinearLayout(getApplicationContext());
                llRow.setOrientation(LinearLayout.HORIZONTAL);
                llRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                llRow.setGravity(Gravity.CENTER);
            }
            ++count;
        }


    }

    /**
     * Start timer
     * @param sec
     */
    public void startTimer(int sec){
        timer = sec;
        isStop = false;
        new GameTimer().execute();

    }

    /**
     * Stop timer
     */
    public void stopTimer(){
        isStop = true;
    }

    // Class listener of answer from user
    class ReadAnswerListerner implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            // Stop the timer
            isStop = true;

            Button btnClicked = (Button)v;
            char letter = btnClicked.getText().charAt(0);
            checkLetter(letter);

           // Toast.makeText(getApplicationContext(), ((Button)v).getText().toString(), Toast.LENGTH_LONG).show();
            v.setEnabled(false);
        }
    }



    // class timer
    class GameTimer extends AsyncTask<Void, String, Void>{


        @Override
        protected Void doInBackground(Void... params) {
            while(!isStop){

                SystemClock.sleep(1000);
                timer--;

                    publishProgress(""+timer);

            }
            return null;
        }


        @Override
        protected void onProgressUpdate(String... values) {

            if (values[0].equalsIgnoreCase("-1")){
                changeChance(--chances);
                timer = TIMERTIME;

                if(chances == -1){
                    isStop = true;
                    exitDialog(getResources().getString(R.string.TimeRunOut));
                }
            }else
                tvTimer.setText( values[0] );

            if(DEBUG) Log.d("DEBUG: GameTimer : ",values[0]);
        }
    }
}
