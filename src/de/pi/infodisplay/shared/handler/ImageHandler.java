package de.pi.infodisplay.shared.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.util.logging.Level;

import de.pi.infodisplay.Main;

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
				while(imageinFile.read(filedata) > 0) {
					// TODO: Hier wird Fortschritt angezeigt.
				}
				base64Image = encoder.encodeToString(filedata);
			} catch (IOException e) {
				Main.LOG.log(Level.SEVERE, "Cannot Convert File to String", e);
			}
		}
		
		public File decodeImage() {
			File image = new File(UUID.randomUUID().toString());
			try(FileOutputStream imageOutFile = new FileOutputStream(image)) {
				byte[] imageByteArray = decoder.decode(base64Image);
				imageOutFile.write(imageByteArray);
			} catch (IOException e) {
				Main.LOG.log(Level.SEVERE, "Cannot decode String to File", e);
			}
			return image;
		}
	}
}
