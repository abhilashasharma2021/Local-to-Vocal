package com.localtovocal.Java;

import android.util.Log;

public class AttackImplement implements Attack {

    private final String move;
    private final String attack;

    public AttackImplement(String move, String attack) {
        this.move = move;
        this.attack = attack;
    }

    @Override
    public void move() {
        System.out.println(move);
        Log.e("kcxmskdc", move);
    }

    @Override
    public void attack() {
        System.out.println(attack);
        Log.e("kcxmskdc", attack);
    }
}
