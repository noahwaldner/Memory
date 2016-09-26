//Memory App by Noah Waldner


package com.example.user.memoryapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button scan1, scan2, add;
    private TextView text1, text2;
    private ImageView image1, image2;
    private Integer button;
    private String result1 = null, result2 = null, arraystring;
    ArrayList<JSONArray> results = new ArrayList<JSONArray>();
    private PopupWindow pw;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scan1 = (Button) findViewById(R.id.button1);
        text1 = (TextView) findViewById(R.id.textView1);
        image1 = (ImageView) findViewById(R.id.imageView1);
        scan2 = (Button) findViewById(R.id.button2);
        text2 = (TextView) findViewById(R.id.textView2);
        image2 = (ImageView) findViewById(R.id.imageView2);
        add = (Button) findViewById(R.id.add_to_array);
    }


    protected void onResume(){
        super.onResume();

        scan1.setOnClickListener(this);
        scan2.setOnClickListener(this);
        add.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add("Log");
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                JSONArray ar = new JSONArray(results);
                log("{\"task\": \"Memory\", \"solution\": " + ar.toString() + "}");
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentIntegrator.REQUEST_CODE
                && resultCode == RESULT_OK) {

            Bundle extras = data.getExtras();
            String path = extras.getString(
                    Intents.Scan.RESULT_BARCODE_IMAGE_PATH);

            // Ein Bitmap zur Darstellung erhalten wir so:
            // Bitmap bmp = BitmapFactory.decodeFile(path)

            String code = extras.getString(
                    Intents.Scan.RESULT);

            setResult(path, code);

        }
    }



    public void onClick(View v){
        switch (v.getId()) {
            case R.id.button1:
                takeQrCodePicture();
                button = 1;
                break;
            case R.id.button2:
                takeQrCodePicture();
                button = 2;
                break;
            case R.id.add_to_array:
                addnewentry();
                break;
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                takeQrCodePicture();
                button = 1;
                break;
            case R.id.button2:
                takeQrCodePicture();
                button = 2;
                break;
            case R.id.add_to_array:
                addnewentry();
                break;
        }

    }


    public void addnewentry() {
        String[] entry = {result1, result2};
        try {
            JSONArray ar2 = new JSONArray(entry);
            results.add(ar2);
        } catch (JSONException e) {
        }
        ;

        add.setEnabled(false);
        image1.setImageURI(null);
        image2.setImageURI(null);
        text1.setText(" ");
        text2.setText(" ");
    }


    public void takeQrCodePicture() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(MyCaptureActivity.class);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setOrientationLocked(false);
        integrator.addExtra(Intents.Scan.BARCODE_IMAGE_ENABLED, true);
        integrator.initiateScan();
    }


    public void setResult(String path, String code) {
        if (button == 1) {
            button = null;
            text1.setText(code);
            image1.setImageURI(Uri.fromFile(new File(path)));
            result1 = code;
            if (text1.getText() != " ") {
                add.setEnabled(true);
            }
        } else {
            button = null;
            text2.setText(code);
            image2.setImageURI(Uri.fromFile(new File(path)));
            result2 = code;
            if (text2.getText() != " ") {
                add.setEnabled(true);
            }
        }
    }


    private void log(String solution) {
        Intent intent = new Intent("ch.appquest.intent.LOG");

        if (getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()) {
            Toast.makeText(this, "Logbook App not Installed", Toast.LENGTH_LONG).show();
            return;
        }

        intent.putExtra("ch.appquest.logmessage", solution);
        startActivity(intent);
    }



}
