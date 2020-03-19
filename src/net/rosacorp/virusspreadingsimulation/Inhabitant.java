package net.rosacorp.virusspreadingsimulation;

import java.util.ArrayList;
import java.util.Random;

public class Inhabitant {

    // Variables
    private Integer id;
    private boolean isInfected;
    private float posX;
    private float posY;
    private boolean isCured;
    private boolean isAlive;
    private int dayInfected;

    // Arraylist used to infect this person's neighbors
    private ArrayList<Integer> neighbors;

    // Constructor : require the person's id
    public Inhabitant(Integer id) {

        // We set the id to the one given by argument
        this.id = id;

        // We set isInfected to false by default
        this.isInfected = false;

        // And alive
        this.isAlive = true;

        // Cured is false by default
        this.isCured = false;

        // The first inhabitant will be the one infected (patient 0)
        if (id == 0) {
            this.isInfected = true;

            // Add this person as an infected one
            VirusSpreadingSimulation.theWorld.getInfected().add(id);
            VirusSpreadingSimulation.theWorld.getLastInfected().add(id);
        }

        // Generate a random position on the map and uniformly distributed
        Random random = new Random(System.currentTimeMillis() + new Random().nextInt());

        // As random.nextFloat() give a random float between 0 and 1 we can set easily the positions as follow
        this.posX = random.nextFloat() * VirusSpreadingSimulation.worldSize;
        this.posY = random.nextFloat() * VirusSpreadingSimulation.worldSize;

        // We set the list of neighbors to empty
        this.neighbors = new ArrayList<>();
    }

    // Get the distance between two persons (using basic algebra)
    public double getDistanceBetween(Inhabitant inhabitant) {
        return Math.sqrt((inhabitant.getPosX() - this.getPosX()) * (inhabitant.getPosX() - this.getPosX()) + (inhabitant.getPosY() - this.getPosY()) * (inhabitant.getPosY() - this.getPosY()));
    }

    // Mark this person as infected
    public void setInfected() {

        // Only if he is not already infected and not dead
        if (!(this.isCured || !this.isAlive)) {

            // Set as infected and set the day when he is infected
            this.isInfected = true;
            this.dayInfected = VirusSpreadingSimulation.theWorld.getDay();

            // We don't do that because we can get a ConcurrentModificationException
            // VirusSpreadingSimulation.theWorld.getInfected().add(this.id);

            // Instead we store him inside a buffer value
            VirusSpreadingSimulation.theWorld.getLastInfected().add(this.id);
        }
    }

    // If this guy has been infected 14 days ago, this method is called. He can either recovers and becomes immune to the virus (with a probability of 0.97), or dies
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

    // Beautify when printing an Inhabitant
    @Override
    public String toString() {
        return "Inhabitant n." + id + " isInfected:" + isInfected + " at (posX=" + posX + ", posY=" + posY + ") with " + neighbors.size() + " neighbors";
    }

    // Getters and Setters

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
