package br.com.lkm.extrator.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageConverter {

	public static byte[] convertTOGif(byte[] data) throws IOException {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();){
	        BufferedImage inputImage = ImageIO.read(bais);
	        String formatName = "GIF";
			ImageIO.write(inputImage, formatName , baos);
			return baos.toByteArray();
		}
	}
}
