package com.example.fritzlor.mybooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static com.example.fritzlor.mybooks.Login.ACC_INFO;

public class Dashboard extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    private static final String TAG = "DashboardActivity";

    GoogleSignInAccount mAcct;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null)
            mAcct = b.getParcelable(ACC_INFO);

        // Capture the layout's TextView and set the string as its text
        TextView textView = (TextView) findViewById(R.id.tvWelcomeText);
        textView.append(mAcct.getDisplayName());
        textView.append(mAcct.getEmail());

        findViewById(R.id.bSignOut).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bSignOut:
                signOut();
        }
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        navigateToLogin();
                    }
                });
        revokeAccess();
    }

    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    public void navigateToLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
