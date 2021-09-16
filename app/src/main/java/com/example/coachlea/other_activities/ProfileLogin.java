/**
 * Created by Christoph Popp
 */

package com.example.coachlea.other_activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.coachlea.R;
import com.example.coachlea.data_access.CSVFileWriter;
import com.example.coachlea.data_access.PatientDA;
import com.example.coachlea.data_access.PatientDataService;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProfileLogin extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_username;
    private TextView et_date;
    private EditText et_phone;
    private PatientDA patientData;
    private DatePickerDialog datePickerDialog;
    private Date birthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.profile_login);
        getSupportActionBar().setTitle(getResources().getString(R.string.Login)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize
        et_date = findViewById(R.id.editTextTextbirthdate);
        tv_username = findViewById(R.id.editTextName);
        et_phone = findViewById(R.id.editTextPhone);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("patientData", 0);
        SharedPreferences.Editor editor = pref.edit();

        if (!pref.getString("Username", "noname").equals("noname"))
        {
            tv_username.setText(pref.getString("Username", "noname"));
        }

        et_date.setText("dd/mm/yyyy");
        et_date.setOnClickListener(this);
        datePickerDialog = new DatePickerDialog(this, AlertDialog.BUTTON_NEUTRAL, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(mYear, mMonth, mDay);
                birthdate = calendar.getTime();
                et_date.setText(DateFormat.getDateInstance().format(birthdate));
            }
        }, 2000, 1, 1);

        findViewById(R.id.button_continue).setOnClickListener(this);
        findViewById(R.id.button_back1).setOnClickListener(this);
    }

    private boolean parseData() {
        RadioGroup rg_gender = findViewById(R.id.rgsex);
        RadioGroup rg_side_of_implant = findViewById(R.id.rgSideOfImplant);
        RadioGroup rg_implant = findViewById(R.id.rgTypeOfImplant);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("patientData", 0);
        SharedPreferences.Editor editor = pref.edit();
        String name = tv_username.getText().toString();
        if (name.isEmpty()) {
            return false;
        } else {
            editor.putString("Username", name);
            editor.apply();
        }
        String number = et_phone.getText().toString();
        if (number.isEmpty()) {
            return false;
        } else {
            editor.putString("PhoneNumber", number);
            editor.apply();
        }
        patientData = new PatientDA(name, number);
        int checkedGenderRadioButtonId = rg_gender.getCheckedRadioButtonId();
        if (checkedGenderRadioButtonId == -1) {
            return false;
        } else {
            RadioButton genderRadioButton = findViewById(checkedGenderRadioButtonId);
            editor.putString("Gender", genderRadioButton.getText().toString());
            editor.apply();
            patientData.setGender(genderRadioButton.getText().toString());
        }

        int checkedHandRadioButtonId = rg_side_of_implant.getCheckedRadioButtonId();
        switch (checkedHandRadioButtonId) {
            case R.id.rbRight:
                editor.putString("side_of_implant", "right");
                editor.apply();
                patientData.setSide("right");
                break;
            case R.id.rbLeft:
                editor.putString("side_of_implant", "left");
                editor.apply();
                patientData.setSide("left");
                break;
            case R.id.rbBoth:
                editor.putString("side_of_implant", "both");
                editor.apply();
                patientData.setSide("both");
                break;

            case R.id.rbNoSide:
                editor.putString("side_of_implant", "none");
                editor.apply();
                patientData.setSide("none");
                break;
            default:
                return false;
        }


        int checkedTypeRadioButtonId = rg_implant.getCheckedRadioButtonId();
        switch (checkedTypeRadioButtonId) {
            case R.id.rbCochlearImplant:
                editor.putString("CochlearImplant", "CochlearImplant");
                editor.apply();
                patientData.setType("CochlearImplant");
                break;
            case R.id.rbHearingAid:
                editor.putString("CochlearImplant", "HearingAid");
                editor.apply();
                patientData.setType("HearingAid");
                break;
            case R.id.rbBothCI:
                editor.putString("CochlearImplant", "BothCI");
                editor.apply();
                patientData.setType("BotCI");
                break;

            case R.id.rbNoneCI:
                editor.putString("CochlearImplant", "None");
                editor.apply();
                patientData.setType("None");
                break;
            default:
                return false;
        }

        String date_string = et_date.getText().toString();
        if (date_string.isEmpty() || date_string.equals("dd/mm/yyyy")) {
            return false;
        } else {
            editor.putString("Birthdate", date_string);
            editor.apply();
            patientData.setBirthday(birthdate);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_continue:
                boolean parseSuccess = parseData();
                if (!parseSuccess) {
                    Toast.makeText(getApplicationContext(), "Some information is still missing!", Toast.LENGTH_LONG).show();
                } else {
                    SharedPreferences prefs = getSharedPreferences("LoginPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("UserCreated",13);
                    editor.apply();
                    PatientDataService pds = new PatientDataService(getApplicationContext());
                    pds.savePatient(patientData);
                    patientData = pds.getPatient();
                    try {
                        export_profile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(ProfileLogin.this, MainActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.button_back1:
                onBackPressed();
                break;
            case R.id.editTextTextbirthdate:
                datePickerDialog.show();
        }
    }

    private void export_profile()  throws IOException {
        String PATH = Environment.getExternalStorageDirectory() + "/CoachLea/METADATA/PROFILE/";
        CSVFileWriter mCSVFileWriter = new CSVFileWriter("Profile", PATH);

        String[] Name={"Name", patientData.getUsername()};
        String[] Birthday={"Birthday", DateFormat.getDateInstance().format(patientData.getBirthday())};
        String[] ID={"Telephone-Number", String.valueOf(patientData.getGovtId())};
        String[] Gender={"Gender", patientData.getGender()};
        String[] Side={"Side", patientData.getSide()};
        String[] Type={"Type", patientData.getType()};
        mCSVFileWriter.write(Name);
        mCSVFileWriter.write(Birthday);
        mCSVFileWriter.write(ID);
        mCSVFileWriter.write(Gender);
        mCSVFileWriter.write(Side);
        mCSVFileWriter.write(Type);
        mCSVFileWriter.close();

    }

    //This allows you to return to the activity before
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
