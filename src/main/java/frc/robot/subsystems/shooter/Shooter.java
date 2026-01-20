package frc.robot.subsystems.shooter;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LoggedTunableNumber;
import org.littletonrobotics.junction.Logger;

/** Shooter subsystem. Owns all logic, tunables, and decision making. */
public class Shooter extends SubsystemBase {

  private final ShooterIO io;
  private final ShooterIOInputsAutoLogged inputs = new ShooterIOInputsAutoLogged();

  // Tunable numbers (show up in NT + logs)
  private final LoggedTunableNumber targetVelocityRadPerSec =
      new LoggedTunableNumber("Shooter/TargetVelocityRadPerSec", 400.0);

  private final LoggedTunableNumber flywheelVoltage =
      new LoggedTunableNumber("Shooter/FlywheelVoltage", 8.0);

  private final LoggedTunableNumber feederVoltage =
      new LoggedTunableNumber("Shooter/FeederVoltage", 6.0);

  private final LoggedTunableNumber velocityTolerance =
      new LoggedTunableNumber("Shooter/VelocityTolerance", 20.0);

  public Shooter(ShooterIO io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs("Shooter", inputs);
  }

  /** Spin up flywheel, optionally feed when at speed */
  public void runShooter(boolean feed) {
    io.setFlywheelVoltage(flywheelVoltage.get());

    if (feed && atTargetVelocity()) {
      io.setFeederVoltage(feederVoltage.get());
    } else {
      io.setFeederVoltage(0.0);
    }
  }

  /** Stop everything */
  public void stop() {
    io.setFlywheelVoltage(0.0);
    io.setFeederVoltage(0.0);
  }

  /** Velocity check used by commands */
  public boolean atTargetVelocity() {
    return Math.abs(inputs.flywheelVelocityRadPerSec - targetVelocityRadPerSec.get())
        <= velocityTolerance.get();
  }
}
