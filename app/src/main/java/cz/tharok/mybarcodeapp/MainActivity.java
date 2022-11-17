package cz.tharok.mybarcodeapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

// use library https://github.com/journeyapps/zxing-android-embedded

public class MainActivity extends AppCompatActivity {
    private DecoratedBarcodeView barcodeView;
    private TextView outputTextView;
    private BeepManager beepManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        outputTextView = findViewById(R.id.textView);
        beepManager = new BeepManager(this);

        barcodeView = findViewById(R.id.barcode_scanner);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(null));
        barcodeView.decodeContinuous(((BarcodeResult result) -> {
            if(result.getText() == null) {
                return;
            }

            barcodeView.setStatusText(result.getText());
            beepManager.playBeepSoundAndVibrate();

            outputTextView.setText(result.getText());
        }));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            barcodeView.resume();
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}