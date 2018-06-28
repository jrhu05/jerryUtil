package net.jerryfu.qRCode;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


/**
 * 二维码生成工具类
 * (支持内嵌LOGO) 
 * @author jerryfu
 *
 */
public class QRCodeUtil {


	/** 二维码字符编码(默认utf-8) **/
	public static String QRCODE_CHARSET = "utf-8";
	/** 二维码图片格式(默认jpg) **/
	public static String QRCODE_FORMAT = "jpg";
	/** 二维码内容(请求路径) **/
	public static String QRCODE_CONTENT = "";
	/** 二维码尺寸(默认300) **/
	public static int QRCODE_SIZE = 300;
	/** 二维码LOGO宽度(默认100) **/
	public static int QRCODE_LOGO_WIDTH = 100;
	/** 二维码LOGO高度(默认100) **/
	public static int QRCODE_LOGO_HEIGHT = 100;
	/** 二维码LOGO路径 **/
	public static String QRCODE_LOGO_PATH = "";
	/** 二维码签名密钥 **/
	public static String QRCODE_SECRET = "";
	
    private static BufferedImage createImage(String content, String logoPath, boolean needCompress) throws Exception {  
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        hints.put(EncodeHintType.CHARACTER_SET,  QRCODE_CHARSET);  
        hints.put(EncodeHintType.MARGIN, 1);  
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,  QRCODE_SIZE,  QRCODE_SIZE,  
                hints);  
        int width = bitMatrix.getWidth();  
        int height = bitMatrix.getHeight();  
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
        for (int x = 0; x < width; x++) {  
            for (int y = 0; y < height; y++) {  
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);  
            }  
        }  
        if (logoPath == null || "".equals(logoPath)) {  
            return image;  
        }  
        // 插入图片  
        QRCodeUtil.insertImage(image, logoPath, needCompress);  
        return image;  
    }  
  
    /** 
     * 插入LOGO 
     *  
     * @param source 
     *            二维码图片 
     * @param logoPath 
     *            LOGO图片地址 
     * @param needCompress 
     *            是否压缩 
     * @throws Exception 
     */  
    private static void insertImage(BufferedImage source, String logoPath, boolean needCompress) throws Exception {  
        File file = new File(logoPath);  
        if (!file.exists()) {  
            throw new Exception("logo file not found.");  
        }
        Image src = ImageIO.read(new File(logoPath));  
        int width = src.getWidth(null);  
        int height = src.getHeight(null);  
        if (needCompress) { // 压缩LOGO  
            if (width >  QRCODE_LOGO_WIDTH) {  
                width =  QRCODE_LOGO_WIDTH;  
            }  
            if (height >  QRCODE_LOGO_HEIGHT) {  
                height =  QRCODE_LOGO_HEIGHT;  
            }  
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);  
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  
            Graphics g = tag.getGraphics();  
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
            g.dispose();  
            src = image;  
        }  
        // 插入LOGO  
        Graphics2D graph = source.createGraphics();  
        int x = ( QRCODE_SIZE - width) / 2;  
        int y = ( QRCODE_SIZE - height) / 2;  
        graph.drawImage(src, x, y, width, height, null);  
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);  
        graph.setStroke(new BasicStroke(3f));  
        graph.draw(shape);  
        graph.dispose();  
    }  
  
    /** 
     * 生成二维码(内嵌LOGO) 
     * 二维码文件名随机，文件名可能会有重复 
     *  
     * @param content 
     *            内容 
     * @param logoPath 
     *            LOGO地址 
     * @param destPath 
     *            存放目录 
     * @param needCompress 
     *            是否压缩LOGO 
     * @throws Exception 
     */  
    public static String encode(String content, String logoPath, String destPath, boolean needCompress) throws Exception {  
        BufferedImage image = QRCodeUtil.createImage(content, logoPath, needCompress);  
        mkdirs(destPath);  
        String fileName = new Random().nextInt(99999999) + "." +  QRCODE_FORMAT.toLowerCase();  
        ImageIO.write(image,  QRCODE_FORMAT, new File(destPath + "/" + fileName));  
        return fileName;  
    }  
    
    /** 
     * 生成二维码(内嵌LOGO) 
     * 调用者指定二维码文件名 
     *  
     * @param content 
     *            内容 
     * @param logoPath 
     *            LOGO地址 
     * @param destPath 
     *            存放目录 
     * @param fileName 
     *            二维码文件名 
     * @param needCompress 
     *            是否压缩LOGO 
     * @throws Exception 
     */  
    public static String encode(String content, String logoPath, String destPath, String fileName, boolean needCompress) throws Exception {  
        BufferedImage image = QRCodeUtil.createImage(content, logoPath, needCompress);  
        mkdirs(destPath);  
        fileName = fileName.substring(0, fileName.indexOf(".")>0?fileName.indexOf("."):fileName.length())   
                + "." +  QRCODE_FORMAT.toLowerCase();  
        ImageIO.write(image,  QRCODE_FORMAT, new File(destPath + "/" + fileName));  
        return fileName;  
    }  
  
    /** 
     * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir． 
     * (mkdir如果父目录不存在则会抛出异常) 
     * @param destPath 
     *            存放目录 
     */  
    public static void mkdirs(String destPath) {  
        File file = new File(destPath);  
        if (!file.exists() && !file.isDirectory()) {  
            file.mkdirs();  
        }  
    }  
  
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 
     *            内容 
     * @param logoPath 
     *            LOGO地址 
     * @param destPath 
     *            存储地址 
     * @throws Exception 
     */  
    public static String encode(String content, String logoPath, String destPath) throws Exception {  
        return QRCodeUtil.encode(content, logoPath, destPath, false);  
    }  
  
    /** 
     * 生成二维码 
     *  
     * @param content 
     *            内容 
     * @param destPath 
     *            存储地址 
     * @param needCompress 
     *            是否压缩LOGO 
     * @throws Exception 
     */  
    public static String encode(String content, String destPath, boolean needCompress) throws Exception {  
        return QRCodeUtil.encode(content, null, destPath, needCompress);  
    }  
    
    /**
     * 函数功能说明 ：获取二维码输入流 <br/>
     * 修改者名字： <br/>
     * 修改日期： <br/>
     * 修改内容：<br/>
     * 作者：roncoo-lrx <br/>
     * 参数：@param content 内容 
     * 参数：@param logoPath LOGO地址 
     * 参数：@param needCompress 是否压缩LOGO 
     * 参数：@return
     * 参数：@throws Exception <br/>
     * return：InputStream <br/>
     */
    public static InputStream getInputStream(String content, String logoPath, boolean needCompress) throws Exception {  
        BufferedImage image = QRCodeUtil.createImage(content, logoPath, needCompress);  
        ByteArrayOutputStream bs =new ByteArrayOutputStream();
        ImageOutputStream output =ImageIO.createImageOutputStream(bs);
        ImageIO.write(image,  QRCODE_FORMAT, output); 
        InputStream inputStream =new ByteArrayInputStream(bs.toByteArray());
        return inputStream;  
    }
  
    /** 
     * 生成二维码 
     *  
     * @param content 
     *            内容 
     * @param destPath 
     *            存储地址 
     * @throws Exception 
     */  
    public static String encode(String content, String destPath) throws Exception {  
        return QRCodeUtil.encode(content, null, destPath, false);  
    }  
  
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 
     *            内容 
     * @param logoPath 
     *            LOGO地址 
     * @param output 
     *            输出流 
     * @param needCompress 
     *            是否压缩LOGO 
     * @throws Exception 
     */  
    public static void encode(String content, String logoPath, OutputStream output, boolean needCompress)  
            throws Exception {  
        BufferedImage image = QRCodeUtil.createImage(content, logoPath, needCompress);  
        ImageIO.write(image,  QRCODE_FORMAT, output);  
    }  
  
    /** 
     * 生成二维码 
     *  
     * @param content 
     *            内容 
     * @param output 
     *            输出流 
     * @throws Exception 
     */  
    public static void encode(String content, OutputStream output) throws Exception {  
        QRCodeUtil.encode(content, null, output, false);  
    }  
  
    /** 
     * 解析二维码 
     *  
     * @param file 
     *            二维码图片 
     * @return 
     * @throws Exception 
     */  
    public static String decode(File file) throws Exception {  
        BufferedImage image;  
        image = ImageIO.read(file);  
        if (image == null) {  
            return null;  
        }  
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);  
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
        Result result;  
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();  
        hints.put(DecodeHintType.CHARACTER_SET,  QRCODE_CHARSET);  
        result = new MultiFormatReader().decode(bitmap, hints);  
        String resultStr = result.getText();  
        return resultStr;  
    }  
  
    /** 
     * 解析二维码 
     *  
     * @param path 
     *            二维码图片地址 
     * @return 
     * @throws Exception 
     */  
    public static String decode(String path) throws Exception {  
        return QRCodeUtil.decode(new File(path));  
    }  
  
    public static void main(String[] args) throws Exception {  
        String text = "http://www.baidu.com";  
        //不含Logo  
        //QRCodeUtil.encode(text, null, "e:\\", true);  
        //含Logo，不指定二维码图片名  
        //QRCodeUtil.encode(text, "e:\\csdn.jpg", "e:\\", true);  
        //含Logo，指定二维码图片名  
        QRCodeUtil.encode(text, "e:\\test_icon.png", "e:\\", "qrcode", true);  
    }  

}
