package deep.com.writesocketteste;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

@SuppressLint("StaticFieldLeak")
public class ESP8266Task extends AsyncTask<String, Integer, Void> {

    public interface callbackInterface {
        void finalizar(int[] rssi);
    }
    public callbackInterface call = null;

    public void setCallback(callbackInterface call){
        this.call = call;
    }
    public void setIp(String ip,int port){
        this.IpAddress = ip;
        this.Port = port;
    }

    ESP8266Task(){};
    public String IpAddress;
    public int Port;
    int[] RSSI = {0,0,0,0,0,0,0,0};


    @Override
    protected Void doInBackground(String... arg0) {
        Socket socket = null;
        DataOutputStream dataOutputStream = null;
        DataInputStream dataInputStream = null;
        Log.v("fatal",IpAddress+":"+Port);
        try {
            socket = new Socket(IpAddress, Port);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream.writeBytes("manda");
            byte[] buffer = new byte[10];
            int read;
            int i, j = 0;
            while (( read = dataInputStream.read(buffer, 0, buffer.length)) != -1) {
                for (i = 0; i < read; i++ ) {
                    RSSI[j] = buffer[i];
                    j++;
                }
            }
            //publishProgress(count);
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
    protected void onProgressUpdate(Integer... values) {

        //txt.setText("Running..."+ values[0]);
        //progressBar.setProgress(values[0]);

    }

    @Override
    protected void onPostExecute(Void result) {
        call.finalizar(RSSI);
        super.onPostExecute(result);
    }

}
