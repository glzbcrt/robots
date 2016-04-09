/*
 * This file is subject to the terms and conditions defined in
 * file LICENSE.md, which is part of this source code package.
 */

package com.georgeluiz.robots;

import java.util.ArrayList;
import java.util.List;

public class Planet {

    private List<PlanetListener> planetListeners;
    private CollisionDetector collisionDetector;
    private List<Robot> robots;
    private String name;
    private int sizeX;
    private int sizeY;

    public Planet(String name, int sizeX, int sizeY, PlanetListener planetListener) {

        this.name = name;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.robots = new ArrayList<Robot>();
        this.collisionDetector = new CollisionDetector(this);
        this.planetListeners = new ArrayList<PlanetListener>();

        // Notify the listeners about this event.
        if (planetListener != null) {
            this.planetListeners.add(planetListener);
            planetListener.planetCreated(this);
        }
    }

    public CollisionDetector getCollisionDetector() {
        return collisionDetector;
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public String getName() {
        return name;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public Robot tryLanding(String name, Position initialPosition, Orientation initialOrientation) throws Exception {

        // Check if there is another robot at the specified position before landing.
        for(Robot robot : getRobots()) {
            if (robot.getPosition().toString().equals(initialPosition.toString())) {
                throw new Exception("It was not possible to land. There is already a robot on this position.");
            }
        }

        // Create a new robot and add to the collection.
        Robot tmp = new Robot(this, name, initialPosition, initialOrientation);
        robots.add(tmp);

        // Notify the listeners about this event.
        for(PlanetListener planetListener : planetListeners) {
            planetListener.robotLanded(tmp);
        }

        return tmp;
    }

    public void addPlanetListener(PlanetListener planetListener) {
        planetListeners.add(planetListener);
    }
}
