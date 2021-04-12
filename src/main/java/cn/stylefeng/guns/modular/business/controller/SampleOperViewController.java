package cn.stylefeng.guns.modular.business.controller;

import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import org.springframework.stereotype.Controller;

/**
 * 文件管理界面
 *
 * @author lgq
 * @date 2021/1/9
 */
@Controller
@ApiResource(name = "文件管理界面")
public class SampleOperViewController {

    /**
     * 文件管理首页
     *
     * @author lgq
     * @date 2021/1/9
     */
    @GetResource(name = "文件管理首页", path = "/view/sample")
    public String fileIndex() {
        return "modular/business/sample_oper/sample.html";
    }

    /**
     * 文件详情页面
     *
     * @author lgq
     * @date 2021/1/9
     */
    @GetResource(name = "文件详情页面", path = "/view/sampleDetails")
    public String details() {
        return "modular/business/sample_oper/sample_details.html";
    }

}
