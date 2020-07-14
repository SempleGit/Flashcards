package flashcards;

import java.io.*;
import java.util.*;

public class FlashCard {
    Scanner sc = new Scanner(System.in);
    Map<String, String> cards = new Hashtable<>();
    Map<String, Integer> mistakeCount = new Hashtable<>();
    List<String> logList = new ArrayList<>();
    static String key;
    static String value;

    String importFile;
    boolean importSet = false;

    String exportFile;
    boolean exportSet = false;

    public String getInput() {
        String input = sc.nextLine();
        logList.add(input);
        return input;
    }

    public void output(String out) {
        logList.add(out);
        System.out.print(out);
    }

    public boolean addCard() {
        output("The card:\n");
        key = getInput();

        if (cards.containsKey(key)) {
            output(String.format("The card \"%s\" already exists.%n", key));
            return false;
        }
        output("The definition of the card:\n");
        value = getInput();

        if (cards.containsValue(value)) {
            output(String.format("The definition \"%s\" already exists.%n", value));
            return false;
        }

        cards.put(key, value);
        output(String.format("The pair (\"%s\":\"%s\") has been added.%n", key, value));
        return true;
    }

    public void removeCard() {
        output("The card:\n");
        String remove = getInput();

        if (cards.containsKey(remove)) {
            cards.remove(remove);
            mistakeCount.remove(remove);
            output(String.format("%s has been removed.%n", remove));
        } else {
            output(String.format("Can't remove \"%s\": there is no such card.%n", remove));
        }
    }

    public void askCard() {
        output("How many time to ask?\n");
        int n = Integer.parseInt(getInput());
        Random random = new Random();
        String randomTerm;
        String guess;
        String guessKey = "";
        Set<String> keySet = cards.keySet();
        List<String> terms = new ArrayList<>(keySet);

        for (int i = 0; i < n; i++) {
            randomTerm = terms.get(random.nextInt(terms.size()));
            output(String.format("Print the definition of \"%s\":%n", randomTerm));
            guess = getInput();
            if (cards.get(randomTerm).equals(guess)) {
                output("Correct answer\n");
            } else if (cards.containsValue(guess)) {
                for (String term : terms) {
                    if (cards.get(term).equals(guess)) {
                        guessKey = term;
                    }
                }
                output(String.format("Wrong answer. The correct one is \"%s\"" +
                        ", you've just written the definition of \"%s\".%n", cards.get(randomTerm), guessKey));
                incorrectGuess(randomTerm);

            } else {
                output(String.format("Wrong answer. The correct one is \"%s\".%n", cards.get(randomTerm)));
                incorrectGuess(randomTerm);
            }
        }
    }

    public void incorrectGuess(String randomTerm) {
        mistakeCount.put(randomTerm, mistakeCount.getOrDefault(randomTerm, 0) + 1);
    }

    public void importCard() {
        if (!importSet) {
            output("File name:\n");
            importFile = getInput();
        }
        File file = new File(importFile);
        int mc = 0;
        int count = 0;

        try (Scanner inFile = new Scanner(file)) {
            while (inFile.hasNextLine()) {
                key = inFile.nextLine();
                value = inFile.nextLine();
                mc = Integer.parseInt(inFile.nextLine());
                if (!"null".equals(value)) {
                    cards.put(key, value);
                    mistakeCount.put(key, mc);
                    count++;
                }
            }
            output(String.format("%d cards have been loaded.%n", count));
        } catch (FileNotFoundException e) {
            output("File not found.\n");
        }
    }

    public void exportCard() {
        if (!exportSet) {
            output("File name:\n");
            exportFile = getInput();
        }
        File file = new File(exportFile);

        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (String s : cards.keySet()) {
                printWriter.println(s);
                printWriter.println(cards.get(s));
                printWriter.println(mistakeCount.getOrDefault(s, 0));
            }
            output(String.format("%d cards have been saved.%n", cards.size()));
        } catch (IOException e) {
            output(String.format("Error: %s%n", e.getMessage()));
        }
    }

    public void log() {
        output("File name:\n");
        String fileLoc = getInput();
        File file = new File(fileLoc);

        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (String s : mistakeCount.keySet()) {
                printWriter.println(s);
                printWriter.println(mistakeCount.get(s));
            }
            output(String.format("The log has been saved.%n"));
        } catch (IOException e) {
            output(String.format("Error: %s%n", e.getMessage()));
        }
    }

    public void hardestCard() {
        int max = 0;
        Map<String, Integer> maxList = new Hashtable<>();
        for (Integer i : mistakeCount.values()) {
            max = i > max ? i : max;
        }

        if (max == 0) {
            output("There are no cards with errors.\n");
        } else {
            for (String s : mistakeCount.keySet()) {
                if (mistakeCount.get(s) == max) {
                    maxList.put(s, mistakeCount.get(s));
                }
            }
        }
        if (maxList.size() > 1) {
            String str = "";
            boolean firstString = true;
            for (String s : maxList.keySet()) {
                if (firstString) {
                    str += "\"" + s + "\"";
                    firstString = false;
                } else {
                    str += ", \"" + s + "\"";
                }
            }
            output(String.format("The hardest cards are %s. You have %d errors answering them.%n", str, max));
        } else {
            for (String s : maxList.keySet()) {
                output(String.format("The hardest card is \"%s\". You have %d errors answering it.%n", s, maxList.get(s)));
            }
        }
    }

    public void resetStats() {
        mistakeCount.clear();
        output("Card statistics has been reset.\n");
    }

    public void exit() {
        output("Bye bye!\n");
        if (exportSet) {
            exportCard();
        }
    }
}
