package net.jerryfu.ftp;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import net.jerryfu.dateAndTime.DateUtils;
import net.jerryfu.string.StringUtil;


/**
 * FTP上传工具类
 * @author jerryfu
 *
 */
public class FtpUpLoadUtil {

	private static final Log LOG = LogFactory.getLog(FtpUpLoadUtil.class);

	// public static final String FTP_CHARSET = charSet.trim();
	// public static final String FTP_HOST = SystemConfig.FTP_HOST.trim();
	// public static final int FTP_PORT =
	// Integer.valueOf(SystemConfig.FTP_PORT.trim()).intValue();
	// public static final String FTP_USER_NAME =
	// SystemConfig.FTP_USERNAME.trim();
	// public static final String FTP_PASSWORD =
	// SystemConfig.FTP_PASSWORD.trim();
	// public static final String FTP_DEST_PATH =
	// SystemConfig.FTP_DEST_PATH.trim();

	private FtpUpLoadUtil() {

	}

	/**
	 * 函数功能说明 ： 上传文件到FTP服务器
	 * @param ftpHost
	 * @param ftpPassword
	 * @param ftpUserName
	 * @param ftpPort
	 * @param charSet
	 * @param ftpDestPath
	 * @param projectName
	 * @param inputStream
	 * @param fileName
	 * @param model
	 * @param flag
	 * @return
	 * @throws IOException
	 */
	public static String upload(
			String ftpHost, String ftpPassword, String ftpUserName, int ftpPort,String charSet,
			String ftpDestPath,
			String projectName, InputStream inputStream, String fileName, String model, String flag)
			throws IOException {
		LOG.info("开始上传文件到FTP!");
		FTPClient ftpClient = null;
		// 存放路径
		String destPath = "";
		try {
			// 连接FTP服务器
			ftpClient = getFTPClient(ftpHost,ftpPassword, ftpUserName,
					ftpPort);
			// 中文支持
			ftpClient.setControlEncoding(charSet);
			// 设置PassiveMode传输
			ftpClient.enterLocalPassiveMode();
			// 设置以二进制流的方式传输
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			// ftpClient不能一次创建多层目录,只能进入目录后再创建下一级目录.
			// 创建目录文件夹.
			ftpClient.makeDirectory(projectName + model);
			// 进入指定目录.
			ftpClient.changeWorkingDirectory(projectName + model);
			ftpClient.makeDirectory(projectName + model + DateUtils.getFormatedDate("/yyyy"));
			ftpClient.changeWorkingDirectory(projectName + model + DateUtils.getFormatedDate("/yyyy"));
			ftpClient.makeDirectory(projectName + model + DateUtils.getFormatedDate("/yyyy/MM_dd"));
			ftpClient.changeWorkingDirectory(projectName + model + DateUtils.getFormatedDate("/yyyy/MM_dd"));
			ftpClient.makeDirectory(projectName + model + DateUtils.getFormatedDate("/yyyy/MM_dd/") + flag);
			ftpClient.changeWorkingDirectory(projectName + model + DateUtils.getFormatedDate("/yyyy/MM_dd/") + flag);
			ftpClient.storeFile(fileName, inputStream);
			inputStream.close();
			LOG.info("上传文件" + fileName + "到FTP成功!");
			// FTP图片全路径
			destPath = ftpDestPath + "/" + model + DateUtils.getFormatedDate("/yyyy/MM_dd/") + flag + "/"
					+ fileName;
		} catch (Exception e) {
			LOG.error(e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				LOG.error(e);
				destPath = "";
			}
		}
		return destPath;
	}

	/**
	 * 读取ftp上面的文件，下载到本地目录
	 * 
	 * @param ftpPath
	 * @param fileName
	 * @param localFile
	 */
	public static void down(String ftpHost, String ftpPassword, String ftpUserName, int ftpPort,String charSet,
			String ftpPath, String fileName, String localFile) {

		LOG.info("开始读取绝对路径" + ftpPath + "文件!,文件名:" + fileName + ",目标文件夹:" + localFile);
		try {
			FTPClient ftpClient = getFTPClient(ftpHost, ftpPassword,
					ftpUserName, ftpPort);
			ftpClient.setControlEncoding("UTF-8"); // 中文支持
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(ftpPath);
			OutputStream is = new FileOutputStream(localFile);
			ftpClient.retrieveFile(fileName, is);
		} catch (Exception e) {
			LOG.error("没有找到" + ftpPath + "文件", e);
		}
	}

	/**
	 * 读取FTP指定路径下文件
	 * 
	 * @param ftpPath
	 * @param fileName
	 * @return
	 */
	public static List<String[]> readFile(String ftpHost, String ftpPassword, String ftpUserName, int ftpPort,String charSet,
			String ftpPath, String fileName) {
		InputStream ins = null;
		List<String[]> list = new ArrayList<>();
		LOG.info("开始读取绝对路径" + ftpPath + "文件!,文件名:" + fileName);
		try {
			FTPClient ftpClient = getFTPClient(ftpHost, ftpPassword,
					ftpUserName, ftpPort);
			ftpClient.setControlEncoding("UTF-8"); // 中文支持
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(ftpPath);
			ins = ftpClient.retrieveFileStream(fileName);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
			String lineTxt = null;
			while ((lineTxt = bufferedReader.readLine()) != null) {
				// 去掉空的行
				if (StringUtil.isEmpty(lineTxt)) {
					continue;
				}
				String[] strings = lineTxt.split("\\|");
				list.add(strings);
			}
		} catch (Exception e) {
			LOG.error("没有找到" + ftpPath + "文件", e);
		}

		return list;
	}

	/**
	 * 获取FTPClient对象
	 * 
	 * @param ftpHost
	 *            FTP主机服务器
	 * @param ftpPassword
	 *            FTP 登录密码
	 * @param ftpUserName
	 *            FTP登录用户名
	 * @param ftpPort
	 *            FTP端口 默认为21
	 * @return
	 */
	public static FTPClient getFTPClient(String ftpHost, String ftpPassword, String ftpUserName, int ftpPort) {
		FTPClient ftpClient = null;
		try {
			ftpClient = new FTPClient();
			ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
			ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
			if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
				LOG.info("未连接到FTP，用户名或密码错误。");
				ftpClient.disconnect();
			} else {
				LOG.info("FTP连接成功。");
			}
		} catch (IOException e) {
			LOG.error(e);
			LOG.info("FTP的IP地址或端口错误,请正确配置。");
		}
		return ftpClient;
	}
}
