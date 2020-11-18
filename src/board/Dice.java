package board;

import java.util.Random;

public class Dice {

    private static Random random = new Random();
    private static String[] rules = {"", "", // valorile pentru 0 si 1
        "SCHIMBA TABLA", // 2
        "DISPAR REGINELE. URMATOAREA DATA APAR PE POZITIILE UNDE AU DISPARUT", // 3
        "TURA POATE MERGE PE DIAGONALA (DOAR TURA ASTA)", // 4
        "INTERSCHIMBA PE TABLA UN CAL CU UN NEBUN (PROPRIU)", // 5
        "ACUMULEZI UN PUNCT MAGIC", // 6
            "NICI O REGULA", // 7
        "ACUMULEZI UN PUNCT MAGIC", // 8
        "SCHIMBA TABLA", // 9
        "SCHIMBA TABLA", // 10
        "DISPAR REGINELE", // 11
        "SCHIMBA TABLA", // 12
            // LA 5 PUNCTE MAGICE => POTI OPTA SA BLOCHEZI UN EFECT AL ADVERSARULUI
            // DACA NU IESE ASA => DOAR PUNCTE IN PLUS SI AIA E
    };

    private int firstDice = 1;
    private int secondDice = 1;
    private String rule = rules[2];

    public void rowDices (){
        firstDice = Math.abs(random.nextInt()) % 6 + 1; // VALORI [1..6]
        secondDice = Math.abs(random.nextInt()) % 6 + 1; // VALORI [1..6]
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
}
