package com.qdone.common.util;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 利用zxing生成二维码
 */
public class ZxingQRCode{
	// 图片宽度的一半
	private static final int IMAGE_WIDTH = 100;
	private static final int IMAGE_HEIGHT = 100;
	private static final int IMAGE_HALF_WIDTH = IMAGE_WIDTH / 2;
	private static final int FRAME_WIDTH = 2;
	// 二维码写码器
	private static MultiFormatWriter mutiWriter = new MultiFormatWriter();
    
	
	/**
	 * 生成二维码
	 * @param content:二维码内容(UTF-8)
	 * @param width:二维码宽度
	 * @param height:二维码高度
	 * @param srcImagePath:图片logo地址
	 * @param destImagePath
	 */
	public static void encode(String content, int width, int height,
			String srcImagePath, String destImagePath) {
		try {
			//生成图片文件
			ImageIO.write(genBarcode(content, width, height, srcImagePath),
					"jpg", new File(destImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
    /**
     * 生成二维码
     * @param content:二维码内容
     * @param size:图片尺寸
     * @param srcImagePath:图片logo地址
     * @throws WriterException
     * @throws IOException
     */
	public static BufferedImage genBarcode(String content, int size, String srcImagePath) throws WriterException,
			IOException {
		// 读取源图像
		BufferedImage scaleImage = scale(srcImagePath, IMAGE_WIDTH,
				IMAGE_HEIGHT, true);
		int[][] srcPixels = new int[IMAGE_WIDTH][IMAGE_HEIGHT];
		for (int i = 0; i < scaleImage.getWidth(); i++) {
			for (int j = 0; j < scaleImage.getHeight(); j++) {
				srcPixels[i][j] = scaleImage.getRGB(i, j);//图片的像素点
			}
		}
       //编码
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		//二维码内容编码
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		// 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）  
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.MARGIN, 1);//设置二维码边的空度，非负数 
		//二维码尺寸
		int imgSize = 67 + 12 * (size - 1); 
		// 生成二维码
		BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE,imgSize, imgSize, hints);
		// 二维矩阵转为一维像素数组
		int halfW = matrix.getWidth() / 2;
		int halfH = matrix.getHeight() / 2;
		int[] pixels = new int[imgSize * imgSize];

		for (int y = 0; y < matrix.getHeight(); y++) {
			for (int x = 0; x < matrix.getWidth(); x++) {
				// 左上角颜色,根据自己需要调整颜色范围和颜色
				if (x > 0 && x < 170 && y > 0 && y < 170) {
					Color color = new Color(231, 144, 56);
					int colorInt = color.getRGB();
					pixels[y * imgSize + x] = matrix.get(x, y) ? colorInt
							: 16777215;
				}
				// 读取图片
				else if (x > halfW - IMAGE_HALF_WIDTH
						&& x < halfW + IMAGE_HALF_WIDTH
						&& y > halfH - IMAGE_HALF_WIDTH
						&& y < halfH + IMAGE_HALF_WIDTH) {
					pixels[y * imgSize + x] = srcPixels[x - halfW
							+ IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];
				}
				// 在图片四周形成边框
				else if ((x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
						&& x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH
						&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
						+ IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW + IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
								+ IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
								- IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
								+ IMAGE_HALF_WIDTH + FRAME_WIDTH)) {
					pixels[y * imgSize + x] = 0xfffffff;
				} else {
					// 二维码颜色（RGB）
					int num1 = (int) (50 - (50.0 - 13.0) / matrix.getHeight()
							* (y + 1));
					int num2 = (int) (165 - (165.0 - 72.0) / matrix.getHeight()
							* (y + 1));
					int num3 = (int) (162 - (162.0 - 107.0)
							/ matrix.getHeight() * (y + 1));
					Color color = new Color(num1, num2, num3);
					int colorInt = color.getRGB();
					// 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
					pixels[y * imgSize + x] = matrix.get(x, y) ? colorInt
							: 16777215;// 0x000000:0xffffff
				}
			}
		}

		BufferedImage image = new BufferedImage(imgSize, imgSize,
				BufferedImage.TYPE_INT_RGB);
		image.getRaster().setDataElements(0, 0, imgSize, imgSize, pixels);

		return image;
	}
    /**
     * 生成二维码
     * @param content:二维码内容
     * @param width:图片宽度
     * @param height:图片高度
     * @param srcImagePath:图片logo地址
     * @return
     * @throws WriterException
     * @throws IOException
     */
	public static BufferedImage genBarcode(String content, int width,
			int height, String srcImagePath) throws WriterException,
			IOException {
		// 读取源图像
		BufferedImage scaleImage = scale(srcImagePath, IMAGE_WIDTH,
				IMAGE_HEIGHT, true);
		int[][] srcPixels = new int[IMAGE_WIDTH][IMAGE_HEIGHT];
		for (int i = 0; i < scaleImage.getWidth(); i++) {
			for (int j = 0; j < scaleImage.getHeight(); j++) {
				srcPixels[i][j] = scaleImage.getRGB(i, j);//图片的像素点
			}
		}
       //编码
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		//二维码内容编码
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		// 指定纠错等级,纠错级别（L 7%、M 15%、Q 25%、H 30%）  
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.MARGIN, 1);//设置二维码边的空度，非负数 
		// 生成二维码
		BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE,
				width, height, hints);

		// 二维矩阵转为一维像素数组
		int halfW = matrix.getWidth() / 2;
		int halfH = matrix.getHeight() / 2;
		int[] pixels = new int[width * height];

		for (int y = 0; y < matrix.getHeight(); y++) {
			for (int x = 0; x < matrix.getWidth(); x++) {
				// 左上角颜色,根据自己需要调整颜色范围和颜色
				if (x > 0 && x < 170 && y > 0 && y < 170) {
					Color color = new Color(231, 144, 56);
					int colorInt = color.getRGB();
					pixels[y * width + x] = matrix.get(x, y) ? colorInt
							: 16777215;
				}
				// 读取图片
				else if (x > halfW - IMAGE_HALF_WIDTH
						&& x < halfW + IMAGE_HALF_WIDTH
						&& y > halfH - IMAGE_HALF_WIDTH
						&& y < halfH + IMAGE_HALF_WIDTH) {
					pixels[y * width + x] = srcPixels[x - halfW
							+ IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];
				}
				// 在图片四周形成边框
				else if ((x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
						&& x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH
						&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
						+ IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW + IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
								+ IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
								- IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
								+ IMAGE_HALF_WIDTH + FRAME_WIDTH)) {
					pixels[y * width + x] = 0xfffffff;
				} else {
					// 二维码颜色（RGB）
					int num1 = (int) (50 - (50.0 - 13.0) / matrix.getHeight()
							* (y + 1));
					int num2 = (int) (165 - (165.0 - 72.0) / matrix.getHeight()
							* (y + 1));
					int num3 = (int) (162 - (162.0 - 107.0)
							/ matrix.getHeight() * (y + 1));
					Color color = new Color(num1, num2, num3);
					int colorInt = color.getRGB();
					// 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
					pixels[y * width + x] = matrix.get(x, y) ? colorInt
							: 16777215;// 0x000000:0xffffff
				}
			}
		}

		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		image.getRaster().setDataElements(0, 0, width, height, pixels);

		return image;
	}

	/**
	 * 把传入的原始图像按高度和宽度进行缩放，生成符合要求的图标
	 * 
	 * @param srcImageFile
	 *            源文件地址
	 * @param height
	 *            目标高度
	 * @param width
	 *            目标宽度
	 * @param hasFiller
	 *            比例不对时是否需要补白：true为补白; false为不补白;
	 * @throws IOException
	 */
	private static BufferedImage scale(String srcImageFile, int height,
			int width, boolean hasFiller) throws IOException {
		double ratio = 0.0; // 缩放比例
		File file = new File(srcImageFile);
		BufferedImage srcImage = ImageIO.read(file);
		Image destImage = srcImage.getScaledInstance(width, height,
				BufferedImage.SCALE_SMOOTH);
		// 计算比例
		if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
			if (srcImage.getHeight() > srcImage.getWidth()) {
				ratio = (new Integer(height)).doubleValue()
						/ srcImage.getHeight();
			} else {
				ratio = (new Integer(width)).doubleValue()
						/ srcImage.getWidth();
			}
			AffineTransformOp op = new AffineTransformOp(
					AffineTransform.getScaleInstance(ratio, ratio), null);
			destImage = op.filter(srcImage, null);
		}
		if (hasFiller) {// 补白
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic = image.createGraphics();
			graphic.setColor(Color.white);
			graphic.fillRect(0, 0, width, height);
			if (width == destImage.getWidth(null))
				graphic.drawImage(destImage, 0,
						(height - destImage.getHeight(null)) / 2,
						destImage.getWidth(null), destImage.getHeight(null),
						Color.white, null);//画图片
			else
				graphic.drawImage(destImage,
						(width - destImage.getWidth(null)) / 2, 0,
						destImage.getWidth(null), destImage.getHeight(null),
						Color.white, null);
			graphic.dispose();
			destImage = image;
		}
		return (BufferedImage) destImage;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		// 依次为内容(不支持中文),宽,长,logo图标路径,储存路径
		ZxingQRCode.encode("http://www.baidu.com/", 512, 512, "D://qrcode//timg.jpg",
				"D:\\2013-01.jpg");
	}
}
