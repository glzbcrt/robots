/*
 * This file is subject to the terms and conditions defined in
 * file LICENSE.md, which is part of this source code package.
 */

package com.georgeluiz.robots;

public interface PlanetListener {
    void planetCreated(Planet planet);
    void robotLanded(Robot robot);
}
