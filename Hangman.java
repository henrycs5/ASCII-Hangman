import java.io.FileWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.BufferedWriter;

/**
 * Don't torture Victim... what?
 *
 * @Henry. C
 * @5/15/25 - 5/22/25 (Perfectly a week)
 */
public class Hangman
{
    private String fileName;
    private String currentWord;
    private String hiddenWord;
    private String hangmanWords;
    private int round;
    private int totalAttempts;
    private int attempts;
    private int wins;
    private int points;
    private boolean ongoing = true;
    private ArrayList<String> list;

    public static void main(String[] args) {
        Hangman victim = new Hangman();
        victim.runGame();
        //System.out.println(victim);
    }

    public Hangman(String fileName, int attempts) {
        this.hangmanWords = "Hangman Words.txt";
        this.fileName = fileName;
        this.attempts = attempts;
        totalAttempts = attempts;
        list = new ArrayList<>();
    }

    public Hangman(String fileName) {
        this(fileName, 7);
    }

    public Hangman() {
        this("Victim.txt", 7);
    }

    public void runGame() {
        File victim = new File(fileName);
        Scanner input = new Scanner(System.in);
        boolean yes = false;
        if(!victim.exists()) {
            yes = addWords(hangmanWords);
            round = 1;
            storeData(fileName);
            System.out.println("New Hangman!");
        }
        if(victim.exists()) { // VICTIM WILL ALWAYS EXIST EZ EZ EZ.
            if(!yes) {
                getData(fileName);
            }
            while(list.size() >= 0) {
                if(round == 0) {
                    round = 1;
                }
                boolean areYouSure = false;
                if(ongoing && currentWord != null && !currentWord.equals("null")) {
                    while(attempts > 0) {
                        if(currentWord.equals(hiddenWord)) {
                            System.out.println("Hidden Word: " + hiddenWord);
                            System.out.println("!!!!!!!!!!!!!! YOU GOT THE WORD !!!!!!!!!!!!!");
                            wins++;
                            break;
                        }
                        try {
                            System.out.println("<<<<<<<<<<<<<<<<<<<<<<>>>>>>>>>>>>>>>>>>>>>");
                            System.out.println("Round " + round + " 🔥️‍");
                            System.out.println("Attempt " + attempts);
                            hangman_ASCII_Art();
                            System.out.println("Hidden Word: " + hiddenWord);
                            System.out.println("Please enter a \"LETTER\"");
                            System.out.print("Your answer: ");
                            String word = input.nextLine();
                            if(word.toLowerCase().equals("guess who got powers?")) {
                                attempts = 0;
                                areYouSure = true;
                                break;
                            }
                            if(word.toLowerCase().equals("skip")) {
                                attempts = 0;
                                break;
                            }
                            if(word.toLowerCase().equals("stop")) {
                                hangman_ASCII_Art("Frame???");
                                return;
                            }
                            guess(word);
                        }
                        catch (Exception e) {
                            System.out.println("FOOL! YOU WASTED YOUR ATTEMPT!");
                        }
                        storeData(fileName);
                    }
                    if(areYouSure) {
                        System.out.println("Are you sure?");
                        System.out.println("(End game?)");
                        if(input.nextLine().toLowerCase().equals("pretty sure")) {
                            System.out.println("No more game :P");
                            break;
                        }
                        else {
                            areYouSure = false;
                        }
                    }
                    hangman_ASCII_Art();
                    System.out.println("\nThe word was: " + currentWord);
                    System.out.println("Wins: " + wins);
                    System.out.println("Points: " + points);
                    System.out.println("Remaining Attempts: " + attempts);
                    wait(2000);
                    System.out.println();
                    attempts = totalAttempts;
                    round++;
                    ongoing = false;
                    storeData(fileName);
                }
                else {
                    if(!list.isEmpty()) {
                        hiddenWord = randomWord();
                        ongoing = true;
                        storeData(fileName);
                    }
                    else {
                        break;
                    }
                }
            }

            System.out.println("Total Wins: " + wins);
            wait(1000); // Ahh the goodness of Lua.
            System.out.println("Total Points: " + points);
            wait(1000);

            do {
                System.out.println("Play again?");
                System.out.println("yes? no? reset data?");
                if(input.hasNextLine()) {
                    String option = input.nextLine().toLowerCase();
                    System.out.println();
                    if(option.equals("reset data")) {
                        round = 1;
                        wins = 0;
                        points = 0;
                        attempts = totalAttempts;
                        currentWord = null;
                        hiddenWord = null;
                        while(list.size() > 0) {
                            list.remove(0);
                        }
                        storeData(fileName);
                        System.out.println("Reseted!");
                    }
                    if(option.equals("yes")) {
                        addWords(hangmanWords);
                        break;
                    }
                    if(option.equals("no")) {
                        hangman_ASCII_Art("Frame???");
                        return;
                    }
                }
            }
            while(true);
            runGame();
        }
    }

    private void guess(String str) {
        if(currentWord.indexOf(str.toLowerCase()) < 0 && currentWord.indexOf(str.toUpperCase()) < 0) {
            System.out.println("WRONG! LOSE A POINT!");
            points--;
            attempts--;
            return;
        }
        for(int i = 0; i < hiddenWord.length(); i++) {
            String sub = currentWord.substring(i,i+1);
            if(sub.equalsIgnoreCase(str) && !hiddenWord.substring(i,i+1).equalsIgnoreCase(str)) {
                points++;
                hiddenWord = hiddenWord.substring(0,i) + sub + hiddenWord.substring(i+1);
                System.out.println("You got one point :D!");
            }
        }
    }

    public boolean addWords(String textFile) {
        File text = new File(textFile);
        try {
            Scanner reader = new Scanner(text);
            while(reader.hasNextLine()) {
                String word = reader.nextLine();
                list.add(word);
            }
            reader.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("Victim isn't playing any games.");
            System.out.println("File has not been found.");
            e.printStackTrace();
        }
        return true;
    }

    private void storeData(String data) {
        File victim = new File(data);
        boolean apend = false;
        try {
            FileWriter fw = new FileWriter(victim, apend);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write("//////////////////////// Round //////////////////////// \n" + round + "\n");
            bw.write("//////////////////////// Ongoing //////////////////////// \n" + ongoing + "\n");
            bw.write("//////////////////////// Total Attempts //////////////////////// \n" + totalAttempts + "\n");
            bw.write("//////////////////////// Attempts //////////////////////// \n" + attempts + "\n");

            bw.write("//////////////////////// Wins //////////////////////// \n" + wins + "\n");
            bw.write("//////////////////////// Points //////////////////////// \n" + points + "\n");
            bw.write("//////////////////////// Current Word //////////////////////// \n" + currentWord + "\n");
            bw.write("//////////////////////// Hidden Word //////////////////////// \n" + hiddenWord + "\n");

            bw.write("//////////////////////// File Name //////////////////////// \n" + fileName + "\n");
            bw.write("//////////////////////// List //////////////////////// " + "\n"); 
            for(int i = 0; i < list.size(); i++) {
                bw.write(list.get(i) + "\n");
            }
            bw.close();
            fw.close();
        }
        catch(IOException e) {
            System.out.println("\"Your data has been corrupted\"");
            e.printStackTrace();
        }
    }

    private void getData(String text) {
        File victim = new File(text);
        try {
            Scanner readFile = new Scanner(victim);
            while(readFile.hasNextLine()) {
                String str = readFile.nextLine();
                // System.out.println(str);
                // [About game]
                if(str.indexOf("Round") >= 0) {
                    round = readFile.nextInt();
                }
                else if(str.indexOf("Ongoing") >= 0) {
                    ongoing = readFile.nextBoolean();
                }
                else if(str.indexOf("Total Attempts") >= 0) {
                    totalAttempts = readFile.nextInt();
                }
                else if(str.indexOf("Attempts") >= 0) {
                    attempts = readFile.nextInt();
                }

                // [Stats]

                else if(str.indexOf("Wins") >= 0) {
                    wins = readFile.nextInt();
                }
                else if(str.indexOf("Points") >= 0) {
                    points = readFile.nextInt();
                }
                else if(str.indexOf("Current Word") >= 0) {
                    currentWord = readFile.nextLine();
                }
                else if(str.indexOf("Hidden Word") >= 0) {
                    hiddenWord = readFile.nextLine();
                }

                // [Files]

                else if(str.indexOf("File Name") >= 0) {
                    fileName = readFile.nextLine();
                }
                else if(str.indexOf("List") >= 0) {
                    while(readFile.hasNextLine()) {
                        list.add(readFile.nextLine());
                    }
                }
            }
            readFile.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("Victim escapes?");
            e.printStackTrace();
        }
    }

    public String randomWord() {
        int rng = (int)(Math.random() * list.size());
        String word = list.remove(rng);
        currentWord = word;
        String blank = "";

        for(int i = 0; i < word.length(); i++) {
            String sub = word.substring(i,i+1);
            char symbol = sub.charAt(0);
            
            if(!Character.isLetterOrDigit(symbol) && !Character.isWhitespace(symbol)) {
                blank += sub;
            }
            else if(!sub.equals(" ")) {
                blank += "_";
            }
            else {
                blank += " ";
            }
        }
        return blank;
    }

    public void hangman_ASCII_Art() {
        hangman_ASCII_Art("Frame" + attempts);
    }

    public void hangman_ASCII_Art(String str) {
        String text = "Hangman ASCII Art.txt";
        File asciiArt = new File(text);
        String kill = "$End$";
        try {
            Scanner reader = new Scanner(asciiArt);
            while(reader.hasNextLine()) {
                String input = reader.nextLine();
                // System.out.println("Looking: " + input);
                if(input.indexOf(str) >= 0) {
                    // System.out.println("Well we got in!");
                    while(reader.hasNextLine()) 
                    {
                        input = reader.nextLine();
                        if(input.indexOf(kill) < 0) {
                            System.out.println(input);
                        }
                        else {
                            return;
                        }
                    }
                }
            }
            reader.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("Error reading file " + text);
            e.printStackTrace();
        }
    }

    public static void wait(int time) {
        try {
            Thread.sleep(time);
        }
        catch(Exception e) {
            System.out.println("The pause aint pausing.");
        }
    }

    public String toString() {
        String wordaaa = "";
        for(String word : list) {
            wordaaa += word + "\n";
        }
        return 
        "***************************************"       +
        "\n[About Game]"                                +
        "\nRound " + round + "️‍ ️‍🔥"                       +
        "\nIs Ongoing?: " + ongoing                     +
        "\nTotal Attempts: " + totalAttempts            +
        "\nCurrent Attempts: " + attempts               +
        "\n***************************************"     +
        "\n[Stats]"                                     +
        "\nWins: " + wins                               +
        "\nPoints: " + points                           +
        "\nCurrent Word: " + currentWord                +
        "\nHidden Word: "+ hiddenWord                   + 
        "\n***************************************"     +
        "\nList:\n" + list                           +
        "\n***************************************"     +
        "\n[Files]"                                     +
        "\nFile Name: " + fileName                      + 
        "\nHangman Words File: " + hangmanWords         +
        "\n***************************************";
    }
}