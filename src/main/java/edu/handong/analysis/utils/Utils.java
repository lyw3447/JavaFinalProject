package edu.handong.analysis.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;

public class Utils {
		
		public static String getExtension(String path) {
			return path.substring(path.lastIndexOf(".")+1);
		}
		
		public static String getPathNoExt(String path) {
			return path.substring(0, path.lastIndexOf("."));
		}
		
		private static void ensureDestDir(File dir) throws IOException {
			if (!dir.exists()) dir.mkdirs(); 
		
	}	
	
		public static void unzip(File zip, File destDir) throws IOException {
			InputStream is = new FileInputStream(zip);
			String encoding = Charset.defaultCharset().name();
			
			ZipArchiveInputStream zipInput;
			ZipArchiveEntry entry;
			String name;
			File target;
			int nWritten = 0;
			BufferedOutputStream bufferOutput;
			byte[] buf = new byte[1024 * 8];

			ensureDestDir(destDir);
			zipInput = new ZipArchiveInputStream(is, encoding, false);
			
			while ((entry = zipInput.getNextZipEntry())!= null) {
				name = entry.getName();
				target = new File (destDir, name);
				
				if (entry.isDirectory())
					ensureDestDir(target);
					
				else {
					target.createNewFile();
					bufferOutput = new BufferedOutputStream(new FileOutputStream(target));
					
					while ((nWritten = zipInput.read(buf)) >= 0 ) {
						bufferOutput.write(buf, 0, nWritten);
					}
					bufferOutput.close();
				}
			}
			zipInput.close();
		}
}
