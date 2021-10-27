package org.usfirst.frc.team7636.lib;

import org.opencv.core.Mat;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoMode;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.cameraserver.CameraServer;

public class RobotCamera {

	private int n;
	private int ModeCount;
	private boolean checkboolean1;
	private boolean flagboolean1;
	private boolean flagboolean2;
	
	private UsbCamera camera1;
	private VideoSink server1;
	private CvSink imageSink1;
    private CvSource imageSource1;
    
	
	public RobotCamera(int num) {
		n = num;
		ModeCount = 0;
		checkboolean1 = false;
		flagboolean1 = false;
		flagboolean2 = false;
	}

    public void init() {

		// camera1
		System.out.print("Setting camera"+n+"\n");
		camera1 = CameraServer.getInstance().startAutomaticCapture(n);
		camera1.setResolution(640, 480);
		camera1.setFPS(5);
		
		// imageSink1 & imageSource1
		imageSink1 = CameraServer.getInstance().getVideo(camera1);
		
		imageSource1 = new CvSource("imageSource1", VideoMode.PixelFormat.kMJPEG, 640, 480, 30);
		CameraServer.getInstance().addCamera(imageSource1);
		server1 = CameraServer.getInstance().addServer("server1");
		server1.setSource(imageSource1);
		
    }

    public void run() {
		
		Thread VisionProcess = new Thread(() -> {

			Mat inputImage1 = new Mat();
			
			while(flagboolean1) {

				try {

					imageSink1.grabFrame(inputImage1);

					


					
					                      // Execute " OpenCV " Here **
					




					/*
					if(ModeCount%2 == 0){
						ModeCount ++;
					}
					*/

					imageSource1.putFrame(inputImage1);	
					checkboolean1 = true;
					
					Thread.sleep(10); // delay

				} catch(InterruptedException e) {
					flagboolean1 = false;
					e.printStackTrace();
				}
				
			}

		});

		Thread VisionProcess_SpawnerThread = new Thread(() -> {

			while(!Thread.interrupted()) {

				if(checkboolean1) {

					checkboolean1 = false;
					System.out.print("thread"+n+"is still running\n");

					try {
						Thread.sleep(3000);

					} catch(InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else if(!checkboolean1) {

					//flagboolean1 = false;
					flagboolean1 = true;
					checkboolean1 = true;
					
					Thread thread = new Thread(VisionProcess);
					thread.setPriority(4);
					thread.start();
					
					System.out.print("starting VisionProcess"+n+"\n");

				}

			}

		});
		
		Thread VisionProcess_ModeThread = new Thread(() -> {

			flagboolean2 = true;
			
			while(flagboolean2) {

				try {

					Thread.sleep(2000);
					
					ModeCount ++;
					System.out.print("VisionProcess_ModeCount ++\n");
					
					//checkboolean3 = true;

				} catch(InterruptedException e) {
					flagboolean2 = false;
					e.printStackTrace();
				}

			}

		});

		VisionProcess_SpawnerThread.setPriority(5);
		VisionProcess_SpawnerThread.start();
		
		VisionProcess_ModeThread.setPriority(5);
		//VisionProcess_ModeThread.start();
		
	}
	
}