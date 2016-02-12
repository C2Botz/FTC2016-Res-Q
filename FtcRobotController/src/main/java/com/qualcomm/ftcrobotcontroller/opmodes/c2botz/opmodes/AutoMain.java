package com.qualcomm.ftcrobotcontroller.opmodes.c2botz.opmodes;

//------------------------------------------------------------------------------
//
// PushBotAuto
//

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.List;

/**
 * Provide a basic autonomous operational mode that uses the left and right
 * drive motors and associated encoders implemented using a state machine for
 * the Push Bot.
 *
 * @author SSI Robotics
 * @version 2015-08-01-06-01
 */


public class AutoMain extends OpMode {

    public static final int TURN_TIME = 75;
    public static final float TURN_SPEED = .5f;
    public static final int TURN_TIME_2 = 75;
    public static final float TURN_SPEED_2 = .5f;
    public static final int STRAIGHT_TIME = 130;
    public static final float STRAIGHT_SPEED = .3f;
    public static final int STRAIGHT_TIME_2 = 315;
    public static final float STRAIGHT_SPEED_2 = .45f;
    public static final int STRAIGHT_TIME_3 = 30;
    public static final float STRAIGHT_SPEED_3 = .45f;
    public static final int STRAIGHT_TIME_4 = 60;
    public static final float STRAIGHT_SPEED_4 = .75f;
    public static AutoMode mode;
    DcMotor motorRearRight;
    DcMotor motorRearLeft;
    DcMotor motorFrontRight;
    DcMotor motorFrontLeft;
    DcMotor armRotationMotor;
    DcMotor armExtensionMotor;
    List<DcMotor> allDriveMotors = new ArrayList<DcMotor>();
    float speedMultiplier = 0;
    private int a_state = 0;
    private long milis = 0;

    @Override
    public void init() {
        motorFrontLeft = hardwareMap.dcMotor.get("motor_fl");
        motorRearLeft = hardwareMap.dcMotor.get("motor_rl");
        motorFrontRight = hardwareMap.dcMotor.get("motor_fr");
        motorRearRight = hardwareMap.dcMotor.get("motor_rr");
        armRotationMotor = hardwareMap.dcMotor.get("rMotor");
        armExtensionMotor = hardwareMap.dcMotor.get("eMotor");
        speedMultiplier = .5f;
        allDriveMotors.add(motorFrontLeft);
        allDriveMotors.add(motorFrontRight);
        allDriveMotors.add(motorRearLeft);
        allDriveMotors.add(motorRearRight);
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRearLeft.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {
        telemetry.addData("Data", mode);
        milis++;
        switch (a_state) {
            case 0:
                //Goes Straight 1
                goStraight(STRAIGHT_SPEED);
                if (milis >= STRAIGHT_TIME) {
                    goStraight(0);
                    a_state++;
                    milis = 0;
                    break;
                }
                break;
            case 1:
                //Turns Left or Right based on Color
                if (mode == AutoMode.RED) {
                    turnLeft(TURN_SPEED);
                } else if (mode == AutoMode.BLUE) {
                    turnRight(TURN_SPEED);
                }
                if (milis >= TURN_TIME) {
                    goStraight(0);
                    milis = 0;
                    a_state++;
                }
                break;
            case 2:
                //Continues Straight To Parking Zone
                goStraight(STRAIGHT_SPEED_2);
                if (milis >= STRAIGHT_TIME_2) {
                    goStraight(0);
                    a_state++;
                    milis = 0;
                    break;
                }
                break;
            case 3:
                //Backs Up
                goBack(STRAIGHT_SPEED_3);
                if (milis >= STRAIGHT_TIME_3) {
                    goStraight(0);
                    a_state++;
                    milis = 0;
                    break;
                }
                break;
            case 4:
                //TUrns Left or Right based on Color
                if (mode == AutoMode.RED) {
                    turnRight(TURN_SPEED_2);
                } else if (mode == AutoMode.BLUE) {
                    turnLeft(TURN_SPEED_2);
                }
                if (milis >= TURN_TIME_2) {
                    goStraight(0);
                    milis = 0;
                    a_state++;
                }
            case 5:
                //Backs Up the Mountain
                goBack(STRAIGHT_SPEED_4);
                if (milis >= STRAIGHT_TIME_4) {
                    a_state++;
                    milis = 0;
                    break;
                }
            default:
                //Default
                milis = 0;
                goStraight(0);
                break;

        }
    }


    void turnLeft(float power) {
        motorFrontLeft.setPower(-power);
        motorRearLeft.setPower(-power);
        motorFrontRight.setPower(power);
        motorRearRight.setPower(power);
    }

    void turnRight(float power) {
        motorFrontLeft.setPower(power);
        motorRearLeft.setPower(power);
        motorFrontRight.setPower(-power);
        motorRearRight.setPower(-power);
    }

    void goStraight(float power) {
        motorFrontLeft.setPower(power);
        motorFrontRight.setPower(power);
        motorRearLeft.setPower(power);
        motorRearRight.setPower(power);
    }

    void goBack(float power) {
        motorFrontLeft.setPower(-power);
        motorFrontRight.setPower(-power);
        motorRearLeft.setPower(-power);
        motorFrontRight.setPower(-power);
    }
}
