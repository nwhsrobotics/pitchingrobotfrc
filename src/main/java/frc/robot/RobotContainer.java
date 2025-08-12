// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.DriveCommand;
import edu.wpi.first.wpilibj.XboxController;
//import frc.robot.commands.Autos;
//import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.DriveBaseSubsystem;
import frc.robot.subsystems.FlywheelShooter;
//import frc.robot.subsystems.LEDSubsystem;
//import com.revrobotics.RelativeEncoder;
//import com.revrobotics.spark.SparkLowLevel.MotorType;
//import com.revrobotics.spark.SparkMax;
import frc.robot.subsystems.IndexSubsystem;




/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */




public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public final FlywheelShooter flywheelShooter;
  public final DriveBaseSubsystem driveBase;
  public final IndexSubsystem indexSubsystem;
  //public final LEDSubsystem leds;
  //private final SparkMax flywheelMotor1 = new SparkMax(1, MotorType.kBrushless);
  //private final SparkMax flywheelMotor2 = new SparkMax(2, MotorType.kBrushless);
  //private final RelativeEncoder encoder1 = flywheelMotor1.getEncoder();
  //private final RelativeEncoder encoder2 = flywheelMotor2.getEncoder();



  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);
  

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {

    //flywheelShooter = new FlywheelShooter(flywheelMotor1, encoder1, flywheelMotor2, encoder2); do i use this one?
    flywheelShooter = new FlywheelShooter();
    driveBase = new DriveBaseSubsystem();
    indexSubsystem = new IndexSubsystem(); 
    //leds = new LEDSubsystem(Constants.LEDs.PWM_PORT, Constants.LEDs.LENGTH, flywheelShooter);

    driveBase.setDefaultCommand(
      new DriveCommand(driveBase, m_driverController)
  );

    

    configureBindings();
  }

  /**
   * Use this method to define your trigger->command mappings. Triggers can be created via the
   * {@link Trigger#Trigger(java.util.function.BooleanSupplier)} constructor with an arbitrary
   * predicate, or via the named factories in {@link
   * edu.wpi.first.wpilibj2.command.button.CommandGenericHID}'s subclasses for {@link
   * CommandXboxController Xbox}/{@link edu.wpi.first.wpilibj2.command.button.CommandPS4Controller
   * PS4} controllers or {@link edu.wpi.first.wpilibj2.command.button.CommandJoystick Flight
   * joysticks}.
   */
  private void configureBindings() {
    m_driverController.a().onTrue(
      Commands.runOnce(() -> flywheelShooter.startFlywheel(50), flywheelShooter)//change this to change RPM
    );

    m_driverController.a().onFalse(
        Commands.runOnce(() -> flywheelShooter.stopFlywheel(), flywheelShooter)
    );

    m_driverController.b().onTrue(
        Commands.runOnce(() -> indexSubsystem.feedBall(), indexSubsystem)
    );
    
  }

}
