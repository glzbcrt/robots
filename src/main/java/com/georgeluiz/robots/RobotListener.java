/*
 * This file is subject to the terms and conditions defined in
 * file LICENSE.md, which is part of this source code package.
 */

package com.georgeluiz.robots;

public interface RobotListener {
    void robotMoved(Robot robot);
    void robotChangedOrientation(Robot robot);
}
