package com.adrenalinici.adrenaline.model;

import java.util.List;
import java.util.Optional;

public class PlayerDashboard {
    private List<AmmoColor> ammos;
    private List<PlayerColor> damages;
    private List<PlayerColor> marks;
    private List<Gun> loadedGuns;
    private List<Gun> unloadedGuns;
    private List<PowerUpCard> powerUpCards;
    private int skullsNumber;
    private int points;
    private boolean firstPlayer;

    public void addAmmmo(AmmoColor ammo) throws IllegalStateException{
        int countAmmo= 0;
        for(AmmoColor a : ammos){
            if(a.equals(ammo)){
                countAmmo++;
            }
        }
        if(countAmmo > 3){
            throw new IllegalStateException("i have plus than 3 ammos of the same colors");
        }else{
            ammos.add(ammo);
        }
    }
    public void removeAmmos(List<AmmoColor> ammos){
        for(AmmoColor a : this.ammos){
            if(a.equals(ammos)){
                this.ammos.remove(ammos);
            }
        }
    }

    public List<AmmoColor> getAmmos() {
        return ammos;
    }

    public void addDamages(List<PlayerColor> damages){
        this.damages.addAll(damages);
    }

    public void removeAllDamages(){
        this.damages.clear();
    }

   public List<PlayerColor> getDamages() {
        return damages;
    }

    public Optional<PlayerColor> getFirstDamage(){
        if (damages.size() >= 1)
        return Optional.of(damages.get(0));
        else return Optional.empty();
    }

    public Optional<PlayerColor> getKillDamage(){
        if (damages.size() >= 1)
            return Optional.of(damages.get(10));
        else return Optional.empty();
    }

    public Optional<PlayerColor> getCruelDamage(){
        if (damages.size() >= 1)
            return Optional.of(damages.get(11));
        else return Optional.empty();
    }

    public void addMarks(List<PlayerColor> marks){
        this.marks.addAll(marks);
    }

    public void removeMarks(List<PlayerColor> marks){
        for(PlayerColor a : this.marks){
            if(a.equals(marks)){
                this.marks.remove(marks);
            }
        }
    }

    public List<PlayerColor> getMarks() {
        return marks;
    }

    public void addLoadedGun(Gun loadedGun){
        loadedGuns.add(loadedGun);
    }

    public void removeLoadedGun(Gun loadedGun){
        loadedGuns.remove(loadedGun);
    }
    public List<Gun> getLoadedGuns(){
        return loadedGuns;
    }

    public void removeUnloadedGun(Gun loadedGun){
        unloadedGuns.remove(loadedGun);
    }

    public List<Gun> getUnloadedGuns(){
        return unloadedGuns;
    }

    public void addPowerUpCard(PowerUpCard powerUp){
        powerUpCards.add(powerUp);
    }

    public void removePowerUpCard(PowerUpCard powerUp){
        powerUpCards.remove(powerUp);
    }

    public List<PowerUpCard> getPowerUpCards(){
        return powerUpCards;
    }

    public void incrementSkullsNumber(){
        skullsNumber++;
    }

    public int getSkullsNumber(){
        return skullsNumber;
    }
    public void addPoints(int points){
        this.points+=points;
    }

    public int getPoints() {
        return points;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }
}
