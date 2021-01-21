package gui;

import board.Controller;
import board.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Start {
    private static Dimension frameSize = new Dimension(550,375);
    private static Dimension rulesSize = new Dimension(400,500);

    private static String backgroundLocation = "images/mainBackground.png";

    private JFrame startFrame;
    private Board boardFrame;
    private String humanPlayerName;

    private JLabel titleLabel;
    private JLabel backgroundLabel;
    private JLabel enterYourNameLabel;
    private JTextField humanPlayerText;
    private JButton rulesButton;
    private JButton playButton;


    public Start() {
        //boardFrame = new Board(humanPlayerName);
        this.startFrame = new JFrame("LUCKY CHESS");
        this.startFrame.setSize(frameSize);
        //this.startFrame.setLayout(new BorderLayout());
        this.startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // set the background image
        setBackgroundImage();
        // set the title and the other components
        this.titleLabel = new JLabel("LUCKY CHESS", SwingConstants.CENTER);
        this.titleLabel.setFont(new Font("LUCKY CHESS", Font.BOLD, 40));
        this.startFrame.add(titleLabel, BorderLayout.NORTH);
        // other components
        this.enterYourNameLabel = new JLabel("ENTER YOUR NAME, LITTLE PLAYER", SwingConstants.CENTER);
        this.enterYourNameLabel.setFont(new Font("ENTER YOUR NAME, LITTLE PLAYER", Font.PLAIN, 14));

        this.humanPlayerText = new JTextField(15);
        this.humanPlayerText.setFont(new Font("", Font.ITALIC, 15));

        this.rulesButton = new JButton("CHECK THE RULES");
        this.rulesButton.setBackground(Color.WHITE);
        this.rulesButton.setOpaque(true);
        this.rulesButton.setForeground(Color.BLACK);

        this.playButton = new JButton("   PLAY  ");
        this.playButton.setFont(new Font("   PLAY  ", Font.BOLD, 22));
        this.playButton.setBackground(Color.BLACK);
        this.playButton.setOpaque(true);
        this.playButton.setForeground(Color.WHITE);

        this.rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame rulesFrame = new JFrame("RULES FRAME");
                rulesFrame.setSize(rulesSize);

                //this.startFrame.setLayout(new BorderLayout());
                rulesFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                rulesFrame.setResizable(false);
                rulesFrame.setVisible(true);
                rulesFrame.setLocation(1100, 200);

                // TO DO: sa pun regulile
            }
        });


        this.playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                //Game game = new Game(humanPlayerName);
                foo();

                //boardFrame.play();
                //Controller controller = new Controller(game, boardFrame);

                //controller.play();



            }
        });



        this.startFrame.setLayout(new FlowLayout());
        this.startFrame.add(enterYourNameLabel, BorderLayout.SOUTH);
        this.startFrame.add(humanPlayerText, BorderLayout.SOUTH);
        this.startFrame.add(rulesButton);
        this.startFrame.add(playButton);

        this.startFrame.setResizable(false);
        this.startFrame.setVisible(true);
        centreFrame();

    }

    private void foo() {
        String humanPlayerName = humanPlayerText.getText();
        Board boardFrame = new Board(humanPlayerName, this);
        startFrame.setVisible(false);
    }

    public JFrame getStartFrame() {
        return startFrame;
    }

    public String getHumanPlayerName() {
        return humanPlayerName;
    }

    public Board getBoardFrame() {
        return boardFrame;
    }

    public void centreFrame() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.startFrame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.startFrame.getHeight()) / 2);
        this.startFrame.setLocation(x, y);
    }


    void setBackgroundImage (){
        ImageIcon imageIcon = new ImageIcon(backgroundLocation);
        Image image = imageIcon.getImage();
        Image resizedImage = image.getScaledInstance(648, 200, Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(resizedImage);
        this.backgroundLabel = new JLabel(imageIcon);
        this.startFrame.add(backgroundLabel, BorderLayout.CENTER);
    }



}
