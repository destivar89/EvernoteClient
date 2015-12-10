package com.bq.evernoteclient.login;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.bq.evernoteclient.R;
import com.bq.evernoteclient.evernoteapi.EvernoteApiManager;
import com.bq.evernoteclient.notes.activity.NoteListActivity;
import com.evernote.client.android.EvernoteSession;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EvernoteApiManager manager;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = EvernoteApiManager.getInstance(getApplicationContext());

        loginButton = (Button) findViewById (R.id.login_button);
        loginButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button){
            manager.authenticate(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EvernoteSession.REQUEST_CODE_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    Intent notesIntent = new Intent(this, NoteListActivity.class);
                    startActivity(notesIntent);
                } else {
                    //TODO: show retry message
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


}
