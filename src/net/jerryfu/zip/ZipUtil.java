package net.jerryfu.zip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * zip压缩、解压缩工具类
 * @author jerryfu
 *
 */
public class ZipUtil {

	 /**  
     * 解压缩zip包  
     * @param zipFilePath zip文件的全路径  
     * @param unzipFilePath 解压后的文件保存的路径  
     * @param includeZipFileName 解压后的文件保存的路径是否包含压缩文件的文件名。true-包含；false-不包含  
     */    
    @SuppressWarnings("unchecked")    
    public static void unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws Exception    
    {    
        if (StringUtils.isBlank(zipFilePath) || StringUtils.isBlank(unzipFilePath))    
        {    
            throw new RuntimeException("zip file path is null!");              
        }    
        File zipFile = new File(zipFilePath);    
        //如果解压后的文件保存路径包含压缩文件的文件名，则追加该文件名到解压路径    
        if (includeZipFileName)    
        {    
            String fileName = zipFile.getName();    
            if (StringUtils.isNotEmpty(fileName))    
            {    
                fileName = fileName.substring(0, fileName.lastIndexOf("."));    
            }    
            unzipFilePath = unzipFilePath + File.separator + fileName;    
        }    
        //创建解压缩文件保存的路径    
        File unzipFileDir = new File(unzipFilePath);    
        if (!unzipFileDir.exists() || !unzipFileDir.isDirectory())    
        {    
            unzipFileDir.mkdirs();    
        }    
            
        //开始解压    
        ZipEntry entry = null;    
        String entryFilePath = null, entryDirPath = null;    
        File entryFile = null, entryDir = null;    
        int index = 0, count = 0, bufferSize = 1024;    
        byte[] buffer = new byte[bufferSize];    
        BufferedInputStream bis = null;    
        BufferedOutputStream bos = null;    
        ZipFile zip = new ZipFile(zipFile);    
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>)zip.entries();    
        //循环对压缩包里的每一个文件进行解压         
        while(entries.hasMoreElements())    
        {    
            entry = entries.nextElement();    
            //构建压缩包中一个文件解压后保存的文件全路径    
            entryFilePath = unzipFilePath + File.separator + entry.getName();    
            //构建解压后保存的文件夹路径    
            index = entryFilePath.lastIndexOf(File.separator);    
            if (index != -1)    
            {    
                entryDirPath = entryFilePath.substring(0, index);    
            }    
            else    
            {    
                entryDirPath = "";    
            }               
            entryDir = new File(entryDirPath);    
            //如果文件夹路径不存在，则创建文件夹    
            if (!entryDir.exists() || !entryDir.isDirectory())    
            {    
                entryDir.mkdirs();    
            }    
                
            //创建解压文件    
            entryFile = new File(entryFilePath);    
            if (entryFile.exists())    
            {    
//                //检测文件是否允许删除，如果不允许删除，将会抛出SecurityException    
//                SecurityManager securityManager = new SecurityManager();    
//                securityManager.checkDelete(entryFilePath);    
                //删除已存在的目标文件    
                entryFile.delete();     
            }    
                
            //写入文件    
            bos = new BufferedOutputStream(new FileOutputStream(entryFile));    
            bis = new BufferedInputStream(zip.getInputStream(entry));    
            while ((count = bis.read(buffer, 0, bufferSize)) != -1)    
            {    
                bos.write(buffer, 0, count);    
            }    
            bos.flush();    
            bos.close();                
        }    
    }    

    private static Log log = LogFactory.getLog(ZipUtil.class);
	private static final int  BUFFER_SIZE = 2 * 1024;
	
	/**
	 * 压缩成ZIP 方法1
	 * @param srcDir 压缩文件夹路径 
	 * @param out    压缩文件输出流
	 * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构; 
	 * 							false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws RuntimeException 压缩失败会抛出运行时异常
	 */
	public static void toZip(String srcDir, OutputStream out, boolean KeepDirStructure)
			throws RuntimeException{
		
		long start = System.currentTimeMillis();
		ZipOutputStream zos = null ;
		try {
			zos = new ZipOutputStream(out);
			File sourceFile = new File(srcDir);
			compress(sourceFile,zos,sourceFile.getName(),KeepDirStructure);
			long end = System.currentTimeMillis();
			log.info("压缩完成，耗时：" + (end - start) +" ms");
		} catch (Exception e) {
			throw new RuntimeException("zip error from ZipUtils",e);
		}finally{
			if(zos != null){
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	/**
	 * 递归压缩方法
	 * @param sourceFile 源文件
	 * @param zos		 zip输出流
	 * @param name		 压缩后的名称
	 * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构; 
	 * 							false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
	 * @throws Exception
	 */
	private static void compress(File sourceFile, ZipOutputStream zos, String name,
			boolean KeepDirStructure) throws Exception{
		byte[] buf = new byte[BUFFER_SIZE];
		if(sourceFile.isFile()){
			// 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
			zos.putNextEntry(new ZipEntry(name));
			// copy文件到zip输出流中
			int len;
			FileInputStream in = new FileInputStream(sourceFile);
			while ((len = in.read(buf)) != -1){
				zos.write(buf, 0, len);
			}
			// Complete the entry
			zos.closeEntry();
			in.close();
		} else {
			File[] listFiles = sourceFile.listFiles();
			if(listFiles == null || listFiles.length == 0){
				// 需要保留原来的文件结构时,需要对空文件夹进行处理
				if(KeepDirStructure){
					// 空文件夹的处理
					zos.putNextEntry(new ZipEntry(name + "/"));
					// 没有文件，不需要文件的copy
					zos.closeEntry();
				}
				
			}else {
				for (File file : listFiles) {
					// 判断是否需要保留原来的文件结构
					if (KeepDirStructure) {
						// 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
						// 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
						compress(file, zos, name + "/" + file.getName(),KeepDirStructure);
					} else {
						compress(file, zos, file.getName(),KeepDirStructure);
					}
					
				}
			}
		}
	}
	
	public static void main(String[] args) {
		FileOutputStream out;
		try {
			out = new FileOutputStream("D:\\work\\temp\\test.zip");
			toZip("D:\\work\\temp\\batch", out, true);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    
}
