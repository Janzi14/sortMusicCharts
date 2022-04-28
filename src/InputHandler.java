/*
 * InputHandler
 * Includes functionality for reading data from csv-files.
 * Author: Jakob Janz
 * Last Change: 22.04.2022
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputHandler {
    private static final List<List<String>> charts = new ArrayList<>();
    private static BufferedWriter buffer;
    private static final File file = new File("logFile.txt");

    protected static BufferedWriter handleBufferedWriters(File fileName) throws IOException {
        try {

            buffer = new BufferedWriter(new FileWriter(fileName));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static void handleInput() {
        StringBuilder logString = new StringBuilder();
        boolean isFirst = true;
        try (BufferedReader reader = new BufferedReader(new FileReader("music2022.csv")); BufferedWriter bw = handleBufferedWriters(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                if (fields.length != 6) {
                    logString.append(appendLine(fields, "Malformed input @ line: "));
                    continue;
                }
                logString.append(parseChecker(fields, isFirst));
                logString.append(ratingChecker(fields, isFirst));
                charts.add(Arrays.asList(fields));
                isFirst = false;
            }
            bw.write(logString.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String parseChecker(String[] array, boolean isFirst) throws IOException {
        String exceptionField = "";
        if (!isFirst) {
            try {
                for (int i = 0; i <= 2; i++) {
                    switch (i) {
                        case 0 -> exceptionField = "DW";
                        case 1 -> exceptionField = "LW";
                        case 2 -> exceptionField = "WW";
                    }
                    Integer.parseInt(array[i]);
                }

            } catch (NumberFormatException e) {
                return appendLine(array, "Number parsing error for " + exceptionField + " ");
            }
        }
        return "";
    }

    private static String ratingChecker(String[] array, boolean isFirst) throws IOException {
        if (!isFirst) {
            try {
                Double.parseDouble(array[5].replace(',', '.'));
            } catch (NumberFormatException e) {
                return appendLine(array, "Parse exception for Bewertung @ line: ");
            }
        }
        return "";
    }

    private static String appendLine(String[] array, String message) {
        StringBuilder lineString = new StringBuilder();
        lineString.append(message);
        for (int i = 0; i < array.length; i++) {
            if (i == 0) {
                lineString.append(array[i]);
            } else {
                lineString.append(";");
                lineString.append(array[i]);
            }
        }
        lineString.append(" -- ignoring line\n");
        return lineString.toString();
    }

    public static List<List<String>> getCharts() {
        return charts;
    }
}
