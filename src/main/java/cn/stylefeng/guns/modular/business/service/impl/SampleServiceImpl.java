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
package cn.stylefeng.guns.modular.business.service.impl;

import static cn.stylefeng.roses.kernel.file.api.constants.FileConstants.DEFAULT_BUCKET_NAME;
import static cn.stylefeng.roses.kernel.file.api.constants.FileConstants.FILE_POSTFIX_SEPARATOR;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.guns.modular.business.entity.Sample;
import cn.stylefeng.guns.modular.business.facotry.SampleFactory;
import cn.stylefeng.guns.modular.business.mapper.SampleMapper;
import cn.stylefeng.guns.modular.business.service.SampleService;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.file.api.FileOperatorApi;
import cn.stylefeng.roses.kernel.file.api.exception.FileException;
import cn.stylefeng.roses.kernel.file.api.exception.enums.FileExceptionEnum;
import cn.stylefeng.roses.kernel.file.api.util.DownloadUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 文件信息表 服务实现类
 *
 * @author stylefeng
 * @date 2020/6/7 22:15
 */
@Service
@Slf4j
public class SampleServiceImpl extends ServiceImpl<SampleMapper, Sample> implements SampleService {

    @Resource
    private FileOperatorApi fileOperatorApi;


	@Override
    public Sample getSampleResult(Long fileId) {

        // 查询库中文件信息
   
		Sample sample = new Sample();
		sample = this.getById(fileId);
        return sample;
    }



	@Override
    @Transactional(rollbackFor = Exception.class)
    public Sample uploadFile(MultipartFile file) {

        // 文件请求转换存入数据库的附件信息
		Sample sample =SampleFactory.createSample(file);
     
     

        // 保存附件到附件信息表
        this.save(sample);

      
       
        return sample;
    }

  

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteReally(Sample sample) {

       
        // 批量删除
        this.removeById(sample.getSampleId());

        // 删除具体文件
       
        this.fileOperatorApi.deleteFile(sample.getSampleBucket(), sample.getSampleObjectName());
    }

 
	@Override
    public PageResult<Sample> sampleListPage(Sample sample) {
        Page<Sample> page = PageFactory.defaultPage();
        List<Sample> list = this.baseMapper.SampleList(page,sample);
        return PageResultFactory.createPageResult(page.setRecords(list));
    }


	@Override
    public void packagingDownload(String sampleIds, HttpServletResponse response) {

        // 获取文件信息
        List<Long> sampleIdList = Arrays.stream(sampleIds.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        List<Sample> sampleList = this.baseMapper.getSampleListByFileIds(sampleIdList);

        // 输出流等信息
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(bos);

        try {
            for (int i = 0; i < sampleList.size(); i++) {
                Sample sample = sampleList.get(i);
                if (ObjectUtil.isNotEmpty(sample)) {
                    String fileOriginName = sample.getSampleOriginName();
                   

                    byte[] fileBytes = fileOperatorApi.getFileBytes(DEFAULT_BUCKET_NAME, sample.getSampleObjectName());
                    ZipEntry entry = new ZipEntry(i + 1 + "." + fileOriginName);
                    entry.setSize(fileBytes.length);
                    zip.putNextEntry(entry);
                    zip.write(fileBytes);
                }
            }
            zip.finish();

            // 下载文件
            DownloadUtil.download(DateUtil.now() + "-打包下载" + FILE_POSTFIX_SEPARATOR + "zip", bos.toByteArray(), response);
        } catch (Exception e) {
            log.error("获取文件流异常，具体信息为：{}", e.getMessage());
            throw new FileException(FileExceptionEnum.FILE_STREAM_ERROR);
        } finally {
            try {
                zip.closeEntry();
                zip.close();
                bos.close();
            } catch (IOException e) {
                log.error("关闭数据流失败，具体信息为：{}", e.getMessage());
            }
        }
    }


	@Override
    public List<Sample> getSampleListByFileIds(String fileIds) {
        List<Long> fileIdList = Arrays.stream(fileIds.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
        return this.baseMapper.getSampleListByFileIds(fileIdList);
    }

    
    

    

   

	@Override
    public Sample detail(long sampleId) {
        return this.getSampleResult(sampleId);
    }

	@Override
    @Transactional(rollbackFor = Exception.class)
    public Sample update(MultipartFile file,Sample sample) {
		
		
		Sample sampleSave = new Sample();
		 
		sampleSave = getSampleResult(sample.getSampleId());
		
		if(!file.isEmpty()){
		       // 删除具体文件
		    this.fileOperatorApi.deleteFile(sample.getSampleBucket(), sample.getSampleObjectName());
			
	        // 文件请求转换存入数据库的附件信息
			
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
	        
	        sampleSave.setSampleObjectName(finalFileName);
	        sampleSave.setSampleOriginName(originalFilename);
	        sampleSave.setSamplePath(path);
	        sampleSave.setSampleSuffix(fileSuffix);
	        sampleSave.setSampleSizeKb(fileSizeKb);
	        sampleSave.setUpdateTime(dNow);
			}
	        LoginUser loginUser = LoginContext.me().getLoginUser();
	        
	        sampleSave.setSampleBucket(DEFAULT_BUCKET_NAME);
	       
	        sampleSave.setSampleStatus("0");//0：未分析
	        sampleSave.setUpdateUser(loginUser.getUserId());
	        
	        sampleSave.setUpdateUserName(loginUser.getAccount());
	        sampleSave.setSampleType(sample.getSampleType());
	        sampleSave.setSampleZhName(sample.getSampleZhName());
	        sampleSave.setSampleEnName(sample.getSampleEnName());
	        sampleSave.setSampleIntroduction(sample.getSampleIntroduction());
	        // 保存附件到附件信息表
            this.saveOrUpdate(sampleSave);

      
        // 返回结果
        return sampleSave;
    }


	


   



}
