package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.controller.PIDController;
import com.revrobotics.spark.SparkLowLevel.MotorType;//is this needed? ask mr wheeler

public class FlywheelShooter extends SubsystemBase {
    /*
     * private final MotorController flywheelMotor;
    private final MotorController flywheelMotor2;
     */
    private final SparkMax flywheelMotor;
    private final SparkMax flywheelMotor2;
    private final RelativeEncoder flywheelEncoder;
    private final RelativeEncoder flywheelEncoder2;

    private final PIDController pidController;

    private double targetRPM = 0;

    public FlywheelShooter() {// do i have to add two fly wheel motors?
       /*
        * this.flywheelMotor = firstmotor;
        this.flywheelEncoder = encoder1;
        this.flywheelMotor2 = secondmotor;
        this.flywheelEncoder2 = encoder2; do i need this
        */ 

        flywheelMotor = new SparkMax(18, MotorType.kBrushless);  // adjust CAN IDs
        flywheelMotor2 = new SparkMax(16, MotorType.kBrushless);  // adjust as needed
        flywheelEncoder = flywheelMotor.getEncoder();
        flywheelEncoder2 = flywheelMotor2.getEncoder();

        pidController = new PIDController(0.0100, 0, 0.0001);//change these to change the pid values
        pidController.setTolerance(10);  // tolerance +50, -50
        stopFlywheel(); 
    }

    public static final double MIN_PER_HOUR = 60.0;
    public static final double INCHES_PER_MILE = 5280.0 * 12.0;
    public static final double INCHES_PER_REV = 2.0 * Math.PI * 8.0; // 8 in pneumatic wheels

    public void startFlywheel(double mph) {
        targetRPM = INCHES_PER_MILE * mph / MIN_PER_HOUR / INCHES_PER_REV;
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
    }

    // Get current RPM from encoder
    public double getCurrentRPM() {
        return (flywheelEncoder.getVelocity() + flywheelEncoder2.getVelocity()) / 2.0;
    }



    public boolean isAtSpeed() {
        return pidController.atSetpoint();
    }


}

/*
 * package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.math.controller.PIDController;
import org.littletonrobotics.junction.Logger; // AdvantageKit

public class FlywheelShooter extends SubsystemBase {

    public enum ShooterState {
        IDLE,
        SPINNING_UP,
        FEEDING
    }

    private final MotorController flywheelMotor;
    private final Encoder flywheelEncoder;

    private final MotorController starwheelMotor;

    private final PIDController pidController;

    private double targetRPM = 0;
    private final double feedMotorSpeed = 0.5;

    private ShooterState state = ShooterState.IDLE;

    private boolean feeding = false;
    private double feedStartTime = 0;

    public FlywheelShooter(MotorController flywheelMotor, Encoder flywheelEncoder,
                           MotorController starwheelMotor) {
        this.flywheelMotor = flywheelMotor;
        this.flywheelEncoder = flywheelEncoder;
        this.starwheelMotor = starwheelMotor;

        pidController = new PIDController(0.01, 0.001, 0.005);
        pidController.setTolerance(50); // ±50 RPM tolerance
    }

    public void setState(ShooterState newState) {
        this.state = newState;
    }

    public ShooterState getState() {
        return state;
    }

    public void setFlywheelRPM(double rpm) {
        targetRPM = rpm;
        setState(ShooterState.SPINNING_UP);
    }

    public void increaseRPM() {
        targetRPM += 100;
    }

    public void decreaseRPM() {
        targetRPM = Math.max(0, targetRPM - 100);
    }

    public void stopFlywheel() {
        targetRPM = 0;
        flywheelMotor.set(0);
        setState(ShooterState.IDLE);
    }

    public double getCurrentRPM() {
        return flywheelEncoder.getRate() * 60;
    }

    public boolean isAtSpeed() {
        return pidController.atSetpoint();
    }

    public void feedBall() {
        if (!feeding) {
            feeding = true;
            feedStartTime = Timer.getFPGATimestamp();
            setState(ShooterState.FEEDING);
        }
    }

    private double getFeedDurationSeconds() {
        return 0.5; // test & tune!
    }

    @Override
    public void periodic() {
        double currentRPM = getCurrentRPM();
        double output = 0;

        switch (state) {
            case SPINNING_UP:
                output = pidController.calculate(currentRPM, targetRPM);
                output = Math.min(Math.max(output, 0), 1);
                flywheelMotor.set(output);
                break;

            case FEEDING:
                output = pidController.calculate(currentRPM, targetRPM);
                flywheelMotor.set(Math.min(Math.max(output, 0), 1));

                double elapsed = Timer.getFPGATimestamp() - feedStartTime;
                if (elapsed < getFeedDurationSeconds()) {
                    starwheelMotor.set(feedMotorSpeed);
                } else {
                    starwheelMotor.set(0);
                    feeding = false;
                    setState(ShooterState.SPINNING_UP);
                }
                break;

            case IDLE:
            default:
                flywheelMotor.set(0);
                starwheelMotor.set(0);
                break;
        }

        //logging
        Logger.recordOutput("Shooter/State", state.toString());
        Logger.recordOutput("Shooter/TargetRPM", targetRPM);
        Logger.recordOutput("Shooter/CurrentRPM", currentRPM);
        Logger.recordOutput("Shooter/AtSpeed", isAtSpeed());
        Logger.recordOutput("Shooter/FlywheelOutput", flywheelMotor.toString());
    }
}

 */
