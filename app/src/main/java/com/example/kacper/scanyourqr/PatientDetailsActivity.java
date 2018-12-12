package com.example.kacper.scanyourqr;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PatientDetailsActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private TextView patientName;
    private EditText newTemperature;
    private View mProgressView;
    private View mLoginFormView;
    private Patient currentPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        /* Set up the login form. */
        String selectedPatientNumber = getIntent().getStringExtra("foundPatientNumber");
        currentPatient = getPatient(selectedPatientNumber);
        patientName = (TextView) findViewById(R.id.PatientLastName);

        patientName.setText(currentPatient.getForname() + " " + currentPatient.getSurname());
        newTemperature = (EditText) findViewById(R.id.Temp);

        Button saveButton = (Button) findViewById(R.id.button3);
        Button closeButton = (Button) findViewById(R.id.buttonClose);
        closeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PatientDetailsActivity.this.finish();
            }
        });


        ListView listView = findViewById(R.id.TempList);
        final ArrayAdapter<String> temperaturesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,
                currentPatient.getStringsTemps());
        listView.setAdapter(temperaturesAdapter);
// Odszukanie grafu w UI
        final GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setMaxY(45.0);
        graph.getViewport().setMaxX(50.0);
        graph.getViewport().setMinX(0.0);
        graph.getViewport().setMinY(35.0);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
// Przenosznie danych miedzy UI a danymi w pamięci
        final LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {

        });
        graph.addSeries(series);
        fillChart(currentPatient.getTemperatures(), series, graph, temperaturesAdapter);
// Implementacja zachowania wciśnięcia guzika
        // Guzik uruchamia poniższą logikę
        saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                // Przypisanie do zmiennej raw wartości temperatury pacjenta.
                String rawString = newTemperature.getText().toString();
                newTemperature.setText("");

                if(rawString.trim().isEmpty()) {
                    return;
                }
                //Konwertowanie danych ze string na double
                Double givenTemp = 0d;
                try {
                    givenTemp = Double.parseDouble(rawString);
                } catch (NumberFormatException nfe) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Prosze podać wartość liczbową", Toast.LENGTH_SHORT);
                    toast.show();
                }
                //rozszerzenie zakresu danych o nowy element
                series.appendData(new DataPoint(currentPatient.getTemperatures().size(), givenTemp), false, 100);
             // Odświeżenie UI
               graph.refreshDrawableState();
// dodać nowy element do listy temperatur
                currentPatient.addTemperature(givenTemp);
                savePatient();
                // Informacja, że dane się zmieniły
               temperaturesAdapter.notifyDataSetChanged();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void savePatient() {
        PrefSingleton preferences = PrefSingleton.getInstance();
        SharedPreferences sharedData = preferences.getAppPreferences();
        SharedPreferences.Editor editor = sharedData.edit();
        editor.remove(currentPatient.getNumber());
        String patientInJson = AddPatient.getPatientAsJson(currentPatient);
        editor.putString(currentPatient.getNumber(), patientInJson);
        editor.commit();
    }

    private void fillChart(List<Temperature> temperatures, LineGraphSeries<DataPoint> series, GraphView graph, ArrayAdapter<String> temperaturesAdapter) {
        Collections.sort(temperatures, new Temperature.CompareTempAsc());
        int counter = 0;
        for (Temperature temperature : temperatures) {
            series.appendData(new DataPoint(counter++, temperature.value), false, 100);
            graph.refreshDrawableState();
            temperaturesAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private Patient getPatient(String patientNumber) {
        PrefSingleton preferences = PrefSingleton.getInstance();
        SharedPreferences sharedData = preferences.getAppPreferences();
        String patientJson = sharedData.getString(patientNumber, "NotFound");
        return getPatientFromJson(patientJson);
    }

    private Patient getPatientFromJson(String patientJson) {
        Gson gson = new Gson();
        return gson.fromJson(patientJson, Patient.class);
    }
}

