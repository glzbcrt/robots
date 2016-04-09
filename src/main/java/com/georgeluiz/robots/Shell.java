/*
 * This file is subject to the terms and conditions defined in
 * file LICENSE.md, which is part of this source code package.
 */

package com.georgeluiz.robots;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Shell implements PlanetListener, RobotListener {

    // The robot shape is a square and the planet terrain will be multiple units
    // of the robot size, however the terraing maybe a square or rectangle.
    public static final int ROBOT_SIZE = 150;

    private Map<String, ViewControl> viewControl;
    private Planet selectedPlanet;
    private Robot selectedRobot;

    public static void main(String[] args) {

        try {

            if (args.length != 0) {

                switch (args[0].toLowerCase()) {

                    case "--interactive":
                        new Shell().runInteractive();
                        break;

                    case "--test":
                        if (args.length != 2) {
                            println("No commands specified.");
                            return;
                        }

                        runTestOnMars(args[ 1 ]);
                        break;

                    case "/?":
                    case "--help":
                    default:
                        printCommandLineHelp();
                }
            }
            else {
                printCommandLineHelp();
            }
        }
        catch (Exception e) {
            Ooppss(e);
        }
    }

    public static void Ooppss(Exception e) {
        println("Ooppss....");
        println("");
        println("The following error has occurred:");
        println("");
        e.printStackTrace();
    }

    public static String runTestOnMars(String commands) throws Exception {

        Planet mars = new Planet("Mars", 5, 5, null);
        Robot robot = mars.tryLanding("Wall-E", new Position(0, 0), Orientation.North);

        String position = robot.sendCommands(commands);

        println(position);

        return position;
    }

    public static void printCommandLineHelp() {

        printHeader();

        println("Usage:");
        println("           --help: show this help");
        println("    --interactive: open the Robots Control Shell");
        println("                   $ javar -jar robots.jar --interactive");
        println("");
        println("           --test: send the commands to a default robot on Mars");
        println("                   $ javar -jar robots.jar --test MML");
        println("");
    }

    public static void printHeader() {
        println("Robots 1.0");
        println("Created by George Luiz Bittencourt (george@georgeluiz.com)");
        println("");
    }

    public void createPlanet(String[] line) {

        if (line.length != 4) {
            println("Syntax error. Type help for more information.");
            return;
        }

        if (viewControl.containsKey(line[1])) {
            println("There is already a planet named " + line[ 1 ]);
            return;
        }

        int sizeX = getInteger(line[2], "Size X");
        int sizeY = getInteger(line[3], "Size Y");

        if ((sizeX == Integer.MIN_VALUE) || (sizeY == Integer.MIN_VALUE)) {
            return;
        }

        if (sizeX <= 0) {
            println("Size X must be greater than 0");
            return;
        }

        if (sizeY <= 0) {
            println("Size Y must be greater than 0");
            return;
        }

        Planet planet = new Planet(line[1].trim(), sizeX, sizeY, this);
        selectedPlanet = planet;
        selectedRobot = null;

        println("Planet " + line[1] + " created and selected.");
    }

    public int getInteger(String number, String parameter)
    {

        try {
            return Integer.parseInt(number);
        }
        catch (Exception e) {
            println(parameter + " is not a number.");
            return Integer.MIN_VALUE;
        }
    }

    public void listPlanets() {

        for(String planet : viewControl.keySet()) {
            println("   - " + planet);
        }
    }

    public void selectPlanet(String[] line) {

        if (line.length != 2) {
            println("Syntax error. Type help for more information");
            return;
        }

        if (!viewControl.containsKey(line[ 1 ])) {
            println("Planet " + line[ 1 ] + " not found.");
            return;
        }

        selectedPlanet = viewControl.get(line[ 1 ]).getPlanet();
        println("Planet " + selectedPlanet.getName() + " selected");
        selectedRobot = null;
    }

    public void landRobot(String[] line) {

        if (selectedPlanet == null) {
            println("No planet selected. Create or select one first.");
            return;
        }

        if (line.length != 5) {
            println("Syntax error. Type help for more information.");
            return;
        }

        int positionX = getInteger(line[ 2 ], "Position X");
        int positionY = getInteger(line[ 3 ], "Position Y");

        if ((positionX == Integer.MIN_VALUE) || (positionY == Integer.MIN_VALUE)) {
            return;
        }

        if ((positionX > (selectedPlanet.getSizeX() - 1)) || (positionX < 0)) {
            println("You are trying to land out of the X limits.");
            return;
        }

        if ((positionY > (selectedPlanet.getSizeY() - 1)) || (positionY < 0)) {
            println("You are trying to land out of the Y limits.");
            return;
        }

        Orientation orientation = null;

        try {
            orientation = Orientation.valueOf(line[4].substring(0, 1).toUpperCase() + line[4].substring(1));
        }
        catch (Exception e) {
            println("The specified orientation is not valid.");
            return;
        }

        for(Robot robot : selectedPlanet.getRobots()) {

            if (robot.getName().equals(line[1])) {
                println("There is already a robot with this name on the selected planet.");
                return;
            }
        }

        try {
            Robot robot = selectedPlanet.tryLanding(line[1].trim(), new Position(positionX, positionY), orientation);
            robot.addRobotListener(this);
            selectedRobot = robot;

            println("Robot " + selectedRobot.getName() + " landed on " + selectedPlanet.getName());
        }
        catch (Exception ex) {
            println("It was not possible to land the robot on the specified coordinates. It crashed :(");
        }
    }

    public void whereRobot() {

        if (selectedPlanet == null) {
            println("No planet selected. Create or select one and try again.");
            return;
        }

        if (selectedRobot == null) {
            println("No robot selected. Create or select one first using the create-robot or select-robot.");
            return;
        }

        println(selectedRobot.getPosition().toString());
    }

    public void listRobots() {

        if (selectedPlanet == null) {
            println("No planet selected. Create or select one and try again.");
            return;
        }

        for(Robot robot : selectedPlanet.getRobots()) {
            println("   - " + robot.getName());
        }
    }

    public void selectRobot(String[] line) {

        if (line.length != 2) {
            println("Syntax error. Type help for more information.");
            return;
        }

        if (selectedPlanet == null) {
            println("No planet selected. Create or select one and try again.");
            return;
        }

        Robot robotFound = null;

        for(Robot robot : selectedPlanet.getRobots()) {

            if (robot.getName().equals(line[ 1 ])) {
                robotFound = robot;
                break;
            }
        }

        if (robotFound != null) {
            println("Robot " + robotFound.getName() + " selected.");
            selectedRobot = robotFound;
        }
        else {
            println("Robot " + line[ 1 ] + " not found.");
        }
    }

    public void sendCommands(String[] line) throws Exception {

        if (line.length != 2) {
            println("Syntax error. Type help for more information.");
            return;
        }

        if (selectedRobot == null) {
            println("No robot selected. Create or select one first using the create-robot or select-robot.");
            return;
        }

        String command = "";

        for(int i = 1; i < line.length; i++) {
            command += line[ i ];
        }

        selectedRobot.sendCommands(command);
    }

    public void showHelp() {

        printHeader();

        println("Commands:");
        println("");
        println("  create-planet: create a new planet in our synthetic universe");
        println("         syntax: create-planet <name> <size x> <size y>");
        println("             eg: create-planet Mars 5 5");
        println("");

        println("   list-planets: list existing planets");
        println("         syntax: list-planets");
        println("             eg: list-planets");
        println("");

        println("  select-planet: set the context to the specified planet");
        println("         syntax: select-planet <name>");
        println("             eg: select-planet Mars");
        println("");

        println("     land-robot: try to land a new robot on the selected planet");
        println("         syntax: land-robot <name> <pos x> <pos y> <North | South | East | West>");
        println("             eg: land-robot Wall-E 1 1 North");
        println("");

        println("    list-robots: list the existing robots on the selected planet");
        println("         syntax: list-robots");
        println("             eg: list-robots");
        println("");

        println("   select-robot: set the context to the specified robot");
        println("         syntax: select-robot <name>");
        println("             eg: select-robot Wall-E");
        println("");

        println("    where-robot: show the position of the selected robot");
        println("         syntax: where-robot");
        println("             eg: where-robot ");
        println("");

        println("  send-commands: send commands to the selected robot");
        println("         syntax: send-commands <sequenceOf: L | R | M>");
        println("             eg: send-commands LMMRMM");
        println("");

        println("           exit: leave the Robots Control shell");
        println("           help: show this help");
        println("");
    }

    public void runInteractive()
    {
        try {
            viewControl = new HashMap<String, ViewControl>();

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String[] line;

            showHelp();

            do {

                System.out.print("ROBOTS:" + (selectedPlanet == null ? "none" : selectedPlanet.getName()) + ":" + (selectedRobot == null ? "none" : selectedRobot.getName()) + "$ ");
                line = reader.readLine().trim().split (" ");

                switch (line[0].toLowerCase().trim()) {

                    case "":
                        break;

                    case "create-planet":
                        createPlanet(line);
                        break;

                    case "list-planets":
                        listPlanets();
                        break;

                    case "select-planet":
                        selectPlanet(line);
                        break;

                    case "land-robot":
                        landRobot(line);
                        break;

                    case "list-robots":
                        listRobots();
                        break;

                    case "select-robot":
                        selectRobot(line);
                        break;

                    case "where-robot":
                        whereRobot();
                        break;

                    case "send-commands":
                        sendCommands(line);
                        break;

                    case "exit":
                        println("Sayonara!");
                        return;

                    case "help":
                    default:
                        showHelp();
                        break;
                }
            }
            while (true);
        }
        catch (Exception e) {
            Ooppss(e);
        }
    }

    @Override
    public void planetCreated(Planet planet) {

        JFrame frame = new JFrame("Robots Planet Viewer - " + planet.getName());
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(planet.getSizeX() * Shell.ROBOT_SIZE, planet.getSizeY() * Shell.ROBOT_SIZE);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);

        frame.toFront();
        frame.repaint();

        viewControl.put(planet.getName(), new ViewControl(planet, frame));
    }

    @Override
    public void robotLanded(Robot robot) {

        ViewControl viewControl = this.viewControl.get(robot.getPlanet().getName());

        JButton btn = new JButton();
        btn.setText(robot.getName() + " - " + robot.getOrientation().toString());
        btn.setSize(Shell.ROBOT_SIZE, Shell.ROBOT_SIZE);
        updateRobotCoordinates( robot, btn, viewControl.getJFrame());
        viewControl.getJFrame().getContentPane().add(btn);

        viewControl.getJFrame().toFront();
        viewControl.getJFrame().repaint();

        viewControl.getRobots().put(robot.getName(), btn);
    }

    public void updateRobotCoordinates(Robot robot, JButton button, JFrame frame) {

        int posY = (((robot.getPlanet().getSizeY() - (robot.getPosition().getPointY() + 1)) ) * Shell.ROBOT_SIZE);
        button.setLocation(robot.getPosition().getPointX() * Shell.ROBOT_SIZE, posY);
    }

    @Override
    public void robotMoved(Robot robot) {

        ViewControl viewControl = this.viewControl.get(robot.getPlanet().getName());
        JButton btn = viewControl.getRobots().get(robot.getName());

        viewControl.getJFrame().toFront();
        viewControl.getJFrame().repaint();

        updateRobotCoordinates(robot, btn, viewControl.getJFrame());

        try {
            Thread.sleep(500);
        } catch (Exception e) {}
    }

    @Override
    public void robotChangedOrientation(Robot robot) {

        ViewControl viewControl = this.viewControl.get(robot.getPlanet().getName());
        JButton btn = viewControl.getRobots().get(robot.getName());
        btn.setText(robot.getName() + " - " + robot.getOrientation().toString());

        viewControl.getJFrame().toFront();
        viewControl.getJFrame().repaint();
    }

    public static void println(String message) {

        System.out.println(message);
    }
}
