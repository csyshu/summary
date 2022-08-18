package com.csy.summary.daily.qrcode.method2;

import com.google.zxing.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * <p>
 * QrCodeTest.java
 * </p>
 *
 * @author XinLau
 * @version 1.0
 * @since 2019年4月19日 下午2:06:38
 */
public class QrCodeTest {

    private static final Logger log = LoggerFactory.getLogger(QrCodeTest.class);

    /**
     * <p>
     * main
     * 测试类
     * </p>
     *
     * @author XinLau
     * @since 2019年2月12日下午5:36:45
     */
    public static void main(String[] args) {
        try {
            CreateQrCode createQrCode = new CreateQrCode();
            //	图片存放地址
            String projectPath = new File("").getAbsolutePath();
            log.info("工程路径:{}", projectPath);
            String path = projectPath + "/QrCode";
            log.info("二维码存放路径:{}", path);
            //	路径不存在就创建此路径
            File dir = new File(path);
            if (!dir.exists()) {
                if (dir.mkdirs()) {
                    log.info("创建成功");
                }
            }
            path += "/" + "20220818200159" + ".png";
            //	图片显示的内容
            String text = "技术委员会";

            //	Logo图片地址加名称
            String logoPath = projectPath + "/src/main/resources/logo.png";

            //	二维码
//            createQrCode.createQrCode(path,text,300,300);

            //	Logo
//            createQrCode.createLogoQrCode(path,text,300,300,logoPath);

            //	文字
            createQrCode.createWordQrcode(path, "", text, 400, 470);

            //  Logo+文字
            createQrCode.createWordLogoQrcode(path, "", text, 400, 470, logoPath);

            //System.out.println(File.separator)
            //解析二维码
            Result result = createQrCode.readQrCode(path);
            log.info("读取二维码：{}", result.toString());
            log.info("二维码格式：{}", result.getBarcodeFormat());
            log.info("二维码内容：{}", result.getText());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}