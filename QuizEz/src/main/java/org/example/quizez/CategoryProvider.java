package org.example.quizez;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class that returns all quiz categories
 * used in checkBoxes
 */
public class CategoryProvider {
    public static List<String> getAllAvailableCategories(){

        return Arrays.asList(
                "Nieokreślona",
                "Gry wideo",
                "Geografia",
                "Religie świata",
                "Filmy i seriale",
                "Historia świata",
                "Kosmos",
                "Nauki ścisłe"
                );
    }
}
