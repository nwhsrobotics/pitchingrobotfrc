package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.CANAssignments;
import frc.robot.subsystems.RobotCanUtils.CANSparkMaxController;
import frc.robot.subsystems.RobotCanUtils.MotorKind;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.controller.PIDController;
import com.revrobotics.spark.SparkLowLevel.MotorType;//is this needed? ask mr wheeler

public class FlywheelShooter extends SubsystemBase {
    /*
     * private final MotorController flywheelMotor;
    private final MotorController flywheelMotor2;
     */

    public static double rpmNotStatic;


    private final PIDController pidController;

    private final SparkMaxConfig configEl = new SparkMaxConfig();

    private final CANSparkMaxController flywheelMotor = new CANSparkMaxController(Constants.CANAssignments.FLYWHEEL_MOTOR_ID, MotorKind.NEO30AMP, configEl, IdleMode.kBrake, 0.8, 0.0, 0.0, ElevatorConstants.MAX_VELOCITY_RPM, ElevatorConstants.MAX_ACCEL_RPM_S, 0.02 / ElevatorConstants.ELEVATOR_MOTOR_ENCODER_ROT2METER, 11.0);
    private final CANSparkMaxController flywheelMotor2 = new CANSparkMaxController(Constants.CANAssignments.FLYWHEEL_MOTOR2_ID, MotorKind.NEO30AMP, configEl, IdleMode.kBrake, 0.8, 0.0, 0.0, ElevatorConstants.MAX_VELOCITY_RPM, ElevatorConstants.MAX_ACCEL_RPM_S, 0.02 / ElevatorConstants.ELEVATOR_MOTOR_ENCODER_ROT2METER, 11.0);

    private final RelativeEncoder flywheelEncoder;
    private final RelativeEncoder flywheelEncoder2;

    private double targetRPM = 0;

    public FlywheelShooter() {// do i have to add two fly wheel motors?
       /*
        * this.flywheelMotor = firstmotor;
        this.flywheelEncoder = encoder1;
        this.flywheelMotor2 = secondmotor;
        this.flywheelEncoder2 = encoder2; do i need this
        */ 
        flywheelEncoder = flywheelMotor.getEncoder();
        flywheelEncoder2 = flywheelMotor2.getEncoder();

        //pidController = flywheelMotor.getPIDController();
        //pidController.setFeedbackDevice(flywheelEncoder);



        // Make second flywheel follow, inverted
        //flywheelMotor2.follow(flywheelMotor, true);

        pidController = new PIDController(0.0100, 0, 0.0001);//change these to change the pid values
        pidController.setTolerance(10);  // tolerance +50, -50
        stopFlywheel(); 
    }


    public void startFlywheel(double mph) {
        targetRPM = Constants.CANAssignments.INCHES_PER_MILE * mph / Constants.CANAssignments.MIN_PER_HOUR / Constants.CANAssignments.INCHES_PER_REV;
    }

    public void testFireAtLowPower() {
    flywheelMotor.set(0.1); // 10% power, start low
    flywheelMotor2.set(-0.1);
}

    public void stopFlywheel() {
        targetRPM = 0;
        flywheelMotor.set(0);
        flywheelMotor.set(0);
    }

    // Call this periodically
    @Override
    public void periodic() {
        // Flywheel control
        if (targetRPM > 0) {
            double currentRPM = getCurrentRPM();
            double output = pidController.calculate(currentRPM, targetRPM);
            output = Math.min(Math.max(output, 0), 1);
            flywheelMotor.set(output);
            flywheelMotor2.set(-output);
            System.out.printf("curr RPM: %f, output: %f\n", currentRPM, output);

        } else {
            flywheelMotor.set(0);
            flywheelMotor2.set(0);
        }

        // Feeding control
        rpmNotStatic = getCurrentRPM();
    }

    // Get current RPM from encoder
    public double getCurrentRPM() {
        return (flywheelEncoder.getVelocity() + flywheelEncoder2.getVelocity()) / 2.0;
    }



    public boolean isAtSpeed() {
        return pidController.atSetpoint();
    }


}
