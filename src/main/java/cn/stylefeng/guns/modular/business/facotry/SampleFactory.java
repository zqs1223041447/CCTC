/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.guns.modular.business.facotry;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.guns.modular.business.entity.Sample;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.dict.modular.service.DictService;
import cn.stylefeng.roses.kernel.file.api.FileOperatorApi;
import cn.stylefeng.roses.kernel.file.api.enums.FileLocationEnum;
import cn.stylefeng.roses.kernel.file.api.enums.FileStatusEnum;
import cn.stylefeng.roses.kernel.file.api.exception.FileException;
import cn.stylefeng.roses.kernel.file.api.exception.enums.FileExceptionEnum;
import cn.stylefeng.roses.kernel.file.api.pojo.request.SysFileInfoRequest;
import cn.stylefeng.roses.kernel.file.modular.entity.SysFileInfo;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import static cn.stylefeng.roses.kernel.file.api.constants.FileConstants.DEFAULT_BUCKET_NAME;
import static cn.stylefeng.roses.kernel.file.api.constants.FileConstants.FILE_POSTFIX_SEPARATOR;

/**
 * 文件信息组装工厂
 *
 * @author fengshuonan
 * @date 2020/12/30 22:16
 */
public class SampleFactory {

    /**
     * 封装附件信息
     *
     * @author majianguo
     * @date 2020/12/27 12:55
     */
    public static Sample createSample(MultipartFile file) {

        FileOperatorApi fileOperatorApi = SpringUtil.getBean(FileOperatorApi.class);

        // 生成文件的唯一id
        Long fileId = IdWorker.getId();

        // 获取文件原始名称
        String originalFilename = file.getOriginalFilename();

        // 获取文件后缀（不包含点）
        String fileSuffix = null;
        if (ObjectUtil.isNotEmpty(originalFilename)) {
            fileSuffix = StrUtil.subAfter(originalFilename, FILE_POSTFIX_SEPARATOR, true);
        }

        // 生成文件的最终名称
        String finalFileName = fileId + FILE_POSTFIX_SEPARATOR + fileSuffix;

        // 存储文件
        byte[] bytes;
        try {
            bytes = file.getBytes();
         
            fileOperatorApi.storageFile(DEFAULT_BUCKET_NAME, finalFileName, bytes);
        } catch (IOException e) {
            throw new FileException(FileExceptionEnum.ERROR_FILE, e.getMessage());
        }

        
        // 计算文件大小kb
        long fileSizeKb = Convert.toLong(NumberUtil.div(new BigDecimal(file.getSize()), BigDecimal.valueOf(1024)).setScale(0, BigDecimal.ROUND_HALF_UP));
        Date dNow = new Date( );
        
        
       
        String path = "C:\\CuckooFile" + File.separator + DEFAULT_BUCKET_NAME + File.separator + finalFileName;
        // 封装存储文件信息（上传替换公共信息）
        Sample sample = new Sample();
        LoginUser loginUser = LoginContext.me().getLoginUser();
        
        sample.setSampleBucket(DEFAULT_BUCKET_NAME);
        sample.setSampleObjectName(finalFileName);
        sample.setSampleOriginName(originalFilename);
        sample.setSamplePath(path);
        sample.setSampleSuffix(fileSuffix);
        sample.setSampleSizeKb(fileSizeKb);
        sample.setSampleStatus("0");//0：未分析
        sample.setCreateUser(loginUser.getUserId());
        sample.setCreateTime(dNow);
        sample.setCreateUserName(loginUser.getAccount());
        // 返回结果
        return sample;
    }

}
