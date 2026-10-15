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

/** Minimal flywheel test subsystem with one leader and one CTRE follower. */
public class Shooter {
  @Getter private final Flywheel flywheel;

  public Shooter(CANBus bus) {
    PhoenixTalonFollower follower =
        new PhoenixTalonFollower(Constants.CanIds.FLYWHEEL_FOLLOWER, MotorAlignmentValue.Opposed);

    switch (Constants.currentMode) {
      case REAL ->
          flywheel =
              new Flywheel(
                  new PhoenixTalonFX(
                      Constants.CanIds.FLYWHEEL_LEADER, bus, "FlywheelLeader", follower));
      case SIM ->
          flywheel =
              new Flywheel(
                  new TalonFXFlywheelSim(
                      Constants.CanIds.FLYWHEEL_LEADER,
                      bus,
                      "FlywheelLeader",
                      0.0007567661,
                      1 / 1.25,
                      follower));
      default -> flywheel = new Flywheel(new NoOppTalonFX("FlywheelLeader", 1));
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
