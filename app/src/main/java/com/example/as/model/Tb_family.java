package com.example.as.model;

public class Tb_family {
    private String account;
    private String password;
    private String call;

    @Override
    public String toString() {
        return "Tb_family{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", call='" + call + '\'' +
                '}';
    }

    public Tb_family(){
        super();
    }

    public Tb_family(String account,String call){
        super();
        this.account = account;
        this.call = call;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }

    public String getCall() {
        return call;
    }
}
