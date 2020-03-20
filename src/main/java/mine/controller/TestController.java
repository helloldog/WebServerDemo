package mine.controller;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import mine.utils.AES;
import mine.utils.SavePic;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static mine.controller.WebSocketServer.msgandSes;
import static mine.controller.WebSocketServer.snAndVer;

@RestController
public class TestController
{
    private static final Logger log = Logger.getLogger(TestController.class);

    @Value("${test.server.address}")
    private String severaddress;

    @Value("${test.file.path}")
    private String testfilepath;

    @PostMapping({"/SendToDev"})
    @ResponseBody
    public String getShopInJSON(@RequestBody String request) throws UnsupportedEncodingException {
        String myrespone = null;
        WebSocketServer s = new WebSocketServer();
        JSONObject  result = JSONObject.parseObject(new String(request.getBytes(), "UTF-8"));
        if (!(snAndVer.isEmpty())&& !(msgandSes.get(result.get("deviceSn")) ==null)) {
                s.sendInfo(msgandSes.get(result.get("deviceSn")), new String(request.getBytes(), "UTF-8"));
                log.info("消息发送成功："+new String(request.getBytes(), "UTF-8"));
            myrespone = "发送成功！";
        }
        else {
            myrespone = String.format("设备%s没连接！",result.get("deviceSn"));
            log.info(myrespone);
        }
        return myrespone;
    }

    @RequestMapping({"/geturl"})
    public String url()
    {
        return this.severaddress;
    }

    @PostMapping({"/uploadFile.htm"})
    public String uppic(@RequestParam("bigImage") MultipartFile fileBig, @RequestParam(name="littleImage", required=false) MultipartFile fileMin)
    {
        if (fileMin == null) {
            return SavePic.SaveOnePic(fileBig, this.testfilepath);
        }

        if ((SavePic.SaveOnePic(fileBig, this.testfilepath).equals("{\"state\":0}")) && (SavePic.SaveOnePic(fileMin, this.testfilepath).equals("{\"state\":0}"))) {
            return "{\"state\":0}";
        }

        return "{\"state\":1}";
    }

    @PostMapping({"/Decrypt"})
    @ResponseBody
    public String decryptAES7(@RequestBody String request)
            throws Exception
    {
        String AfterdecryptAES7 = AES.decryptAES7(request, "F7A0B971B199FD2A", AES.VI());
        System.out.println(AfterdecryptAES7);
        return AfterdecryptAES7;
    }
}