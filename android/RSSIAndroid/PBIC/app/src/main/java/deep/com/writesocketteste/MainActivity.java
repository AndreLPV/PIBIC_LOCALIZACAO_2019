package deep.com.writesocketteste;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import deep.com.writesocketteste.Models.RSSI8;
import deep.com.writesocketteste.Models.RSSI8List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    //int[] RSSI = new int[8];
    int[] RSSI = {0,0,0,0,0,0,0,0};
    TextView textResponse;
    Button buttonConnect;
    EditText welcomeMsg;
    String IpAddress, serverUrl;
    int Port, times;

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
    EditText name;
    Button refreshButton, btn_upload;
    Switch preventScreenLockSwitch;
    boolean appInBackground = false;
    boolean simpleFind = true;

    String title;

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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch(id){
            case R.id.simpleFind:
                Log.v("fatal","simples find");
                simpleFind = true;
                break;
            case R.id.phoneFind:
                Log.v("fatal","achar sozinho");
                simpleFind = false;
                break;
            case R.id.sensorIp:
                Log.v("fatal","trocando IP");
                trocarIp();
                break;
            case R.id.sensorPort:
                Log.v("fatal","trocando porta");
                trocarPorta();
                break;
            case R.id.server:
                Log.v("fatal","trocando server");
                trocarServer();
                break;
            case R.id.times:
                Log.v("fatal","trocando vezes");
                trocarTimes();
                break;

        }
        return true;
    }

    private void trocarTimes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Número de leituras");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        input.setText(""+SharedPrefsUtils.getIntegerPreference(getApplicationContext(),"times",times));
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPrefsUtils.setIntegerPreference(getApplicationContext(),"times",Integer.parseInt(input.getText().toString()));
                times = Integer.parseInt(input.getText().toString());
                Toast.makeText(getApplicationContext(),"Nova vezes salva",Toast.LENGTH_LONG);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void trocarServer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Url do server");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        input.setText(SharedPrefsUtils.getStringPreference(this,"server",serverUrl));
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPrefsUtils.setStringPreference(getApplicationContext(),"server",input.getText().toString());
                serverUrl = input.getText().toString();
                Toast.makeText(getApplicationContext(),"Novo server salvo",Toast.LENGTH_LONG);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void trocarIp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ip do sensor na rede");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        input.setText(SharedPrefsUtils.getStringPreference(this,"ip",IpAddress));
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPrefsUtils.setStringPreference(getApplicationContext(),"ip",input.getText().toString());
                IpAddress = input.getText().toString();
                Toast.makeText(getApplicationContext(),"Novo IP salvo",Toast.LENGTH_LONG);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void trocarPorta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Porta do sensor na rede");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        input.setText(""+SharedPrefsUtils.getIntegerPreference(getApplicationContext(),"porta",Port));
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPrefsUtils.setIntegerPreference(getApplicationContext(),"porta",Integer.parseInt(input.getText().toString()));
                Port = Integer.parseInt(input.getText().toString());
                Toast.makeText(getApplicationContext(),"Nova porta salva",Toast.LENGTH_LONG);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rssi_data);
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); // Turn screen on if off
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        IpAddress = SharedPrefsUtils.getStringPreference(this,"ip","192.168.0.117");
        Port = SharedPrefsUtils.getIntegerPreference(this,"porta",8090);
        serverUrl = SharedPrefsUtils.getStringPreference(this,"server","http://192.168.0.11:5000");
        times = SharedPrefsUtils.getIntegerPreference(this,"times",10);

        AP1ValueTextView = (TextView) findViewById(R.id.AP1ValueTextView);
        AP2ValueTextView = (TextView) findViewById(R.id.AP2ValueTextView);
        AP3ValueTextView = (TextView) findViewById(R.id.AP3ValueTextView);
        AP4ValueTextView = (TextView) findViewById(R.id.AP4ValueTextView);
        AP5ValueTextView = (TextView) findViewById(R.id.AP5ValueTextView);
        AP6ValueTextView = (TextView) findViewById(R.id.AP6ValueTextView);
        AP7ValueTextView = (TextView) findViewById(R.id.AP7ValueTextView);
        AP8ValueTextView = (TextView) findViewById(R.id.AP8ValueTextView);
        localTextView = (TextView) findViewById(R.id.localTextView);
        name = (EditText) findViewById(R.id.et_name);
        sensorsDataReceivedTimeTextView = (TextView) findViewById(R.id.sensorsDataReceivedTimeTextView);
        refreshButton = (Button) findViewById(R.id.refreshButton);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        preventScreenLockSwitch = (Switch) findViewById(R.id.preventScreenLockSwitch);


        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(simpleFind){
                    ESP8266Task esp8266Task = new ESP8266Task();
                    esp8266Task.setIp(IpAddress,Port);
                    esp8266Task.setCallback(new ESP8266Task.callbackInterface() {
                        @Override
                        public void finalizar(int[] res) {
                            RSSI = res;
                            updateUserInterface();
                            localization();

                        }
                    });
                    esp8266Task.execute("manda");
                }else{
                    WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager.startScan();

                    List<ScanResult> wifiList = wifiManager.getScanResults();
                    Log.v("fatal",wifiList.toString());
                    for (ScanResult scanResult : wifiList) {
                        if(scanResult.SSID.equals("Andre.L.96")) RSSI[0] = scanResult.level;
                        if(scanResult.SSID.equals("Rafael net")) RSSI[1] = scanResult.level;
                        if(scanResult.SSID.equals("#NET-CLARO-WIFI")) RSSI[2] = scanResult.level;
                        if(scanResult.SSID.equals("Andre.L.96")) RSSI[3] = scanResult.level;
                        if(scanResult.SSID.equals("Andre.L.96")) RSSI[4] = scanResult.level;
                        if(scanResult.SSID.equals("Andre.L.96")) RSSI[5] = scanResult.level;
                        if(scanResult.SSID.equals("Andre.L.96")) RSSI[6] = scanResult.level;
                        if(scanResult.SSID.equals("Andre.L.96")) RSSI[7] = scanResult.level;

                        if(scanResult.SSID.equals("AP1")) RSSI[0] = scanResult.level;
                        if(scanResult.SSID.equals("AP2")) RSSI[1] = scanResult.level;
                        if(scanResult.SSID.equals("AP3")) RSSI[2] = scanResult.level;
                        if(scanResult.SSID.equals("AP4")) RSSI[3] = scanResult.level;
                        if(scanResult.SSID.equals("AP5")) RSSI[4] = scanResult.level;
                        if(scanResult.SSID.equals("AP6")) RSSI[5] = scanResult.level;
                        if(scanResult.SSID.equals("AP7")) RSSI[6] = scanResult.level;
                        if(scanResult.SSID.equals("AP8")) RSSI[7] = scanResult.level;

                    }
                    updateUserInterface();
                    localization();
                }
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
                ESP8266UploadData esp8266 = new ESP8266UploadData();
                esp8266.setIp(IpAddress,Port);
                esp8266.setTimes(times);
                esp8266.setCallback(new ESP8266UploadData.callbackInterface() {
                    @Override
                    public void finalizar(ArrayList<RSSI8> data) {
                        for(RSSI8 x: data){
                            Log.v("fatal",x.toString());
                        }
                        progressBar.setVisibility(View.GONE);
                        RSSI8List list = new RSSI8List(data);

                        upload(list);
                        name.setKeyListener((KeyListener) name.getTag());

                    }

                    @Override
                    public void atualizar(int percent) {
                        progressBar.setProgress(percent);
                    }
                });
                title = name.getText().toString().trim();
                if(!title.trim().isEmpty()){
                    name.setTag(name.getKeyListener());
                    name.setKeyListener(null);
                    esp8266.execute("manda");
                }else{
                    Toast.makeText(getApplicationContext() , "Digite o nome", Toast.LENGTH_SHORT).show();
                }


            }
        });

        preventScreenLockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (isChecked) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    btn_upload.setText("Adicionar novo local");
                    simpleFind = true;
                    Log.v("fatal",SharedPrefsUtils.getStringPreference(getApplicationContext(),"ip","Nao salvou"));
                    Log.v("fatal",SharedPrefsUtils.getIntegerPreference(getApplicationContext(),"porta",0)+"");
                    Log.v("fatal",SharedPrefsUtils.getStringPreference(getApplicationContext(),"server","sem url"));

                } else {
                    getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                    wifiManager.startScan();
                    List<ScanResult> wifiList = wifiManager.getScanResults();
                    String s= "";
                    for (ScanResult scanResult : wifiList) {
                        s += scanResult.SSID+": "+scanResult.level+" dB\n";
                    }
                    btn_upload.setText(s);
                    simpleFind = false;
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
        ApiClient.getRSSI8Client(serverUrl).getLocation(RSSI).enqueue(new Callback<String>() {
            public void onResponse(Call<String> call, Response<String> response ){

                if (response.isSuccessful() ) {
                    if(response.body() != null){
                        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);


                        localTextView.setText(response.body().toString());
                        localTextView.startAnimation(shake);
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
                Log.v("fatal",t.toString());
                Toast.makeText(getApplicationContext() , "Erro encontrado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void upload(RSSI8List data){
        ApiClient.getRSSI8Client(serverUrl).putLocation(data,title).enqueue(new Callback<String>() {
            public void onResponse(Call<String> call, Response<String> response ){

                if (response.isSuccessful() ) {
                    if(response.body() != null){
                        String list =  response.body();
                        Log.v("fatal",list);

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

}