package de.pi.infodisplay.shared.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

public class ImageHandler {
	
	public ImageHandler() {
		
	}
	
	public ConvertedImage convertImageToConvertedImage(File image) {
		return new ConvertedImage(image);
	}

	public class ConvertedImage {
		
		private final Base64.Decoder decoder = Base64.getDecoder();
		private final Base64.Encoder encoder = Base64.getEncoder();
		
		private String base64Image;
		
		public ConvertedImage(File imageFile) {
			try(FileInputStream imageinFile = new FileInputStream(imageFile)) {
				byte[] filedata = new byte[(int) imageFile.length()];
				imageinFile.read(filedata);
				base64Image = encoder.encodeToString(filedata);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public File decodeImage() {
			File image = new File(UUID.randomUUID().toString());
			try(FileOutputStream imageOutFile = new FileOutputStream(image)) {
				byte[] imageByteArray = decoder.decode(base64Image);
				imageOutFile.write(imageByteArray);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return image;
		}
	}
}
