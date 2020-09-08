package org.trackerlounge;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main {

	public static String getCurrentPath() {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		System.out.println("Current relative path is: " + s);
		return s;
	}

	public static String getResourcePath() {
		String s = getCurrentPath();
		String path = s + "\\resources\\";
		return path;
	}

	// We will be creating a number of versions of image1.
	// We will stored these versions of the image1 in this directory
	public static String getOutputPathImage1() {
		String s = getCurrentPath();
		String path = s + "\\image1\\";
		return path;
	}

	// We will be creating a number of versions of image2.
	// We will stored these versions of the image2 in this directory
	public static String getOutputPathImage2() {
		String s = getCurrentPath();
		String path = s + "\\image2\\";
		return path;
	}

	public static void copyFile(String from, String to) throws IOException {
		Path src = Paths.get(from);
		Path dest = Paths.get(to);
		File d = dest.toFile();
		if(d.exists() && d.isFile()) {
			d.delete();
		}
		Files.copy(src, dest);
	}

	public static void prepareFiles(String fileName, String outputPathForImage, 
			boolean invertImage, boolean edgeDetectImage) throws IOException {
		String resourcePath = Main.getResourcePath();
		File file1 = new File(resourcePath, fileName);

		File dir = new File(outputPathForImage);
		if(!dir.exists()) {
			dir.mkdir();
		}
		File outputFile1 = new File(outputPathForImage, fileName);
		copyFile(file1.getAbsolutePath(), outputFile1.getAbsolutePath());
		
		if(invertImage) {
			Main.invertImage(outputFile1.getAbsolutePath());
		}
		
		boolean showResult = false;
		boolean saveResult = true;
		BandPassFilterAndBinaryInOpenCV bp;
		
		//Use this code block to test running for once
		if(false){
			bp = new BandPassFilterAndBinaryInOpenCV(file1, outputPathForImage);
			double filterLargeDia = 40F;
			double filterSmallDia = 3F;
			
			String resultPath = bp.run(filterLargeDia, filterSmallDia, showResult, saveResult); 
			if(edgeDetectImage) {
				Main.edgeDetectImage(resultPath);
			}
		}
		
		//Use this code block to run the process to create multiple versions of the image 
		//using multiple bandpass settings.
		if(true){
			ArrayList<Float> largeDias = new ArrayList<>(Arrays.asList(40f, 100f, 200f, 300f, 400f, 500f));
	//		ArrayList<Float> largeDias = new ArrayList<>(Arrays.asList(1f, 40f, 100f, 200f, 300f, 400f, 500f, 600f, 700f));
			ArrayList<Float> smallDias = new ArrayList<>(Arrays.asList(1f, 5f, 10f));
	//		ArrayList<Float> smallDias = new ArrayList<>(Arrays.asList(0.1f, 1f, 2f, 3f, 5f, 10f));
			double filterSmallDia = 0F;
			double filterLargeDia = 0F;
			for (int j=0; j<smallDias.size(); j++) {
				filterSmallDia = smallDias.get(j);
				for (int i=0; i<largeDias.size(); i++) {
					filterLargeDia =largeDias.get(i);
					System.out.println("File Name: "+fileName+" "+filterLargeDia+" "+filterSmallDia);
					bp = new BandPassFilterAndBinaryInOpenCV(file1, outputPathForImage);
					String resultPath = bp.run(filterLargeDia, filterSmallDia, showResult, saveResult);
	//				if(invertImage) {
	//					Main.invertImage(resultPath);
	//				}
					if(edgeDetectImage) {
						Main.edgeDetectImage(resultPath);
					}
				}
			}
			
	//		for (double filterSmallDia = 0.1F; filterSmallDia < 12F; filterSmallDia = filterSmallDia + 1F) {
	//			for (double filterLargeDia = 1F; filterLargeDia < 702F; filterLargeDia = filterLargeDia + 50F) {
	//				System.out.println("File Name: "+fileName+" "+filterLargeDia+" "+filterSmallDia);
	//				bp = new BandPassFilterAndBinaryInOpenCV(file1, outputPathForImage);
	//				String resultPath = bp.run(filterLargeDia, filterSmallDia, showResult, saveResult);
	//				if(invertImage) {
	//					Main.invertImage(resultPath);
	//				}
	//				if(edgeDetectImage) {
	//					Main.edgeDetectImage(resultPath);
	//				}
	//			}
	//		}
		}
	}
	
	public static void invertImage(String file) {
		Imgcodecs imageCodecs = new Imgcodecs();
		Mat img = imageCodecs.imread(file);
		Core.bitwise_not(img, img);
		imageCodecs.imwrite(file, img);
	}
	//https://docs.opencv.org/trunk/da/d22/tutorial_py_canny.html
	public static void edgeDetectImage(String file) {
		Imgcodecs imageCodecs = new Imgcodecs();
		Mat image = imageCodecs.imread(file);
		Imgproc.cvtColor(image, image, Imgproc.COLOR_RGB2GRAY);
		Mat edges = new Mat();
		//Automatic calculate thresholds
		//https://www.programcreek.com/java-api-examples/?class=org.opencv.imgproc.Imgproc&method=Canny
		double otsu_thresh_val = Imgproc.threshold(image, image, 0, 255, Imgproc.THRESH_OTSU);
		double threshold1 = otsu_thresh_val * 0.5;//130F;//150.0F;
		double threshold2 = otsu_thresh_val;//100F;//125.0F;
		Imgproc.Canny(image, edges, threshold1, threshold2);
		imageCodecs.imwrite(file, edges);
//		String output = getResourcePath()+"edges1.jpg";
//		System.out.println("Saving: "+output);
//		boolean saved = imageCodecs.imwrite(output, edges);
//		System.out.println("Did it save? "+saved);
	}
	
	public static void main(String[] args) throws IOException {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		if(false) {
//			String fileName = "my_merrill_small.jpg";
			String fileName = "RF_Merrill_WetSand_VerySlow_Colored_Z_axis_small.jpg";
			String file =  getResourcePath()+fileName;
			Main.edgeDetectImage(file);
		}
		
//		String file = "C:\\Users\\aeb\\Documents\\NetBeansProjects\\TrackAndSoleComparison\\image2\\my_merrill_small_40.0_3.0.jpg";
//		Main.invertImage(file);


		/* Change false to true and run code to Perpare test images in two project folders 
		 ./TrackAndSoleComparison/image1/
		 ./TrackAndSoleComparison/image2/
		 If these folders do not exist they will be created while the code runs.
		 If these folders already exist before running, I tend to delete the folders before running the code,
		 to ensure that I am not including images from a prior run in the next test.
		 */
		if(false) {
			String fileName1 = "RF_Merrill_WetSand_VerySlow_Colored_Z_axis_small.jpg";
			String outputPathImage1 = getOutputPathImage1();
	
			String fileName2 = "my_merrill_small.JPG";
			String outputPathImage2 = getOutputPathImage2();
	
			System.out.println("TrackAndSoleComparison");
			boolean invertImage = false;//You can experiment with inverting images (change white to black and black to white) to see if that produces better result
			boolean edgeDetectImage = true;//You can experiment with including edge detection in the preprocessing pipeline to see if that produces a better result or not.
			Main.prepareFiles(fileName1, outputPathImage1, invertImage, edgeDetectImage);
			invertImage = false;
			Main.prepareFiles(fileName2, outputPathImage2, invertImage, edgeDetectImage);
		}

		/*
		Change false to true and run code to compare the images in ./TrackAndSoleComparison/image1/ with the images in ./TrackAndSoleComparison/image2/
		
		This code simplistically looks for the two images that have the highest number of matches. It doesn't evaluate how good those matches are.
		*/
		if(false) {
			long start = System.currentTimeMillis();
			String outputPathImage1 = getOutputPathImage1();
			File image1 = new File(outputPathImage1);
			String[] versionsOfimage1 = image1.list();
			
			String outputPathImage2 = getOutputPathImage2();
			File image2 = new File(outputPathImage2);
			String[] versionsOfimage2 = image2.list();
			
			String path1;
			String path2;
			int numOfMatches = 0;
			int maxNumOfMatches = -1;
			String maxPath1="";
			String maxPath2="";
			boolean showResult = false;
			float percent =0F;
			float current = 0F;
			float total = (float)versionsOfimage2.length * versionsOfimage1.length;
			SurfFeatuerHomography sfh = null;
			for(int i=0; i<versionsOfimage1.length; i++) {
				path1 = outputPathImage1+versionsOfimage1[i];
				sfh = new SurfFeatuerHomography(path1);
				for(int j=0; j<versionsOfimage2.length; j++) {
					System.out.print(".");
					path2 = outputPathImage2+versionsOfimage2[j];
	//				System.out.println("---------------------------------------------------------------------------------");
	//				System.out.println("Path1: "+path1);
	//				System.out.println("Path2: "+path2);
					try {
					numOfMatches = sfh.run(path2, showResult);
					//System.out.println("numOfMatches: "+numOfMatches);
					if(numOfMatches>maxNumOfMatches) {
						System.out.println();
						System.out.println("************* New max ***************");
						maxNumOfMatches = numOfMatches;
						maxPath1 = path1;
						maxPath2 = path2;
						System.out.println("maxNumOfMatches: "+maxNumOfMatches);
						System.out.println("Max Path1: "+maxPath1);
						System.out.println("Max Path2: "+maxPath2);
						System.out.println();
					}
					}catch(Exception ex) {
						System.out.println("---------------------------------------------------------------------------------");
						System.out.println("Error while working with:");
						System.out.println("Path1: "+path1);
						System.out.println("Path2: "+path2);
						ex.printStackTrace();
					}
	//				System.out.println();
				}
				System.out.println();
				current = (float)versionsOfimage2.length*(float)(i+1);
				percent = ((float)current/(float)total)*((100F));
				System.out.println("Percent: "+percent+" -- Current: "+current+" of Total: "+total);
			}
			System.out.println("---------------------------------------------------------------------------------");
			System.out.println();
			System.out.println("Done checking - final result:");
			System.out.println("MaxNumOfMatches: "+maxNumOfMatches);
			System.out.println("Max Path1: "+maxPath1);
			System.out.println("Max Path2: "+maxPath2);
			
			long end = System.currentTimeMillis();
			
			NumberFormat formatter = new DecimalFormat("#0.00000");
			System.out.println("Execution time is " + formatter.format((end - start) / 1000d) + " seconds");
			System.out.println(" ---- OR ----");
			System.out.println("Execution time is " + formatter.format(((end - start) / 1000d)/60d) + " minutes");
			System.out.println(" ---- OR ----");
			System.out.println("Execution time is " + formatter.format((((end - start) / 1000d)/60d)/60d) + " hours");
			
			showResult = true;
			SurfFeatuerHomography sfhMax = new SurfFeatuerHomography(maxPath1);
			numOfMatches = sfhMax.run( maxPath2, showResult);	
		}
		
		/* Change false to true and run code to compare two images using unaltered code from opencv tutorial */
		if(false){
			String filename1 = getResourcePath()+"RF_Merrill_WetSand_VerySlow_Colored_Z_axis_small.jpg";
			String filename2 = getResourcePath() + "my_merrill_small.jpg";

			SURFMatching sm = new SURFMatching();
			sm.run(filename1, filename2);
		}
		
		
		/* Change false to true and run code to compare two images */
		if(false) {
//			String filename1 = "C:\\Users\\aeb\\Documents\\NetBeansProjects\\TrackAndSoleComparison\\image1\\RF_Merrill_WetSand_VerySlow_Colored_Z_axis_small_100.0_10.0.jpg";
//			String filename2 = "C:\\Users\\aeb\\Documents\\NetBeansProjects\\TrackAndSoleComparison\\image2\\my_merrill_small.jpg";
			
//			String filename1 = "C:\\Users\\aeb\\Documents\\NetBeansProjects\\TrackAndSoleComparison\\resources\\my_merrill_small.JPG";
//			String filename2 = "C:\\Users\\aeb\\Documents\\NetBeansProjects\\TrackAndSoleComparison\\resources\\merrill_shiver_moc_shoe.jpg";
			
			int numOfMatches = 0;
			boolean showResult = true;
			SurfFeatuerHomography sfh = new SurfFeatuerHomography(filename1);
			numOfMatches = sfh.run(filename2, showResult);
			System.out.println("NumOfMatches: "+numOfMatches);
		}

	}

}
