/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Pushbot:Extendomatic TeleOp", group="Pushbot")
public class ExtendomaticsBot_TeleOp extends OpMode{

    /* Declare OpMode members. */
     ExtendomaticsHardware robot       = new ExtendomaticsHardware(telemetry); // use the class created to define a Pushbot's hardware

    static final double LIFT_SPEED = 0.6;
    static final int Closed_Position = 0;
    static final int Open_Position = 3200;
    static final int Grabbing_Position = 2400;
    static final double Grabber_Power = 1;
    static final double LIFT_MAX_EXTENSION_LIMIT = 10000;
    static final boolean isDriveEnabled = true;
    static final boolean isLiftEnabled = true;
    static final boolean isGrabberEnabled = true;

    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap, isDriveEnabled, isGrabberEnabled, isLiftEnabled);
        robot.grabber.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello, Good Luck!");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop( ) {

        // LIFT MOTOR controls section
        double liftInput = -gamepad2.left_stick_y;
        // do not allow movement beyond limits
        if(isLiftEnabled) {
            if (robot.lift.getCurrentPosition() > LIFT_MAX_EXTENSION_LIMIT && liftInput > 0) {
                telemetry.addData("Lift", "You have reached the max position, please stop moving");
                robot.lift.setPower(0);
            }
            else if (robot.lift.getCurrentPosition() <= 0 && liftInput < 0) {
                telemetry.addData("Lift", "You have reached the Minimum position, please stop moving");
                robot.lift.setPower(0);
            }
            else {
                robot.lift.setPower(liftInput * LIFT_SPEED);
            }

            telemetry.addData("lift input value (Gamepad 2 left stick)",
                    "%.2f",
                    liftInput);
            telemetry.addData("lift encoder value",
                    "%7d",
                    robot.lift.getCurrentPosition());
        }

        // define motor class variables
        double X;
        double Y;
        double Z;
        // DRIVE MOTOR contols section
        // collect user input from left and right gamepad controls and set internal variable X & Y & Z
        X = gamepad1.left_stick_x;
        Y = -gamepad1.left_stick_y;
        Z = gamepad1.right_stick_x;

        // use X, Y, & Z to set power for each of the motors
        if(isDriveEnabled) {
            robot.leftFrontDrive.setPower(Range.clip(Y + X + Z, -1, 1));
            robot.rightFrontDrive.setPower(Range.clip(X - Y + Z, -1, 1));
            robot.leftRearDrive.setPower(Range.clip(Y - X + Z, -1, 1));
            robot.rightRearDrive.setPower(Range.clip(-X - Y + Z, -1, 1));

            // Send telemetry message to signify robot running;
            telemetry.addData("leftpad Y", "%.2f", Y);
            telemetry.addData("leftpad X", "%.2f", X);
            telemetry.addData("rightpad Z", "%.2f", Z);
        }
        // GRABBER controls section
        if(isGrabberEnabled) {

            if (robot.grabber.getCurrentPosition() != robot.grabber.getTargetPosition()) {
                robot.grabber.setPower(Grabber_Power);
                telemetry.addData("Path2", "Running at %7d, moving towards %7d", robot.grabber.getCurrentPosition(), robot.grabber.getTargetPosition());
            } else {
                robot.grabber.setPower(0);
                telemetry.addData("Path1", "grabber reached target of %7d", robot.grabber.getTargetPosition());
            }
            // GRABBER controls section
            if (!robot.grabber.isBusy()) {
                // x is open.
                if (gamepad2.x) {
                    robot.grabber.setTargetPosition(Open_Position);
                }
                // y is grab.
                else if (gamepad2.y) {
                    robot.grabber.setTargetPosition(Grabbing_Position);
            }
                // b is closed.
                else if (gamepad2.b) {
                    robot.grabber.setTargetPosition(Closed_Position);
                }

            } else {
                // Display it for the driver.);
                telemetry.addData("grabber",  "grabber is running, no input accepted");
            }
        }
        telemetry.update();
    }
    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
