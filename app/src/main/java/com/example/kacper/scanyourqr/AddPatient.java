package com.example.kacper.scanyourqr;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.Set;

public class AddPatient extends AppCompatActivity {

    private EditText forname;
    private EditText surname;
    private EditText qrCode;
    private Button registerPatientBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        forname = (EditText) findViewById(R.id.input_forname);
        surname = (EditText) findViewById(R.id.input_surname);
        qrCode = (EditText) findViewById(R.id.input_qrcode);
        registerPatientBtn = (Button) findViewById(R.id.btn_register);
        registerPatientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Patient pat = new Patient(forname.getText().toString(), surname.getText().toString(), qrCode.getText().toString());
                savePatient(pat);
            }
        });
    }

    private void savePatient(Patient patient) {
        PrefSingleton preferences = PrefSingleton.getInstance();
        SharedPreferences sharedData = preferences.getAppPreferences();
        SharedPreferences.Editor editor = sharedData.edit();
        Set<String> keys = sharedData.getAll().keySet();
        if (!keys.contains(patient.getNumber())) {
            editor.putString(patient.getNumber(), getPatientAsJson(patient));
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Pacjent o podanym numerze już istnieje", Toast.LENGTH_SHORT);
            toast.show();
        }
        editor.commit();
        finish();
    }

    public static String getPatientAsJson(Patient patient) {
        Gson gson = new Gson();
        return gson.toJson(patient);
    }
}
