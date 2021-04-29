package com.localtovocal.Java;

import android.util.Log;

public class Bee extends Insect implements Attack {

    private Attack attack;

    public Bee(int size, String color, Attack attack) {
        super(size, color);
        this.attack = attack;
    }

    @Override
    public void move() {

        attack.move();
    }

    @Override
    public void attack() {
        attack.attack();
    }


    public Attack getAttack() {
        return attack;
    }

    public void setAttack(Attack attack) {
        this.attack = attack;
    }

    @Override
    public int getSize() {
        return super.getSize();
    }
}
