package deep.com.writesocketteste.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RSSI8 {

    @SerializedName("ap1")
    @Expose
    private Integer ap1;
    @SerializedName("ap2")
    @Expose
    private Integer ap2;
    @SerializedName("ap3")
    @Expose
    private Integer ap3;
    @SerializedName("ap4")
    @Expose
    private Integer ap4;
    @SerializedName("ap5")
    @Expose
    private Integer ap5;
    @SerializedName("ap6")
    @Expose
    private Integer ap6;
    @SerializedName("ap7")
    @Expose
    private Integer ap7;
    @SerializedName("ap8")
    @Expose
    private Integer ap8;

    public RSSI8(int ap1, int ap2, int ap3, int ap4, int ap5, int ap6, int ap7, int ap8){
        this.ap1 = ap1;
        this.ap2 = ap2;
        this.ap3 = ap3;
        this.ap4 = ap4;
        this.ap5 = ap5;
        this.ap6 = ap6;
        this.ap7 = ap7;
        this.ap8 = ap8;
    }

    public Integer getAp1() {
        return ap1;
    }

    public void setAp1(Integer ap1) {
        this.ap1 = ap1;
    }

    public Integer getAp2() {
        return ap2;
    }

    public void setAp2(Integer ap2) {
        this.ap2 = ap2;
    }

    public Integer getAp3() {
        return ap3;
    }

    public void setAp3(Integer ap3) {
        this.ap3 = ap3;
    }

    public Integer getAp4() {
        return ap4;
    }

    public void setAp4(Integer ap4) {
        this.ap4 = ap4;
    }

    public Integer getAp5() {
        return ap5;
    }

    public void setAp5(Integer ap5) {
        this.ap5 = ap5;
    }

    public Integer getAp6() {
        return ap6;
    }

    public void setAp6(Integer ap6) {
        this.ap6 = ap6;
    }

    public Integer getAp7() {
        return ap7;
    }

    public void setAp7(Integer ap7) {
        this.ap7 = ap7;
    }

    public Integer getAp8() {
        return ap8;
    }

    public void setAp8(Integer ap8) {
        this.ap8 = ap8;
    }

    @Override
    public String toString(){
        String s ="[";
        s+=this.ap1+",";
        s+=this.ap2+",";
        s+=this.ap3+",";
        s+=this.ap4+",";
        s+=this.ap5+",";
        s+=this.ap6+",";
        s+=this.ap7+",";
        s+=this.ap8+"]";

        return s;

    }

}