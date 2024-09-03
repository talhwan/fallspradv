package com.thc.fallspradv.controller.page;

import com.thc.fallspradv.util.FileUpload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

@RequestMapping("")
@Controller
public class DefaultController {

    @GetMapping({"","/","/index"})
    public String index(){
        return "index";
    }

    @GetMapping("/doc")
    public String getSwagger() {
        return "redirect:/swagger-ui/index.html";
    }

    @GetMapping("/home") public String home(){ return "home"; }
    @GetMapping({"/admin"}) public String admin(){ return "admin"; }


    @GetMapping("/agree")
    public String agree(){
        return "agree";
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @ResponseBody
    @RequestMapping(value = "/uploadfile/{file_name:.+}", method = {RequestMethod.GET,RequestMethod.POST})
    public byte[] getImage(@PathVariable("file_name") String file_name, HttpServletRequest request) throws Exception {
        //logger.info("file_name : " + file_name);
        String root_path = FileUpload.path(request);
        //logger.info("root_path : " + root_path);
        byte[] return_byte = null;
        //해당 이미지를 byte[]형태로 반환
        File file = new File(root_path + file_name);
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            return_byte = IOUtils.toByteArray(in);
        } catch (FileNotFoundException e) {
            //logger.info("FileNotFoundException / file_name : " + file_name);
            //e.printStackTrace();
        } catch (IOException e) {
            //logger.info("IOException / file_name : " + file_name);
            //e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    //logger.info("final Exception / file_name : " + file_name);
                }
            }
        }
        return return_byte;
    }
    @ResponseBody
    @RequestMapping(value = "/download/{file_name:.+}", method = RequestMethod.GET)
    public void download(@PathVariable("file_name") String file_name, @RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //logger.info("download 호출 성공 map : " + file_name);

        String root_path = FileUpload.path(request);
        //logger.info("root_path : " + root_path);
        File file = new File(root_path + file_name);

        //여기는 response 에 설정해주는 부분인데, 어려우면 당분간은 패쓰!!
        String mimeType= URLConnection.guessContentTypeFromName(file_name);		//--- 파일의 mime타입을 확인합니다.
        if(mimeType==null){			//--- 마임타입이 없을 경우 application/octet-stream으로 설정합니다.
            mimeType = "application/octet-stream";
        }
        response.setContentType(mimeType);	//--- response에 mimetype을 설정합니다.
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment; filename=\""+ URLEncoder.encode(file.getName(), "utf-8") +"\"");
        //

        InputStream inputStream = new BufferedInputStream(new FileInputStream(file));	//--- inputstream 객체를 얻습니다.
        FileCopyUtils.copy(inputStream, response.getOutputStream());		//--- inputstream으로 파일을 읽고 outputsream으로 파일을 씁니다.
    }

}
