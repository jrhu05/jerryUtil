package net.jerryfu.qRCode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.swetake.util.Qrcode;
/**
 * 使用QRcode生成二维码辅助工具类
 * @author jerryfu
 *
 */
public class MyQRCodeUtil {
	/**
	 * 日志工具
	 */
	private static Log log=LogFactory.getLog(MyQRCodeUtil.class);
	/**
	 * 根据传入的参数生成二维码
	 * @param QrcodeErrorCorrectlevel 纠错等级 纠错等级分为
	 * <br>level L : 最大 7% 的错误能够被纠正；
	 * <br>level M : 最大 15% 的错误能够被纠正；
     * <br>level Q : 最大 25% 的错误能够被纠正；
     * <br>level H : 最大 30% 的错误能够被纠正；
	 * @param QrcodeEncodeMode 二维码编码版本, N代表数字 、A代表 a-z,A-Z、B代表 其他,默认为B
	 * @param QrcodeVersion 版本号  1-40
	 * @param content 二维码承载的内容
	 * @param picType 二维码图片格式，jpg/png等
	 * @param picName	二维码存储图片名称
	 * @param storePath	二维码存储路径（父目录路径）
	 * @return
	 */
	public static boolean createQrCode(String QrcodeErrorCorrectlevel,String QrcodeEncodeMode,Integer QrcodeVersion,
			String content,String picType,String picName,String storePath) {
		log.info("开始生成QRCode二维码，相关信息如下：");
		log.info("二维码存储内容："+content);
		log.info("生成二维码图片类型："+picType);
		log.info("生成二维码存储名称："+picName);
		log.info("二维码存储的路径是："+storePath);
		if (storePath==null||storePath.equals("")) {
			if (log.isDebugEnabled()) {
				log.debug("二维码生成图片存储路径为空！");
			}
			return false;
		}
		if (QrcodeErrorCorrectlevel==null||QrcodeErrorCorrectlevel.equals("")) {
			log.info("二维码纠错等级为空，使用默认等级：L");
			QrcodeErrorCorrectlevel="L";
		}
		if (QrcodeEncodeMode==null||QrcodeEncodeMode.equals("")) {
			log.info("二维码编码版本为空，使用默认版本：B");
			QrcodeEncodeMode="B";
		}
		if (QrcodeVersion==null) {
			log.info("二维码版本号为空，使用默认版本号：6");
			QrcodeVersion=6;
		}
		if (picType==null||picType.equals("")) {
			log.info("图片类型为空，使用默认格式：png");
			picType="png";
		}
		if (picName==null||picName.equals("")) {
			log.info("图片存储名称为空，使用默认的图片名：qrcode");
			picName="qrcode";
		}
		 //计算二维码图片的高宽比
        // API文档规定计算图片宽高的方式 ，v是本次测试的版本号
        int v =QrcodeVersion;
        int width = 67 + 12 * (v - 1);
        int height = 67 + 12 * (v - 1);
        Qrcode x = new Qrcode();
        /**
         * 纠错等级分为
         * level L : 最大 7% 的错误能够被纠正；
         * level M : 最大 15% 的错误能够被纠正；
         * level Q : 最大 25% 的错误能够被纠正；
         * level H : 最大 30% 的错误能够被纠正；
         */
        x.setQrcodeErrorCorrect(QrcodeErrorCorrectlevel.charAt(0));
        x.setQrcodeEncodeMode(QrcodeEncodeMode.charAt(0));//注意版本信息 N代表数字 、A代表 a-z,A-Z、B代表 其他)
        x.setQrcodeVersion(QrcodeVersion);//版本号  1-40
        String qrData = content;//内容信息
        byte[] d = null;
        try {
			d=qrData.getBytes("utf-8");//汉字转格式需要抛出异常
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			log.error("文字内容格式转换异常，二维码生成失败！");
			if (log.isDebugEnabled()) {
				log.debug("堆栈信息如下：");
				e.printStackTrace();
			}
			return false;
		}
        
        File outPut=null;
        try {
        	outPut=new File(storePath+java.io.File.separator+picName+"."+picType);
		} catch (Exception e) {
			log.error("二维码输出文件创建失败！");
			if (log.isDebugEnabled()) {
				log.debug("堆栈信息如下：");
				e.printStackTrace();
			}
			return false;
		}
        
        //缓冲区
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);

        //绘图
        Graphics2D gs = bufferedImage.createGraphics();

        gs.setBackground(Color.WHITE);
        gs.setColor(Color.BLACK);
        gs.clearRect(0, 0, width, height);

        //偏移量
        int pixoff = 2;


        /**
         * 容易踩坑的地方
         * 1.注意for循环里面的i，j的顺序，
         *   s[j][i]二维数组的j，i的顺序要与这个方法中的 gs.fillRect(j*3+pixoff,i*3+pixoff, 3, 3);
         *   顺序匹配，否则会出现解析图片是一串数字
         * 2.注意此判断if (d.length > 0 && d.length < 120)
         *   是否会引起字符串长度大于120导致生成代码不执行，二维码空白
         *   根据自己的字符串大小来设置此配置
         */
        if (d.length > 0 && d.length < 120) {
            boolean[][] s = x.calQrcode(d);

            for (int i = 0; i < s.length; i++) {
                for (int j = 0; j < s.length; j++) {
                    if (s[j][i]) {
                        gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
                    }
                }
            }
        }
        gs.dispose();
        bufferedImage.flush();
        
      //设置图片格式，与输出的路径
        try {
			ImageIO.write(bufferedImage, picType, outPut);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("二维码输出文件创建失败！");
			if (log.isDebugEnabled()) {
				log.debug("堆栈信息如下：");
				e.printStackTrace();
			}
			return false;
		}
        log.info("二维码生成结束，图片路径："+outPut.getAbsolutePath());
		return true;
	}
	
	public static boolean createQrCode(String content,String picType,String picName,String storePath) {
		return createQrCode("", "", null, content, picType, picName, storePath);
	}
	
	public static boolean createQrCode(String content,String picName,String storePath) {
		return createQrCode("", "", null, content, "", picName, storePath);
	}
	
	public static boolean createQrCode(String content,String storePath) {
		return createQrCode("", "", null, content, "", "", storePath);
	}
	
}
