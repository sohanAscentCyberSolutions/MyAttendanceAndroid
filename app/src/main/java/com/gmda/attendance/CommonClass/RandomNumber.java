package com.gmda.attendance.CommonClass;

import java.util.Random;

public class RandomNumber {

    public float randomFloatBetween(float min, float max) {
        Random r = new Random();
        return min + r.nextFloat() * (max - min);
    }
}
