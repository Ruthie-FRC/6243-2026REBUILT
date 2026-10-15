package frc.robot.subsystems.shooter;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import frc.robot.Constants;
import frc.robot.subsystems.shooter.flywheel.Flywheel;
import frc.robot.util.LoggedTalon.Follower.PhoenixTalonFollower;
import frc.robot.util.LoggedTalon.TalonFX.NoOppTalonFX;
import frc.robot.util.LoggedTalon.TalonFX.PhoenixTalonFX;
import frc.robot.util.LoggedTalon.TalonFX.TalonFXFlywheelSim;
import lombok.Getter;

/** Minimal flywheel test subsystem. */
public class Shooter {
  @Getter private final Flywheel flywheel;

  public Shooter(CANBus bus) {
    switch (Constants.currentMode) {
      case REAL ->
          flywheel =
              new Flywheel(
                  new PhoenixTalonFX(
                      12,
                      bus,
                      "FlywheelRight",
                      new PhoenixTalonFollower(11, MotorAlignmentValue.Opposed)));
      case SIM ->
          flywheel =
              new Flywheel(
                  new TalonFXFlywheelSim(
                      12,
                      bus,
                      "FlywheelRight",
                      0.0007567661,
                      1 / 1.25,
                      new PhoenixTalonFollower(11, MotorAlignmentValue.Opposed)));
      default -> flywheel = new Flywheel(new NoOppTalonFX("FlywheelRight", 1));
    }
  }

  public boolean hasDisconnectedMotor() {
    return flywheel.hasDisconnectedMotor();
  }

  public boolean isReadyToShoot() {
    return flywheel.isAtTarget();
  }

  public boolean hasOverheatedMotor(double thresholdCelsius) {
    return flywheel.hasOverheatedMotor(thresholdCelsius);
  }
}
