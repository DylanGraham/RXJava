package org.dylangraham.rxjava;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class StuffGenerator {
    private Context context;

    public StuffGenerator(Context context) {
        this.context = context;
    }

    public List<String> generateStuff() {
        try {
            // "Simulate" the delay of network.
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return makeList();
    }

    private List<String> makeList() {
        List<String> stuffList = new ArrayList<>();
        stuffList.add("abc");
        stuffList.add("def");
        stuffList.add("ghi");
        stuffList.add("jkl");
        stuffList.add("mno");
        stuffList.add("pqr");
        stuffList.add("stu");
        stuffList.add("vwx");
        stuffList.add("yz");
        return stuffList;
    }
}
