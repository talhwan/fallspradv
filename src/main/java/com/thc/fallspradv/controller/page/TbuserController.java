package com.thc.fallspradv.controller.page;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.thc.fallspradv.util.WebConnect;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RequestMapping("/tbuser")
@Controller
public class TbuserController {
    @GetMapping("/{page}")
    public String page(@PathVariable String page){
        return "tbuser/" + page;
    }

    @GetMapping("/{page}/{id}")
    public String page(@PathVariable String page, @PathVariable String id){
        return "tbuser/" + page;
    }

    @GetMapping("/login/kakao")
    public String kakaoLogin(@RequestParam String code, Model model) throws Exception {
        if(code != null && !code.isEmpty()){
            Map<String,Object> params = WebConnect.getKaKaoToken(code);
            String access_token = (String) params.get("access_token");
            if(access_token != null && !access_token.isEmpty()){
                Map<String,Object> resultParam = WebConnect.getKaKaoInfo(access_token);
                Map<String,Object> kakao_account = (Map<String,Object>) resultParam.get("kakao_account");

                String username = kakao_account.get("email") + "";
                String password = resultParam.get("id") + "";

                System.out.println(kakao_account.get("email") + "");

            }
        }
        return "tbuser/kakao";
    }

    @PostMapping("/login/google")
    public String googleLogin(@RequestParam String credential, Model model){
        HttpTransport transport = null;
        transport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(
                        "client_id값 잘 넣어주시면 되어요"
                ))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        GoogleIdToken idToken = null;
        try{
            idToken = verifier.verify(credential);
        } catch(Exception e){
        }
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            // Print user identifier
            String userId = payload.getSubject();
            //logger.info("payload ID: " + payload);
            String username = payload.get("email") + "";
            String password = payload.get("sub") + "";

            System.out.println(username + "////" + password);
            model.addAttribute("token", username);

        } else {
            System.out.println("Invalid ID token.");
        }
        return "tbuser/google";
    }

}
