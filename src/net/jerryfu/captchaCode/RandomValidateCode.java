package net.jerryfu.captchaCode;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 生成验证码的工具类2
 * @author jerryfu
 *
 */
public class RandomValidateCode {

	private static Random random = new Random();
	private String randString = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";// 随机生成字符串的取值范围

	private int width = 80; // 图片宽度
	private int height = 34; // 图片高度
	private int StringNum = 4; // 验证码图片中随机产生字符的数量

	/**
	 * 获取随机字符,并返回字符的String格式
	 * 
	 * @param index
	 *            (指定位置)
	 * @return
	 */
	public String getRandomChar(int index) {
		// 获取指定位置index的字符，并转换成字符串表示形式
		return String.valueOf(randString.charAt(index));
	}

	/**
	 * 获得字体
	 * 
	 * @return
	 */
	private Font getFont() {
		return new Font("Fixedsys", Font.CENTER_BASELINE, 18); // 名称、样式、磅值
	}

	/**
	 * 获得颜色
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	private Color getRandColor(int frontColor, int backColor) {
		if (frontColor > 255)
			frontColor = 255;
		if (backColor > 255)
			backColor = 255;

		int red = frontColor + random.nextInt(backColor - frontColor - 16);
		int green = frontColor + random.nextInt(backColor - frontColor - 14);
		int blue = frontColor + random.nextInt(backColor - frontColor - 18);
		return new Color(red, green, blue);
	}

	/**
	 * 绘制字符串,返回绘制的字符串
	 * 
	 * @param g
	 * @param randomString
	 * @param i
	 * @return
	 */
	private String drawString(Graphics g, String randomString, int i) {
		g.setFont(getFont()); // 设置字体
		g.setColor(new Color(random.nextInt(101), random.nextInt(111), random.nextInt(121)));// 设置颜色
		String randChar = String.valueOf(getRandomChar(random.nextInt(randString.length())));
		randomString += randChar; // 组装
		g.translate(random.nextInt(3), random.nextInt(3));
		g.drawString(randChar, 13 * i, 16);
		return randomString;
	}

	/**
	 * 生成随机图片
	 * 
	 * @param request
	 * @param response
	 * @param key
	 */
	public void getRandomCode(HttpServletRequest request, HttpServletResponse response, String key) {
		// BufferedImage类是具有缓冲区的Image类,Image类是用于描述图像信息的类
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Random rand = new Random();
		Graphics2D g2 = image.createGraphics();// 获得BufferedImage对象的Graphics对象
		Color[] colors = new Color[5];
		Color[] colorSpaces = new Color[] { Color.WHITE, Color.CYAN, Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK, Color.YELLOW };
		float[] fractions = new float[colors.length];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
			fractions[i] = rand.nextFloat();
		}
		Arrays.sort(fractions);

		g2.setColor(Color.GRAY);// 设置边框色
		g2.fillRect(0, 0, width, height);// 填充边框颜色

		Color c = getRandColor(150, 250);
		g2.setColor(c);// 设置背景颜色
		g2.fillRect(0, 2, width, height - 4);// 填充背景颜色

		// 绘制干扰线
		Random random = new Random();
		g2.setColor(getRandColor(160, 200));// 设置线条的颜色
		for (int i = 0; i < 20; i++) {
			int x = random.nextInt(width - 1);
			int y = random.nextInt(height - 1);
			int xl = random.nextInt(6) + 1;
			int yl = random.nextInt(12) + 1;
			g2.drawLine(x, y, x + xl + 40, y + yl + 20);
		}

		// 添加噪点
		float yawpRate = 0.01f;// 噪声率
		int area = (int) (yawpRate * width * height);
		for (int i = 0; i < area; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int rgb = getRandomIntColor();
			image.setRGB(x, y, rgb);
		}

		shear(g2, width, height, c);// 使图片扭曲

		g2.setColor(getRandColor(100, 160));
		int fontSize = height - 4;
		Font font = new Font("Fixedsys", Font.ITALIC, fontSize);
		g2.setFont(font);// 设置字体

		// 绘制字符
		String randomString = "";
		for (int i = 1; i <= StringNum; i++) {
			randomString = drawString(g2, randomString, i);
		}
		g2.dispose();

		// 将生成的验证码放入session
		String sessionId = request.getSession().getId();// 获取session的id
		request.getSession().setAttribute(sessionId + key, randomString);
		try {
			ByteArrayOutputStream tmp = new ByteArrayOutputStream();
			ImageIO.write(image, "png", tmp);// 将会值得图片输出到流
			tmp.close();
			Integer contentLength = tmp.size();// 内容长度
			response.setHeader("content-length", contentLength + "");
			response.getOutputStream().write(tmp.toByteArray());// 通过response输出图片
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	private static int getRandomIntColor() {
		int[] rgb = getRandomRgb();
		int color = 0;
		for (int c : rgb) {
			color = color << 8;
			color = color | c;
		}
		return color;
	}

	private static int[] getRandomRgb() {
		int[] rgb = new int[3];
		for (int i = 0; i < 3; i++) {
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}

	private static void shear(Graphics g, int w1, int h1, Color color) {
		shearX(g, w1, h1, color);
		shearY(g, w1, h1, color);
	}

	private static void shearX(Graphics g, int w1, int h1, Color color) {

		int period = random.nextInt(2);

		boolean borderGap = true;
		int frames = 1;
		int phase = random.nextInt(2);

		for (int i = 0; i < h1; i++) {
			double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
			g.copyArea(0, i, w1, 1, (int) d, 0);
			if (borderGap) {
				g.setColor(color);
				g.drawLine((int) d, i, 0, i);
				g.drawLine((int) d + w1, i, w1, i);
			}
		}

	}

	private static void shearY(Graphics g, int w1, int h1, Color color) {

		int period = random.nextInt(40) + 10; // 50;

		boolean borderGap = true;
		int frames = 20;
		int phase = 7;
		for (int i = 0; i < w1; i++) {
			double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
			g.copyArea(i, 0, 1, h1, 0, (int) d);
			if (borderGap) {
				g.setColor(color);
				g.drawLine(i, (int) d, i, 0);
				g.drawLine(i, (int) d + h1, i, h1);
			}

		}

	}

}
