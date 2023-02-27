package com.wanglj.lmm.common.base.util;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Ase加密
 */
public class AesUtil {

    private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";//默认的加密算法

    public static void main(String[] args) {
/*        String content = "{\n" +
                "  \"username\": \"admin\",\n" +
                "  \"password\": \"123456\"\n" +
                "}"; */

        String content = "{\n" +
                "  \"patientName\": \"王五\"\n" +
                "}";
        //reqEncodeKey: B6vUtiNXv9Op17fN  //请求参数加解密
        //respEncodeKey: kxJnG5237F2Y0ASi //返回参数加解密
        String keyStr = "B6vUtiNXv9Op17fN";

        String encryptStr = encrypt(content, keyStr);
        System.out.println(encryptStr);

        //String decryptStr = decrypt("A4rFbyRxY2SB6Hfygl5LH/f/oSj/baclLTunU29Mu1AkQQd2O9+6WIHbNx/J3MOBaP/9YHsN/yJFEo2VWc/KHA==", keyStr);
        String decryptStr = decrypt("Yds/5eMBqlrkFsIwDXv+eYcHKvRbpbaoctO8OsBrrHi29L3toWz/MtFMcxypFY+URJj3JYsDAdQjzjTjMvj7kaeLZqoJM0160rJMH1hAinwSDB7arw8WLWRmj5N04IMku3VZStvRNjWmRYKj74Nsuhmp/TXpMqpHGRImmCLo2uE/NOnYaBruV56p59ktecASfKUj1WrazuHwKPgt6wtJlsl3TO+rc+CC5STkarNIh9xvFfqmLwZ0BLac0PHVtnvEpui8SUjHIYkBLduIfMd+a7zRMla+SK0Azq2n+tK38hm0sw9ZuKm3V98i/9EgulppXM2thl27YKAVEhX3tdGRAM5PeKEDHvIGCA7D1ONQ42A9G6+hvRYR1tm7EA0Z7jQGjDPu4ZadtOmc3QTXTehDg+WAjZ7BTVMC+T9EEBQJp8SOhifTCQZ2ndlkAA61FL0ma7JYnYRSVXLvaX4eT0u5rd82SmiUnClMsJmoqrz/MXw8PIA44cChVHpAi9DEm2WRYk7Yt5mz6eytFMBMa+mr9HC/Bk6pabkVYcciilaeh+QhW1Pxc0HIHfNCnzWOtFrZRBp7pMGoJDEiqH06B29Sd8os1gprarlS7RsN039VXBiEcrhPZWCvREAJOyUK9cKDy9jqVw0+BDiTo3V9l++iUL0s/BpRN9ZAKe/A9NjpStFhCCY9WLD2Cib9/40WG+RdT29hXaMDwbs60a8dMQ53aa84sx6DDeiCkm1jCJvBfWFvNZ0dKWhOCQ6XRY/SddNkdhqT8byrhBw35005ATXtYAAWx+dmUJaGRecAFF8REsElRv2wczWr6vnys62zhEwI3rn407QWt86JpVZLuJJrsRSK0yHpxpmaFJ6G3NWFGjGB/gaxuo5fOh95jmSi0BJjNKplzcjN8Jjep0KRiu29UdbVHtPZF7fKT+OTxJiBK5D2a3GklED+ENDDpui5Hs6aQrSiBaNwun2WZ6Cdo/D7HSmLjr94+o6pX5E1CyAYvPeP4O3LC732GlQ6hs1op5acooHJsB4U3Yli7coPmctsU51hzTJAEf15OlPy4eW5An2DK2C6kBPiUtJijoVZqYpLKQNx3qpmrUCkgwlp9ucqkzTApC2zWJH8aMGZFjZ4va2CB9ihObheXUVFM+CstcSeWjXVs6RRxbU9++d1CGyY9K5gROuFOvWm3WREBxLovp7bWmYymAJfSa1z4RS1r7lO03E3W2J5qPXUN/xvMQvbaiIeivkr7AiT7Y+ebmxESHaMHrxViMq9tdfbp5fInAs80d0J5bIB2rYupVqdNcywaA7C/AddmXHHfuPdzn55wL9hFb3KL57RyKVntEjAeBcguK/G5qniXTouWK+ts1PP+oIMXxGZ31H1vxH+gwThSl5Pa69ZprEHxnfwjYecyFonJRKyTrHTGY9Zb8LBHJ74EGO9eU8ONS95I2mkjps0ABetWPBDigpQHysQyw9LNYk0QnUQJaAPfeE8zN8zb/NTO4+zywMRpsKHjMkX3cZKbUH+Mxr529NTtDUGm9bela2kISXiaaHziyt53BXMdHdXC+g+OpkKO3AWTZErhtM43WYEof63ZhkoUbOfsQQ+vs+qGGAdQ7Y8ppVyHWNs7LTbHUhWOSvpDmiplyDBdzDa7qvS1EU1rjXw5mc/ZN9uyih/EAIgukF7KXNyLRVlXqGTYexMHiYfKdc5KLz1n8Wj8a9He5McyzWhKze2ykxCDr5dLpRFQDa0I6EIqyb+1xqwPA/PTFc8CPPY2p+ZS/D1mCpW7KC8z7s/+HOMmXk2GQ4yYfQnf8ymSE6tpH+kAzQS3T/QAG1I+2GU/WuwFZjXp3v7Q9QwMdD4NRpo24iEY/nq/vhuLHkoRuQGmZt4qAH9kbPZMbB5+uxHx2ud0EJVBj9efXZMddoEPdrLHcz0PZGa3sCphR9EV2ehaAw+158oSUQOrugz/zkomTwiExeJrM1Sl8XCSIEKTUWNTmXPvdu2UoM2usVYpg9shzxS116l5iYRa5iY+GS95axD6pQYBWssMIlOvG5Qj1XscbZ82AF/gsDOHELY+wx0hkFHl0sKAo6iFXtokbeeYvzvrNZJQBAfLWDnVSjyGI/PVYkvfppZV50RZVocwQtGc83FU/IF8WgKhFcMX2AjJJ6nZxKbFwc1AE8hyfMIt8KUxYoXsZg7fP5wK6/62Pn4dqixD76In2Q37O+QfTWjf8Nl9sGcKsS2WKdbye+nMVUBhdR8uc0NORGX30gWU4xaY5yRq99AIodZ+U7zVWqMYP+ICsGBJH5XbDqjhnmG3q3ItggWMu0jfCDWrK6ea8nfq7OyFsB52TE286GI+ewCSiJWgWLyG0UMlKpovBp/b6cBrvwUmiBkKD+1o7kkY6F+5l4xxcSxkKnI6MIhPImLx41GX+QurieD5XGUO9SRQs3ajeYtnOv/AtxDHVj8DyifJqeCd5tA5FfF2+N1WDjT7Rb8HMPNjo/smfAvUJ2f2GYHhpaZD+C+1IOQtwJdY97uPAyedH9SAUDpKwhByrEjqOMDqFZm+ggZ8DSu3evPs7izdk6Nj8tqDRlAOJ4dpDC7ctH+zarAV3f/XlIXhCwvHyDZWgdg1rWLEPpbAqlGOrrE0d3MHOdhtdrAVF0sjg56Qdkn7QN6/Ab7I7wRAwDmoS9ZxpFUOL1He5McyzWhKze2ykxCDr5dl7wP7JwyMVfGZl9cO3KaPippm1+zDOT+mCSfCaVi/58dYVwkyHikqT5ceR4JelUvPt54DnjXgCz+5O7UOW2lpG6MLJGKeuj49dC2IVgdD63C5H/PtKKoYeFUC2vjR8I7xXgNl6r7PUQZZumBiM0UoNTkXCSe48i0R5u0mlHgqQGrOqG1fULsFXiRKHgYUip4b0H9/thkPxHwfzV2NIsEnYHB5PmE35HOkhnXWAX0mDTa27H3yiK7BWw17OEYxry88syo/gPevee/bXaSxv6B/Vv8CHMK7axQBJxh2FCLLWDsu3SwEEjl67efEekMsd3X9g35bUkAf7+ZBjHt/pYD09sDeN3pgSofgc4KCn14B6PU3grNC1ncAMYZKWvojNqbABJBJJsm3Sp4wgzMtqUgmneoz8EYOhhpY00Uv58cQaPDhntmotIhOP8v0R8TzObM7FsdJqngOpxUjPqXD0K9ksAaHTU+jxZW5H/Bxl5hP8b/QqguTuGtDbqRooM7u4wj7dCAHX+ptDiDlPeF3OU5kxDuHcGkGiBpPRWUpflJf29jhT6fRZZ8qgxXDxzVIoUkgPLlJnjMYCQJ7mn55rOykKqPT5gftW8AWvGUsrmSVYTHx22kdRAC+ZvWUsNJNXPtuYCQFOlccYpQU0Q7Wl1do0tJ1dmxsKbsR02UG6JZf6Z6IktP5PZhifr3pOx1lUnM04NV9bcwvGa+yJeFzY5gJWpuv4WH/SW27GLLv5Ah23vctyYx9KBRCg3sYXClQKF53omFX9BlIhmzIBptbwKyTIhqh5n4BvnBbj/wxgm1ChMvJcxZ1UhZcf0+gIYmnlJdgzclaIpcnl1s/hzLlVORjIpjThRnq+BGESzx28QyfvVEG/34+L53uf/izn70ZM9iW4Q6ROVdCDrJZrnhrXiEpGGAl9w10CpSQBSOfs8tJtwPO5SAUeyvRlkAIP0zed+f5O4YbUAJIr2lg9zAEMBGcbYnuqFWxoU94s59O7aARPCIAbJ8DpsROrBiIhxfBomy", "kxJnG5237F2Y0ASi");
        System.out.println(decryptStr);
    }

    /**
     * AES 加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));// 初始化为加密模式的密码器
            byte[] result = cipher.doFinal(byteContent);// 加密
            return Base64Utils.encodeToString(result);
        } catch (Exception ex) {
            Logger.getLogger(AesUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * AES 解密操作
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt(String content, String password) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            //执行操作
            byte[] result = cipher.doFinal(Base64Utils.decodeFromString(content));
            return new String(result, "utf-8");
        } catch (Exception ex) {
            Logger.getLogger(AesUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    /**
     * AES 解密操作,不能解密则原内容返回
     *
     * @param content
     * @param password
     * @return
     */
    public static String decrypt2Orig(String content, String password) {
        try {
            //实例化
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            //使用密钥初始化，设置为解密模式
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
            //执行操作
            byte[] result = cipher.doFinal(Base64Utils.decodeFromString(content));
            return new String(result, "utf-8");
        } catch (Exception ex) {
            Logger.getLogger(AesUtil.class.getName()).log(Level.SEVERE, "解密错误：" + content + "  " + ex.getMessage());
        }

        return content;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     */
    private static SecretKeySpec getSecretKey(final String password) {
        //返回生成指定算法密钥生成器的 KeyGenerator 对象
        /*KeyGenerator kg = null;
        try {
            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
            //AES 要求密钥长度为 128
            kg.init(128, new SecureRandom(password.getBytes()));
            //生成一个密钥
            SecretKey secretKey = kg.generateKey();
            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(AseUtil.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        try {
            return new SecretKeySpec(Arrays.copyOf(password.getBytes("utf-8"), 16), KEY_ALGORITHM);// 转换为AES专用密钥
        } catch (UnsupportedEncodingException e) {
            Logger.getLogger(AesUtil.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}