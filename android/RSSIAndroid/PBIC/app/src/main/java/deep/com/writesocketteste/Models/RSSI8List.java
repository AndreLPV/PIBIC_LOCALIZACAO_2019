package deep.com.writesocketteste.Models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RSSI8List {

    @SerializedName("RSSI8")
    @Expose
    private ArrayList<RSSI8> rSSI8 = null;

    public RSSI8List(ArrayList<RSSI8> rssi){
        this.rSSI8 = rssi;
    }

    public ArrayList<RSSI8> getRSSI8() {
        return rSSI8;
    }

    public void setRSSI8(ArrayList<RSSI8> rSSI8) {
        this.rSSI8 = rSSI8;
    }

}
