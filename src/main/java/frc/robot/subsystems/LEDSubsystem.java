// package frc.robot.subsystems;

// import edu.wpi.first.wpilibj.AddressableLED;
// import edu.wpi.first.wpilibj.AddressableLEDBuffer;
// import edu.wpi.first.wpilibj.LEDPattern;
// import edu.wpi.first.wpilibj.LEDPattern.GradientType;
// import edu.wpi.first.wpilibj.util.Color;
// import edu.wpi.first.wpilibj2.command.SubsystemBase;

// /**
//  * Addressable LED subsystem with simple state machine and progress-based patterns.
//  * - IDLE: sparse orange pulses on black.
//  * - CHARGEUP: progress mask from start to end as shooter spins up.
//  * - CHARGEDOWN: progress mask from end to start as shooter spins down.
//  */
// public class LEDSubsystem extends SubsystemBase {

//   public enum LEDState {
//     IDLE,
//     CHARGEUP,
//     CHARGEDOWN
//   }

//   private final AddressableLED strip;
//   private final AddressableLEDBuffer buffer;
//   private final FlywheelShooter shooter;

//   private LEDState state = LEDState.IDLE;

//   // Team color
//   private static final Color ORANGE = new Color(223, 70, 1);

//   // Base patterns
//   private final LEDPattern idlePattern = LEDPattern.gradient(
//       GradientType.kDiscontinuous,
//       Color.kBlack, Color.kBlack, ORANGE,
//       Color.kBlack, Color.kBlack, ORANGE,
//       Color.kBlack, Color.kBlack, ORANGE);

//   // Progress-driven masks created in constructor after shooter is assigned
//   private LEDPattern chargeUpMask;
//   private LEDPattern chargeDownMask;

//   // Base gradients for charge patterns
//   private final LEDPattern chargeUpBase = LEDPattern.gradient(GradientType.kDiscontinuous, Color.kBlack, ORANGE);
//   private final LEDPattern chargeDownBase = LEDPattern.gradient(GradientType.kDiscontinuous, ORANGE, Color.kBlack);

//   public LEDSubsystem(int pwmPort, int length, FlywheelShooter shooter) {
//     this.shooter = shooter;
//     this.strip = new AddressableLED(pwmPort);
//     this.buffer = new AddressableLEDBuffer(length);

//     strip.setLength(buffer.getLength());
//     // Build masks now that shooter is available
//     this.chargeUpMask = LEDPattern.progressMaskLayer(shooterProgress());
//     this.chargeDownMask = LEDPattern.progressMaskLayer(() -> 1.0 - clamp01(shooter.getProgress()));
//     // Initialize off
//     LEDPattern.kOff.applyTo(buffer, buffer);
//     strip.setData(buffer);
//     strip.start();
//   }

//   public void setState(LEDState newState) {
//     this.state = newState;
//   }

//   private java.util.function.DoubleSupplier shooterProgress() {
//     return () -> clamp01(shooter.getProgress());
//   }

//   private static double clamp01(double v) {
//     if (v < 0) return 0;
//     if (v > 1) return 1;
//     return v;
//   }

//   private void applyPattern(LEDPattern pattern) {
//     pattern.applyTo(buffer, buffer);
//     strip.setData(buffer);
//   }

//   @Override
//   public void periodic() {
//     // Auto-state logic based on shooter
//     double target = shooter.getTargetRPM();
//     double rpm = shooter.getCurrentRPM();

//     LEDState desired;
//     if (target > 0.0) {
//       desired = LEDState.CHARGEUP;
//     } else if (rpm > 50.0) { // small hysteresis to avoid flicker around 0
//       desired = LEDState.CHARGEDOWN;
//     } else {
//       desired = LEDState.IDLE;
//     }
//     state = desired; // follow automatic state; setState() can still be used to override if needed

//     switch (state) {
//       case IDLE:
//         applyPattern(idlePattern);
//         break;
//       case CHARGEUP:
//         applyPattern(chargeUpBase.mask(chargeUpMask));
//         break;
//       case CHARGEDOWN:
//         applyPattern(chargeDownBase.mask(chargeDownMask));
//         break;
//     }
//   }
// }
