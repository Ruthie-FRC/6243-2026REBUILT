// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

/**
 * This class defines the runtime mode used by AdvantageKit. The mode is always "real" when running
 * on a roboRIO. Change the value of "simMode" to switch between "sim" (physics sim) and "replay"
 * (log replay from a file).
 */
public final class Constants {
  public static final Mode simMode = Mode.SIM;
  public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;

  public static final boolean tuningMode = true;

  public static final double loopPeriodSecs = 0.02;

  public static final String MECHANISM_CAN_BUS = "rio";

  /** CAN IDs for the motors used by this bring-up program. */
  public static final class CanIds {
    private CanIds() {}

    /** The commanded flywheel motor. */
    public static final int FLYWHEEL_LEADER = 12;

    /** The flywheel motor following {@link #FLYWHEEL_LEADER}. */
    public static final int FLYWHEEL_FOLLOWER = 11;
  }

  public static enum Mode {
    /** Running on a real robot. */
    REAL,

    /** Running a physics simulator. */
    SIM,

    /** Replaying from a log file. */
    REPLAY
  }

  public static boolean disableHAL = false;

  public static void disableHAL() {
    disableHAL = true;
  }

  /** checks that tuning mode is disabled before deploying code */
  public static class CheckDeploy {
    public static void main(String... args) {
      if (tuningMode) {
        System.err.println("Cannot deploy: tuning mode is enabled.");
        System.exit(1);
      }
    }
  }
}
