package org.dylangraham.rxjava;


import android.content.Context;

import java.util.concurrent.ThreadLocalRandom;

public class StuffGenerator {
    private Context context;

    public StuffGenerator(Context context) {
        this.context = context;
    }

    public String generateStuff() {
        try {
            // "Simulate" the delay of network.
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return makeList();
    }

    private String makeList() {
        Integer rand = ThreadLocalRandom.current().nextInt(0, 100);
        return rand.toString();
    }
}
