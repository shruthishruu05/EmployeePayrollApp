package com.bridgelabz.employeepayrollapp;

import java.io.File;

public class FileUtils {
	public static boolean deleteFolder(File file) {
		for (File subFile : file.listFiles()) {
			if (subFile.isDirectory()) {
				deleteFolder(subFile);
			} 
			else {
				subFile.delete();
			}
		}
		return file.delete();
	}
}
