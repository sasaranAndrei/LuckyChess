package board;

import java.util.Random;

public class Dice {

    private static Random random = new Random();
    private static String[] rules = {"", "", // valorile pentru 0 si 1
        "SE ROTESTE TABLA CU 180°", // 2
        "<html> DISPAR REGINELE. URMATOAREA DATA <br> APAR PE POZITII ALEATORII </html>", // 3
        "<html> TURA POATE MERGE PE DIAGONALA </html>", // 4
        "<html> POTI INTERSCHIMBA PE TABLA <br> UN CAL CU UN NEBUN (PROPRIU) </html>", // 5
        "ACUMULEZI UN PUNCT MAGIC", // 6
            "NICI O REGULA", // 7
        "ACUMULEZI UN PUNCT MAGIC", // 8
        "<html> POTI INTERSCHIMBA PE TABLA <br> UN CAL CU UN NEBUN (PROPRIU) </html>", // 9
        "<html> TURA POATE MERGE PE DIAGONALA </html>", // 10
        "<html> DISPAR REGINELE. URMATOAREA DATA <br> APAR PE POZITII ALEATORII </html>", // 11
        "SE ROTESTE TABLA CU 180°", // 12
            // LA 3 PUNCTE MAGICE => POTI OPTA SA BLOCHEZI URMATORUL EFECT AL ADVERSARULUI

    };

    private int firstDice = 1;
    private int secondDice = 1;
    private String rule = rules[2];

    private static int[] firstDiceTest =  {1, 1, 1, 1, 2, 2, 1, 1} ;
    private static int[] secondDiceTest = {2, 1, 2, 1, 2, 3, 2, 2};
    private static int testCounter = 0; // 3  2  3  2  4  5  3  3

    public void rollDice (){
        firstDice = Math.abs(random.nextInt()) % 6 + 1; // VALORI [1..6]
        secondDice = Math.abs(random.nextInt()) % 6 + 1; // VALORI [1..6]

        // here we force the dices
        //firstDice = 1;
        //secondDice = 2;

        /*
        if (testCounter < firstDiceTest.length){
            firstDice = firstDiceTest[testCounter];
            secondDice = secondDiceTest[testCounter];
            testCounter++;
        }

         */





        rule = rules[firstDice + secondDice];

    }

    public int getFirstDice (){
        return firstDice;
    }

    public int getSecondDice (){
        return secondDice;
    }

    public String getRule (){
        return rule;
    }

    @Override
    public String toString() {
        return "Dice{" +
                "firstDice=" + firstDice +
                ", secondDice=" + secondDice +
                ", rule='" + rule + '\'' +
                '}';
    }
}
