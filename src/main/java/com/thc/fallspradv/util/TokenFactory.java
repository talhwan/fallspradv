package com.thc.fallspradv.util;

import java.util.Arrays;

public class TokenFactory {
    String temp_key = "12345678901234567890123456789012";
    String prefix = "Bearer/u0020";
    int intervalRefreshToken = 1209600;
    int intervalAccessToken = 1800;

    public String issueRefreshToken(String tbuserId){
        return generateToken(tbuserId, intervalRefreshToken);
    }
    public String issueAccessToken(String refreshToken) throws Exception {
        return generateToken(verifyToken(refreshToken), intervalAccessToken);
    }

    public String generateToken(String claim, int second){
        String returnVal = "";
        // 저장해야 하는 정보
        // 유저 아이디값, 만료일.
        long period = 0;
        AES256Cipher aes = new AES256Cipher();
        try{
            NowDate now = new NowDate();
            String due = now.getDue(second);
            System.out.println("claim : " + claim);
            System.out.println("due : " + due);
            returnVal = aes.AES_Encode(temp_key, claim + "_" + due);
        } catch (Exception e){
            System.out.println("error....");
        }
        return prefix + returnVal;
    }
    public String verifyToken(String token) throws Exception {
        String returnVal = "";
        AES256Cipher aes = new AES256Cipher();
        try{
            if(!token.startsWith(prefix)){
                throw new Exception("token does not start with prefix");
            }
            token = token.substring(prefix.length());
            System.out.println("token : " + token);
            // id_만료일
            returnVal = aes.AES_Decode(temp_key, token);
        } catch (Exception e){
            System.out.println("error....");
        }
        if("".equals(returnVal)){
            throw new Exception("....");
        }

        String[] arrayVal = returnVal.split("_");
        String tbuserId = arrayVal[0];
        String due = arrayVal[1];
        NowDate nowDate = new NowDate();
        String now = nowDate.getNow();
        System.out.println("due : " + due);
        System.out.println("now : " + now);

        String[] arrayNow = {due, now};
        Arrays.sort(arrayNow);
        if(due.equals(arrayNow[1])){
            //정상!
            return tbuserId;
        } else {
            //만료!
            throw new Exception("due....");
        }
    }
}
