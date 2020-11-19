package board;

import java.util.Random;

public class Dice {

    private static Random random = new Random();
    private static String[] rules = {"", "", // valorile pentru 0 si 1
        "SCHIMBA TABLA", // 2
        "<html> DISPAR REGINELE. URMATOAREA DATA <br> APAR PE POZITIILE UNDE AU DISPARUT </html>", // 3
        "<html> TURA POATE MERGE PE <br> DIAGONALA (DOAR TURA ASTA) </html>", // 4
        "<html> INTERSCHIMBA PE TABLA <br> UN CAL CU UN NEBUN (PROPRIU) </html>", // 5
        "ACUMULEZI UN PUNCT MAGIC", // 6
            "NICI O REGULA", // 7
        "ACUMULEZI UN PUNCT MAGIC", // 8
        "<html> INTERSCHIMBA PE TABLA <br> UN CAL CU UN NEBUN (PROPRIU) </html>", // 9
        "<html> TURA POATE MERGE PE <br> DIAGONALA (DOAR TURA ASTA) </html>", // 10
        "<html> DISPAR REGINELE. URMATOAREA DATA <br> APAR PE POZITIILE UNDE AU DISPARUT </html>", // 11
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
