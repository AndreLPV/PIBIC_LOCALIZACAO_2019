package deep.com.writesocketteste;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import deep.com.writesocketteste.Models.RSSI8;

@SuppressLint("StaticFieldLeak")
public class ESP8266UploadData extends AsyncTask<String, Integer, Void> {

    public interface callbackInterface {
        void finalizar(ArrayList<RSSI8> data);
        void atualizar(int percent);
    }
    public callbackInterface call = null;

    public void setCallback(callbackInterface call){
        this.call = call;
    }
    public void setIp(String ip,int port){
        this.IpAddress = ip;
        this.Port = port;
        dataRSSI = new ArrayList<RSSI8>();
    }

    public void setTimes(int times){
        n = times;
    }

    ESP8266UploadData(){};
    public String IpAddress;
    public int Port;
    int[] RSSI = {0,0,0,0,0,0,0,0};
    List<RSSI8> dataRSSI;

    int n = 10;


    @Override
    protected Void doInBackground(String... arg0) {
        int z;
        for(z = 0; z < n; z++){
            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;
            Log.v("fatal",IpAddress+":"+Port);
            try {
                socket = new Socket(IpAddress, Port);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream.writeBytes("receba");
                byte[] buffer = new byte[10];
                int read;
                int i, j = 0;
                while (( read = dataInputStream.read(buffer, 0, buffer.length)) != -1) {
                    for (i = 0; i < read; i++ ) {
                        RSSI[j] = buffer[i];
                        j++;
                    }
                }
                publishProgress((100/n)*(z+1));
                //if(RSSI[0] != 0 && RSSI[1] != 0 && RSSI[2] != 0 && RSSI[3] != 0 && RSSI[4] != 0 && RSSI[5] != 0 && RSSI[6] != 0 && RSSI[7] != 0)
                    dataRSSI.add(new RSSI8(RSSI[0],RSSI[1],RSSI[2],RSSI[3],RSSI[4],RSSI[5],RSSI[6],RSSI[7]));
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
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        call.atualizar(values[0]);
    }

    @Override
    protected void onPostExecute(Void result) {
        call.finalizar((ArrayList<RSSI8>) dataRSSI);
        super.onPostExecute(result);
    }

}