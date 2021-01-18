package console;

import java.util.Scanner;

public class Command {
    public String typeOfCommand;
    public int startX;
    public int endX;
    public int startY;
    public int endY;

    public Command(Scanner scanner) {
        String command;
        boolean validCommand = false;
        System.out.println("WAITING COMMAND. TRY:");
        System.out.println("show <x> <y> | x,y -> [0,7] OR");
        //System.out.println("move <xS> <yS> <xD> <yD> | xS.xD.yS,yD -> [0,7]");

        while (validCommand == false){
            command = scanner.nextLine();
            typeOfCommand = command.substring(0,4);
            if (typeOfCommand.equals("show") &&  command.charAt(4) == ' '
                                        && command.charAt(6) == ' '){
                startX = Integer.parseInt(command.substring(5,6));
                endX = Integer.parseInt(command.substring(7,8));
            }
            else if (typeOfCommand.equals("move") && command.charAt(4) == ' '
                                                && command.charAt(6) == ' '
                                                && command.charAt(8) == ' '
                                                && command.charAt(10) == ' '){

                startX = Integer.parseInt(command.substring(5,6));
                endX = Integer.parseInt(command.substring(7,8));
                startY =Integer.parseInt(command.substring(9,10));
                endY = Integer.parseInt(command.substring(11,12));

            }
            else {
                System.out.println("INVALID COMMAND! TRY:");
                System.out.println("show <x> <y> | x,y -> [0,7] OR");
                //System.out.println("move <xS> <yS> <xD> <yD> | xS.xD.yS,yD -> [0,7]");
                validCommand = false;
            }

            if (startX < 0 || endX > 7 || startY < 0 || endY > 7) {
                System.out.println("INVALID INDEX! TRY:");
                System.out.println("show <x> <y> | x,y -> [0,7] OR");
                //System.out.println("move <xS> <yS> <xD> <yD> | xS.xD.yS,yD -> [0,7]");

                validCommand = false;
            }
            else {
                validCommand = true;
            }
        }

    }

    public boolean isShowCommand (){
        return typeOfCommand.equals("show");
    }

    public boolean isMoveCommand (){
        return typeOfCommand.equals("move");
    }
}
