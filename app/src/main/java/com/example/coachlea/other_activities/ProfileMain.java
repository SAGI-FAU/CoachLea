/**
 * Created by Christoph Popp
 */

package com.example.coachlea.other_activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileMain extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST = 0;
    private static final int GALLERY_PICTURE = 1;

    private TextView tv_username, profile_name;
    private TextView et_date;
    private EditText et_phone;
    private RadioGroup rg_gender, rg_side_of_implant, rg_smoker, rg_implant;
    private PatientDA patientData;
    private DatePickerDialog datePickerDialog;
    private Date birthdate;
    private PatientDataService pds;
    private CircleImageView takePicture, profile_picture;
    private Bitmap bitmap;
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.profile_main);
        getSupportActionBar().setTitle(getResources().getString(R.string.Profile)); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        //initialize
        et_date = findViewById(R.id.editTextTextbirthdate);
        tv_username = findViewById(R.id.editTextName);
        et_phone = findViewById(R.id.editTextPhone);
        profile_name = findViewById(R.id.profile_name);
        pds = new PatientDataService(getApplicationContext());
        patientData = pds.getPatient();
        //takePicture = (CircleImageView) findViewById(R.id.takePicture);
       // profile_picture = (CircleImageView) findViewById(R.id.circleImageView);
        RadioButton rb_male = findViewById(R.id.rbGenderMale);
        RadioButton rb_female = findViewById(R.id.rbGenderFemale);
        RadioButton rb_other = findViewById(R.id.rbGenderOther);
        RadioButton rb_left = findViewById(R.id.rbLeft);
        RadioButton rb_right = findViewById(R.id.rbRight);
        RadioButton rb_both = findViewById(R.id.rbBoth);
        RadioButton rb_none = findViewById(R.id.rbNoSide);
        RadioButton rb_cochlear = findViewById(R.id.rbCochlearImplant);
        RadioButton rb_hearing_aid = findViewById(R.id.rbHearingAid);
        RadioButton rb_both_ci = findViewById(R.id.rbBothCI);
        RadioButton rb_none_CI = findViewById(R.id.rbNoneCI);
        File filepath = Environment.getExternalStorageDirectory();// + "/CITA/PROFILE/profile_pic.jpg";
        File dir = new File(filepath.getAbsolutePath() + "/CITA/PROFILE_PICTURE/");
        dir.mkdir();
        File file = new File(dir, "current_profile_pic.jpg");
        //Fill in all the data and get the profile picture
        if(file.exists()) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(Uri.fromFile(file));
                BitmapFactory.decodeStream(inputStream);
                profile_picture.setImageURI(Uri.fromFile(file));
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences pref = getApplicationContext().getSharedPreferences("patientData", 0);
        SharedPreferences.Editor editor = pref.edit();

        if (!pref.getString("Username", "noname").equals("noname"))
        {
            tv_username.setHint(pref.getString("Username", "noname"));
            profile_name.setText(pref.getString("Username", "noname"));
        }
        if (!pref.getString("Gender", "noGender").equals("noGender"))
        {
            switch(pref.getString("Gender", "noGender")) {
                case "male":
                case "Männlich":
                    rb_male.setChecked(true);break;
                case "female":
                case "Weiblich":
                    rb_female.setChecked(true);break;
                case "divers":
                case "Divers":
                    rb_other.setChecked(true);break;
            }
        }
        if (!pref.getString("side_of_implant", "noSide").equals("noSide"))
        {
            switch(pref.getString("side_of_implant", "noSide")) {
                case "right":
                    rb_right.setChecked(true);break;
                case "left":
                    rb_left.setChecked(true);break;
                case "both":
                    rb_both.setChecked(true);break;
                case "none":
                    rb_none.setChecked(true);break;
            }
        }


        if (!pref.getString("CochlearImplant", "noAid").equals("noAid")) {
            switch (pref.getString("CochlearImplant", "noAid")) {
                case "CochlearImplant":
                    rb_cochlear.setChecked(true);break;
                case "HearingAid":
                    rb_hearing_aid.setChecked(true);break;
                case "BothCI":
                    rb_both_ci.setChecked(true);break;
                case "None":
                    rb_none_CI.setChecked(true);break;
            }
        }

        if (!pref.getString("Birthdate", "noDate").equals("noDate"))
        {
            et_date.setHint(pref.getString("Birthdate", "noDate"));
        } else {
            et_date.setHint("dd/mm/yyyy");
        }
        if (!pref.getString("PhoneNumber", "noNumbeer").equals("noNumber"))
        {
            et_phone.setHint(pref.getString("PhoneNumber", "noDate"));
        } else {
            et_phone.setHint("000/000000");
        }
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
       // takePicture.setOnClickListener(this);
    }

    private void parseData() {
        rg_gender = findViewById(R.id.rgsex);
        rg_side_of_implant = findViewById(R.id.rgSideOfImplant);
        rg_implant = findViewById(R.id.rgTypeOfImplant);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("patientData", 0);
        SharedPreferences.Editor editor = pref.edit();
        int checkedGenderRadioButtonId = rg_gender.getCheckedRadioButtonId();
        if (checkedGenderRadioButtonId != -1) {
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
                break;
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
                patientData.setType("BothCI");
            case R.id.rbNoneCI:
                editor.putString("CochlearImplant", "None");
                editor.apply();
                patientData.setType("None");
                break;
            default:
                break;
        }


        String date_string = et_date.getText().toString();
        if (!date_string.isEmpty() && !date_string.equals("dd/mm/yyyy")) {
            editor.putString("Birthdate", date_string);
            editor.apply();
            patientData.setBirthday(birthdate);
        }
        String name = tv_username.getText().toString();
        if (!name.isEmpty()) {
            editor.putString("Username", name);
            editor.apply();
            patientData.setUsername(name);
        }
        String number = et_phone.getText().toString();
        if (!number.isEmpty()) {
            editor.putString("PhoneNumber", number);
            editor.apply();
            patientData.setGovtId(number);
        }
        return;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_continue:
                parseData();
                pds.savePatient(patientData);
                try {
                    export_profile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(ProfileMain.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.button_back1:
                onBackPressed();
                break;
            case R.id.editTextTextbirthdate:
                datePickerDialog.show();break;
 //           case R.id.takePicture:
 //               pictureDialog();
 //               break;
        }
    }

    private void pictureDialog() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
        myAlertDialog.setTitle(getResources().getString(R.string.UploadPictures));
        myAlertDialog.setMessage(getResources().getString(R.string.UploadPicturesGallery));

        myAlertDialog.setPositiveButton(getResources().getString(R.string.Gallery),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(
                                pictureActionIntent,
                                GALLERY_PICTURE);

                    }
                });
        myAlertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        bitmap = null;
        selectedImagePath = null;

        if (resultCode == RESULT_OK && requestCode == GALLERY_PICTURE) {
            if (data != null) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                selectedImagePath = c.getString(columnIndex);
                c.close();

                bitmap = BitmapFactory.decodeFile(selectedImagePath); // load
                // preview image
                File filepath = Environment.getExternalStorageDirectory();// + "/CITA/PROFILE/profile_pic.jpg";
                File dir = new File(filepath.getAbsolutePath() + "/CITA/PROFILE_PICTURE/");
                dir.mkdir();
                File file = new File(dir, "current_profile_pic.jpg");
                OutputStream outputStream = null;
                if (file.exists()) {
                    profile_picture.setImageDrawable(null);
                }
                try {
                    outputStream = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);

                int rotate = 0;
                try {
                    ExifInterface exif = new ExifInterface(selectedImagePath);
                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            rotate = 270;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            rotate = 180;
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            rotate = 90;
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Matrix matrix = new Matrix();
                matrix.postRotate(rotate);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight(), matrix, true);

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                try {
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                profile_picture.setImageBitmap(bitmap);

            } else {
                Toast.makeText(getApplicationContext(), "Cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void export_profile()  throws IOException {
        String PATH = Environment.getExternalStorageDirectory() + "/CITA/METADATA/PROFILE/";
        CSVFileWriter mCSVFileWriter = new CSVFileWriter("Profile", PATH);

        String[] Name={"Name", patientData.getUsername()};
        String[] Birthday={"Birthday", DateFormat.getDateInstance().format(patientData.getBirthday())};
        String[] ID={"Telephone-Number", String.valueOf(patientData.getGovtId())};
        String[] Gender={"Gender", patientData.getGender()};
        String[] Smoker={"Smoker", String.valueOf(patientData.getSmoker())};
        String[] Side={"Side", patientData.getSide()};
        String[] Type={"Type", String.valueOf(patientData.getType())};
        mCSVFileWriter.write(Name);
        mCSVFileWriter.write(Birthday);
        mCSVFileWriter.write(ID);
        mCSVFileWriter.write(Gender);
        mCSVFileWriter.write(Smoker);
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