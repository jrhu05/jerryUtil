package net.jerryfu.sms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.aliyun.mns.model.TopicMessage;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsRequest;
import com.aliyuncs.sms.model.v20160927.SingleSendSmsResponse;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * 阿里大鱼短信接口工具类
 * @author jerryfu
 *
 */
public class SmsUtil {
	private static final Log LOG = LogFactory.getLog(SmsUtil.class);
	
	/** 系统所属 */
	public static String SYSTEM_OWNER = "jerryfu";
	
	/** 阿里大鱼请求地址 */
	public static String ALIBABA_MSG_URL = "";
	/** 阿里大鱼key */
	public static String ALIBABA_APP_KEY = "";
	/** 阿里大鱼secret */
	public static String ALIBABA_APP_SECRET = "";
	/** 阿里大鱼template */
	public static String ALIBABA_MSG_TEMPLATE_ID = "";

	/** 阿里云短信 状态 */
	public static String ALIYUN_SMS_STATUS = "";
	/** 阿里云短信key */
	public static String ALIYUN_SMS_KEY = "";
	/** 阿里云短信secret */
	public static String ALIYUN_SMS_SECRET = "";
	/** 阿里云短信template */
	public static String ALIYUN_SMS_TEMPLATE_ID = "";
	/** 阿里云短信签名 */
	public static String ALIYUN_SMS_SIGNNAME = "";
	/** 阿里云短信endpoint */
	public static String ALIYUN_SMS_ENDPOINT = "";

    /**
     * 发送身份验证
     * 
     * @param moblie
     *            手机号码（支持多号码发送，以英文逗号分隔，一次调用最多传入200个）
     * @param code
     *            验证码
     * @return
     * @throws ApiException
     */
    public static String sendCodeForRegister(String moblie, String code) throws ApiException {
        return sendMsg(moblie, "{\"code\":\"" + code + "\",\"product\":\"测试产品\"}");
    }
    
   /* public static void main(String[] args) {
		try {
			String code = RandomUtils.randomInt(6);
			System.out.println(sendCodeForRegister("13692645213", code));
		} catch (ApiException e) {
			// TODO Auto-generated catch block
		}
	}*/

    /**
     * @param smsTemplateCode
     * @param recNum
     * @param smsParamString
     * @return
     * @throws ApiException
     */
    private static String sendMsg(String moblie, String smsParamString) throws ApiException {
    	
        TaobaoClient client = new DefaultTaobaoClient(ALIBABA_MSG_URL, ALIBABA_APP_KEY, ALIBABA_APP_SECRET);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend(moblie);
        req.setSmsType("normal");
        req.setSmsFreeSignName("身份验证");
        req.setSmsParamString(smsParamString);
        req.setRecNum(moblie);
        req.setSmsTemplateCode(ALIBABA_MSG_TEMPLATE_ID);
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        return rsp.getBody();
    }

    /**
     * 发送短信验证码(阿里云短信)
     * 
     * @param moblie
     *            手机号码
     * @param code
     *            验证码
     * @return
     * @throws ApiException
     */
    public static String sendCode(String moblie, String code) throws Exception {
    	String returnMsg = "";
    	String systemOwner = SYSTEM_OWNER;
    	
    	if(systemOwner.equals("bess")||systemOwner.equals("keke")||systemOwner.equals("wobao")){
    		// 旧的短信发送方式
        	IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", ALIYUN_SMS_KEY, ALIYUN_SMS_SECRET);
            try {
    			DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms",  "sms.aliyuncs.com");
    		} catch (ClientException e) {
    			LOG.info("发送短信异常",e);
    		}
           IAcsClient client = new DefaultAcsClient(profile);
           SingleSendSmsRequest request = new SingleSendSmsRequest();
           request.setSignName( ALIYUN_SMS_SIGNNAME);
           request.setTemplateCode( ALIYUN_SMS_TEMPLATE_ID);
           request.setParamString("{\"code\":\""+code+"\"}");
           request.setRecNum(moblie);
           SingleSendSmsResponse httpResponse = client.getAcsResponse(request);
           returnMsg = httpResponse.getRequestId();
    	}else{
    		 LOG.info("短信发送，key：" +  ALIYUN_SMS_KEY+";secret："+ ALIYUN_SMS_SECRET+";endpoint:"+ ALIYUN_SMS_ENDPOINT+";signname:"+ ALIYUN_SMS_SIGNNAME+";templateid:"+ ALIBABA_MSG_TEMPLATE_ID);
    		// 新的短信发送方式
    	       /**
    	        * Step 1. 获取主题引用
    	        */
    	       CloudAccount account = new CloudAccount( ALIYUN_SMS_KEY,  ALIYUN_SMS_SECRET,  ALIYUN_SMS_ENDPOINT);
    	       MNSClient client = account.getMNSClient();
    	       CloudTopic topic = client.getTopicRef("sms.topic-cn-hangzhou");
    	       /**
    	        * Step 2. 设置SMS消息体（必须）
    	        *
    	        * 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
    	        */
    	       RawTopicMessage msg = new RawTopicMessage();
    	       msg.setMessageBody("sms-message");
    	       /**
    	        * Step 3. 生成SMS消息属性
    	        */
    	       MessageAttributes messageAttributes = new MessageAttributes();
    	       BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
    	       // 3.1 设置发送短信的签名（SMSSignName）
    	       batchSmsAttributes.setFreeSignName( ALIYUN_SMS_SIGNNAME);
    	       // 3.2 设置发送短信使用的模板（SMSTempateCode）
    	       batchSmsAttributes.setTemplateCode( ALIYUN_SMS_TEMPLATE_ID);
    	       // 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
    	       BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
    	       smsReceiverParams.setParam("code", code);
    	       // 3.4 增加接收短信的号码
    	       batchSmsAttributes.addSmsReceiver(moblie, smsReceiverParams);
    	       messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
    	       try {
    	           /**
    	            * Step 4. 发布SMS消息
    	            */
    	           TopicMessage ret = topic.publishMessage(msg, messageAttributes);
    	           LOG.info("短信发送成功，moblie:"+moblie+";code"+code+";MessageId：" + ret.getMessageId()+";MessageMD5："+ret.getMessageBodyMD5());
    	           returnMsg = ret.getMessageId();
    	       } catch (ServiceException se) {
    	    	   LOG.info(se.getErrorCode() + se.getRequestId());
    	    	   LOG.info(se.getMessage());
    	    	   LOG.info("短信发送异常",se);
    	       } catch (Exception e) {
    	    	   LOG.info("短信发送异常",e);
    	       }finally {
    	       	client.close();
    			}
    	       
    	}
    	
       return returnMsg;
       
    }
    
    public static void main(String[] args) {
    	
    	/**
         * Step 1. 获取主题引用
         */
        CloudAccount account = new CloudAccount("xxx", "xxx", "http://1595793809309073.mns.cn-hangzhou.aliyuncs.com/");
        MNSClient client = account.getMNSClient();
        CloudTopic topic = client.getTopicRef("sms.topic-cn-hangzhou");
        /**
         * Step 2. 设置SMS消息体（必须）
         *
         * 注：目前暂时不支持消息内容为空，需要指定消息内容，不为空即可。
         */
        RawTopicMessage msg = new RawTopicMessage();
        msg.setMessageBody("sms-message");
        /**
         * Step 3. 生成SMS消息属性
         */
        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();
        // 3.1 设置发送短信的签名（SMSSignName）
        batchSmsAttributes.setFreeSignName("xxx");
        // 3.2 设置发送短信使用的模板（SMSTempateCode）
        batchSmsAttributes.setTemplateCode("SMS_67245175");
        // 3.3 设置发送短信所使用的模板中参数对应的值（在短信模板中定义的，没有可以不用设置）
        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        smsReceiverParams.setParam("code", "4655");
        // 3.4 增加接收短信的号码
        batchSmsAttributes.addSmsReceiver("18988969779", smsReceiverParams);
        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);
        try {
            /**
             * Step 4. 发布SMS消息
             */
            TopicMessage ret = topic.publishMessage(msg, messageAttributes);
            System.out.println("MessageId: " + ret.getMessageId());
            System.out.println("MessageMD5: " + ret.getMessageBodyMD5());
        } catch (ServiceException se) {
            System.out.println(se.getErrorCode() + se.getRequestId());
            System.out.println(se.getMessage());
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
        	client.close();
		}
    	
	}
}
