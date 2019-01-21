package Displayer;

import World.WorldMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DisplayWindow extends JFrame{

    private JTextArea textArea;
    private ActionListener actionListener;

    public DisplayWindow(){
        textArea = new JTextArea();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        add(textArea);
        setLocationRelativeTo(null);
        setTitle("DINOZAURY");
        setSize(300,300);
        setResizable(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN,15));

    }

    public void display(WorldMap map, String statistics){
        textArea.setText(map.displayMap() + statistics);
    }







}
