package frc.robot.subsystems.shooter;

import org.littletonrobotics.junction.AutoLog;

public interface ShooterIO {

  /**
   * All sensor data read from the shooter hardware. This should represent REAL motor feedback only.
   */
  @AutoLog
  class ShooterIOInputs {
    // Flywheel motor
    public double flywheelVelocityRadPerSec = 0.0;
    public double flywheelCurrentAmps = 0.0;
    public double flywheelTempCelsius = 0.0;
    public boolean flywheelConnected = true;

    // Feeder motor
    public double feederCurrentAmps = 0.0;
    public double feederTempCelsius = 0.0;
    public boolean feederConnected = true;
  }

  /** Update all shooter sensor inputs */
  default void updateInputs(ShooterIOInputs inputs) {}

  /** Set desired flywheel velocity. Closed-loop control should be implemented INSIDE the IO. */
  default void setFlywheelVelocity(double radPerSec) {}

  /** Set feeder motor voltage (open-loop is fine) */
  default void setFeederVoltage(double volts) {}
}
