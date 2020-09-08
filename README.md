[Home Page](https://github.com/TrackerLounge/Home)


# Description

I experimented with an image of a track and an image of a shoe sole to see if I could match the track to the shoe sole. I tried a number of pre-processing steps to make the image of the track and shoe sole look as similar as possible (e.g. ImageJ bandpass filter, make binary, edge detection, etc.). In the experiment, I did not know what bandpass filter settings were best, so I automated the process to generated many versions of the images by varying the values. I compare resulting images using SURF. The experiment was a failure.

Note: this is not production quality code. It was written in haste to quickly experiment. It has not been optimized. It is ugly rough-cut code.

# Library Requirements

In order to run this code, you will need to:
* build opencv locally and include the result in build path.
* download imageJ and add the ij.jar to the build path.

# Potential Useful Links:

https://opencv-java-tutorials.readthedocs.io/en/latest/01-installing-opencv-for-java.html

# Image Comparison

https://stackoverflow.com/questions/11541154/checking-images-for-similarity-with-opencv

https://docs.opencv.org/4.4.0/d7/dff/tutorial_feature_homography.html

https://docs.opencv.org/4.4.0/dc/dc3/tutorial_py_matcher.html

https://docs.opencv.org/4.4.0/d5/dde/tutorial_feature_description.html

https://docs.opencv.org/4.4.0/d5/d6f/tutorial_feature_flann_matcher.html

https://docs.opencv.org/4.4.0/d7/dff/tutorial_feature_homography.html

https://docs.opencv.org/4.4.0/dd/dd4/tutorial_detection_of_planar_objects.html

https://docs.opencv.org/4.4.0/db/d70/tutorial_akaze_matching.html

https://docs.opencv.org/4.4.0/d9/dab/tutorial_homography.html




# OpenCV Sample Data

https://github.com/opencv/opencv/tree/master/samples/data

https://github.com/opencv/opencv/blob/master/samples/data/H1to3p.xml


# Canny Edge Detection

https://docs.opencv.org/trunk/da/d22/tutorial_py_canny.html

https://en.wikipedia.org/wiki/Canny_edge_detector

https://docs.opencv.org/3.4/da/d5c/tutorial_canny_detector.html

https://www.programcreek.com/java-api-examples/?class=org.opencv.imgproc.Imgproc&method=Canny

# ImageJ Source Code

https://imagej.nih.gov/ij/developer/source/

https://imagej.nih.gov/ij/developer/source/ij/plugin/Thresholder.java.html

https://imagej.nih.gov/ij/developer/source/ij/IJ.java.html
