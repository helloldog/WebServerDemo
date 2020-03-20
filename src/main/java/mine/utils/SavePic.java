package mine.utils;

import java.io.File;
import java.io.FileOutputStream;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

public class SavePic
{
    private static final Logger log = Logger.getLogger(SavePic.class);

    public static String SaveOnePic(MultipartFile file, String testfilepath)
    {
        String pname = file.getOriginalFilename();
        log.info("收到图片上传请求:" + pname + "开始处理....");
        FileOutputStream fos = null;
        String pathName = testfilepath;
        File file2 = new File(pathName);
        if ((!(file2.exists())) && (!(file2.isDirectory())))
            file2.mkdir();
        try
        {
            pathName = pathName + pname;
            fos = new FileOutputStream(pathName);
            fos.write(file.getBytes());
            log.info(pname + "文件处理成功，给设备返回：{\"state\":0}");
            return "{\"state\":0}";
        } catch (Exception e) {
            e.printStackTrace();
            log.info("文件处理失败，给设备返回：{\"state\":1}");
            return "{\"state\":1}";
        } finally {
            try {
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}