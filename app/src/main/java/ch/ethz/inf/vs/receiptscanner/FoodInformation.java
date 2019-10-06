package ch.ethz.inf.vs.receiptscanner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class FoodInformation {

    public static HashMap<String, Integer> expiryInfo = new HashMap<>();
    public static HashMap<String, String> storageInfo = new HashMap<>();
    public static HashMap<String, String> otherItems = new HashMap<>();

    public static void initialize(String[] names, int[] expiry, String[] storage, String[] other) {

        for (int i = 0; i < names.length; i++) {
            expiryInfo.put(names[i], expiry[i]);
            storageInfo.put(names[i], storage[i]);
        }

        for (int i = 0; i < other.length; i++) {
            otherItems.put(other[i], "nothing");
        }
    }

    public static String calculateExpiryDate(String name, String date) {
        int days = 7;
        if (expiryInfo.containsKey(name)) {
            days = expiryInfo.get(name);
        }
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException p) {
            // exception
        }
        d.setTime(d.getTime() + (long) days * 1000 * 60 * 60 * 24);

        return df.format(d);
    }

    public static int calculateDaysUntilExpiry(String expiryDateString) {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        Date expiryDate = null;
        try {
            expiryDate = df.parse(expiryDateString);
        } catch (ParseException p) {
            // exception
        }
        long startTime = System.currentTimeMillis();
        long endTime = expiryDate.getTime();
        long diffTime = endTime - startTime;
        int diffDays = (int) (diffTime / (1000 * 60 * 60 * 24));
        return diffDays;
    }

}
