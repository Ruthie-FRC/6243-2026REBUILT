# FRC 6243 - Motor Bring-Up Robot

This is a deliberately small WPILib robot project for bringing up and tuning the
drivetrain and flywheel motors. The runtime contains:

- Four-module swerve drive controlled by Xbox controller port `0`
- One Talon FX flywheel leader on RIO CAN ID `12`
- One CTRE follower on RIO CAN ID `11`, configured in Phoenix as opposed
- Phoenix 6 closed-loop velocity control
- AdvantageKit logging and live PID tuning

Vision, LEDs, intake, indexer, hood control, autonomous shooting, and shot
calculation are not part of this bring-up program.

## CAN ID map

The mechanism motors use the RIO CAN bus (`rio`). The flywheel is one
closed-loop pair: only the leader receives velocity commands from this code;
the follower mirrors the leader through CTRE Phoenix follower configuration.

| Device | Role | CAN bus | CAN ID | Code name |
| --- | --- | --- | ---: | --- |
| Flywheel motor | **Leader / commanded** | `rio` | `12` | `FlywheelLeader` |
| Flywheel motor | **Follower / mirrored** | `rio` | `11` | CTRE follower of ID `12` |

The IDs are defined in [`Constants.java`](src/main/java/frc/robot/Constants.java)
as `Constants.CanIds.FLYWHEEL_LEADER` and
`Constants.CanIds.FLYWHEEL_FOLLOWER`. Do not command the follower directly; it
must remain configured as a follower of CAN ID `12`.

## Controls

The driver controller is on USB port `0`.

| Control | Action |
| --- | --- |
| Left stick X/Y | Field-relative translation |
| Right stick X | Robot rotation |

The flywheel runs its default velocity command as soon as the robot is enabled.
Its default target is `3000 RPM`.

## PID tuning

`Constants.tuningMode` is enabled. Tune values from the `/Tuning` NetworkTables
table using AdvantageScope or another NetworkTables client:

- `/Tuning/FlywheelLeader/kP`
- `/Tuning/FlywheelLeader/kI`
- `/Tuning/FlywheelLeader/kD`
- `/Tuning/Flywheel/TargetRPM`

The actual and requested flywheel speeds are logged as
`Flywheel/ActualRPM` and `Flywheel/TargetRPM`. Changes to kP, kI, and kD are
applied to the Talon FX while the robot is running.

Set the target RPM to `0` before working near the mechanism. Keep the robot
secured and follow the team's normal motor safety procedure during bring-up.

## Build and deploy

```bash
./gradlew build
./gradlew deploy
```

The project uses Java 17, WPILib, CTRE Phoenix 6, and AdvantageKit.
