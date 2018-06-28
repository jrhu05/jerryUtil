package net.jerryfu.qiniu;

import java.io.File;

import org.junit.Test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

/**
 * 七牛云上传辅助工具类
 * https://developer.qiniu.com/kodo/sdk/1239/java#upload-file
 * @author jerryfu
 *
 */
public class QiNiuUploadUtil {
	
	@Test
	public void test() {
		upLoad2QiNiu();
	}
	
	public void upLoad2QiNiu() {
		//构造一个带指定Zone对象的配置类
		/**
		 * 华东	Zone.zone0()
			华北	Zone.zone1()
			华南	Zone.zone2()
			北美	Zone.zoneNa0()
		 */
		Configuration cfg = new Configuration(Zone.zone2());
		UploadManager uploadManager = new UploadManager(cfg);
		//...生成上传凭证，然后准备上传
		String accessKey = QiNiuConfig.AccessKey;
		String secretKey = QiNiuConfig.SecretKey;
		String bucket = QiNiuConfig.Bucket;
		//如果是Windows情况下，格式是 D:\\qiniu\\test.png
		String localFilePath = "D:\\work\\jar\\qiniu\\test.jpg";
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		File needUpload=new File(localFilePath);
		String key =null;
		if (needUpload!=null) {
			key = needUpload.getName();;
		}
		
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		try {
			    Response response = uploadManager.put(localFilePath, key, upToken);
			    //解析上传成功的结果
			    DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
			    System.out.println(putRet.key);
			    System.out.println(putRet.hash);
			} catch (QiniuException ex) {
			    Response r = ex.response;
			    System.err.println(r.toString());
			    try {
			        System.err.println(r.bodyString());
			    } catch (QiniuException ex2) {
			        //ignore
			    }
			}
		}
}
