/*
 * This file is subject to the terms and conditions defined in
 * file LICENSE.md, which is part of this source code package.
 */

package com.georgeluiz.robots;

public class Position {

    private int pointX;
    private int pointY;

    public Position(int pointX, int pointY) {
        this.pointX = pointX;
        this.pointY = pointY;
    }

    public int getPointX() {
        return pointX;
    }

    public void setPointX(int pointX) {
        this.pointX = pointX;
    }

    public int getPointY() {
        return pointY;
    }

    public void setPointY(int pointY) {
        this.pointY = pointY;
    }

    public static Position Move(Robot robot) {

        Position position = new Position(robot.getPosition().getPointX(), robot.getPosition().getPointY());

        switch (robot.getOrientation()) {

            case North:
                position.setPointY(position.getPointY() + 1);
                break;

            case South:
                position.setPointY(position.getPointY() - 1);
                break;

            case East:
                position.setPointX(position.getPointX() + 1);
                break;

            case West:
                position.setPointX(position.getPointX() - 1);
                break;
        }

        if (position.getPointX() < 0) {
            position.setPointX(0);
        }

        if (position.getPointY() < 0) {
            position.setPointY(0);
        }

        if (position.getPointX() > (robot.getPlanet().getSizeX() - 1)) {
            position.setPointX(robot.getPlanet().getSizeX() - 1);
        }

        if (position.getPointY() > (robot.getPlanet().getSizeY() - 1)) {
            position.setPointY(robot.getPlanet().getSizeY() - 1);
        }

        return position;
    }

    @Override
    public String toString() {
        return "(" + Integer.toString(getPointX()) + "," + Integer.toString(getPointY()) + ")";
    }
}
