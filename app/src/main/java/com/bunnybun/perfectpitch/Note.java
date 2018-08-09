package com.bunnybun.perfectpitch;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Note {
    private String name;
    private int numTotal;
    private int numCorrect;

    public Note(String name) {
        this.name = name;
        numTotal = 0;
        numCorrect = 0;
    }

    private static double round(double val, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = new BigDecimal(Double.toString(val));
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }

    public String getName() {
        return name;
    }

    public int getNumTotal() {
        return numTotal;
    }

    public int getNumCorrect() {
        return numCorrect;
    }

    public void updateStat(boolean correct) {
        numTotal += 1;

        if (correct) {
            numCorrect += 1;
        }
    }

    public double getCorrectPercentage() {
        if (numTotal == 0) {
            return 0.00;
        }

        return round((numCorrect / (double) numTotal) * 100, 2);
    }
}
