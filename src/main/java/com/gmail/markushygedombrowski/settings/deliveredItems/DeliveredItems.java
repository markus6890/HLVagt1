package com.gmail.markushygedombrowski.settings.deliveredItems;

import java.util.UUID;

public interface DeliveredItems {
    UUID getUUID();
    int getSeed();
    int getBread();
    int getIronHelmet();
    int getIronChestplate();
    int getIronLeggings();
    int getIronBoots();
    int getIronSword();
    int getDiamondHelmet();
    int getDiamondChestplate();
    int getDiamondLeggings();
    int getDiamondBoots();
    int getDiamondSword();
    int getHeads();
    void setSeed(int seed);
    void setBread(int bread);
    void setIronHelmet(int ironHelmet);
    void setIronChestplate(int ironChestplate);
    void setIronLeggings(int ironLeggings);
    void setIronBoots(int ironBoots);
    void setIronSword(int ironSword);
    void setDiamondHelmet(int diamondHelmet);
    void setDiamondChestplate(int diamondChestplate);
    void setDiamondLeggings(int diamondLeggings);
    void setDiamondBoots(int diamondBoots);
    void setDiamondSword(int diamondSword);
    void setHeads(int heads);
    void debug();
}
