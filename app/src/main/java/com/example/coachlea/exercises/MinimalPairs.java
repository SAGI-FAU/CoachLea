package com.example.coachlea.exercises;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.data_access.CSVFileWriter;
import com.example.coachlea.other_activities.MinimalPairsExerciseFinished;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MinimalPairs extends AppCompatActivity {

    private static final int EXERCISE_LENGTH = 9;

    ArrayList<String> usedPair;
    Random random;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minimal_pairs);

        usedPair = new ArrayList<>(); // some minimal pairs are in multiple sets
        random = new Random();

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
        topIMG = findViewById(R.id.topIMG);
        botIMG = findViewById(R.id.botIMG);
        ImageButton play = findViewById(R.id.playBTN);



        //set minimal pairs for the game
        setMinimal_pairs(minimal_pairs_plosives,minimal_pairs_plosives_str,3, 0);
        setMinimal_pairs(minimal_pairs_fricatives,minimal_pairs_fricatives_str,3, 3);
        setMinimal_pairs(minimal_pairs_lateral,minimal_pairs_lateral_str,1, 6);
        setMinimal_pairs(minimal_pairs_trills,minimal_pairs_trills_str,1, 7);
        setMinimal_pairs(minimal_pairs_others,minimal_pairs_others_str,1, 8);


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

                        Intent intent = new Intent(MinimalPairs.this, MinimalPairsExerciseFinished.class);

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

                        //TODO store data
                        try {
                            export_data();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        // time delay for green frame
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {

                            public void run() {
                                v.getContext().startActivity(intent);
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

                }
            }

        });

        // bottom ImageButton
        botIMG.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (oldPos >= 2 * EXERCISE_LENGTH) {
                    Intent intent = new Intent(MinimalPairs.this, MinimalPairsExerciseFinished.class);

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

                    //TODO store data
                    try {
                        export_data();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // time delay for green frame
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        public void run() {
                            v.getContext().startActivity(intent);
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
                    String file = minimal_pairs_correct_str[choose];
                    int resId = getResources().getIdentifier(file, "raw", getPackageName());
                    String path = "a" + resId;
                    String path2 = path.substring(1);
                    player = MediaPlayer.create(v.getContext(), Integer.parseInt(path2));
                    player.start();
                }

            }
        });


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

    /*
    //randomly choose minimal pairs & correct words
    private void setMinimal_pairs() {
        Random random = new Random();
        int position = random.nextInt(EXERCISE_LENGTH * 2) * 2;


        ArrayList<Integer> usedNumber = new ArrayList<>();
        for (int i = 0; i < EXERCISE_LENGTH; i++) {

            while (usedNumber.contains(position)) {
                position = random.nextInt(EXERCISE_LENGTH * 2) * 2;
            }
            int corr = random.nextInt(2);
            int flse = (corr + 1) % 2;
            minimal_pairs_correct_str[i] = minimal_pairs_all_str[position + corr];
            minimal_pairs_false_str[i] = minimal_pairs_all_str[position + flse];
            minimal_pairs_correct[i] = minimal_pairs_all[position + corr];
            minimal_pairs[i * 2] = minimal_pairs_all[position];
            minimal_pairs[i * 2 + 1] = minimal_pairs_all[position + 1];

            usedNumber.add(position);

        }

    }
     */


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



    private void export_data() throws IOException {
        String PATH = Environment.getExternalStorageDirectory() + "/CoachLea/METADATA/RESULTS/";
        CSVFileWriter mCSVFileWriter = new CSVFileWriter("MinimalPairs", PATH); // TODO exerciseName ändern falls name geändert wird
        String[] start = {"correct_word", "chosen_word"};
        mCSVFileWriter.write(start);


        for (int i = 0; i < EXERCISE_LENGTH; i++) {
            String[] correct_result = {minimal_pairs_correct_str[i], minimal_pairs_false_str[i]};
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

}