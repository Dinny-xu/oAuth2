package com.study.oauth2.util;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.experimental.UtilityClass;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

import java.util.List;


/**
 * @program: poney
 * @description: token 解密
 * @author: Xu·yan
 * @create: 2021-01-11 09:05
 **/
@UtilityClass
public class TokenDecryptUtil {

    //公钥
    private static final String PUBLIC_KEY = "public.key";

    public static String getUserId(String accessToken) {
        String publicKey = getPublicKey();
        Jwt jwt = JwtHelper.decodeAndVerify(accessToken, new RsaVerifier((publicKey)));
        String claims = jwt.getClaims();
        JSONObject object = JSONUtil.parseObj(claims);
        return object.getStr("userId");
    }


    private static String getPublicKey() {
        FileReader fileReader = new FileReader(PUBLIC_KEY);
        List<String> lines = fileReader.readLines();
        return StrUtil.join("\n", lines);
    }
}
