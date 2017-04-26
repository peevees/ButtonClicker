package com.mikecoding.buttonclicker;

import android.content.Context;
import android.content.DialogInterface;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.R.attr.right;


public class MainActivity extends AppCompatActivity {

    TextView timerText;
    TextView clicksText;
    String timerCountDown;
    String name;
    int score;
    Button btn;
    Button restartBtn;
    FirebaseDatabase myDatabase;
    DatabaseReference myRef;
    HighScoreItem higScoIte;
    Timer timer;
    int clicks;
    boolean firstClick = true;
    LinearLayout linearLayout;
    Boolean firstRun = true;
    HighScoreItem[] highScoreItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerText = (TextView) findViewById(R.id.timer_Text);
        clicksText = (TextView) findViewById(R.id.clicks);
        timerCountDown = getResources().getString(R.string.timer_ticking);
        btn = (Button) findViewById(R.id.click_button);
        restartBtn = (Button) findViewById(R.id.restart_button);
        linearLayout = (LinearLayout) findViewById(R.id.high_score);
        name = "Anonymous";
        score = 0;
        timer = new Timer(timerText, timerCountDown, this);
        databaseConnection();
    }
    public void showDialog(final Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Name");
        builder.setMessage(R.string.name);
        builder.setCancelable(false);
        //text input
        final EditText input = new EditText(context);

        // Specify the type of input expected, here it is text;
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String m_Text = input.getText().toString().trim();
                if(TextUtils.isEmpty(m_Text)) {
                    name = "Anonymous";
                    higScoIte = new HighScoreItem(name, score);
                    myRef.child("High Scores").push().setValue(higScoIte);
                }else {
                    name = m_Text;
                    higScoIte = new HighScoreItem(name, score);
                    myRef.child("High Scores").push().setValue(higScoIte);
                }
            }
        });
        builder.show();
    }

    public void timerFinished(){
        btn.setEnabled(false);
        score = clicks;
        if (highScoreItems[0].getScore() < clicks){
            showDialog(this);
        }
        restartBtn.setEnabled(true);
    }
    public void myClicker (View v){
        if(firstClick){
            timer.getTimer();
            timer.startTimer();
            clicksText.setText("0");
            clicks = 0;
            firstClick = false;
        }else {
            clicks++;
            String clickText = String.valueOf(clicks);
            clicksText.setText(clickText);
        }
    }
    public void myRestart (View v){
        timerText.setText(R.string.timer_start_text);
        firstClick = true;
        clicksText.setText("");
        btn.setEnabled(true);
        restartBtn.setEnabled(false);
    }
    public void highScoreBuilder(String name, int score){
        TextView myTextView = new TextView(this);
        myTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        myTextView.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        myTextView.setText(name + ": " + score + " Clicks");
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        myTextView.setLayoutParams(textParams);
        linearLayout.addView(myTextView);
    }
    private void databaseConnection(){
        myDatabase = FirebaseDatabase.getInstance();
        myRef = myDatabase.getReference();

        // Read from the database
        Query queryRef = myRef.child("High Scores").orderByChild("score").limitToLast(10);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                linearLayout.removeAllViews();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                highScoreItems = new HighScoreItem[10];
                int i = 0;
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    HighScoreItem score = child.getValue(HighScoreItem.class);
                    highScoreItems[i] = score;
                    Log.d("TAG"," values is " + score.getName()  + " " + score.getScore());
                    i++;
                }
                for (int j = highScoreItems.length-1; j >= 0; j--){

                    HighScoreItem scoreItem = highScoreItems[j];
                        highScoreBuilder(scoreItem.getName(), scoreItem.getScore());
                    Log.d("TAG"," values is " + scoreItem.getName()  + " " + scoreItem.getScore());
                }


            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

}
