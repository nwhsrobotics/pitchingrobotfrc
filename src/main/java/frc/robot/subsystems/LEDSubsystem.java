

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.LEDPattern;
import edu.wpi.first.wpilibj.LEDPattern.GradientType;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
//import frc.robot.RobotContainer;

public class LEDSubsystem extends SubsystemBase {

  private static LEDState state = LEDState.IDLE; 
  
  private static final int LED_LENGTH = 300;
  private static final AddressableLED strip = new AddressableLED(8);
  private static final AddressableLEDBuffer buffer = new AddressableLEDBuffer(LED_LENGTH); 

  private static final Color ORANGE = new Color(223, 70, 1); //RBG = (223, 70, 1) 



  
  private static final LEDPattern IDLE = LEDPattern.gradient(GradientType.kDiscontinuous, Color.kBlack, Color.kBlack, ORANGE, Color.kBlack, Color.kBlack, ORANGE, Color.kBlack, Color.kBlack, ORANGE);
  
  private static final LEDPattern BASE_PATTERN = LEDPattern.gradient(GradientType.kDiscontinuous, Color.kBlack, ORANGE);
  private static final LEDPattern CHARGEUP = BASE_PATTERN.mask(LEDPattern.progressMaskLayer(() -> FlywheelShooter.rpmNotStatic ));

  private static final LEDPattern BASE_PATTERN2 = LEDPattern.gradient(GradientType.kDiscontinuous, ORANGE, Color.kBlack); 
  private static final LEDPattern CHARGEDOWN = BASE_PATTERN2.mask(LEDPattern.progressMaskLayer(() -> FlywheelShooter.rpmNotStatic ));
  
  static {
    strip.setLength(buffer.getLength());
    strip.start();
  }

  public LEDSubsystem() {}

  public enum LEDState {
    IDLE,
    CHARGEUP,
    CHARGEDOWN 
  }

  public static void setState(LEDState newState) {
    state = newState;
    switch (state) {
        case IDLE -> setPattern(IDLE);
        case CHARGEUP -> setPattern(CHARGEUP);
        case CHARGEDOWN -> setPattern(CHARGEDOWN); 
    }
  }

  private static void setPattern(LEDPattern pattern) {
    pattern.applyTo(buffer);
    strip.setData(buffer);
  }   



  @Override
  public void periodic() {

  }


}
