package ch.ethz.inf.vs.receiptscanner.scanner;

import android.graphics.Point;
import android.util.Log;

import com.google.firebase.ml.vision.text.FirebaseVisionText;

import java.util.ArrayList;
import java.util.HashMap;

import ch.ethz.inf.vs.receiptscanner.FoodInformation;

public class TextProcessor {

    public static final String TAG = "TextProcessor";

    private static final double SIMILARITY_THRESHOLD = 0.75;

    private static TextProcessor textProcessor;

    public static TextProcessor getInstance() {
        if (textProcessor == null) {
            textProcessor = new TextProcessor();
        }
        return textProcessor;
    }

    public DetectionResult parseText(FirebaseVisionText recognitionResult) {
        ArrayList<String> foundItems = new ArrayList<>();

        ArrayList<Point[]> sumCornerPointCandidates = new ArrayList<>();
        ArrayList<FirebaseVisionText.Element> sumCandidates = new ArrayList<>();
        for (FirebaseVisionText.TextBlock block : recognitionResult.getTextBlocks()) {
            for (FirebaseVisionText.Line line : block.getLines()) {
                //Log.d(TAG, line.getText());
                for (FirebaseVisionText.Element element : line.getElements()) {
                    String s = element.getText();
                    String key = matchesItems(s);
                    if (key != null && (StringSimilarity.similarity(key, "Total") > SIMILARITY_THRESHOLD || StringSimilarity.similarity(key, "Summe") > SIMILARITY_THRESHOLD)) {
                        sumCornerPointCandidates.add(element.getCornerPoints());
                    } else if (key != null) {
                        foundItems.add(key);
                    } else if (isDouble(element.getText()) > -1) {
                        sumCandidates.add(element);
                    }
                }
            }
        }

        // Trying to find the sum
        double sum = -1;
        if (!sumCornerPointCandidates.isEmpty() && ! sumCandidates.isEmpty()) {
            // select largest candidate
            int maxIndex = 0;
            double maxArea = 0;
            Point[] points;
            for (int i = 0; i < sumCornerPointCandidates.size(); i++) {
                points = sumCornerPointCandidates.get(i);
                double width = points[1].x - points[0].x;
                double height = points[3].y - points[0].y;
                double area = width * height;
                if (area > maxArea) {
                    maxArea = area;
                    maxIndex = i;
                }
            }
            Point[] sumCornerPoints = sumCornerPointCandidates.get(maxIndex);

            double minYDistance = Double.MAX_VALUE;
            int minIndex = 0;
            for (int i = 0; i < sumCandidates.size(); i++) {
                FirebaseVisionText.Element element = sumCandidates.get(i);
                double yDistance = yDistance(sumCornerPoints, element);
                if (yDistance < minYDistance) {
                    minYDistance = yDistance;
                    minIndex = i;
                }
            }
            sum = Double.valueOf(sumCandidates.get(minIndex).getText());
        }

        for (String s : foundItems) {
            Log.d(TAG, "Found item " + s);
        }
        Log.d(TAG, "Sum: " + sum);

        return new DetectionResult(foundItems, sum);
    }

    private String matchesItems(String s) {
        for (String key : FoodInformation.expiryInfo.keySet()) {
            if (StringSimilarity.similarity(key, s) >= 0.75) {
                return key;
            }
        }
        for (String key : FoodInformation.otherItems.keySet()) {
            if (StringSimilarity.similarity(key, s) >= 0.75) {
                return key;
            }
        }
        return null;
    }

    private double yDistance(Point[] sumCornerPoints, FirebaseVisionText.Element element) {
        // Check whether the element is in line with the sumCornerPoints
        double sumYLower = ((double)(sumCornerPoints[0].y + sumCornerPoints[1].y)) / 2;
        double sumYUpper = ((double)(sumCornerPoints[2].y + sumCornerPoints[3].y)) / 2;

        Point[] elementCornerPoints = element.getCornerPoints();
        double elementYLower = ((double)(elementCornerPoints[0].y + elementCornerPoints[1].y)) / 2;
        double elementYUpper = ((double)(elementCornerPoints[2].y + elementCornerPoints[3].y)) / 2;

        //Log.d(TAG, "Y values: " + sumYLower + ", " + sumYUpper + ", " + elementYLower + ", " + elementYUpper);

        double lowerDistance = Math.abs(sumYLower - elementYLower);
        double upperDistance = Math.abs(sumYUpper - elementYUpper);
        return (lowerDistance + upperDistance) / 2;
    }

    private double isDouble(String s) {
        double d = -1;
        try {
            d = Double.parseDouble(s);
        } catch (NumberFormatException nfe) {
        }
        return d;
    }
}
