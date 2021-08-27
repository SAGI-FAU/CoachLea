package com.example.coachlea.exercises;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.data_access.CSVFileWriter;
import com.example.coachlea.other_activities.SpeakingExerciseFinished;
import com.example.coachlea.other_activities.TrainingsetExerciseFinished;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MinimalPairs extends AppCompatActivity {

    private static final int EXERCISE_LENGTH = 9;

    private ArrayList<String> usedPair;
    private Random random;
    private Dialog explanationDialog;

    private int[] minimal_pairs_all;
    private int[] minimal_pairs_plosives;
    private int[] minimal_pairs_fricatives;
    private int[] minimal_pairs_trills;
    private int[] minimal_pairs_lateral;
    private int[] minimal_pairs_others;

    private String[] minimal_pairs_plosives_str;
    private String[] minimal_pairs_fricatives_str;
    private String[] minimal_pairs_trills_str;
    private String[] minimal_pairs_lateral_str;
    private String[] minimal_pairs_others_str;
    private String[] minimal_pairs_all_str;

    private int[] minimal_pairs = new int[2 * EXERCISE_LENGTH];
    private int[] minimal_pairs_result = new int[2 * EXERCISE_LENGTH];
    private String[] minimal_pairs_result_str = new String[2 * EXERCISE_LENGTH];
    private String[] minimal_pairs_correct_str = new String[2 * EXERCISE_LENGTH];
    private String[] minimal_pairs_false_str = new String[2 * EXERCISE_LENGTH];
    private int[] minimal_pairs_correct = new int[2 * EXERCISE_LENGTH];
    private boolean playPressed = false;

    private int choose = 0;
    private int oldPos = 2;

    private ImageButton topIMG;
    private ImageButton botIMG;
    private MediaPlayer player;
    private ProgressBar pb;
    private int currentProgress = 0;
    private TextView progressText;

    private int exerciseCounter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.minimal_pairs);
        getSupportActionBar().setTitle(R.string.minimal_pairs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        usedPair = new ArrayList<>(); // some minimal pairs are in multiple sets
        random = new Random();
        explanationDialog = new Dialog(this);

        //check if MinimalPairs is used for the first time
        SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
        int login = prefs.getInt("MinimalPairsUsed", 0);

        if(login == 0){
            openPopup();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("MinimalPairsUsed",13);
            editor.apply();
        }

        //set String arrays
        minimal_pairs_all_str = getResources().getStringArray(R.array.Minimal_Pairs);

        minimal_pairs_plosives_str = getResources().getStringArray(R.array.Minimal_Pairs_plosives);
        minimal_pairs_fricatives_str = getResources().getStringArray(R.array.Minimal_Pairs_fricatives);
        minimal_pairs_lateral_str = getResources().getStringArray(R.array.Minimal_Pairs_lateral);
        minimal_pairs_trills_str = getResources().getStringArray(R.array.Minimal_Pairs_trills);
        minimal_pairs_others_str = getResources().getStringArray(R.array.Minimal_Pairs_others);

        //set drawable arrays
        setMinimal_pairs_plosives();
        setMinimal_pairs_fricatives();
        setMinimal_pairs_lateral();
        setMinimal_pairs_trills();
        setMinimal_pairs_others();


        //Initialize
        if(getIntent().getExtras() != null){
            exerciseCounter = getIntent().getExtras().getInt("exerciseCounter",0);
        }
        topIMG = findViewById(R.id.topIMG);
        botIMG = findViewById(R.id.botIMG);
        ImageButton play = findViewById(R.id.playBTN);
        pb = findViewById(R.id.minimalPairsProgress);
        currentProgress = 100 / EXERCISE_LENGTH;
        pb.setProgress(currentProgress);
        pb.setMax(100);
        progressText = findViewById(R.id.minimalPairsText);
        progressText.setText((choose+1) + "/"+EXERCISE_LENGTH);



        //set minimal pairs for the game
        setMinimal_pairs(minimal_pairs_plosives,minimal_pairs_plosives_str,3, 0);
        setMinimal_pairs(minimal_pairs_fricatives,minimal_pairs_fricatives_str,3, 3);
        setMinimal_pairs(minimal_pairs_lateral,minimal_pairs_lateral_str,1, 6);
        setMinimal_pairs(minimal_pairs_trills,minimal_pairs_trills_str,1, 7);
        setMinimal_pairs(minimal_pairs_others,minimal_pairs_others_str,1, 8);

        shuffleArrays();


        //setMinimal_pairs();

        topIMG.setImageResource(minimal_pairs[0]);
        topIMG.setBackgroundResource(0);

        botIMG.setImageResource(minimal_pairs[1]);
        botIMG.setBackgroundResource(0);

        //setting tags for correct or false button
        if (minimal_pairs[0] == minimal_pairs_correct[0]) {
            topIMG.setTag("correct");
            botIMG.setTag("false");
        } else {
            topIMG.setTag("false");
            botIMG.setTag("correct");
        }

        // top ImageButton
        topIMG.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (playPressed) {
                    if (oldPos >= 2 * EXERCISE_LENGTH) {

                        Intent intent = new Intent(MinimalPairs.this, SpeakingExerciseFinished.class);

                        if (topIMG.getTag() == "correct") {
                            topIMG.setBackgroundResource(R.color.limegreen);
                            minimal_pairs_result[choose] = 1;
                            minimal_pairs_result_str[choose] = minimal_pairs_correct_str[choose];
                        } else {
                            minimal_pairs_result[choose] = 0;
                            minimal_pairs_result_str[choose] = minimal_pairs_false_str[choose];
                        }

                        intent.putExtra("correct_words", minimal_pairs_correct_str);
                        intent.putExtra("results", minimal_pairs_result);
                        intent.putExtra("exercise","MinimalPairs");


                        try {
                            export_data();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //check if part of daily session
                        if (getIntent().getBooleanExtra("trainingset",false)){
                            intent = new Intent(v.getContext(), TrainingsetExerciseFinished.class);
                            intent.putExtra("exerciseList",getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter",exerciseCounter);
                            intent.putExtra("trainingset",true);
                        }


                        // time delay for green frame
                        Handler handler = new Handler();
                        Intent finalIntent = intent;
                        handler.postDelayed(new Runnable() {

                            public void run() {
                                v.getContext().startActivity(finalIntent);
                            }

                        }, 400);


                    } else {
                        playPressed = false;

                        nextButtons(topIMG);

                        if (player != null) {
                            player.stop();
                            player.release();
                            player = null;
                        }
                    }

                } else {
                    Toast.makeText(MinimalPairs.this,getResources().getString(R.string.minimalPairsError),Toast.LENGTH_SHORT).show();
                }

            }

        });

        // bottom ImageButton
        botIMG.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (playPressed) {
                    if (oldPos >= 2 * EXERCISE_LENGTH) {
                        Intent intent = new Intent(MinimalPairs.this, SpeakingExerciseFinished.class);

                        if (botIMG.getTag() == "correct") {
                            botIMG.setBackgroundResource(R.color.limegreen);
                            minimal_pairs_result[choose] = 1;
                            minimal_pairs_result_str[choose] = minimal_pairs_correct_str[choose];
                        } else {
                            minimal_pairs_result[choose] = 0;
                            minimal_pairs_result_str[choose] = minimal_pairs_false_str[choose];
                        }

                        intent.putExtra("correct_words", minimal_pairs_correct_str);
                        intent.putExtra("results", minimal_pairs_result);
                        intent.putExtra("exercise","MinimalPairs");


                        try {
                            export_data();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //check if part of daily session
                        if (getIntent().getBooleanExtra("trainingset", false)) {
                            intent = new Intent(v.getContext(), TrainingsetExerciseFinished.class);
                            intent.putExtra("exerciseList", getIntent().getExtras().getStringArray("exerciseList"));
                            intent.putExtra("exerciseCounter", exerciseCounter);
                            intent.putExtra("trainingset",true);
                        }


                        // time delay for green frame
                        Handler handler = new Handler();
                        Intent finalIntent = intent;
                        handler.postDelayed(new Runnable() {

                            public void run() {
                                v.getContext().startActivity(finalIntent);
                            }

                        }, 400);


                    } else {
                        playPressed = false;

                        nextButtons(botIMG);

                        if (player != null) {
                            player.stop();
                            player.release();
                            player = null;
                        }
                    }

                } else {
                    Toast.makeText(MinimalPairs.this,getResources().getString(R.string.minimalPairsError),Toast.LENGTH_SHORT).show();
                }
            }

        });


        //play Button
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                playPressed = true;

                if (player != null) {
                    player.seekTo(0);
                    player.start();
                } else {
                    String file = chooseAudio();
                    int resId = getResources().getIdentifier(file, "raw", getPackageName());
                    String path = "a" + resId;
                    String path2 = path.substring(1);
                    player = MediaPlayer.create(v.getContext(), Integer.parseInt(path2));
                    player.start();
                }

            }
        });


    }

    private String chooseAudio(){
        String file = minimal_pairs_correct_str[choose];

        if(choose <= 1){
            file = file;
        } else if(choose <= 3){
            file = file + "_snr25";
        } else if(choose <= 5){
            file = file + "_snr20";
        } else if(choose <= 7){
            file = file + "_snr15";
        } else if(choose == 8){
            file = file + "_snr10";
        }

        return file;

    }

    //set next Buttons
    private void nextButtons(ImageButton imageButton) {

        //check whether the right or wrong word was chosen
        if (imageButton.getTag() == "correct") {
            imageButton.setBackgroundResource(R.color.limegreen);
            minimal_pairs_result[choose] = 1;
            minimal_pairs_result_str[choose] = minimal_pairs_correct_str[choose];

        } else {
            minimal_pairs_result[choose] = 0;
            minimal_pairs_result_str[choose] = minimal_pairs_false_str[choose];

        }
        choose++;

        Random random = new Random();
        int position = random.nextInt(2);

        int a = oldPos + (position % 2);
        int b = oldPos + ((position + 1) % 2);


        //Time delay for green frame as feedback
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            //set next Buttons
            public void run() {
                if((choose + 1) != EXERCISE_LENGTH){
                    currentProgress += (100 / EXERCISE_LENGTH);
                }  else {
                    currentProgress = 100;
                }
                progressText.setText((choose+1) + "/"+EXERCISE_LENGTH);
                pb.setProgress(currentProgress);
                pb.setMax(100);

                imageButton.setBackgroundResource(0);
                topIMG.setImageResource(minimal_pairs[a]);
                botIMG.setImageResource(minimal_pairs[b]);

            }

        }, 400);

        //set next tags
        if (minimal_pairs[a] == minimal_pairs_correct[choose]) {
            topIMG.setTag("correct");
            botIMG.setTag("false");
        } else {
            topIMG.setTag("false");
            botIMG.setTag("correct");
        }

        oldPos += 2;
    }



    private void setMinimal_pairs(int[] minimal_pairs, String[] minimal_pairs_str,int amount, int pos){

        for( int i = pos; i < (pos + amount); i++ ){

            int position = random.nextInt(minimal_pairs.length / 2) * 2; //always gets an even number

            //check if both words were already used
            while(usedPair.contains(minimal_pairs_str[position]) && usedPair.contains(minimal_pairs_str[position + 1])){
                 position = random.nextInt(minimal_pairs.length / 2) * 2;
            }



            int corr = random.nextInt(2);
            int flse = (corr + 1) % 2;

            //set String arrays
            minimal_pairs_correct_str[i] = minimal_pairs_str[position + corr];
            minimal_pairs_false_str[i] = minimal_pairs_str[position + flse];

            //set drawable arrays
            minimal_pairs_correct[i] = minimal_pairs[position + corr];
            this.minimal_pairs[i * 2] = minimal_pairs[position];
            this.minimal_pairs[i * 2 + 1] = minimal_pairs[position + 1];

            // add used minimal pair to arraylist
            usedPair.add(minimal_pairs_str[position]);
            usedPair.add(minimal_pairs_str[position+1]);

        }

    }

    private void shuffleArrays(){
        int rand;

        int[] temp_minimalPair = new int[2];
        int temp_minimalPairCorr;
        String temp_mpCorrStr;
        String temp_mpFalseStr;

        for(int i = EXERCISE_LENGTH-1;i>0;i--){
            rand = random.nextInt(i+1);

            temp_minimalPair[0] = minimal_pairs[i*2];
            temp_minimalPair[1] = minimal_pairs[(i*2)+1];
            temp_minimalPairCorr = minimal_pairs_correct[i];
            temp_mpCorrStr = minimal_pairs_correct_str[i];
            temp_mpFalseStr = minimal_pairs_false_str[i];

            minimal_pairs[i*2] = minimal_pairs[rand*2];
            minimal_pairs[(i*2)+1] = minimal_pairs[(rand*2)+1];
            minimal_pairs[rand*2] = temp_minimalPair[0];
            minimal_pairs[(rand*2)+1] = temp_minimalPair[1];

            minimal_pairs_correct[i] = minimal_pairs_correct[rand];
            minimal_pairs_correct_str[i] = minimal_pairs_correct_str[rand];
            minimal_pairs_false_str[i] = minimal_pairs_false_str[rand];
            minimal_pairs_correct[rand] =  temp_minimalPairCorr;
            minimal_pairs_correct_str[rand] = temp_mpCorrStr;
            minimal_pairs_false_str[rand] = temp_mpFalseStr;
        }
    }



    private void export_data() throws IOException {
        String PATH = Environment.getExternalStorageDirectory() + "/CoachLea/METADATA/RESULTS/";
        CSVFileWriter mCSVFileWriter = new CSVFileWriter("MinimalPairs", PATH); // TODO exerciseName ändern falls name geändert wird
        String[] start = {"correct_word", "chosen_word", "noise_amount"};
        mCSVFileWriter.write(start);

        String[] noise_amount = {"no_noise", "no_noise", "SNR = 25", "SNR = 25", "SNR = 20", "SNR = 20", "SNR = 15", "SNR = 15", "SNR = 10"};

        for (int i = 0; i < EXERCISE_LENGTH; i++) {
            String[] correct_result = {minimal_pairs_correct_str[i], minimal_pairs_result_str[i], noise_amount[i]};
            mCSVFileWriter.write(correct_result);
        }
        mCSVFileWriter.close();

    }

    private void setMinimal_pairs_all() {
        minimal_pairs_all = new int[]{R.drawable.gold, R.drawable.geld, R.drawable.sand, R.drawable.wand, R.drawable.see, R.drawable.tee, R.drawable.hose,
                R.drawable.rose, R.drawable.huhn, R.drawable.hut, R.drawable.baum, R.drawable.schaum, R.drawable.geld, R.drawable.welt, R.drawable.tasche, R.drawable.tasse};
    }

    private void setMinimal_pairs_plosives() {
        minimal_pairs_plosives = new int[]{R.drawable.kanne, R.drawable.tanne, R.drawable.kopf, R.drawable.topf, R.drawable.wecker, R.drawable.wetter, R.drawable.kasse,
                R.drawable.tasse, R.drawable.keller, R.drawable.teller, R.drawable.kraene, R.drawable.traene, R.drawable.nagel, R.drawable.nadel, R.drawable.bogen, R.drawable.boden,
                R.drawable.feger, R.drawable.feder, R.drawable.waage, R.drawable.wade, R.drawable.nabel, R.drawable.nadel, R.drawable.wabe, R.drawable.wade, R.drawable.bach, R.drawable.dach,
                R.drawable.geld, R.drawable.gelb, R.drawable.pass, R.drawable.fass, R.drawable.topf, R.drawable.zopf, R.drawable.tee, R.drawable.zeh, R.drawable.tasse, R.drawable.tatze,
                R.drawable.kasse, R.drawable.katze, R.drawable.dieb, R.drawable.sieb, R.drawable.zahn, R.drawable.hahn, R.drawable.tonne, R.drawable.sonne, R.drawable.baecker,
                R.drawable.wecker, R.drawable.baelle, R.drawable.welle, R.drawable.mauer, R.drawable.bauer, R.drawable.oma, R.drawable.opa, R.drawable.pferd, R.drawable.herd,
                R.drawable.brei, R.drawable.hai, R.drawable.bauch, R.drawable.lauch, R.drawable.dorn, R.drawable.horn, R.drawable.baum, R.drawable.schaum, R.drawable.bus, R.drawable.nuss,
                R.drawable.dose, R.drawable.rose, R.drawable.klammer, R.drawable.hammer, R.drawable.fisch, R.drawable.tisch, R.drawable.gitter, R.drawable.ritter, R.drawable.kuh,
                R.drawable.schuh, R.drawable.kutsche, R.drawable.rutsche, R.drawable.seife, R.drawable.pfeife, R.drawable.clown, R.drawable.zaun, R.drawable.kuss, R.drawable.bus};
    }

    private void setMinimal_pairs_fricatives(){
        minimal_pairs_fricatives = new int[]{R.drawable.schal, R.drawable.saal, R.drawable.schuppe, R.drawable.suppe, R.drawable.tasche, R.drawable.tasse, R.drawable.busch, R.drawable.bus,
                R.drawable.sand, R.drawable.hand, R.drawable.salz, R.drawable.hals, R.drawable.kirche, R.drawable.kirsche, R.drawable.weich, R.drawable.weiss, R.drawable.faecher,
                R.drawable.faesser, R.drawable.kueche, R.drawable.kuesse, R.drawable.topf, R.drawable.zopf, R.drawable.tee, R.drawable.zeh, R.drawable.tasse, R.drawable.tatze,
                R.drawable.kasse, R.drawable.katze, R.drawable.fahne, R.drawable.sahne, R.drawable.fee, R.drawable.see, R.drawable.pass, R.drawable.fass, R.drawable.dieb, R.drawable.sieb,
                R.drawable.zahn, R.drawable.hahn, R.drawable.tonne, R.drawable.sonne, R.drawable.schnecke, R.drawable.hecke, R.drawable.spiegel, R.drawable.igel, R.drawable.baum,
                R.drawable.schaum, R.drawable.kuh, R.drawable.schuh, R.drawable.seife, R.drawable.pfeife, R.drawable.clown, R.drawable.zaun, R.drawable.schal, R.drawable.wal,
                R.drawable.reh, R.drawable.see};
    }

    private void setMinimal_pairs_trills(){
        minimal_pairs_trills = new int[]{R.drawable.reis, R.drawable.heiss, R.drawable.rasen, R.drawable.hasen, R.drawable.rand, R.drawable.hand, R.drawable.rose, R.drawable.hose,
                R.drawable.rose, R.drawable.lose, R.drawable.gras, R.drawable.glas, R.drawable.ratte, R.drawable.latte, R.drawable.leiter, R.drawable.reiter, R.drawable.dose,
                R.drawable.rose, R.drawable.gitter, R.drawable.ritter, R.drawable.kutsche, R.drawable.rutsche, R.drawable.watte, R.drawable.ratte, R.drawable.reh, R.drawable.see};
    }

    private void setMinimal_pairs_lateral(){
        minimal_pairs_lateral = new int[]{R.drawable.lunge, R.drawable.junge, R.drawable.lacke, R.drawable.jacke, R.drawable.rose, R.drawable.lose, R.drawable.gras, R.drawable.glas,
                R.drawable.ratte, R.drawable.latte, R.drawable.leiter, R.drawable.reiter, R.drawable.beil, R.drawable.bein, R.drawable.lupe, R.drawable.hupe, R.drawable.bauch,
                R.drawable.lauch};
    }

    private void setMinimal_pairs_others(){
        minimal_pairs_others = new int[]{R.drawable.maehen, R.drawable.naehen, R.drawable.schwamm, R.drawable.schwan, R.drawable.ringe, R.drawable.rinne, R.drawable.nase,
                R.drawable.hase, R.drawable.maus, R.drawable.haus, R.drawable.mund, R.drawable.hund};
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate((R.menu.explanation_menu), menu);
        return true;

    }

    //This allows you to return to the activity before
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.explanation_menu){
            openPopup();
        } else {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void openPopup(){
        TextView close;
        TextView title_e;
        TextView explanation;
        ImageButton explanation_mp3;
        String mp3_file = null;
        final MediaPlayer[] player = {null};
        int resId;
        String path;

        explanationDialog.setContentView(R.layout.popup_explanation);


        title_e = (TextView) explanationDialog.findViewById(R.id.explanation_title);
        explanation = (TextView)  explanationDialog.findViewById(R.id.text_explanation);
        explanation_mp3 = (ImageButton)  explanationDialog.findViewById(R.id.play_explanation);
        close = (TextView)  explanationDialog.findViewById(R.id.txtclose);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player[0]!= null){
                    player[0].stop();
                    player[0].release();
                    player[0] = null;
                }
                explanationDialog.dismiss();
            }
        });

        explanation.setText(R.string.minimalPairs_explanation);
        title_e.setText(R.string.minimal_pairs);
        resId = getResources().getIdentifier("minimal_pairs_explanation", "raw", getPackageName());
        path = "a" + resId;
        mp3_file  = path.substring(1);

        String finalMp3_file = mp3_file;
        explanation_mp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player[0] != null) {
                    player[0].seekTo(0);
                    player[0].start();
                } else {
                    player[0] = MediaPlayer.create(v.getContext(), Integer.parseInt(finalMp3_file));
                    player[0].start();
                }
            }
        });


        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(explanationDialog.getWindow().getAttributes());
        layoutParams.width =WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;


        explanationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        explanationDialog.getWindow().setAttributes(layoutParams);
        explanationDialog.show();

    }


}