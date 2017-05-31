package amjath.purosh.zencode;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String[] permissions = new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (getApplicationContext()
                .checkCallingOrSelfPermission(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(permissions,
                    100);

        } else {

            startActivity(new Intent(MainActivity.this, IssuesActivity.class));
            finish();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startActivity(new Intent(MainActivity.this, IssuesActivity.class));
                finish();

            } else {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage("Permission not Granted\nUser Avatar will not be Displayed");

                alertDialogBuilder.setPositiveButton("Continue",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                arg0.dismiss();

                                startActivity(new Intent(MainActivity.this, IssuesActivity.class));
                                finish();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        }
    }
}
