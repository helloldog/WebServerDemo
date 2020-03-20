package mine.controller;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import mine.utils.AES;
import mine.utils.commutils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@ServerEndpoint("/deviceWsv")
public class WebSocketServer
{
    private static final Logger log = Logger.getLogger(WebSocketServer.class);

    public static List<Session> sessionPools = new ArrayList();
    public static Map< String,Session> msgandSes = new HashMap();
    public static Map<String, String> snAndVer = new HashMap();

    public void sendMessage(Session session, String message) throws IOException {
        if (session != null)
            session.getBasicRemote().sendText(message);
    }

    @OnOpen
    public void onOpen(Session session)
    {
        sessionPools.add(session);
        snAndVer.put("TEST1","TEST2");
    }

    @OnClose
    public void onClose(Session session)
    {
        for (int i = 0; i < sessionPools.size(); ++i)
            if (((Session)sessionPools.get(i)).equals(session))
                sessionPools.remove(i);
    }

    @OnMessage
    public void onMessage(String message, Session session)
            throws Exception
    {
        String resMsg = null;
        log.info(message + "  " + session.getId());

        if (commutils.isJson(message)) {
            JSONObject result;
            result = JSONObject.parseObject(message);
            String requestId = result.get("requestId").toString();
            String data = result.get("data").toString();
            String Sn = result.get("deviceSn").toString();
            if (message.contains("Conn")) {
                msgandSes.put(Sn, session);
                boolean b=true;
                for (String sn : snAndVer.keySet()) {
                    if(sn.equals(Sn)){
                        b=false;
                    }
                }
                if(b){
                    if(message.contains("version")){
                        String ver = result.get("version").toString();
                        snAndVer.put(Sn,ver);
                    }
                    else {
                        snAndVer.put(Sn,"oldestver");
                    }
                }

                String  ssss= snAndVer.get(Sn);
                if(ssss.equals("oldestver")){
                    String oldmodel="{\n" +
                            "    \"code\":200,\n" +
                            "    \"command\":\"Conn.R\",\n" +
                            "    \"data\":\"\",\n" +
                            "    \"msg\":\"OK\",\n" +
                            "    \"requestId\":\"%s\",\n" +
                            "    \"time\":\"%s\"\n" +
                            "}";
                    resMsg= String.format(oldmodel,requestId,commutils.NowTime());
                }
                else {
                    String oldmodel="{\n" +
                            "    \"code\":200,\n" +
                            "    \"command\":\"Conn.R\",\n" +
                            "    \"data\":\"\",\n" +
                            "    \"msg\":\"OK\",\n" +
                            "    \"requestId\":\"%s\",\n" +
                            "    \"version\":\"%s\",\n" +
                            "    \"time\":\"%s\"\n" +
                            "}";
                    resMsg= String.format(oldmodel,requestId,snAndVer.get(Sn),commutils.NowTime());
                }
              
            }


            if (message.contains("HB")) {
                if(snAndVer.get(Sn).contains("oldestver")){
                    String oldmodel="{\n" +
                            "    \"code\":200,\n" +
                            "    \"command\":\"HB.R\",\n" +
                            "    \"data\":\"\",\n" +
                            "    \"msg\":\"OK\",\n" +
                            "    \"requestId\":\"%s\",\n" +
                            "    \"time\":\"%s\"\n" +
                            "}";
                    resMsg= String.format(oldmodel,requestId,commutils.NowTime());
                }
                else {
                    String oldmodel="{\n" +
                            "    \"code\":200,\n" +
                            "    \"command\":\"HB.R\",\n" +
                            "    \"data\":\"\",\n" +
                            "    \"msg\":\"OK\",\n" +
                            "    \"requestId\":\"%s\",\n" +
                            "    \"version\":\"%s\",\n" +
                            "    \"time\":\"%s\"\n" +
                            "}";
                    resMsg= String.format(oldmodel,requestId,snAndVer.get(Sn),commutils.NowTime());
                }


            }

            if ((!(data.isEmpty())) && (!(data.equals("")))) {
                String DecryptData = AES.decrypt(data, AES.VI(), "F7A0B971B199FD2A");
                log.info("DecryptData = " + DecryptData);
            }
        }

        sendInfo(session, resMsg);
        log.info("给设备回复：" + resMsg);
    }

    @OnError
    public void onError(Session session, Throwable throwable)
    {
        System.out.println("发生错误");
        throwable.printStackTrace();
    }

    public void sendInfo(Session session, String message)
    {
        try {
            sendMessage(session, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean equals(Object o)
    {
        if (o == this) return true; if (!(o instanceof WebSocketServer)) return false; WebSocketServer other = (WebSocketServer)o; return (other.canEqual(this)); }
    protected boolean canEqual(Object other) { return other instanceof WebSocketServer; }
    public int hashCode() { int result = 1; return 1; }
    public String toString() { return "WebSocketServer()";
    }
}