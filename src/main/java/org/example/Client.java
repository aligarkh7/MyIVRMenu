package org.example;

public class Client {
    private String name;
    private int balance;
    private long internetTraffic;

    public Client(String name, int balance, long internetTraffic) {
        this.name = name;
        this.balance = balance;
        this.internetTraffic = internetTraffic;
    }

    public String getName() {
        return name;
    }

    public int getBalance() {
        return balance;
    }

    public long getInternetTraffic() {
        return internetTraffic;
    }
}
