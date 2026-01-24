package frc.robot.subsystems.shooter;

import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;

public class ShooterIOSim implements ShooterIO {

  private final FlywheelSim flywheelSim =
      new FlywheelSim(
          LinearSystemId.identifyVelocitySystem(
              0.02, // kV (tune later)
              0.001 // kA (tune later)
              ),
          DCMotor.getNEO(1),
          1.0);

  private double appliedFlywheelVoltage = 0.0;
  private double appliedFeederVoltage = 0.0;

  @Override
  public void updateInputs(ShooterIOInputs inputs) {
    flywheelSim.setInput(appliedFlywheelVoltage);
    flywheelSim.update(0.020);

    inputs.flywheelVelocityRadPerSec = flywheelSim.getAngularVelocityRadPerSec();
    inputs.flywheelCurrentAmps = flywheelSim.getCurrentDrawAmps();
    inputs.flywheelTempCelsius = 30.0; // fake but stable
    inputs.flywheelConnected = true;

    inputs.feederCurrentAmps = Math.abs(appliedFeederVoltage) * 2.0;
    inputs.feederTempCelsius = 30.0;
    inputs.feederConnected = true;
  }

  @Override
  public void setFlywheelVelocity(double radPerSec) {
    // Simple proportional control for sim only
    double error = radPerSec - flywheelSim.getAngularVelocityRadPerSec();
    appliedFlywheelVoltage = error * 0.02;
  }

  @Override
  public void setFeederVoltage(double volts) {
    appliedFeederVoltage = volts;
  }
}
