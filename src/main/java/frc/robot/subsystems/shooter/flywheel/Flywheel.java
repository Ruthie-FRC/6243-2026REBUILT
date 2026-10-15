package frc.robot.subsystems.shooter.flywheel;

import static edu.wpi.first.units.Units.RPM;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicVelocityTorqueCurrentFOC;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.units.measure.MutAngularVelocity;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LoggedTalon.TalonFX.LoggedTalonFX;
import frc.robot.util.LoggedTunableMeasure;
import frc.robot.util.LoggedTunableNumber;
import java.util.function.DoubleSupplier;
import lombok.Getter;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class Flywheel extends SubsystemBase {

  // Single leader motor. Followers (if any) should be configured at the LoggedTalon
  // construction level using PhoenixTalonFollower so the CTRE hardware handles
  // aligning follower outputs.
  private final LoggedTalonFX leaderMotor;

  private final MotionMagicVelocityTorqueCurrentFOC mmControl =
      new MotionMagicVelocityTorqueCurrentFOC(0);

  private AngularVelocity targetVelocity = RotationsPerSecond.of(0);

  @Getter @AutoLogOutput private boolean atSetpoint = false;

  private final LoggedTunableMeasure<MutAngularVelocity> tolerance =
      new LoggedTunableMeasure<>("Flywheel/Tolerance", RPM.mutable(5));
  private final LoggedTunableNumber targetRPM =
      new LoggedTunableNumber("Flywheel/TargetRPM", 3000, "rpm");

  /**
   * Construct a Flywheel that commands a single leader motor. Any followers should be configured on
   * the LoggedTalon implementation (see {@code PhoenixTalonFollower}).
   */
  public Flywheel(LoggedTalonFX leaderMotor) {
    this.leaderMotor = leaderMotor;
    var config =
        new TalonFXConfiguration()
            .withSlot0(new Slot0Configs().withKP(0.1).withKI(0).withKD(0).withKV(0.12))
            .withMotionMagic(
                new MotionMagicConfigs()
                    .withMotionMagicCruiseVelocity(100)
                    .withMotionMagicAcceleration(200))
            .withMotorOutput(new MotorOutputConfigs().withNeutralMode(NeutralModeValue.Brake))
            .withCurrentLimits(
                new CurrentLimitsConfigs()
                    .withStatorCurrentLimit(80)
                    .withStatorCurrentLimitEnable(true));
    leaderMotor.withConfig(config).withPIDTunable(config.Slot0);
    setDefaultCommand(run(() -> setSetpoints(RPM.of(targetRPM.get()))));
  }

  public void setSetpoints(AngularVelocity velocity) {
    targetVelocity = velocity;
    leaderMotor.setControl(mmControl.withVelocity(velocity));
  }

  /** Manual control command for teleop: supplier should provide a target velocity (rot/s). */
  public Command manualShoot(DoubleSupplier targetRotPerSec) {
    return run(() -> setSetpoints(RotationsPerSecond.of(targetRotPerSec.getAsDouble())));
  }

  public boolean isAtTarget() {
    return atSetpoint;
  }

  @Override
  public void periodic() {
    leaderMotor.periodic();

    Logger.recordOutput("Flywheel/TargetRPM", targetVelocity.in(RPM));
    Logger.recordOutput("Flywheel/ActualRPM", leaderMotor.getVelocity().in(RPM));

    atSetpoint =
        Math.abs(
                leaderMotor.getVelocity().in(RotationsPerSecond)
                    - targetVelocity.in(RotationsPerSecond))
            < tolerance.get().in(RotationsPerSecond);
  }

  /** Returns the current flywheel velocity in rotations per second. */
  public double getVelocity() {
    return leaderMotor.getVelocity().in(RotationsPerSecond);
  }

  public boolean hasDisconnectedMotor() {
    return leaderMotor.hasDisconnectedMotor();
  }

  public boolean hasOverheatedMotor(double tempThresholdC) {
    return leaderMotor.hasOverheatedMotor(tempThresholdC);
  }
}
