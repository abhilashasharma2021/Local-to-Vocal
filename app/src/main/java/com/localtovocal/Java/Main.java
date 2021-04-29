package com.localtovocal.Java;

public class Main {

    public static void main(String[] args) {

        Bee bee1 = new Bee(1, "red", new AttackImplement("bee move", "bee attack"));
        bee1.attack();
    }
}
