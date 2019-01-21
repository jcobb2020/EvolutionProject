package World.WorldObjects;

public class Plant {

    private LifeBeingType type = LifeBeingType.PLANT;
    private int energy;

    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("|*|");
        return builder.toString();
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }
}
