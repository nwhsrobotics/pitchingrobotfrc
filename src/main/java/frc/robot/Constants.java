// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }
    public static final class CANAssignments {
      public static final int FRONT_LEFT_DRIVE_MOTOR_ID = 6;
      public static final int BACK_LEFT_DRIVE_MOTOR_ID = 1;
      public static final int FRONT_RIGHT_DRIVE_MOTOR_ID = 10;
      public static final int BACK_RIGHT_DRIVE_MOTOR_ID = 3;

      public static final double MIN_PER_HOUR = 60.0;
      public static final double INCHES_PER_MILE = 5280.0 * 12.0;
      public static final double INCHES_PER_REV = Math.PI * 8.0; // 8 in pneumatic wheels

      

      public static final int FRONT_LEFT_STEER_MOTOR_ID = 8;
      public static final int BACK_LEFT_STEER_MOTOR_ID = 2;
      public static final int FRONT_RIGHT_STEER_MOTOR_ID = 11;
      public static final int BACK_RIGHT_STEER_MOTOR_ID = 4;

      public static final int FRONT_LEFT_STEER_ABSOLUTE_ENCODER_ID = 22;
      public static final int BACK_LEFT_STEER_ABSOLUTE_ENCODER_ID = 20;
      public static final int FRONT_RIGHT_STEER_ABSOLUTE_ENCODER_ID = 21;
      public static final int BACK_RIGHT_STEER_ABSOLUTE_ENCODER_ID = 23;

      public static final int CLIMB_LEFT_MOTOR_ID = 32;
      public static final int CLIMB_RIGHT_MOTOR_ID = 30;

      public static final int FLYWHEEL_MOTOR_ID = 16;
      public static final int FLYWHEEL_MOTOR2_ID = 18;

      public static final int INDEX_MOTOR_ID = 13;
      public static final int SECONDARY_FLYWHEEL_MOTOR_ID = 41;
      public static final int SECONDARY_INDEX_MOTOR_ID = 42;

      public static final int INTAKE_MOTOR_ID = 15;

      // shoulder/wrist not used.
      // DO NOT INITIALIZE THEIR SUBSYSTEMS.
      public static final int RIGHT_SHOULDER_MOTOR_ID = 19;
      public static final int LEFT_SHOULDER_MOTOR_ID = 17;
      public static final int WRIST_MOTOR_ID = 16;
      public static final int WRIST_INTAKE_ID = 18;

      private final double kP = 0.00025;
      private final double kI = 0.0;
      private final double kD = 0.0;
      private final double kFF = 0.00017; // Feedforward gain
      private final double maxRPM = 6000.0;
      

      public static final int PDU_ID = 24;
    }
public static class DriveBase{
  public static final int LEFT_MOTOR_ID = 1;//change obv
  public static final int RIGHT_MOTOR_ID = 2;//change obv

}
    }