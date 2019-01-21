package World.WorldObjects;
import World.MoveDirection;
import World.Position;

import java.util.Random;


public class Herbivore {
    private LifeBeingType lifeBeingType = LifeBeingType.HERBIVORE;


    private int energy;
    private int[] movingGenes = {0,0,0,0,0,0,0,0};
    private Position position;
    private int daysBetweenMultiplication = 5;



    private int multiplicationDayCounter = 0;

    public int getMaximumEnergy() {
        return maximumEnergy;
    }

    private int maximumEnergy = 30;


    public Herbivore(Position position){
        Random random = new Random();
        this.energy = maximumEnergy;
        for (int gene = 0; gene < 8; gene++){
            movingGenes[gene] = random.nextInt(10) + 1;
        }
        this.position = position;
    }

    public Herbivore(int[] genes, Position position){
        this.movingGenes=genes;
        this.position = position;
        this.energy = maximumEnergy;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    private void eat(Plant plant){
        this.energy = this.energy + plant.getEnergy();
    }

    public Herbivore multiplayAsexualy(Position freePosition){
        Random random = new Random();
        int geneNumber = random.nextInt(8);
        int mutationValue = random.nextInt(3);
        int sign = random.nextInt(2);
        if(sign==1){
            sign = -1;
        }
        else sign = 1;
        int modifiedGeneValue = this.movingGenes[geneNumber] + sign*mutationValue;
        int[] genes = this.movingGenes;
        genes[geneNumber] = modifiedGeneValue;
        for(int i = 0; i<8; i++){
            if(genes[i] <= 0){
                genes[i] = 1;
            }
        }
        Herbivore offspring = new Herbivore(genes, freePosition);
        return offspring;
    }

    public String toString(){
        return "|H|";
    }

    public void addEnergy(Plant plant){
        this.energy = this.energy + plant.getEnergy();
    }

    public Position move(){
        MoveDirection direction = directionToMove();
        Position toMove = moveByDirecion(direction);
        return toMove;
    }

    public Position moveByDirecion(MoveDirection direction){
        Position newPosition;
        switch (direction){
            case NORTH:
                newPosition = new Position(this.position.getX(), this.position.getY()+1);
                break;
            case SOUTH:
                newPosition = new Position(this.position.getX(), this.position.getY()-1);
                break;
            case EAST:
                newPosition = new Position(this.position.getX()-1, this.position.getY());
                break;
            case WEST:
                newPosition = new Position(this.position.getX()+1, this.position.getY());
                break;
            case NORTHEAST:
                newPosition = new Position(this.position.getX()-1, this.position.getY()+1);
                break;
            case SOUTHEAST:
                newPosition = new Position(this.position.getX()-1, this.position.getY()-1);
                break;
            case NOTRHWEST:
                newPosition = new Position(this.position.getX()+1, this.position.getY()+1);
                break;
            case SOUTHWEST:
                newPosition = new Position(this.position.getX()+1, this.position.getY()-1);
                break;
            default:
                newPosition = this.position;
        }
        return newPosition;
    }

    private MoveDirection directionToMove(){
        Random random = new Random();
        int moveProbabilitySum = sumProbability();
        int moveNumber = random.nextInt(moveProbabilitySum);
        int geneNumber = 0;
        while (moveNumber>0){
            moveNumber = moveNumber - movingGenes[geneNumber];
            geneNumber ++;
        }
        geneNumber --;
        return MoveDirection.parseFromInt(geneNumber);
    }

    private int sumProbability(){
        int probabilitySum=0;
        for(int i = 0; i<8; i++){
            probabilitySum = probabilitySum + movingGenes[i];
        }
        return probabilitySum;
    }

    public int getEnergy() {
        return energy;
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getDaysBetweenMultiplication() {
        return daysBetweenMultiplication;
    }

    public void setDaysBetweenMultiplication(int daysBetweenMultiplication) {
        this.daysBetweenMultiplication = daysBetweenMultiplication;
    }

    public int getMultiplicationDayCounter() {
        return multiplicationDayCounter;
    }

    public void setMultiplicationDayCounter(int multiplicationDayCounter) {
        this.multiplicationDayCounter = multiplicationDayCounter;
    }

}
