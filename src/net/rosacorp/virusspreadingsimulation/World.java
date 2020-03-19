package net.rosacorp.virusspreadingsimulation;

import java.util.*;

public class World {

    // LinkedHashMap where we store all the people of the world

    // This is the only thing that comes in my mind to face the scalability problem as it is not stored continuously in the RAM, and it is very quick get the value when the key is given
    private LinkedHashMap<Integer, Inhabitant> inhabitants;

    // Infected persons (this will be easier when we will spread the virus ;) )
    private LinkedList<Integer> infected;
    private ArrayList<Integer> lastInfected;

    // We put the dead persons here
    private LinkedList<Integer> deads;
    private ArrayList<Integer> lastDeads;

    // The people cured
    private LinkedList<Integer> cured;
    private ArrayList<Integer> lastCured;

    // The current day for every iteration
    private int day;

    // The constructor
    public World() {

        // We use LinkedHashMap and LinkedList instead of HashMaps and ArrayList for scalability reasons.
        // As there are not stored continuously and their size will be enormous, the memory will be optimized
        this.inhabitants = new LinkedHashMap<>();
        this.infected = new LinkedList<>();
        this.lastInfected = new ArrayList<>();
        this.cured = new LinkedList<>();
        this.lastCured = new ArrayList<>();
        this.deads = new LinkedList<>();
        this.lastDeads = new ArrayList<>();
        this.day = 0;
    }

    // Generate all the Inhabitants one by one
    public void generateInhabitants() {
        for (int i = 0; i < VirusSpreadingSimulation.numberOfInhabitants; i++) {
            this.inhabitants.put(i, new Inhabitant(i));
        }
    }

    // Methods used to find the neighbors of a given Inhabitant according to the radius and the maxNumberOfFriends
    public void findRelations() {

        // This is the only way I found... this will be terribly long especially when maxNumberOfFriends grows up, but I don't know how to do it in an other way
        // I could have used threads but I don't really know how to do it efficiently and without ConcurrentModificationException
        for (Map.Entry<Integer, Inhabitant> entry1 : this.inhabitants.entrySet()) {
            for (Map.Entry<Integer, Inhabitant> entry2 : this.inhabitants.entrySet()) {

                // If the two entries are the same (the same persons we break, for optimization)
                if (entry1.getValue().equals(entry2.getValue()))
                    break;

                // Else we continue and calculate the distance between the two persons
                double distance = entry1.getValue().getDistanceBetween(entry2.getValue());

                // If the distance is less than the radius we are looking for
                if (distance < VirusSpreadingSimulation.radius) {

                    // We check is this person is not already in the "friend list" and that the friend list is not full
                    if (!entry1.getValue().getNeighbors().contains(entry2.getKey()) && entry1.getValue().getNeighbors().size() < VirusSpreadingSimulation.maxNumberOfFriends) {
                        entry1.getValue().getNeighbors().add(entry2.getKey());
                    }

                    // Hum can we do that ? I don't think so. But it's not written if I can, this appears to be more efficient as we are filling two friend lists every iterations and thus we get more people infected but those persons might not be very close to each other in reality
                    /*
                    if (!entry2.getValue().getNeighbors().contains(entry1.getKey()) && entry2.getValue().getNeighbors().size() < VirusSpreadingSimulation.maxNumberOfFriends) {
                        entry2.getValue().getNeighbors().add(entry1.getKey());
                    }
                    */
                }
            }
        }

        // Now we need to add a random person to the people he knows (we do it after not to set a person which is in the radius)
        for (Map.Entry<Integer, Inhabitant> entry : this.inhabitants.entrySet()) {

            // We take a random person (with its id) which is not the current person and not a person of the current person's neighbors
            Random random = new Random(System.currentTimeMillis() + new Random().nextInt());
            int personID;

            // We loop while the person id fulfil the two conditions
            do {
                personID = random.nextInt(VirusSpreadingSimulation.numberOfInhabitants);
            } while (entry.getKey() == personID || entry.getValue().getNeighbors().contains(personID));

            // Then we can add this friend the current person's list
            entry.getValue().getNeighbors().add(personID);
        }
    }

    // We add a whole day, that means we want the infected ones to spread the virus to the others
    public void addOneDay() {

        // We add a day
        this.day++;

        // We reset the last infected ArrayList (for this day)
        this.lastInfected = new ArrayList<>();
        this.lastCured = new ArrayList<>();
        this.lastDeads = new ArrayList<>();

        // We declare the variables here
        ArrayList<Integer> randomFriends;
        Integer friendID;
        Inhabitant currentHolder;
        ArrayList<Integer> randomIDToCure;
        Integer luckyPersonID;

        // We loop through all the infected persons
        for (Integer holderID : this.infected) {

            // Each day each person can contaminate up to 3 other individuals
            currentHolder = this.getInhabitants().get(holderID);

            // The
            randomFriends = new ArrayList<>();

            // If the guy has 3 or less friends we take those friends to be the next victims
            if (currentHolder.getNeighbors().size() <= 3) {
                randomFriends = (ArrayList<Integer>) currentHolder.getNeighbors().clone();
            }

            //Otherwise we take 3 random persons from the friend list
            else {
                for (int index = 0; index < 3; index++) {

                    // We loop until we get 3 different persons (and not already infected)
                    do {
                        friendID = currentHolder.getNeighbors().get(new Random(System.currentTimeMillis() + new Random().nextInt()).nextInt(currentHolder.getNeighbors().size()));
                    } while (randomFriends.contains(friendID));

                    randomFriends.add(friendID);
                }
            }

            // Now we loop all their relations and spread the virus
            for (Integer friendID2 : randomFriends) {

                // We first check that this person is not already infected (for optimization). And if not we contaminate him
                if (!(this.getInfected().contains(friendID2) || this.getLastInfected().contains(friendID2))) {

                    // We can infect the person
                    this.getInhabitants().get(friendID2).setInfected();
                }
            }
        }

        // Here we want to find after a period T, V random persons to be cured by the vaccine
        if (this.day > VirusSpreadingSimulation.numberOfDayToFindAVaccine) {

            // We reset this list for every persons
            randomIDToCure = new ArrayList<>();

            // We find V or less persons to take the vaccine if they are infected

            // If the number of infected persons is less than V we take the whole ArrayList
            if (this.getInfected().size() <= VirusSpreadingSimulation.numberOfVaccineEachDay) {
                randomIDToCure.addAll(this.infected);
            }

            // Else we find V random persons
            else {
                for (int index = 0; index < VirusSpreadingSimulation.numberOfVaccineEachDay; index++) {
                    // We loop until we get 3 or less different persons (and not already infected)
                    do {
                        luckyPersonID = this.getInfected().get(new Random(System.currentTimeMillis() + new Random().nextInt()).nextInt(this.getInfected().size()));
                    } while (randomIDToCure.contains(luckyPersonID));

                    randomIDToCure.add(luckyPersonID);
                }
            }

            // Now we only have to cure them
            for (Integer id : randomIDToCure) {
                this.getInhabitants().get(id).giveVaccine();
            }

            // Remove the curred persons from the infected ones
            this.infected.removeAll(this.lastCured);
        }

        // If we are at day 15 we can begin to know if people are dying or not from the virus (because of the incubation time of the virus and to optimize)
        if (this.day > 14) {

            // We loop through all the infected to know if they recover or die
            for (Integer infectedID : this.getInfected()) {
                this.getInhabitants().get(infectedID).makeHimRecover();
            }

            // Remove all the deads and the cured ones from the infected list
            this.infected.removeAll(this.lastCured);
            this.infected.removeAll(this.lastDeads);
        }

        // We add all the infected of the day into the infected list
        this.infected.addAll(this.getLastInfected());

        // And the deads and cured one if they exist
        if (!this.lastCured.isEmpty())
            this.cured.addAll(this.lastCured);

        if (!this.lastDeads.isEmpty())
            this.deads.addAll(this.lastDeads);

        // Give some stats for each day
        System.out.println("Day " + this.day + ": " + this.infected.size() + " infected persons, " + this.cured.size() + " are cured, " + this.deads.size() + " are dead and " + this.lastInfected.size() + " today");
    }

    // Getters and setters

    public LinkedHashMap<Integer, Inhabitant> getInhabitants() {
        return inhabitants;
    }

    public void setInhabitants(LinkedHashMap<Integer, Inhabitant> inhabitants) {
        this.inhabitants = inhabitants;
    }

    public LinkedList<Integer> getInfected() {
        return infected;
    }

    public void setInfected(LinkedList<Integer> infected) {
        this.infected = infected;
    }

    public void removeFromInfected(Integer infectedID) {
        this.infected.remove(infectedID);
    }

    public LinkedList<Integer> getDeads() {
        return deads;
    }

    public void setDeads(LinkedList<Integer> deads) {
        this.deads = deads;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public ArrayList<Integer> getLastInfected() {
        return lastInfected;
    }

    public void setLastInfected(ArrayList<Integer> lastInfected) {
        this.lastInfected = lastInfected;
    }

    public LinkedList<Integer> getCured() {
        return cured;
    }

    public void setCured(LinkedList<Integer> cured) {
        this.cured = cured;
    }

    public ArrayList<Integer> getLastDeads() {
        return lastDeads;
    }

    public void setLastDeads(ArrayList<Integer> lastDeads) {
        this.lastDeads = lastDeads;
    }

    public ArrayList<Integer> getLastCured() {
        return lastCured;
    }

    public void setLastCured(ArrayList<Integer> lastCured) {
        this.lastCured = lastCured;
    }

}
