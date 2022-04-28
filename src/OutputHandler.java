/*
 * OutputHandler
 * Handles writing and sorting the output.
 * Author: Jakob Janz
 * Last Change: 22.4.2022
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class OutputHandler {
    private static final DecimalFormat format = new DecimalFormat("0.00");
    private static final List<List<String>> chartList = InputHandler.getCharts();
    private static final File file = new File("musicSorted2022.csv");
    private static final ListCompare compareArtists = new ListCompare();

    public static void sortList() {
        List<List<String>> sortedListWithDuplicates = new ArrayList<>();
        for (List<String> l : chartList) {
            String artist = String.valueOf(l.get(4));
            if (!sortedListWithDuplicates.contains(artist)) {
                if (artist.contains(",")) {
                    String[] artistArray = artist.split(",");
                    for (String musician : artistArray) {
                        musician = musician.trim();
                        sortedListWithDuplicates.add(sortingMethod(musician, chartList.subList(1, chartList.size())));
                    }
                } else {
                    sortedListWithDuplicates.add(sortingMethod(artist, chartList.subList(1, chartList.size())));
                }
            }
        }
        List<List<String>> sortedList = new ArrayList<>(
                new HashSet<>(sortedListWithDuplicates));
        writeToCSV(sortedList);
    }

    private static List<String> sortingMethod(String artist, List<List<String>> list) {
        short count = 0;
        float avgRating = 0;
        StringBuilder ratings = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            for (String s : list.get(i)) {
                if (s.contains(artist)) {
                    count++;
                    avgRating += Float.parseFloat(list.get(i).get(5).replace(",", "."));
                    if (i == 0) {
                        ratings.append(list.get(i).get(5).replace(",", "."));
                    } else {
                        ratings.append(" ,");
                        ratings.append(list.get(i).get(5).replace(",", "."));
                    }
                }
            }

        }
        avgRating /= count;
        return Arrays.asList(artist, String.valueOf(count), format.format(avgRating).replace(",", "."), ratings.toString());
    }

    private static void writeToCSV(List<List<String>> list) {
        try (final BufferedWriter buffer = InputHandler.handleBufferedWriters(file)) {
            list.sort(compareArtists);
            list.add(0, Arrays.asList("Interpret", "Number of Tracks", "Average Rating", "Single Ratings"));
            for (List<String> strings : list) {
                if (strings.contains("Interpret")) {
                    if (strings.contains("NaN")) {
                        continue;
                    }

                }
                buffer.append(String.join(";", strings)).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
