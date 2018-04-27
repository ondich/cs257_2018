/**
 * Autocompleter.java
 * Jeff Ondich, 20 March 2018
 *
 * This class exposes a very simple interface for generating auto-completions of search strings.
 * The purpose of this class is to give the students in CS257 an opportunity to practice creating
 * unit tests.
 */
package edu.carleton.jondich;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Autocompleter {

    private ArrayList<String> actors;

    /**
     * @param dataFilePath the path to the data file containing the set of items to
     * from which auto-completed results will be drawn. (In the context of this assignment,
     * this will be the path to the actors.txt file I provided you. But we'll also talk
     * later about how you might use inheritance to create subclasses of Autocompleter
     * to use different datasets and different approaches to doing the autocompletion.)
     */
    public Autocompleter(String dataFilePath) {
        actors = new ArrayList<String>();

        File inputFile = new File(dataFilePath);
        Scanner scanner = null;
        try {
            scanner = new Scanner(inputFile);
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return;
        }

        // Load the lines of the actors file into the private ArrayList.
        while (scanner.hasNextLine()) {
            String actor = scanner.nextLine();
            actors.add(actor);
        }
    }

    /**
     * @param searchString the string whose autocompletions are sought
     * @return the list of potential completions that match the search string,
     *  sorted in decreasing order of quality of the match (that is, the matches
     *  are sorted from best match to weakest match)
     */
    public List<String> getCompletions(String searchString) {
        return actors;//new ArrayList<String>();
    }
}
