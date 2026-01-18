package frc.robot.subsystems.shooter;

import static frc.robot.subsystems.shooter.ShooterConstants.*;

import org.littletonrobotics.junction.Logger;

/**
 * Simulated shooter for AdvantageKit. Optimized for low-CPU environments (Chromebook, lightweight
 * simulation).
 */
public class ShooterIOSim implements ShooterIO {

  private double shooterVelocity = 0;
  private double targetVelocity = 0;
  private double feederVoltage = 0;
  private double shooterOutput = 0;

  private final double maxAcceleration = 300; // units/sec^2
  private final double shooterCurrentFactor = 0.01; // arbitrary scaling

  // Counter for throttled logging
  private int logCounter = 0;

  @Override
  public void setShooterSpeed(double speed, boolean firingBoost) {
    double ff = firingBoost ? DEFAULT_FIRE_BOOST : 0;

    if (Math.abs(speed) < 1) {
      targetVelocity = 0;
      return;
    }

    speed = Math.max(MIN_SHOOTER_SPEED, Math.min(MAX_SHOOTER_SPEED, speed));
    targetVelocity = speed + ff;
    shooterOutput = targetVelocity / MAX_SHOOTER_SPEED;
  }

  @Override
  public boolean isAtSpeed(double speed) {
    return Math.abs(shooterVelocity - speed) < SHOOTER_VELOCITY_RANGE;
  }

  @Override
  public void setFiring(boolean isFiring) {
    feederVoltage = isFiring ? FEEDER_FIRING_VOLTAGE : 0;
  }

  @Override
  public double getShooterVelocity() {
    return shooterVelocity;
  }

  @Override
  public double getFeederCurrent() {
    return feederVoltage * shooterCurrentFactor;
  }

  @Override
  public void periodic() {
    // Velocity ramp (first-order)
    double delta = targetVelocity - shooterVelocity;
    double ramp = maxAcceleration / 50.0; // assuming 50Hz loop
    if (Math.abs(delta) > ramp) delta = Math.signum(delta) * ramp;
    shooterVelocity += delta;

    // Throttle logging to 10 Hz to reduce CPU load
    if (logCounter++ % 5 == 0) {
      Logger.recordOutput("Shooter/Main Velocity", shooterVelocity);
      Logger.recordOutput("Shooter/Target Velocity", targetVelocity);
      Logger.recordOutput("Shooter/Applied Output", shooterOutput);
      Logger.recordOutput("Shooter/Firing Voltage", feederVoltage);
      Logger.recordOutput("Shooter/Feeder Current", getFeederCurrent());
    }
  }

  @Override
  public double getIdleSpeed() {
    return DEFAULT_IDLE_SPEED;
  }
}
