package World;

import World.WorldObjects.Herbivore;
import World.WorldObjects.LifeBeingType;
import World.WorldObjects.Plant;

import java.util.*;

public class WorldMap {

    private final int length;
    private final int height;
    public HashMap<Position, Herbivore> herbivoreMap;
    public HashMap<Position, Plant> plantMap;

    public WorldMap(int length, int height) {
        this.length = length;
        this.height = height;
        this.herbivoreMap = new HashMap<>();
        this.plantMap = new HashMap<>();
    }

    public Boolean canMoveTo(Position position, HashMap<Position, Herbivore> map) {

        if (map.get(position) != null) {
            return false;
        }
        return true;
    }

    private Position correctPosition(Position position){
        if (position.getX() > length - 1) {
            position = new Position(0, position.getY());        //this block stops animals from
                                                                   // moving outside of map
        }
        if (position.getY() > height - 1) {
            position = new Position(position.getX(), 0);
        }
        if (position.getX() < 0) {
            position = new Position(length - 1, position.getY());
        }
        if (position.getY() < 0) {
            position = new Position(position.getX(), height - 1);
        }
        return position;
    }

    public void moveAnimalOnMap(Herbivore herbivore, HashMap<Position, Herbivore> map) {
        Position toMove = herbivore.getPosition();
        for (int i = 0; i < 8; i++) {                             //each animal has 8 tries to make a valid move
            toMove = herbivore.move();
            toMove = correctPosition(toMove);
            if (canMoveTo(toMove, herbivoreMap)) {
                break;
            } else {
                toMove = herbivore.getPosition();
            }

        }
        herbivore.setPosition(toMove);
        map.put(toMove, herbivore);
    }

    private void placePlant(Position positionToPlace, int energy) {
        Plant plant = new Plant();
        plant.setEnergy(energy);
        if (plantMap.get(positionToPlace) == null) {
            plantMap.put(positionToPlace, plant);
        }
    }

    public Position findFirstFreePosition(Herbivore herbivore) {
        Position startingPosition = herbivore.getPosition();
        for (int i = 0; i < 8; i++) {                             //finds first free spot around animal to multiply
            if (canMoveTo(herbivore.moveByDirecion(MoveDirection.parseFromInt(i)), herbivoreMap)) {
                return herbivore.moveByDirecion(MoveDirection.parseFromInt(i));
            }
        }
        return startingPosition;
    }

    public void generatePlants(int howMany, int jungleOnlyPercent, int energy, int jungleDivision) {
        Random random = new Random();
        int jungleXBegining = length/2 - jungleDivision;
        int jungleYBegining = height/2 - jungleDivision;
        int howManyInJungle = howMany * jungleOnlyPercent / 100;
        int howManyAnywhere = howMany - howManyInJungle;
        for (; howManyInJungle > 0; howManyInJungle--) {
            int xCoord = random.nextInt(length - jungleXBegining) + jungleXBegining;
            int yCoord = random.nextInt(height - jungleYBegining) + jungleYBegining;
            placePlant(new Position(xCoord, yCoord), energy);
        }
        for (; howManyAnywhere > 0; howManyAnywhere--) {
            int xCoord = random.nextInt(length);
            int yCoord = random.nextInt(height);
            placePlant(new Position(xCoord, yCoord), energy);
        }
    }

    public void eatPlant(Herbivore h) {
        if (plantMap.get(h.getPosition()) != null) {
            h.addEnergy(plantMap.get(h.getPosition()));
            plantMap.remove(h.getPosition());
            if (h.getEnergy() > h.getMaximumEnergy()) {
                h.setEnergy(h.getMaximumEnergy());
            }
        }
    }

    private void removeDeadAnimals() {
        HashMap<Position, Herbivore> newMap = new HashMap<>();
        for (Position position : herbivoreMap.keySet()) {
            Herbivore herbivore = herbivoreMap.get(position);
            if (herbivore.getEnergy() > 0) {
                newMap.put(herbivore.getPosition(), herbivore);
            }
        }
        herbivoreMap = newMap;
    }

    private void moveAllAnimals() {
        HashMap<Position, Herbivore> newMap = new HashMap<>();
        for (Position position : herbivoreMap.keySet()) {
            Herbivore herbivore = herbivoreMap.get(position);
            herbivore.setEnergy(herbivore.getEnergy() - 1);
            herbivore.setMultiplicationDayCounter(herbivore.getMultiplicationDayCounter() + 1);
            moveAnimalOnMap(herbivore, newMap);
        }
        herbivoreMap = newMap;


    }

    private void allAnimalsEat() {
        for (Position position : herbivoreMap.keySet()) {
            Herbivore herbivore = herbivoreMap.get(position);
            eatPlant(herbivore);
        }
    }


    private void animalsMultiply() {
        HashMap<Position, Herbivore> newMap = new HashMap<>();
        for (Position position : herbivoreMap.keySet()) {
            Herbivore herbivore = herbivoreMap.get(position);
            if (herbivore.getEnergy() > herbivore.getMaximumEnergy()/2) {
                Position firstFreePosition = findFirstFreePosition(herbivore);
                if (!firstFreePosition.equals(herbivore.getPosition()) && herbivore.getMultiplicationDayCounter() >= herbivore.getDaysBetweenMultiplication()) {
                    Herbivore offspring = herbivore.multiplayAsexualy(firstFreePosition);
                    herbivore.setMultiplicationDayCounter(0);
                    herbivore.setEnergy(herbivore.getEnergy() - offspring.getEnergy()/2);
                    newMap.put(offspring.getPosition(), offspring);
                }
            }
            newMap.put(herbivore.getPosition(), herbivore);
        }
        this.herbivoreMap = newMap;
    }

    public void generateDay(int howMany, int jungleOnlyPercent, int energy, int jungleDivide) {
        generatePlants(howMany, jungleOnlyPercent, energy, jungleDivide);
        removeDeadAnimals();
        moveAllAnimals();
        allAnimalsEat();
        animalsMultiply();
    }

    public String displayMap() {
        StringBuilder builder = new StringBuilder();
        Position currentPosition;
        for (int h = 0; h < height; h++) {
            for (int l = 0; l < length; l++) {
                currentPosition = new Position(h, l);
                if (herbivoreMap.containsKey(currentPosition)) {
                    builder.append(herbivoreMap.get(currentPosition).toString());
                } else if (plantMap.containsKey(currentPosition)) {
                    builder.append(plantMap.get(currentPosition).toString());
                } else builder.append(currentPosition.toString());
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public Boolean putHerbivore(Herbivore herbivore) {
        if (canMoveTo(herbivore.getPosition(), herbivoreMap)){
            herbivoreMap.put(herbivore.getPosition(), herbivore);
            return true;
        }
        return false;
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public int howManyAnimalsAlive(){
        return herbivoreMap.keySet().size();
    }
    public int howManyPlantsAlive(){
        return plantMap.keySet().size();
    }

}
