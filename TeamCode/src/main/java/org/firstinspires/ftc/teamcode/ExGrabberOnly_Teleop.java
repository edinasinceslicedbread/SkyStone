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

@TeleOp(name="Pushbot:TeleOpGrabberOnly", group="Pushbot")
public class ExGrabberOnly_Teleop extends OpMode{

    /* Declare OpMode members. */
    ExGrabberBot robot       = new ExGrabberBot(); // use the class created to define a Pushbot's hardware

    /*
     * Code to run ONCE when the driver hits INIT
     */
    static final int Closed_Position = 0;
    static final int Open_Position = 600;
    static final int Grabbing_Position = 400;
    static final double Grabber_Power = 1;
    @Override
    public void init() {
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);
        robot.grabber.setTargetPosition(Closed_Position);
        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello, Good Grabbing! Set target to closed");    //            // Display it for the driver.)
        telemetry.addData("Path2",  "Current position %7d, moving towards %7d",robot.grabber.getCurrentPosition(), robot.grabber.getTargetPosition());
        telemetry.update();

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
    public void loop() {

        if (robot.grabber.getCurrentPosition() == robot.grabber.getTargetPosition())
        {
            robot.grabber.setPower(0);
        }
        else
        {
            telemetry.addData("Path1",  "Not yet at your target");
        }
        // GRABBER controls section
        if (!robot.grabber.isBusy())
        {
            boolean isButtonPressed = false;

            // x is open.
            if (gamepad1.x) {
                robot.grabber.setTargetPosition(Open_Position);
                isButtonPressed = true;
            }
            // y is grab.
            else if(gamepad1.y){
                robot.grabber.setTargetPosition(Grabbing_Position);
                isButtonPressed = true;
            }
            // b is closed.
            else if(gamepad1.b){
                robot.grabber.setTargetPosition(Closed_Position);
                isButtonPressed = true;
            }
            if (isButtonPressed){
                robot.grabber.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.grabber.setPower(Grabber_Power);
            }

        }
        else {
            // Display it for the driver.);
            telemetry.addData("Path2",  "Running at %7d, moving towards %7d",robot.grabber.getCurrentPosition(), robot.grabber.getTargetPosition());
            telemetry.update();
        }

    }
    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }
}
