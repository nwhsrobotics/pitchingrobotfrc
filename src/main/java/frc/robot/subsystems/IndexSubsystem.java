
package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class IndexSubsystem extends SubsystemBase {

  private final SparkMax indexMotor = new SparkMax(Constants.DriveBase.INDEX_MOTOR_ID, MotorType.kBrushless); 
  public RelativeEncoder indexRelativeEncoder = indexMotor.getEncoder(); 

  public void feedBall(){
    
  }




  public IndexSubsystem() {}

  @Override
  public void periodic() {

  }
}
