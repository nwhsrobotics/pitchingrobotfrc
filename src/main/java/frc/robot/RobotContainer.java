// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.subsystems.FlywheelShooter;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.Encoder;
import frc.robot.commands.ShootWhenReady;



/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */




public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final FlywheelShooter flywheelShooter;


  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    // Create motor controllers
    CANSparkMax flywheelMotor1 = new CANSparkMax(5, MotorType.kBrushless);  // adjust CAN IDs
    CANSparkMax flywheelMotor2 = new CANSparkMax(6, MotorType.kBrushless);  // adjust as needed
    RelativeEncoder encoder1 = flywheelMotor1.getEncoder();
    RelativeEncoder encoder2 = flywheelMotor2.getEncoder();

    CANSparkMax starwheelMotor = new CANSparkMax(7, MotorType.kBrushless);  // adjust ID
    Encoder starwheelEncoder = new Encoder(0, 1); // digital ports A/B

    flywheelShooter = new FlywheelShooter(flywheelMotor1, encoder1, flywheelMotor2, encoder2,
                                           starwheelMotor, starwheelEncoder);

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
    Commands.runOnce(() -> flywheelShooter.startFlywheel(170), flywheelShooter)//change this to change RPM
    );

    m_driverController.a().onFalse(
        Commands.runOnce(() -> flywheelShooter.stopFlywheel(), flywheelShooter)
    );


    m_driverController.b().onTrue(new ShootWhenReady(flywheelShooter));



  }

 
}
