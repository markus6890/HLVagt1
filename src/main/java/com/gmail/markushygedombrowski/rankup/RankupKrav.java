package com.gmail.markushygedombrowski.rankup;

public class RankupKrav {
    private String rankup;
    private int bread;
    private int seeds;
    private int goldnugget;
    private int vagtposter;
    private int onTime;
    private int vagtlevel;



    private int money;


    public RankupKrav(String rankup, int bread, int seeds, int goldnugget, int vagtposter, int onTime, int vagtlevel, int money) {
        this.rankup = rankup;
        this.bread = bread;
        this.seeds = seeds;
        this.goldnugget = goldnugget;
        this.vagtposter = vagtposter;
        this.onTime = onTime;
        this.vagtlevel = vagtlevel;
        this.money = money;
    }
    public int getMoney() {
        return money;
    }
    public int getBread() {
        return bread;
    }

    public int getSeeds() {
        return seeds;
    }

    public int getGoldnugget() {
        return goldnugget;
    }

    public int getVagtposter() {
        return vagtposter;
    }

    public int getOnTime() {
        return onTime;
    }

    public int getVagtlevel() {
        return vagtlevel;
    }

}
