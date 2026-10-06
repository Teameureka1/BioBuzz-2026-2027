package org.firstinspires.ftc.teamcode.Teleop;


import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.OpModeStorage;
import org.firstinspires.ftc.teamcode.pedro.Constants;

@TeleOp
public class BioBuzzTeleopFieldCentric extends OpMode {

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
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x,
                follower.pose().heading()
        );
        follower.manual(powers);

        // relocalize button
        if (gamepad1.aWasPressed()) {
            Pose resetPose = new Pose(10.5, 10.5, Math.toRadians(90));
            follower.setPose(resetPose); // overrides our pose
        }

        follower.update();
        Pose robotPose = follower.pose(); // returns a Pose object



        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
    }
}
