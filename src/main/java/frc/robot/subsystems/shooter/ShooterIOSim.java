package frc.robot.subsystems.shooter;

public class ShooterIOSim implements ShooterIO {

  private double flywheelVelocityRadPerSec = 0.0;
  private double appliedFlywheelVoltage = 0.0;
  private double appliedFeederVoltage = 0.0;

  @Override
  public void updateInputs(ShooterIOInputs inputs) {
    // Extremely simple fake physics:
    // Voltage slowly pushes velocity toward a made-up max speed.
    double targetVelocity = appliedFlywheelVoltage * 80.0; // FAKE scaling
    flywheelVelocityRadPerSec += (targetVelocity - flywheelVelocityRadPerSec) * 0.1;

    inputs.flywheelVelocityRadPerSec = flywheelVelocityRadPerSec;
    inputs.feederCurrentAmps = Math.abs(appliedFeederVoltage) * 2.0; // FAKE current
  }

  @Override
  public void setFlywheelVoltage(double volts) {
    appliedFlywheelVoltage = volts;
  }

  @Override
  public void setFeederVoltage(double volts) {
    appliedFeederVoltage = volts;
  }
}
