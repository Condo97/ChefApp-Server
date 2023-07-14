package com.pantrypro.core.generation.tagging;

import com.pantrypro.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagFetcher {

    private static String COMMA_DELIMITER = ", ";

    public static List<String> fetchTagsLowercase() {
        try (BufferedReader br = new BufferedReader((new FileReader(Constants.Tag_List_Location)))) {
            List<String> tags = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split(COMMA_DELIMITER);
                for (String tag: splitLine) {
                    tags.add(tag.toLowerCase());
                }

//                tags.addAll(Arrays.asList(line.split(COMMA_DELIMITER)));
            }

            return tags;
        } catch (Exception e) {
            System.out.println("Couldn't locate tags!");
            e.printStackTrace();

            return null;
        }
    }

}
