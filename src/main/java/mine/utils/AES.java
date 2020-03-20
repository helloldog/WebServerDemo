package mine.utils;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AES
{
    private static final Logger log = Logger.getLogger(AES.class);

    public static String encrypt(String data, String vi, String key)
            throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("ASCII"), "AES");
        IvParameterSpec iv = new IvParameterSpec(vi.getBytes());
        cipher.init(1, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
        return new BASE64Encoder().encode(encrypted); }

    public static String VI() throws Exception {
        String VI = null;
        try {
            Date date = new Date();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddyyyyMMdd");
            String nowDate = sf.format(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(sf.parse(nowDate));

            VI = sf.format(cal.getTime());
        }
        catch (Exception localException)
        {
        }

        return VI;
    }

    public static String decrypt(String data, String vi, String key)
            throws Exception
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("ASCII"), "AES");
        IvParameterSpec iv = new IvParameterSpec(vi.getBytes());
        cipher.init(2, skeySpec, iv);
        byte[] buffer = new BASE64Decoder().decodeBuffer(data);
        byte[] encrypted = cipher.doFinal(buffer);
        return new String(encrypted, "UTF-8");
    }

    public static String encryptAES7(String sSrc, String sKey, String ivParameter)
    {
        try
        {
            byte[] iv = ivParameter.getBytes();
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = sSrc.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength += blockSize - (plaintextLength % blockSize);
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(sKey.getBytes("utf-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(1, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            String EncStr = new Base64().encodeAsString(encrypted);
            return new String(EncStr.getBytes(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace(); }
        return null;
    }

    public static String decryptAES7(String sSrc, String sKey, String ivParameter)
    {
        try
        {
            byte[] encrypted1 = new Base64().decode(sSrc);
            byte[] iv = ivParameter.getBytes();
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(sKey.getBytes("utf-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(2, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "UTF-8");
            return originalString.trim();
        } catch (Exception e) {
            e.printStackTrace(); }
        return null;
    }

    public static void main(String[] args)
            throws Exception
    {
        String iv = VI();

        System.out.println(iv);

        String json = "E+tPpEobhZiYm5L8HWhCB2rJmQbujrpwFx8LKjhI7g7Tpon0WL37Jl4GrR6p5J+LtEMcverv+9V5NJW6XaZ3KbMYubu+xZFeTjugXQegkU7t65EkbTwzHfeTEF8iMnPsWNgNQpFXOb+HhZn+2k3b3LObGvBhoJDXG3ershjUvfX4zn8Dm+UXZ+wnpY6tqhp6KZRfFHKD8/xvxX7EWyl5tA7CheYJtdlXAFbVST6yzMA=";
        System.out.println(decrypt(json, iv, "F7A0B971B199FD2A"));
    }
}