import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, David Lutch
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // YOUR CODE HERE — Call your recursive method!
        makeWords("", letters);

    }
    private void makeWords(String currentString, String letters) {
            if (!currentString.isEmpty()) {
                words.add(currentString);
            }
            // The if statement is the base case
            if (!letters.isEmpty()) {
                for (int i = 0; i < letters.length(); i++) {
                    // Taking the current string and adding the next letter to it
                    String newCurrentString = currentString + letters.charAt(i);
                    makeWords(newCurrentString, letters.substring(0, i) + letters.substring(i + 1));
                }
            }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // Sorting via the lexicographic difference (lowest value first)
        words = mergeSort(words);

    }
    private ArrayList<String> mergeSort(ArrayList<String> arrayOfWords) {
        // Return's the array if it's already split up
        if (arrayOfWords.size() == 1) {
            return arrayOfWords;
        }
            ArrayList<String> arr1 = new ArrayList<String>();
            ArrayList<String> arr2 = new ArrayList<String>();

            for (int i = 0; i < arrayOfWords.size(); i++) {
                // Adds the first half of the array to array 1
                if (arrayOfWords.size() / 2 > i) {
                    arr1.add(arrayOfWords.get(i));
                } else {
                    arr2.add(arrayOfWords.get(i));
                }

            }
            // Recursively calls itself to split both halves into single array's
            arr1 = mergeSort(arr1);
            arr2 = mergeSort(arr2);
            return merge(arr1, arr2);
    }
    // Puts the words in order using mergesort
    private ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2) {
            ArrayList<String> merged = new ArrayList<String>();
            int index1 = 0, index2 = 0, count = 0;

            while (index1 < arr1.size() && index2 < arr2.size()) {
                // If the current word in array 1 goes before the current word in array 2
                // Then add it to the arraylist named merged
                if (arr1.get(index1).compareTo(arr2.get(index2)) < 0) {

                    merged.add(count, arr1.get(index1++));
                }
                else {
                    merged.add(count, arr2.get(index2++));
                }
                count++;
            }
            // Copy over any remaining elements
            while (index1 < arr1.size()) {
                merged.add(count++, arr1.get(index1++));
            }

            while (index2 < arr2.size()) {
                merged.add(count++, arr2.get(index2++));
            }
            return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // YOUR CODE HERE
        int startIndex = 0;
        int endIndex = DICTIONARY_SIZE - 1;

        for (int i = 0; i < words.size(); i++) {
            if (!found(words.get(i), startIndex, endIndex)) {
                // Remove the word from the list if it's not a valid word
                words.remove(i);
                i--;
            }
        }
    }
    // Recursively calls itself to see if each word created is a valid word by using binary search
    private boolean found(String targetWord, int startIndex, int endIndex) {
        int midIndex = (startIndex + endIndex) / 2;
        if (targetWord.equals(DICTIONARY[midIndex])) {
            return true;
        }
        // Returns false if the starting index is out of bounds
        if (startIndex > endIndex) {
            return false;
        }
        // Checks to see if the target word is to left of the middle index
        if (targetWord.compareTo(DICTIONARY[midIndex]) < 0) {
            // Shifts the middle index to the left
            return found(targetWord, startIndex,midIndex - 1);
        }
        if (targetWord.compareTo(DICTIONARY[midIndex]) > 0) {
            // Shifts the middle index to the right
            return found(targetWord, midIndex + 1, endIndex);
        }
        return true;
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {
        // Prompt for letters until given only letters.
       Scanner s = new Scanner(System.in);
       String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
