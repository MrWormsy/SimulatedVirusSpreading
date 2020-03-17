package net.rosacorp.virusspreadingsimulation;

import java.util.Map;

public class VirusSpreadingSimulation {

    // N
    public static int worldSize = 100000;

    // Number of people on the map
    public static int numberOfInhabitants = 10000;

    // M
    public static int radius = 10;

    // F
    public static int maxNumberOfFriends = 25;

    public static World theWorld;

    public static void main(String[] args) {

        long now = System.currentTimeMillis() / 1000l;

        theWorld = new World();

        theWorld.generateInhabitants();

        // Use threads ? (not for now)
        theWorld.findRelations();

        // Day 0 : patient 0 has been implemented
        System.out.println("Day 0: patient 0 has been implemented");

        // While there are infected persons
        while (theWorld.getInfected().size() != 0) {
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
