package org.trackerlounge;

import java.io.File;

import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.plugin.Thresholder;
import ij.process.ImageProcessor;

//https://imagej.nih.gov/ij/developer/source/
//https://github.com/imagej/ImageJA

//https://imagej.nih.gov/ij/developer/source/ij/plugin/filter/FFTFilter.java.html
//https://imagej.nih.gov/ij/developer/api/ij/plugin/filter/FFTFilter.html

//https://github.com/imagej/ImageJA/blob/master/src/main/java/ij/plugin/filter/FFTFilter.java

//https://imagej.nih.gov/ij/developer/api/ij/ImagePlus.html
//https://imagej.nih.gov/ij/developer/api/ij/IJ.html

//https://imagej.nih.gov/ij/developer/source/ij/IJ.java.html
//https://imagej.nih.gov/ij/developer/source/ij/plugin/Thresholder.java.html

public class BandPassFilterAndBinaryInOpenCV {
	File file;
	String outputPath;
	ImagePlus imp;
	String arg = "";
	FFTFilter f;
	ImageProcessor ip;
	String args3 = "";
	Thresholder t;
	public BandPassFilterAndBinaryInOpenCV(File file, String outputPath) {
		super();
		this.file = file;
		this.outputPath = outputPath;
		imp = new ImagePlus(file.getAbsolutePath());
	}
	
	public String run(double filterLargeDia, double filterSmallDia, boolean showResult, boolean saveResult) {
		f = new FFTFilter();
		f.setFilterLargeDia(filterLargeDia);
		f.setFilterSmallDia(filterSmallDia);
		f.setup(arg, imp);
		ip = imp.getChannelProcessor();
		f.run(ip);
		WindowManager.setTempCurrentImage(imp);
		t = new Thresholder();
		t.run(args3);
		if(showResult) {
			imp.show();
		}
		if(saveResult) {
			String result3 = outputPath+"\\"+getFileName(filterLargeDia, filterSmallDia);
			IJ.save(IJ.getImage(), result3);
			return result3;
		}
		return "";
	}
	
	public String getFileName(double filterLargeDia, double filterSmallDia) {
		String name = file.getName();
		int pos = name.indexOf('.');
		String part1 = name.substring(0, pos);
		String part2 = name.substring(pos, name.length());
		String result = part1+"_"+filterLargeDia+"_"+filterSmallDia+part2;
		return result;
	};
	
}//End of Class
