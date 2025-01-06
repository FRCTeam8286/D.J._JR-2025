// Author: UMN Robotics Ri3D
// Last Updated: January 2025

package frc.robot.subsystems;

import frc.robot.Constants;
import frc.robot.commands.IntakeSetPosition;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class IntakeSubsystem extends SubsystemBase {
  
  // Intake Motor Controllers
  private SparkMax m_IntakeBar; // NEO 550 motor
  private SparkMax m_DeployIntake; // NEO 550 motor

  private double IntakeBarRPM;
  private double DeployPosition;
  private double intakeMotorCurrent;

  private double intakeLiftMotorCurrent;

  private int preset;

  

  /** Subsystem for controlling the Intake */
  public IntakeSubsystem() {
    // Instantiate the Intake motor controllers
    m_IntakeBar = new SparkMax(Constants.INTAKE_BAR_MOTOR_ID, MotorType.kBrushless);
    m_DeployIntake = new SparkMax(Constants.DEPLOY_INTAKE_MOTOR_ID, MotorType.kBrushless);
    // Configure the Spark MAX motor controllers using the new 2025 method
    configureSparkMAX(m_IntakeBar, Constants.INTAKE_BAR_INVERT);
    preset = 0;
    SmartDashboard.putNumber("Intake Bar Speed", Constants.INTAKE_BAR_SPEED);
  }

  private void configureSparkMAX(SparkMax max, boolean reverse) {
		SparkMaxConfig config = new SparkMaxConfig();
    config.inverted(reverse).idleMode(IdleMode.kBrake);
    max.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
	}

  /* Set power to the intake motor */
  public void setPower(double Power) {
    m_IntakeBar.set(Power);
  }
  public void stop() {
    m_IntakeBar.set(0);
  }

  /* Read the speed of the intake motor */
  public double getIntakeBarRPM() {
    return IntakeBarRPM;
  }
  /* toggle the intake in or out */
  public void setPreset(int preset) {
    this.preset = preset;
  }

  /* get whether the intake is in or out */
  public int getPreset() {
    return preset;
  }
  /* get position */
  public double getPosition() {
    return DeployPosition;
  }

  public double getIntakeMotorCurrent() {
    return intakeMotorCurrent;
  }

  public double getIntakeLiftMotorCurrent() {
    return intakeLiftMotorCurrent;
  }

  public void deployIntake(double power) {
    m_DeployIntake.set(power);
  }

  @Override
  public void periodic() {
    IntakeBarRPM = m_IntakeBar.getEncoder().getVelocity();
    DeployPosition = m_DeployIntake.getEncoder().getPosition();
    intakeMotorCurrent = m_IntakeBar.getOutputCurrent();
    intakeLiftMotorCurrent = m_DeployIntake.getOutputCurrent();

    // Add intake bar RPM readings to SmartDashboard for the sake of data logging
    SmartDashboard.putNumber("Intake Bar RPM", IntakeBarRPM);
  }
}