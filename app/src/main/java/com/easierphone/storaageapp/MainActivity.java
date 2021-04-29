package com.easierphone.storaageapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DocumentsTheme_Document);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnStartActivity).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                BaseActivity.State mState = new BaseActivity.State();
                mState.action = BaseActivity.State.ACTION_GET_CONTENT;
                mState.acceptMimes = new String[]{".vcf"};

                Intent intent = new Intent(MainActivity.this, DocumentsActivity.class);
                intent.putExtra(DocumentsActivity.EXTRA_STATE, mState);
                intent.putExtra(DocumentsActivity.EXTRA_AUTHENTICATED, true);
                intent.putExtra(DocumentsActivity.EXTRA_ACTIONMODE, true);

                startActivityForResult(intent, 101);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            Log.d(TAG, "onActivityResult: resultCode: " + resultCode);
            if (data != null) {
                Log.d(TAG, "onActivityResult: path: " + data.getData().getPath());
            }
        }
    }
}