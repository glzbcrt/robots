/*
 * This file is subject to the terms and conditions defined in
 * file LICENSE.md, which is part of this source code package.
 */

package com.georgeluiz.robots;

import java.util.ArrayList;
import java.util.List;

public class Robot {

    private List<RobotListener> robotListeners;
    private Orientation orientation;
    private Position position;
    private Planet planet;
    private String name;

    public Robot(Planet planet, String name, Position initialPosition, Orientation initialOrientation) {
        this.planet = planet;
        this.name = name;
        this.position = initialPosition;
        this.orientation = initialOrientation;
        this.robotListeners = new ArrayList<RobotListener>();
    }

    public Planet getPlanet() {
        return planet;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public String sendCommands(String commands) {

        List<Command> commandsList = new ArrayList<Command>();

        for(char command : commands.toUpperCase().toCharArray()) {

            switch (command) {

                case 'M':
                    commandsList.add(new Command(CommandType.Move));
                    break;

                case 'L':
                    commandsList.add(new Command(CommandType.TurnLeft));
                    break;

                case 'R':
                    commandsList.add(new Command(CommandType.TurnRight));
                    break;
            }
        }

        return sendCommands(commandsList);
    }

    public String sendCommands(List<Command> commands) {
        Orientation[] orientations = Orientation.values();
        int newOrientation;

        for(Command command : commands) {

            switch (command.getCommandType()) {

                case TurnLeft:

                    newOrientation = getOrientation().ordinal() == 0 ? orientations.length - 1 : getOrientation().ordinal() - 1;
                    setOrientation(orientations[newOrientation]);

                    for(RobotListener robotListener : robotListeners) {
                        robotListener.robotChangedOrientation(this);
                    }

                    break;

                case TurnRight:

                    newOrientation = getOrientation().ordinal() == orientations.length - 1 ? 0 : getOrientation().ordinal() + 1;
                    setOrientation( orientations[ newOrientation ] );

                    for(RobotListener robotListener : robotListeners) {
                        robotListener.robotChangedOrientation(this);
                    }

                    break;

                case Move:

                    // Before moving the robot checking if there is not a robot in the final position.
                    if (getPlanet().getCollisionDetector().canWeMoveSafely(this)) {

                        this.setPosition(Position.Move(this));

                        for(RobotListener robotListener : robotListeners) {
                            robotListener.robotMoved(this);
                        }
                    }

                    break;
            }
        }

        return getFormattedPosition();
    }

    public String getFormattedPosition() {
        return "(" + position.getPointX() + "," + position.getPointY() + "," + orientation.toString().substring(0, 1) + ")";
    }

    public void addRobotListener(RobotListener robotListener) {
        this.robotListeners.add(robotListener);
    }
}
