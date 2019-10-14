package cn.carlisle.official.accounts.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import cn.carlisle.official.accounts.util.AesException;
import cn.carlisle.official.accounts.util.WXBizMsgCrypt;
import cn.carlisle.official.accounts.util.SHA1;
import lombok.extern.slf4j.Slf4j;

/**
 * <p> </p>
 *
 * <pre> Created: 2019-09-10 13:48  </pre>
 * <pre> Project: wechat  </pre>
 *
 * @author ZhuLei
 * @version 1.0
 * @since JDK 1.8
 */

@Slf4j
@RestController
@RequestMapping("official/account")
public class AuthController {

    private static final String TOKEN = "Calisle666Cullen";

    /**
     * 公众号接入接口
     *
     * @param signature 微信加密签名
     * @param timestamp 发送的时间戳
     * @param nonce     随机数验证
     * @param echostr   返回随机码
     * @param response  response
     */
    @GetMapping("auth")
    public void authorize(String signature, String timestamp, String nonce, String echostr, HttpServletResponse response) {
        log.info("访问认证接口程序！！");
        log.info("接收到参数：signature: {}, timestamp: {}, nonce: {}, echostr: {}", signature, timestamp, nonce, echostr);
        try {
            String sig = SHA1.getSHA1(TOKEN, timestamp, nonce, "");
            log.info("生成sig: {}", sig);
            if (signature != null && signature.equals(sig)) {
                PrintWriter out = response.getWriter();
                out.print(echostr);
                log.info("返回echostr: {}", echostr);
            }
        } catch (AesException | IOException e) {
            log.error("访问认证接口程序异常！！");
            e.printStackTrace();
        }
    }

    private boolean validateToken(String timestamp, String nonce, String signature) throws Exception {


        String appId = "wxdcdf6677ad65938f";

        // 需要加密的明文
        String encodingAesKey = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFG";
        String replyMsg = " 中文<xml><ToUserName><![CDATA[oia2TjjewbmiOUlr6X-1crbLOvLw]]></ToUserName><FromUserName><![CDATA[gh_7f083739789a]]></FromUserName><CreateTime>1407743423</CreateTime><MsgType><![CDATA[video]]></MsgType><Video><MediaId><![CDATA[eYJ1MbwPRJtOvIEabaxHs7TX2D-HV71s79GUxqdUkjm6Gs2Ed1KF3ulAOA9H1xG0]]></MediaId><Title><![CDATA[testCallBackReplyVideo]]></Title><Description><![CDATA[testCallBackReplyVideo]]></Description></Video></xml>";

        WXBizMsgCrypt pc = new WXBizMsgCrypt(TOKEN, encodingAesKey, appId);
        String mingwen = pc.encryptMsg(replyMsg, timestamp, nonce);
        System.out.println("加密后: " + mingwen);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        dbf.setXIncludeAware(false);
        dbf.setExpandEntityReferences(false);

        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(mingwen);
        InputSource is = new InputSource(sr);
        Document document = db.parse(is);

        Element root = document.getDocumentElement();
        NodeList nodelist1 = root.getElementsByTagName("Encrypt");
        NodeList nodelist2 = root.getElementsByTagName("MsgSignature");

        String encrypt = nodelist1.item(0).getTextContent();
        String msgSignature = nodelist2.item(0).getTextContent();

        String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
        String fromXML = String.format(format, encrypt);

        //
        // 公众平台发送消息给第三方，第三方处理
        //

        // 第三方收到公众号平台发送的消息
        String result2 = pc.decryptMsg(msgSignature, timestamp, nonce, fromXML);
        System.out.println("解密后明文: " + result2);

        //pc.verifyUrl(null, null, null, null);
        return true;
    }
}
