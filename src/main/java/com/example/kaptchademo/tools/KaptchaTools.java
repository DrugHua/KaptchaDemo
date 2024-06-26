package com.example.kaptchademo.tools;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author 花建文
 * @date 2024/6/12 9:42
 */
@Component
public class KaptchaTools {

    @Autowired
    ValueOperations redisValueOperations;

    @Autowired
    DefaultKaptcha defaultKaptcha;
    /**
     * 生成验证码
     * 1.使用Kaptcha获取到验证码的字符存于kaptchaText、图片存于BufferedImage
     * 2.图片转换成Base64的方式传递给前端
     * 3.kaptchaText放在Redis中，60s有效，使用UUID作为Redis的Key
     */
    public Map<String, String> codeByBase64() {
        String kaptchaText = defaultKaptcha.createText();
        BufferedImage image = defaultKaptcha.createImage(kaptchaText);

        String base64Code = "";
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            base64Code = Base64.encodeBase64String(outputStream.toByteArray());
        } catch (Exception e) {
            System.out.println("verificationCode exception: ");
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    System.out.println("verificationCode outputStream close exception: ");
                }
            }
        }

        //uuid; 唯一标识code
        //code; 验证码图片的Base64串
        Map<String, String> kaptchaVoMap = new HashMap<>();
        String uuid = UUID.randomUUID().toString();
        kaptchaVoMap.put("uuid", uuid);
        kaptchaVoMap.put("code", "data:image/png;base64," + base64Code);
        redisValueOperations.set(uuid, kaptchaText, 60L, TimeUnit.SECONDS);
        return kaptchaVoMap;
    }

}
