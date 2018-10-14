package limf.jlu.edu.cn.scrabblemobile.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import limf.jlu.edu.cn.scrabblemobile.R;

public class LobbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        Intent intent_get = getIntent();
        String userName = intent_get.getStringExtra("name");
    }
}
