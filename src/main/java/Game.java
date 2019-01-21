import Displayer.DisplayWindow;
import World.Position;
import World.WorldMap;
import World.WorldObjects.Herbivore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class Game implements ActionListener {


    private WorldMap gameMap;
    private int startingAnimalsNo;
    private int plantsPerDay;
    private int plantsJungleOnly;
    private Timer timer;
    int plantEnergy;
    private DisplayWindow displayer;
    private int days;
    private int jungleDivide;

    public WorldMap getGameMap() {
        return gameMap;
    }

    public Game(int length, int height, int startingAnimalsNo, int plantsPerDay, int jungleOnlyPercent, int plantEnergy, int jungleDivide) {
        this.displayer = new DisplayWindow();
        timer = new Timer(1000, this);
        this.gameMap = new WorldMap(length, height);
        this.startingAnimalsNo = startingAnimalsNo;
        this.plantsPerDay = plantsPerDay;
        this.plantsJungleOnly = jungleOnlyPercent;
        this.plantEnergy = plantEnergy;
        this.jungleDivide = jungleDivide;
        days = 0;
        generateAnimals();
        timer.start();
    }

    public void generateAnimals() {
        Random random = new Random();
        int length = gameMap.getLength();
        int height = gameMap.getHeight();
        for (int animalsCounter = 0; animalsCounter < startingAnimalsNo; animalsCounter++){
            int positionXValue = random.nextInt(length);
            int positionYValue = random.nextInt(height);
            Herbivore herbivore = new Herbivore(new Position(positionXValue, positionYValue));
            gameMap.putHerbivore(herbivore);
        }
    }

    public void generateDay(int jungleDivide){
        gameMap.generateDay(plantsPerDay, plantsJungleOnly, plantEnergy, jungleDivide);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        days++;
        generateDay(jungleDivide);
        displayer.display(gameMap, generateStatistics());
    }

    public String generateStatistics(){
        StringBuilder builder = new StringBuilder();
        builder.append("\n" + "Days" + days + "\n");
        builder.append("Animals" + gameMap.howManyAnimalsAlive() + "\n");
        builder.append("Plants" + gameMap.howManyPlantsAlive());
        return builder.toString();
    }


}
