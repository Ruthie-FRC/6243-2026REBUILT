package frc.robot.subsystems.rollers;

import frc.robot.subsystems.rollers.RollerSystemIO.RollerSystemIOOutputs;
import frc.robot.util.FullSubsystem;
import frc.robot.util.LoggedTracer;
import lombok.Setter;
import org.littletonrobotics.junction.Logger;

public class RollerSystem extends FullSubsystem {
  private final String name;
  private final String inputsName;
  private final RollerSystemIO io;
  protected final RollerSystemIOInputsAutoLogged inputs = new RollerSystemIOInputsAutoLogged();
  private final RollerSystemIOOutputs outputs = new RollerSystemIOOutputs();

  @Setter private double volts = 0.0;
  @Setter private boolean brakeModeEnabled = true;

  public RollerSystem(String name, String inputsName, RollerSystemIO io) {
    this.name = name;
    this.inputsName = inputsName;
    this.io = io;
  }

  @Override
  public void periodic() {
    io.updateInputs(inputs);
    Logger.processInputs(inputsName, inputs);

    // Run roller
    outputs.appliedVoltage = volts;

    // Update brake mode
    outputs.brakeModeEnabled = brakeModeEnabled;

    // Record cycle time
    LoggedTracer.record(name);

    Logger.recordOutput(inputsName + "/BrakeModeEnabled", brakeModeEnabled);
  }

  @Override
  public void periodicAfterScheduler() {
    io.applyOutputs(outputs);
  }

  public double getTorqueCurrent() {
    return inputs.torqueCurrentAmps;
  }

  public double getVelocity() {
    return inputs.velocityRadsPerSec;
  }

  public void stop() {
    volts = 0.0;
  }
}
