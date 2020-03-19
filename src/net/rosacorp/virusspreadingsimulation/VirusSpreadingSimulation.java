package net.rosacorp.virusspreadingsimulation;

public class VirusSpreadingSimulation {

    // The following variables are by default if there is no args

    // N
    public static int worldSize = 5000;

    // M
    public static int radius = 25;

    // F
    public static int maxNumberOfFriends = 10;

    // T
    public static int numberOfDayToFindAVaccine = 10;

    // V
    public static int numberOfVaccineEachDay = 25;

    // Number of people on the map
    public static int numberOfInhabitants = 10000;

    // Static variable for the world
    public static World theWorld;

    // Main method
    public static void main(String[] args) {

        // We first want to get all the parameters needed to run the program

        // The order is the following : M, N, F, V and T

        // We will do some checks and then get the values if there are args
        if (args.length == 5) {

            // M : The radius inside the people get have a relation
            try {
                radius = Integer.parseInt(args[0]);
            } catch (Exception e) {
                System.err.println("ERROR : M must be an integer");
                System.exit(2);
            }

            // N : The size of the map
            try {
                worldSize = Integer.parseInt(args[1]);
            } catch (Exception e) {
                System.err.println("ERROR : N must be an integer");
                System.exit(3);
            }

            // F : The max number of relation a given person can have in the radius M
            try {
                maxNumberOfFriends = Integer.parseInt(args[2]);
            } catch (Exception e) {
                System.err.println("ERROR : F must be an integer");
                System.exit(2);
            }

            // V : The number of vaccine given each day
            try {
                numberOfVaccineEachDay = Integer.parseInt(args[3]);
            } catch (Exception e) {
                System.err.println("ERROR : V must be an integer");
                System.exit(2);
            }

            // T : The day the virus is given to the population
            try {
                numberOfDayToFindAVaccine = Integer.parseInt(args[4]);
            } catch (Exception e) {
                System.err.println("ERROR : T must be an integer");
                System.exit(2);
            }

            // Here we assume that the density of the population is the square root of the world area
            numberOfInhabitants = worldSize;
        }


        // Used to get the running time
        long now = System.currentTimeMillis();

        // We instantiate a world
        theWorld = new World();

        // Used to generate the Inhabitants of the world
        theWorld.generateInhabitants();

        // Use threads ? (not for now, it will be too confusing and we can have a lot of concurrentmodificationexception...)
        theWorld.findRelations();

        // Day 0 : patient 0 has been implemented, the one with the id 0
        System.out.println("Day 0: patient 0 has been implemented");

        // While there are infected persons we continue to loop
        while (!theWorld.getInfected().isEmpty())
            theWorld.addOneDay();

        // Print the execution time in ms
        System.out.println("This setup took " + (System.currentTimeMillis() - now) + "ms");
    }

}
