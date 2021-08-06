package com.example.qrdemo.controller;

import com.example.qrdemo.util.WxUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author qzz
 */
@RestController
@RequestMapping("/wx")
public class WxController {

    /**
     * 获取小程序码
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getWxQRCode", method = { RequestMethod.POST })
    public Map<String, Object> getWxQRCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //参数
        String sceneStr="m_id=1";
        //跳转页面
        String pageUrl="pages/index/index";
        //调用微信开放接口wxacode.getUnlimited获取小程序码
        return WxUtil.getWXAcode2(sceneStr, pageUrl);
    }
}
