/*
 * This file is subject to the terms and conditions defined in
 * file LICENSE.md, which is part of this source code package.
 */

package com.georgeluiz.robots;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.util.HashMap;

public class ViewControl {

    private HashMap<String, JButton> robots;
    private Planet planet;
    private JFrame frame;

    public ViewControl(Planet planet, JFrame frame) {
        this.robots = new HashMap<String, JButton>();
        this.frame = frame;
        this.planet = planet;
    }

    public Planet getPlanet() {
        return planet;
    }

    public JFrame getJFrame() {
        return frame;
    }

    public HashMap<String, JButton> getRobots() {
        return robots;
    }
}
