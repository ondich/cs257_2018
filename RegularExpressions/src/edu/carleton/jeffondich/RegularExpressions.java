/**
 * RegularExpressions.java
 * Jeff Ondich, April 2017
 *
 * A simple project in support of an in-class lab on regular expressions.
 */
package edu.carleton.jeffondich;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressions {

    /**
     * Handy read-file-as-single-string method. Note that this method assumes
     * that the file's contents are encoded using UTF-8.
     *
     * Nice discussion:
     * http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
     *
     * @param path the file path of the desired file
     * @return the entire contents of the file, or the empty string if the file doesn't exist
     */
    public static String readFile(String path) {
        String result = "";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            result = new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            // No need to do anything here.
        }
        return result;
    }

    /**
     * Given a regular expression and a list of strings, return the strings in the list
     * that match the regular expression exactly (i.e. a matching string must match the
     * regex in its entirety).
     *
     * @param regex the regular expression
     * @param haystacks the list of strings to check for matches
     * @return the matching strings
     */
    public static List<String> stringsThatMatch(String regex, String[] haystacks) {
        ArrayList<String> matchingStrings = new ArrayList<String>();
        for (String haystack: haystacks) {
            if (haystack.matches(regex)) {
                matchingStrings.add(haystack);
            }
        }
        return matchingStrings;
    }

    public static List<String> getAllMatches(String regex, String haystack) {
        ArrayList<String> matches = new ArrayList<String>();
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(haystack);
        while (matcher.find()) {
            matches.add(matcher.group());
        }

        return matches;
    }

    public static void main(String[] args) {
//        // Example 1: the String.matches method
        String regex = ".*oo.*";
        String[] strings = {"cat", "dog", "moose", "raccoon", "okapi", "too cool", "ooze"};
        List<String> matchingStrings = stringsThatMatch(regex, strings);
        System.out.println("======= EXAMPLE 1 =======");
        for (String matchingString: matchingStrings) {
            System.out.println("\t" + matchingString);
        }
        System.out.println();

        // Example 2: the Pattern and Matcher classes, and character classes []
        regex = "[a-zA-Z]+[0-9]*";
        matchingStrings = getAllMatches(regex, "abc123 123 cdcd ef78");
        System.out.println("======= EXAMPLE 2 =======");
        for (String matchingString: matchingStrings) {
            System.out.println("\t" + matchingString);
        }
        System.out.println();

        // Parse the command line
//        String regex = null;
//        String haystack = null;
//        String path = null;
//        boolean hasBadArguments = false;
//        for (String arg: args) {
//            if (arg.startsWith("--path=")) {
//                path = arg.substring("--path".length() + 1);
//            } else if (arg.startsWith("--haystack=")) {
//                haystack = arg.substring("--haystack".length() + 1);
//            } else if (regex == null) {
//                regex = arg;
//            } else {
//                hasBadArguments = true;
//                break;
//            }
//        }
//
//        // If the parsing went badly, print a usage statement and exit.
//        if (hasBadArguments || (path == null && haystack == null)) {
//            System.err.println("Usage: java RegularExpressions [--haystack=string] [--path=string] regex");
//            System.err.println("  Prints all matches of the regex \"needle\" in the haystack. If the --path");
//            System.err.println("  option is present, matches are sought in the file named by the path.");
//            System.err.println("  Otherwise, matches are sought in the haystack. If both --path and --haystack");
//            System.err.println("  options are present, only the haystack will be searched.");
//            System.exit(1);
//        }
//
//        // Find all matches in the haystack
//        List<String> allMatches = null;
//        if (haystack != null) {
//            allMatches = getAllMatches(regex, haystack);
//        } else if (path != null) {
//            allMatches = getAllMatches(regex, readFile(path));
//        }
//
//        // Print the matches
//        for (String match: allMatches) {
//            System.out.println(match);
//        }
    }
}
