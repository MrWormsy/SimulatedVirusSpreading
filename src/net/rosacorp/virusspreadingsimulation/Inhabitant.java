package net.rosacorp.virusspreadingsimulation;

import java.util.ArrayList;
import java.util.Random;

public class Inhabitant {

    private Integer id;
    private boolean isInfected;
    private float posX;
    private float posY;
    private boolean isCured;
    private boolean isAlive;
    private int dayInfected;

    private ArrayList<Integer> neighbors;

    public Inhabitant(Integer id) {

        // We set the id to the one given by argument
        this.id = id;

        // We set isInfected to false by default
        this.isInfected = false;

        // And alive
        this.isCured = false;
        this.isAlive = true;

        // The first inhabitant will be the one infected (patient 0)
        if (id == 0) {
            this.isInfected = true;

            // Add this person as an infected
            VirusSpreadingSimulation.theWorld.getInfected().add(id);
            VirusSpreadingSimulation.theWorld.getLastInfected().add(id);
        }

        // Generate a random position
        Random random = new Random(System.currentTimeMillis() + new Random().nextInt());
        this.posX = random.nextFloat() * VirusSpreadingSimulation.worldSize;
        this.posY = random.nextFloat() * VirusSpreadingSimulation.worldSize;

        // We set the list of neighbors to empty
        this.neighbors = new ArrayList<>();

        // System.out.println(this);
    }

    public double getDistanceBetween(Inhabitant inhabitant) {
        return Math.sqrt((inhabitant.getPosX()-this.getPosX())*(inhabitant.getPosX()-this.getPosX()) + (inhabitant.getPosY()-this.getPosY())*(inhabitant.getPosY()-this.getPosY()));
    }

    // Mark this person as infected
    public void setInfected() {
        if (!(this.isCured || !this.isAlive)) {

            this.isInfected = true;
            this.dayInfected = VirusSpreadingSimulation.theWorld.getDay();

            // Add him to the list of infected persons

            // TODO NOT THIS ONE BECAUSE WE CAN GET A CONCURENT EXCEPTION...
            // VirusSpreadingSimulation.theWorld.getInfected().add(this.id);

            VirusSpreadingSimulation.theWorld.getLastInfected().add(this.id);
        }
    }

    // If this guy has been infected 14 days ago and this method is called he can either recovers and becomes immune to the virus (with a probability of 0.97), or dies
    public void makeHimRecover() {
        if ((VirusSpreadingSimulation.theWorld.getDay() - this.dayInfected < 14) || (this.isCured || !this.isAlive))
            return;

        // This person recovers with a probability of 97%
        if (new Random(System.currentTimeMillis() + new Random().nextInt()).nextFloat() <= 0.97f) {
            // And set him to the cured ones
            VirusSpreadingSimulation.theWorld.getLastCured().add(this.id);
            this.isCured = true;
        } else {
            // Or the dead ones...
            VirusSpreadingSimulation.theWorld.getLastDeads().add(this.id);
            this.isAlive = false;
        }
    }

    // When a vaccine is found, we give it to this person to make him recover
    public void giveVaccine() {
        VirusSpreadingSimulation.theWorld.getLastCured().add(this.id);
        this.isCured = true;
    }

    @Override
    public String toString() {
        return "Inhabitant n." + id + " isInfected:" + isInfected + " at (posX=" + posX + ", posY=" + posY + ") with " + neighbors.size() + " neighbors";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setInfected(boolean infected) {
        isInfected = infected;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public ArrayList<Integer> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Integer> neighbors) {
        this.neighbors = neighbors;
    }

    public boolean isCured() {
        return isCured;
    }

    public void setCured(boolean cured) {
        this.isCured = cured;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getDayInfected() {
        return dayInfected;
    }

    public void setDayInfected(int dayInfected) {
        this.dayInfected = dayInfected;
    }
}
