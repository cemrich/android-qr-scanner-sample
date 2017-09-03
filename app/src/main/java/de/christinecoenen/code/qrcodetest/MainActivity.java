package de.christinecoenen.code.qrcodetest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements BarcodeCallback {

    private static final int PERMISSION_REQUEST_CAMERA = 42;

    private CompoundBarcodeView barcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get reference to our scanner view from xml layout
        barcodeView = findViewById(R.id.barcode_view);

        // if you don't need the line of text at the bottom
        barcodeView.setStatusText("");

        // set scanning mode
        barcodeView.decodeContinuous(this);

        // or decode single if you just want to scan one qr code
        //barcodeView.decodeSingle(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // as we have no button we will try to start scanning on app start
        startScanningWithPermissionCheck();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // pause scanning when activity is in background
        barcodeView.pause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CAMERA &&
                grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // start scanning as soon as camera permission is granted
            startScanningWithPermissionCheck();
        }
    }

    @Override
    public void barcodeResult(BarcodeResult result) {
        barcodeView.setStatusText(result.getText());
    }

    @Override
    public void possibleResultPoints(List<ResultPoint> resultPoints) {
        // we can ignore the points
    }

    private void startScanningWithPermissionCheck() {
        if (hasCameraPermission()) {
            // this is important to actually, really start the scanning process
            barcodeView.resume();
        } else {
            requestCameraPermission();
        }
    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        // shows permission dialog for camera usage
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
    }
}
