package org.firstinspires.ftc.teamcode.Teleop;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.opMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class BioBuzzTeleopRobotCentric extends OpMode {

    private Follower follower;



    public void init() {
        follower = Constants.create(hardwareMap);
    }

    @Override
    public void start() {

            follower.setPose(OpModeStorage.autonomousEndPose);
            follower.update();


    }

    @Override
    public void loop() {
        ManualDrive.driveOrHold(
                follower,
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x
        );


        // relocalize button
        if (gamepad1.aWasPressed()) {
            Pose resetPose = new Pose(10.5, 10.5, Math.toRadians(90));
            follower.setPose(resetPose); // overrides our pose
        }

        Pose robotPose = follower.pose(); // returns a Pose object
        follower.update();
    }


    }


