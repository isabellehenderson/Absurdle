// This class is a game of Absurdle. A dictionary of words is used for each game. For each turn, 
// a user guess is used to consider the list of possible secret words. Each time a user guesses, 
// Absurdle prunes its internal list as little as possible until the player guesses the target word. 

import java.util.*;
import java.io.*;

public class Absurdle  {
    public static final String GREEN = "🟩";
    public static final String YELLOW = "🟨";
    public static final String GRAY = "⬜";

    // [[ ALL OF MAIN PROVIDED ]]
    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        System.out.println("Welcome to the game of Absurdle.");

        System.out.print("What dictionary would you like to use? ");
        String dictName = console.next();

        System.out.print("What length word would you like to guess? ");
        int wordLength = console.nextInt();

        List<String> contents = loadFile(new Scanner(new File(dictName)));
        Set<String> words = pruneDictionary(contents, wordLength);

        List<String> guessedPatterns = new ArrayList<>();
        while (!isFinished(guessedPatterns)) {
            System.out.print("> ");
            String guess = console.next();
            String pattern = record(guess, words, wordLength);
            guessedPatterns.add(pattern);
            System.out.println(": " + pattern);
            System.out.println();
        }
        System.out.println("Absurdle " + guessedPatterns.size() + "/∞");
        System.out.println();
        printPatterns(guessedPatterns);
    }

    // [[ PROVIDED ]]
    // Prints out the given list of patterns.
    // - List<String> patterns: list of patterns from the game
    public static void printPatterns(List<String> patterns) {
        for (String pattern : patterns) {
            System.out.println(pattern);
        }
    }

    // [[ PROVIDED ]]
    // Returns true if the game is finished, meaning the user guessed the word. Returns
    // false otherwise.
    // - List<String> patterns: list of patterns from the game
    public static boolean isFinished(List<String> patterns) {
        if (patterns.isEmpty()) {
            return false;
        }
        String lastPattern = patterns.get(patterns.size() - 1);
        return !lastPattern.contains("⬜") && !lastPattern.contains("🟨");
    }

    // [[ PROVIDED ]]
    // Loads the contents of a given file Scanner into a List<String> and returns it.
    // - Scanner dictScan: contains file contents
    public static List<String> loadFile(Scanner dictScan) {
        List<String> contents = new ArrayList<>();
        while (dictScan.hasNext()) {
            contents.add(dictScan.next());
        }
        return contents;
    }


    // Takes the list containing the contents of the dictionary file and eliminates duplicates and
    // other words so that it contains only the words that are the length specified by the user. 
    // Parameters:
    // List<String> contents: a list containing all the words from dictionary being used
    // int wordLength: the user specified length of words to use
    // Returns:
    // Set<String>: contains only the words from the dictionary that are the length specified by 
    // the user and no duplicates
    // Exceptions:
    // wordLength < 1: throws IllegalArgumentException if the inputted word length is less than 1 
    public static Set<String> pruneDictionary(List<String> contents, int wordLength) {
        if(wordLength < 1){
            throw new IllegalArgumentException("Word length less than 1");
        }

        // creates auxiliary set with all the dictionary contents
        Set<String> pruned = new HashSet<>();
        for(String item : contents){
            pruned.add(item);
        }
        
        // iterates over auxiliary set and removes words that don't match the indicated
        // length
        Iterator<String> iter = pruned.iterator();
        while(iter.hasNext()){
            String element = iter.next();
            if(element.length() != wordLength){
                iter.remove();
            }
        }

        return pruned;
    }

    // Creates Wordle patterns based on a user guess for each word in the set of words. Returns 
    // the pattern that corresponds to the largest set of words still remaining and updates word
    // set to only include those words.
    // Parameters:
    // String guess: the user inputted guess
    // Set<String> words: the set of words being considered
    // int wordLength: the user specified length of words to use
    // Returns:
    // String: the pattern associated with the most words as a string of emojis
    // Exceptions:
    // words.isEmpty(): throws IllegalArgumentException if the set of words is empty
    // guess.length() != wordLength: throws IllegalArgumentException if length of guess doesn't 
    // match indicated word length
    public static String record(String guess, Set<String> words, int wordLength) {
        if(words.isEmpty()){
            throw new IllegalArgumentException("Word set empty");
        } 
        if(guess.length() != wordLength){
            throw new IllegalArgumentException("Guess not matching length");
        }
        
        Map<String, Set<String>> patternCounts = new TreeMap<>();
        // creates map of patterns associated with its set of words
        for(String word : words){
            String pattern = patternFor(word, guess);
            if(!patternCounts.containsKey(pattern)){
                patternCounts.put(pattern, new HashSet<>());
            }
            patternCounts.get(pattern).add(word); 
        }

        String maxPattern = null;
        int maxSize = 0;
        // finds which pattern is associated with the largest set of words
        for(String key : patternCounts.keySet()) {
            int setSize = patternCounts.get(key).size();
            if (setSize > maxSize) {
                maxPattern = key;
                maxSize = setSize;
            }
        }

        // prunes dictionary to just the words associated w/ maxPattern
        words.clear();
        words.addAll(patternCounts.get(maxPattern));
                
        return maxPattern;
    }

    // Takes a user guess and a word from the set of available words. Creates the Wordle pattern
    // for the guess compared against the given word. 
    // Parameters:
    // String word: the word from the set
    // String guess: the user's guess
    // Returns:
    // String: the associated pattern as a string of emoji squares
    public static String patternFor(String word, String guess) {
        
        // creates a list with each character of the guess
        List<String> letters = new ArrayList<>();
        for(int i = 0; i < guess.length(); i++){
            letters.add(guess.substring(i, i + 1));
        }

        // creates a map of each character associated with how many times it 
        // appears before being used
        Map<Character, Integer> counts = new HashMap<>();
        for(int j = 0; j < word.length(); j++){
            if(!counts.containsKey(word.charAt(j))){
                counts.put(word.charAt(j), 1);
            } else {
                counts.put(word.charAt(j), counts.get(word.charAt(j)) + 1);
            }
        }

        // checks if character is in the same location in guess and work and marks
        // w/ green if true and updates the map to reflect that the character has been used
        for(int k = 0; k < word.length(); k++){
            markGreen(k, letters, counts, word);
        }
        
        // checks if there’s an unused matching character from the word, marks w/ yellow
        // in the pattern and updates the map to reflect that the character has been used 
        for(int l = 0; l < word.length(); l++){
            for(int m = 0; m < letters.size(); m++){
                markYellow(l, m, letters, counts, word);
            }
        }

        // replaces all unused characters with gray 
        for(int n = 0; n < letters.size(); n++){
            markGray(n, letters);
        }
        
        // concatenates the list of now squares into a String
        String pattern = "";
        for(String square : letters){
            pattern += square;
        }

        return pattern;
    }  

    // Updates the pattern list to hold a GREEN square for each character which is in the same
    // index location in letters AND word, then updates map so that the count for that character
    // is reduced. 
    // Parameters;
    // int i: the index number of the word and list of letters being compared
    // List<String> letters: the letters of the guess separated out as a list
    // Map<Character, Integer> counts: the map of each character associated with how many times it 
    // appears before being used
    // String word: the word from the set being compared against
    public static void markGreen(int i, List<String> letters, Map<Character, 
                                 Integer> counts, String word){
        if(word.substring(i, i + 1).equals(letters.get(i))){
            letters.set(i, GREEN);
            counts.put(word.charAt(i), counts.get(word.charAt(i)) - 1);
        }
    }
    // Sets a YELLOW square in the pattern for each non-GREEN character and if it is an unused
    // matching character from the word, then updates the map to reflect that the character has
    // been used in a match.  
    // Parameters:
    // int i: index number of word
    // int j: index number of list of letters 
    // List<String> letters: the letters of the guess separated out as a list
    // Map<Character, Integer> counts: the map of each character associated with how many times it 
    // appears before being used
    // String word: the word from the set being compared against
    public static void markYellow(int i, int j, List<String> letters, 
                                    Map<Character, Integer> counts, String word){
        if(counts.get(word.charAt(i)) > 0 && word.substring(i, i + 1).equals(letters.get(j))){
            letters.set(j, YELLOW);
            if(counts.get(word.charAt(i)) > 0){
                counts.put(word.charAt(i), counts.get(word.charAt(i)) - 1);
            }
        }    
    }

    // Replaces all non-GREEN and non-yellow items in the list with GRAY squares.
    // int i: index number of list of characters
    // List<String> letters: the letters of the guess separated out as a list
    public static void markGray(int i, List<String> letters){
        String letter = letters.get(i);
        if(!letter.equals(GREEN) && !letter.equals(YELLOW)) {
            letters.set(i, GRAY);
        }
    }
}