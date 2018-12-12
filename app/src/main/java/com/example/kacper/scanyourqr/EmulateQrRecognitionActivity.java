package com.example.kacper.scanyourqr;

import android.content.Intent;
import com.blikoon.qrcodescanner.QrCodeActivity;
import com.google.zxing.Result;

/**
 * Nadpisuje domysle zachowanie biblioteki do czytania QR codow z powodu problemow
 * z emulatacja maszyny wirtualnej. Otwiera ekran detali pacjenta
 *
 * Created by Kacper
 */
public class EmulateQrRecognitionActivity extends QrCodeActivity {

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        startActivity(new Intent(getApplicationContext(), PatientDetailsActivity.class));
    }

    @Override
    public void handleDecode(Result result) {
        Intent patientDetailsIntent = new Intent(getApplicationContext(), PatientDetailsActivity.class);
        patientDetailsIntent.putExtra("foundPatientNumber", result.getText());
        startActivity(patientDetailsIntent);
    }
}
