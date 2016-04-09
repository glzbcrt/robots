/*
 * This file is subject to the terms and conditions defined in
 * file LICENSE.md, which is part of this source code package.
 */

package com.georgeluiz.robots;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.georgeluiz.robots.Shell;

public class RobotsTest {

    @Test
    public void spinRobotTest() {

        try {
            Assert.assertEquals(
                Shell.runTestOnMars("LLLL"),
                "(0,0,N)"
            );
        }
        catch (Exception ex) { }
    }

    @Test
    public void changeOrientationTest() {

        try {
            Assert.assertEquals(
                Shell.runTestOnMars("L"),
                "(0,0,W)"
            );
        }
        catch (Exception ex) { }
    }

    @Test
    public void moveRobotTest() {

        try {
            Assert.assertEquals(
                Shell.runTestOnMars("RMMLMMLMMLMM"),
                "(0,0,S)"
            );
        }
        catch (Exception ex) { }
    }
}
