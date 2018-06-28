package net.jerryfu.file;/*
											* Copyright 2015-2102 RonCoo(http://www.roncoo.com) Group.
											*
											* Licensed under the Apache License, Version 2.0 (the "License");
											* you may not use this file except in compliance with the License.
											* You may obtain a copy of the License at
											*
											*      http://www.apache.org/licenses/LICENSE-2.0
											*
											* Unless required by applicable law or agreed to in writing, software
											* distributed under the License is distributed on an "AS IS" BASIS,
											* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
											* See the License for the specific language governing permissions and
											* limitations under the License.
											*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * 文件及文件夹工具类
 * @author jerryfu
 *
 */
public class FileUtil {
	private static final Log log = LogFactory.getLog(FileUtil.class);
	
	  /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * 
     * @param dir
     *            将要删除的文件目录
     * @return
     */
    private static boolean deleteDir(File dir) {
        if (!dir.exists()) return false;
        if (dir.isDirectory()) {
            String[] childrens = dir.list();
            // 递归删除目录中的子目录下
            for (String child : childrens) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) return false;
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

	/**
	 * 判断是否是目录
	 * 
	 * @param dirPath
	 * @return
	 */
	public static boolean isDir(String dirPath) {
		if (StringUtils.isBlank(dirPath)) {
			return false;
		}
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory()) {
			return false;
		}
		return true;
	}

	/**
	 * 创建文件夹,只能创建最底层一级的文件夹,如果上级文件夹不存在,则创建失败
	 * 
	 * @param dirPath
	 */
	public static boolean mkDir(String dirPath) {
		if (StringUtils.isBlank(dirPath)) {
			return false;
		}

		File file = new File(dirPath);
		if (file.exists()) {
			return true;
		} else {
			return file.mkdir();
		}
	}

	/**
	 * 创建文件夹,可以创建多级文件夹,如果最底层一级之上的文件夹也不存在,则同时生成上一级文件夹
	 * 
	 * @param dirPath
	 * @return
	 */
	public static boolean mkDirAndSubDir(String dirPath) {
		if (StringUtils.isBlank(dirPath)) {
			return false;
		}

		File file = new File(dirPath);
		if (file.exists()) {
			return true;
		} else {
			return file.mkdirs();
		}
	}

	// 创建单个文件
	public static boolean createFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {// 判断文件是否存在
			System.out.println("目标文件已存在" + filePath);
			return false;
		}
		if (filePath.endsWith(File.separator)) {// 判断文件是否为目录
			System.out.println("目标文件不能为目录！");
			return false;
		}
		if (!file.getParentFile().exists()) {// 判断目标文件所在的目录是否存在
			// 如果目标文件所在的文件夹不存在，则创建父文件夹
			System.out.println("目标文件所在目录不存在，准备创建它！");
			if (!file.getParentFile().mkdirs()) {// 判断创建目录是否成功
				System.out.println("创建目标文件所在的目录失败！");
				return false;
			}
		}
		try {
			if (file.createNewFile()) {// 创建目标文件
				System.out.println("创建文件成功:" + filePath);
				return true;
			} else {
				System.out.println("创建文件失败！");
				return false;
			}
		} catch (IOException e) {// 捕获异常
			e.printStackTrace();
			System.out.println("创建文件失败！" + e.getMessage());
			return false;
		}
	}

	// 创建目录
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {// 判断目录是否存在
			System.out.println("创建目录失败，目标目录已存在！");
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
			destDirName = destDirName + File.separator;
		}
		if (dir.mkdirs()) {// 创建目标目录
			System.out.println("创建目录成功！" + destDirName);
			return true;
		} else {
			System.out.println("创建目录失败！");
			return false;
		}
	}

	/**
	 * 写文件
	 * 
	 * @param newStr
	 *            新内容
	 * @throws IOException
	 */
	public static boolean writeTxtFile(String fileName, String newStr) throws IOException {
		// 先读取原有文件内容，然后进行写入操作
		boolean flag = false;
		String filein = newStr;
		String temp = "";

		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		FileOutputStream fos = null;
		PrintWriter pw = null;
		try {
			// 文件路径
			File file = new File(fileName);
			// 将文件读入输入流
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			StringBuffer buf = new StringBuffer();

			// 保存该文件原有的内容
			for (int j = 1; (temp = br.readLine()) != null; j++) {
				buf = buf.append(temp);
			}
			buf.append(filein);

			fos = new FileOutputStream(file);
			pw = new PrintWriter(fos);
			pw.write(buf.toString().toCharArray());
			pw.flush();
			flag = true;
		} catch (IOException e1) {
			// TODO 自动生成 catch 块
			throw e1;
		} finally {
			if (pw != null) {
				pw.close();
			}
			if (fos != null) {
				fos.close();
			}
			if (br != null) {
				br.close();
			}
			if (isr != null) {
				isr.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		return flag;
	}

	public static void main(String[] args) {
		// System.out.println("" +
		// mkDirAndSubDir("/Users/peter/Desktop/redis/test/test/fast/test.txt"));
		String fileName = "F:/aa/bb/1.txt";
		createFile("F:/aa/bb/1.txt");
		try {
			writeTxtFile(fileName, "2017-02-01 22:10:15|payment001|10");
			writeTxtFile(fileName, "2017-02-01 22:10:15|payment001|11");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
