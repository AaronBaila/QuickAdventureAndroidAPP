package com.example.tfgdam;

public class PlanObj {
    String direccion1, direccion2, direccion3, lugar1, lugar2, lugar3, tipoLugar1, tipoLugar2, tipoLugar3, nPlan;

    public PlanObj(String direccion1, String direccion2, String direccion3, String lugar1, String lugar2, String lugar3, String tipoLugar1, String tipoLugar2, String tipoLugar3, String nPlan) {
        this.direccion1 = direccion1;
        this.direccion2 = direccion2;
        this.direccion3 = direccion3;
        this.lugar1 = lugar1;
        this.lugar2 = lugar2;
        this.lugar3 = lugar3;
        this.tipoLugar1 = tipoLugar1;
        this.tipoLugar2 = tipoLugar2;
        this.tipoLugar3 = tipoLugar3;
        this.nPlan = nPlan;
    }

    public String getDireccion1() {
        return direccion1;
    }

    public void setDireccion1(String direccion1) {
        this.direccion1 = direccion1;
    }

    public String getDireccion2() {
        return direccion2;
    }

    public void setDireccion2(String direccion2) {
        this.direccion2 = direccion2;
    }

    public String getDireccion3() {
        return direccion3;
    }

    public void setDireccion3(String direccion3) {
        this.direccion3 = direccion3;
    }

    public String getLugar1() {
        return lugar1;
    }

    public void setLugar1(String lugar1) {
        this.lugar1 = lugar1;
    }

    public String getLugar2() {
        return lugar2;
    }

    public void setLugar2(String lugar2) {
        this.lugar2 = lugar2;
    }

    public String getLugar3() {
        return lugar3;
    }

    public void setLugar3(String lugar3) {
        this.lugar3 = lugar3;
    }

    public String getTipoLugar1() {
        return tipoLugar1;
    }

    public void setTipoLugar1(String tipoLugar1) {
        this.tipoLugar1 = tipoLugar1;
    }

    public String getTipoLugar2() {
        return tipoLugar2;
    }

    public void setTipoLugar2(String tipoLugar2) {
        this.tipoLugar2 = tipoLugar2;
    }

    public String getTipoLugar3() {
        return tipoLugar3;
    }

    public void setTipoLugar3(String tipoLugar3) {
        this.tipoLugar3 = tipoLugar3;
    }

    public String getnPlan() {
        return nPlan;
    }

    public void setnPlan(String nPlan) {
        this.nPlan = nPlan;
    }
}
