package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.AutoLog;

/** Hardware abstraction for the shooter. Only contains things that physically touch hardware. */
public interface ShooterIO {

  /** Logged inputs for replay/sim */
  @AutoLog
  class ShooterIOInputs {
    public double flywheelVelocityRadPerSec = 0.0;
    public double feederCurrentAmps = 0.0;
  }

  /** Update sensor inputs */
  default void updateInputs(ShooterIOInputs inputs) {}

  /** Set flywheel voltage */
  default void setFlywheelVoltage(double volts) {}

  /** Set feeder voltage */
  default void setFeederVoltage(double volts) {}
}
