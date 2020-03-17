package net.rosacorp.virusspreadingsimulation;

import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

import java.lang.instrument.Instrumentation;
import java.util.ArrayList;
import java.util.Random;

public class Inhabitant {

    private Integer id;
    private boolean isInfected;
    private float posX;
    private float posY;
    private boolean alive;

    private ArrayList<Integer> neighbors;

    public Inhabitant(Integer id) {

        // We set the id to the one given by argument
        this.id = id;

        // We set isInfected to false by default
        this.isInfected = false;

        // And alive
        this.alive = true;

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
        this.isInfected = true;

        // Add him to the list of infected persons

        // TODO NOT THIS ONE BECAUSE WE CAN GET A CONCURENT EXCEPTION...
        // VirusSpreadingSimulation.theWorld.getInfected().add(this.id);
        VirusSpreadingSimulation.theWorld.getLastInfected().add(this.id);
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

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

}
