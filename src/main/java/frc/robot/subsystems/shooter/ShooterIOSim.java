package frc.robot.subsystems.shooter;

/** Simple physics-less shooter simulation. Exists only so commands and logging function in sim. */
public class ShooterIOSim implements ShooterIO {

  private double flywheelVelocityRadPerSec = 0.0;
  private double feederCurrentAmps = 0.0;

  @Override
  public void updateInputs(ShooterIOInputs inputs) {
    inputs.flywheelVelocityRadPerSec = flywheelVelocityRadPerSec;
    inputs.feederCurrentAmps = feederCurrentAmps;
  }

  @Override
  public void setFlywheelVoltage(double volts) {
    // Extremely fake model: voltage -> velocity
    flywheelVelocityRadPerSec = volts * 50.0;
  }

  @Override
  public void setFeederVoltage(double volts) {
    feederCurrentAmps = Math.abs(volts) * 2.0;
  }
}
