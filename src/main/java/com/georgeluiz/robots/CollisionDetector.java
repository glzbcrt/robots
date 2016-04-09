/*
 * This file is subject to the terms and conditions defined in
 * file LICENSE.md, which is part of this source code package.
 */

package com.georgeluiz.robots;

public class CollisionDetector {

    private Planet planet;

    CollisionDetector(Planet planet) {
        this.planet = planet;
    }

    public Planet getPlanet() {
        return planet;
    }

    public Boolean canWeMoveSafely(Robot robotToMove) {

        Position newPosition = Position.Move(robotToMove);

        // Iterate over all robots checking if there is one at the final position.
        // TODO: check if there is another robot in the path.
        for(Robot robot : getPlanet().getRobots()) {

            // The robot will never collide with itself.
            if (robotToMove == robot) {
                continue;
            }

            if (robot.getPosition().toString().equals(newPosition.toString())) {
                return false;
            }
        }

        return true;
    }
}
