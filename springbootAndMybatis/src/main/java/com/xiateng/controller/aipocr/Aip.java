package com.xiateng.controller.aipocr;

import Utils.Access_token;
import Utils.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.shade.org.apache.commons.codec.binary.Base64;
import com.yeepay.shade.org.apache.tika.io.IOUtils;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class Aip {
    private static final Logger logger = LoggerFactory.getLogger(Aip.class);
    //文字识别应用信息  设置APPID/AK/SK
    public static final String APP_ID = "16249045";
    public static final String API_KEY = "b3xPgQ7nNYzHFmozPMjprcWt";
    public static final String SECRET_KEY = "P9GBdqh31vLqW5BM2ixoj2K4FbuVu92d";

    //图像识别应用信息 设置APPID/AK/SK
    public static final String APP_ID_PLANT = "16257221";
    public static final String API_KEY_PLANT = "zDU53CA5D8z9m0ZWFIIbdsSy";
    public static final String SECRET_KEY_PLANT = "EAmOGR9Cgs6D3DWg3OmtVNAGmQV07zna";

    /**
     * 获取百度AI应用数据.
     *
     * @param map map
     * @return message
     */
    @CrossOrigin
    @RequestMapping(value = "/baidu", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map getAi(Model map) {
        Map<String,Object> map1 = new HashMap<>();
        // 获取token
        String auth = Access_token.getAuth(1);
        logger.info("auth === "+ auth);
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate";
        String result ="";
        try {
            //读取本地图片输入流
            FileInputStream inputStream = new FileInputStream("F:/che.png");
            String base = Base64.encodeBase64String(IOUtils.toByteArray(inputStream));
            result = HttpUtil.post(url, auth, base);
            String license = JSONObject.parseObject(result).getJSONObject("words_result").getString("number");
            if (!Strings.isNullOrEmpty(license)) {
//                map.addAttribute("license", license);
                map1.put("license",license);
            } else {
                map1.put("license","无法识别");
            }
        } catch (Exception e) {
            map1.put("result",result);
            map1.put("license","异常哦！");
        }


        return map1;
    }


    /**
     * 获取百度AI应用数据.识别车牌号
     *
     * @return message
     */
    @CrossOrigin
    @RequestMapping(value = "/baidu1", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map getAisss() {
        Map<String,Object> map1 = new HashMap<>();
        // 获取token
        String accessToken = Access_token.getAuth(1);
        // 通用识别url
        String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
        try {

//            byte[] imgData = FileUtil.readFileByBytes(filePath);
            //读取本地图片输入流
            FileInputStream inputStream = new FileInputStream("F:/ch1.png");
            String base = Base64.encodeBase64String(IOUtils.toByteArray(inputStream));
            logger.info(base);
//            String imgStr = Base64Util.encode(base);
            String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(base, "UTF-8");
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
//            String accessToken = "#####调用鉴权接口获取的token#####";
            String result = HttpUtil.post(otherHost, accessToken, params);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray words_result = (JSONArray) jsonObject.get("words_result");
            JSONObject o = (JSONObject)words_result.get(0);
            map1.put("lincens",o.get("words"));
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map1;
    }



    /**
     * 获取百度AI应用数据.识别身份证
     *
     * @param map map
     * @return message
     */
    @CrossOrigin
    @RequestMapping(value = "/baidu2", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map getAisss1111(Model map) {
        Map<String,Object> map1 = new HashMap<>();
        // 获取token
        String accessToken = Access_token.getAuth(1);
        // 通用识别url
        String otherHost = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
        try {
            //读取本地图片输入流
            FileInputStream inputStream = new FileInputStream("F:/zheng.JPG");
            String base = Base64.encodeBase64String(IOUtils.toByteArray(inputStream));
            logger.info(base);
            // 识别身份证正面id_card_side=front;识别身份证背面id_card_side=back;
            String params = "id_card_side=front&" + URLEncoder.encode("image", "UTF-8") + "="
                    + URLEncoder.encode(base, "UTF-8");
//            String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(base, "UTF-8");
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
            String result = HttpUtil.post(otherHost, accessToken, params);
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            JSONArray words_result = (JSONArray) jsonObject.get("words_result");
//            JSONObject o = (JSONObject)words_result.get(0);
            map1.put("isCar",result);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map1;
    }

    /**
     * 获取百度AI应用数据.识别花草
     *
     * @param map map
     * @return message
     */
    @CrossOrigin
    @RequestMapping(value = "/baidu3", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map getAisssBaidu3(Model map) {
        Map<String,Object> map1 = new HashMap<>();
        // 获取token
        String accessToken = Access_token.getAuth(2);
        // 通用识别url
        String otherHost = "https://aip.baidubce.com/rest/2.0/image-classify/v1/plant";
        try {
            //读取本地图片输入流
            FileInputStream inputStream = new FileInputStream("F:/zhiwu.png");
            String base = Base64.encodeBase64String(IOUtils.toByteArray(inputStream));
            logger.info(base);
            String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(base, "UTF-8");
            /**
             * 线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
             */
            String result = HttpUtil.post(otherHost, accessToken, params);
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            JSONArray words_result = (JSONArray) jsonObject.get("words_result");
//            JSONObject o = (JSONObject)words_result.get(0);
            map1.put("zhiwu",result);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return map1;
    }
}
