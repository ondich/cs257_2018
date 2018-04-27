package edu.carleton.jondich;

public class Main {

    public static void main(String[] args) {
        Autocompleter completer = new Autocompleter(args[0]);
        for (String actor : completer.getCompletions("")) {
            System.out.println(actor);
        }
    }
}
