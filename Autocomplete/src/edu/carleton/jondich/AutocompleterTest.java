package edu.carleton.jondich;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AutocompleterTest {

    private Autocompleter completer;

    @BeforeEach
    void setUp() {
        completer = new Autocompleter("actors.txt");
    }

    @AfterEach
    void tearDown() {
        completer = null;
    }

    @Test
    void completeEmptyString() {
        List<String> completions = completer.getCompletions("");
        assertEquals(0, completions.size(), "Empty string generated one or more completions");
    }

    @Test
    void completeLastName() {
        List<String> completions = completer.getCompletions("Huston");
        String[] expected = {"Huston, Anjelica", "Huston, Walter"};
        assertEquals(expected, completions.toArray(), "Last-name search generated wrong results");
    }
}