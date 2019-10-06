package ch.ethz.inf.vs.receiptscanner.scanner;

import java.util.ArrayList;

public class DetectionResult {

    private ArrayList<String> detectedItems;
    private double sum;

    public ArrayList<String> getDetectedItems() {
        return detectedItems;
    }

    public double getSum() {
        return sum;
    }

    public DetectionResult(ArrayList<String> detectedItems, double sum) {
        this.detectedItems = detectedItems;
        this.sum = sum;
    }
}
