package net.rosacorp.virusspreadingsimulation;

import java.util.Map;

public class VirusSpreadingSimulation {

    // N
    public static int worldSize = 10000;

    // Number of people on the map
    public static int numberOfInhabitants = 10000;

    // M
    public static int radius = 50;

    // F
    public static int maxNumberOfFriends = 25;

    public static World theWorld;

    public static void main(String[] args) {

        long now = System.currentTimeMillis() / 1000l;

        theWorld = new World();

        theWorld.generateInhabitants();

        // Use threads ? (not for now)
        theWorld.findRelations();

        // Loop until all the persons are infected
        for (int i = 0; i < 50; i++) {
            theWorld.addOneDay();
        }

        System.out.println("This setup took " + (System.currentTimeMillis() / 1000l - now) + "s");

        /*
        for (Map.Entry<Integer, Inhabitant> entry : world.getInhabitants().entrySet()) {
            System.out.println(entry.getValue());
        }
        */

    }

}
