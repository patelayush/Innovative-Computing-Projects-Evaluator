package info.androidhive.movietickets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {


    APIInterface restapi =
            APIClient.getClient().create(APIInterface.class);
    BarcodeReader barcodeReader;
    static String evaluatorname;
    static String teamname;
    protected  Boolean second;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);

        Intent intent = getIntent();
        second = intent.getBooleanExtra("second",false);
    }

    @Override
    public void onScanned(final Barcode barcode) {

        // playing barcode reader beep sound
        barcodeReader.playBeep();

        System.out.println(barcode.displayValue);
        if(getIntent().getBooleanExtra("evaluator",false)) {

            User user = new User();
            user.setRole("EVALUATOR");
            retrofit2.Call<UserResponse> call = restapi.getUser(user, barcode.displayValue);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(retrofit2.Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(ScanActivity.this, BottomNav.class);
                        intent.putExtra("evaluator", response.body().getUsername());
                        evaluatorname = response.body().getUsername();
                        startActivity(intent);
                        finish();
                    }
                    else{
                        setResult(103);
                        finish();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<UserResponse> call, Throwable t) {
                    System.out.println(t.getLocalizedMessage());
                    t.printStackTrace();
                    finish();
                }

            });


            System.out.println("yaha");


        }



        if(getIntent().getBooleanExtra("team",false))
        {
            User user = new User();
            user.setRole("EXHIBIT");
            retrofit2.Call<UserResponse> call = restapi.getUser(user, barcode.displayValue);
            call.enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(retrofit2.Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Intent intent = new Intent(ScanActivity.this, BottomNav.class);
                        intent.putExtra("team",response.body().getUsername());
                        BottomNav.flag=true;
                        teamname = response.body().getUsername();
                        startActivity(intent);
                        finish();
                    }
                    else{/*
                        setResult(103);
                        System.out.println("finis call kara");
                        finish();*/
                        barcodeReader.pauseScanning();
                        Toast.makeText(ScanActivity.this, "Invalid QR code for team. Please scan again", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<UserResponse> call, Throwable t) {
                    System.out.println(t.getLocalizedMessage());
                    t.printStackTrace();
                    finish();
                }

            });


            System.out.println("waha");
        }
    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String s) {
        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraPermissionDenied() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
