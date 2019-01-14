package deep.com.writesocketteste;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends Activity {
    //int[] RSSI = new int[8];
    int[] RSSI = {0,0,0,0,0,0,0,0};
    TextView textResponse;
    Button buttonConnect;
    EditText welcomeMsg;
    String IpAddress = "192.168.0.117";
    int Port = 8090;

    TextView sensorsDataReceivedTimeTextView;
    TextView AP1ValueTextView;
    TextView AP2ValueTextView;
    TextView AP3ValueTextView;
    TextView AP4ValueTextView;
    TextView AP5ValueTextView;
    TextView AP6ValueTextView;
    TextView AP7ValueTextView;
    TextView AP8ValueTextView;
    TextView localTextView;
    Button refreshButton;
    Switch preventScreenLockSwitch;
    boolean appInBackground = false;
    boolean doneEditing = true;

    @Override
    protected void onResume() {
        super.onResume();
        appInBackground = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        appInBackground = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rssi_data);
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); // Turn screen on if off
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        AP1ValueTextView = (TextView) findViewById(R.id.AP1ValueTextView);
        AP2ValueTextView = (TextView) findViewById(R.id.AP2ValueTextView);
        AP3ValueTextView = (TextView) findViewById(R.id.AP3ValueTextView);
        AP4ValueTextView = (TextView) findViewById(R.id.AP4ValueTextView);
        AP5ValueTextView = (TextView) findViewById(R.id.AP5ValueTextView);
        AP6ValueTextView = (TextView) findViewById(R.id.AP6ValueTextView);
        AP7ValueTextView = (TextView) findViewById(R.id.AP7ValueTextView);
        AP8ValueTextView = (TextView) findViewById(R.id.AP8ValueTextView);
        localTextView = (TextView) findViewById(R.id.localTextView);
        sensorsDataReceivedTimeTextView = (TextView) findViewById(R.id.sensorsDataReceivedTimeTextView);
        refreshButton = (Button) findViewById(R.id.refreshButton);
        preventScreenLockSwitch = (Switch) findViewById(R.id.preventScreenLockSwitch);


        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ESP8266Task esp8266Task = new ESP8266Task();
                esp8266Task.execute();
            }
        });

        preventScreenLockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        });
    }

    void updateUserInterface() {
        try {
            sensorsDataReceivedTimeTextView.post(new Runnable() {
                public void run() {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    sensorsDataReceivedTimeTextView.setText(dateFormat.format(new Date()));
                }
            });
            AP1ValueTextView.post(new Runnable() {
                public void run() {
                    AP1ValueTextView.setText(String.valueOf(RSSI[0]) + "dB");
                }
            });
            AP2ValueTextView.post(new Runnable() {
                public void run() {
                    AP2ValueTextView.setText(String.valueOf(RSSI[1]) + "dB");
                }
            });
            AP3ValueTextView.post(new Runnable() {
                public void run() {
                    AP3ValueTextView.setText(String.valueOf(RSSI[2]) + "dB");
                }
            });
            AP4ValueTextView.post(new Runnable() {
                public void run() {
                    AP4ValueTextView.setText(String.valueOf(RSSI[3]) + "dB");
                }
            });
            AP5ValueTextView.post(new Runnable() {
                public void run() {
                    AP5ValueTextView.setText(String.valueOf(RSSI[4]) + "dB");
                }
            });
            AP6ValueTextView.post(new Runnable() {
                public void run() {
                    AP6ValueTextView.setText(String.valueOf(RSSI[5]) + "dB");
                }
            });
            AP7ValueTextView.post(new Runnable() {
                public void run() {
                    AP7ValueTextView.setText(String.valueOf(RSSI[6]) + "dB");
                }
            });
            AP8ValueTextView.post(new Runnable() {
                public void run() {
                    AP8ValueTextView.setText(String.valueOf(RSSI[7]) + "dB");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("DeepBluee",e.getMessage());
        }
    }

    public void localization(){
        ApiClient.getRSSI8Client().getLocation(RSSI).enqueue(new Callback<String>() {
            public void onResponse(Call<String> call, Response<String> response ){

                if (response.isSuccessful() ) {
                    if(response.body() != null){
                        localTextView.setText(response.body().toString());
                    }
                }else {
                    Toast.makeText(getApplicationContext() ,
                            "Problemas com o server, tente novamente mais tarde",
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext() , "Erro encontrado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    public class ESP8266Task extends AsyncTask<Void, Void, Void> {
        String response = "";

        ESP8266Task(){};

        @Override
        protected Void doInBackground(Void... arg0) {
            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;
            try {
                socket = new Socket(IpAddress, Port);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                byte[] buffer = new byte[10];
                int read;
                int i = 0, j = 0;
                while (( read = dataInputStream.read(buffer, 0, buffer.length)) != -1) {
                    for (i = 0; i < read; i++ ) {
                        RSSI[j] = buffer[i];
                        j++;
                    }
                }
            } catch (IOException e) { }
            finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {}
                }
                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {}
                }
                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {}
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            updateUserInterface();
            localization();
            super.onPostExecute(result);
        }
    }
}