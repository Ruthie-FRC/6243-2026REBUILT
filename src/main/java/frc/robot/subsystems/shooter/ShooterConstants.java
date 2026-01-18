package frc.robot.subsystems.shooter;

import frc.robot.util.LoggedTunableNumber;

public class ShooterConstants {
    public static final LoggedTunableNumber kP =
        new LoggedTunableNumber("Shooter/kP", 0.0005);
    public static final LoggedTunableNumber kI =
        new LoggedTunableNumber("Shooter/kI", 0.0);
    public static final LoggedTunableNumber kD =
        new LoggedTunableNumber("Shooter/kD", 0.0);

    public static final double RPM_TOLERANCE = 75.0;
}
