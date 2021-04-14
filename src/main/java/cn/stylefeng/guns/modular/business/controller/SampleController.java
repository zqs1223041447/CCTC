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
package cn.stylefeng.guns.modular.business.controller;

import cn.stylefeng.guns.modular.business.entity.Sample;
import cn.stylefeng.guns.modular.business.pojo.sampleRequest;
import cn.stylefeng.roses.kernel.file.api.constants.FileConstants;
import cn.stylefeng.roses.kernel.file.api.pojo.request.SysFileInfoRequest;
import cn.stylefeng.roses.kernel.file.api.pojo.response.SysFileInfoResponse;
import cn.stylefeng.roses.kernel.file.modular.service.SysFileInfoService;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.rule.util.HttpServletUtil;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.stylefeng.guns.modular.business.service.SampleService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author majianguo
 * @date 2020/12/27 13:39
 */
@RestController
@ApiResource(name = "样本管理相关接口")
public class SampleController {

	@Resource
	private SampleService sampleService;

	/**
	 * 上传文件
	 *
	 * @author majianguo
	 * @date 2020/12/27 13:17
	 */
	@PostResource(name = "上传文件", path = "/Sample/upload", requiredPermission = false)
	public ResponseData upload(@RequestPart("file") MultipartFile file, @Validated(Sample.class) Sample sample) {
		Sample fileUploadInfoResult = this.sampleService.uploadFile(file);
		return new SuccessResponseData(fileUploadInfoResult);
	}

	/**
	 * 根据附件IDS查询附件信息
	 *
	 * @param fileIds 附件IDS
	 * @return 附件返回类
	 * @author majianguo
	 * @date 2020/12/27 13:17
	 */
	@GetResource(name = "根据附件IDS查询附件信息", path = "/Sample/getSampleListByFileIds", requiredPermission = false)
	public ResponseData getSampleListByFileIds(@RequestParam(value = "sampleIds") String sampleIds) {
		List<Sample> list = this.sampleService.getSampleListByFileIds(sampleIds);
		return new SuccessResponseData(list);
	}

	/**
	 * 删除文件信息（真删除文件信息）
	 *
	 * @author fengshuonan
	 * @date 2020/11/29 11:19
	 */
	@PostResource(name = "删除文件信息（真删除文件信息）", path = "/Sample/deleteReally", requiredPermission = false)
	public ResponseData deleteReally(@RequestBody @Validated(Sample.class) Sample sample) {
		this.sampleService.deleteReally(sample);
		return new SuccessResponseData();
	}

	/**
	 * 分页查询文件信息表
	 *
	 * @author fengshuonan
	 * @date 2020/11/29 11:29
	 */
	@GetResource(name = "分页查询文件信息表", path = "/Sample/sampleListPage", requiredPermission = false)
	public ResponseData fileInfoListPage(Sample sample) {
		return new SuccessResponseData(this.sampleService.sampleListPage(sample));
	}

	/*    *//**
			 * 确认替换附件
			 * <p>
			 * 在替换接口替换文件以后，需要调用本接口替换操作才会生效
			 *
			 * @author majianguo
			 * @date 2020/12/27 13:18
			 *//*
				 * @PostResource(name = "确认替换附件", path = "/Sample/confirmReplaceFile",
				 * requiredPermission = false) public ResponseData
				 * confirmReplaceFile(@RequestBody List<Long> fileIdList) {
				 * this.sysFileInfoService.confirmReplaceFile(fileIdList); return new
				 * SuccessResponseData(); }
				 */

	/**
	 * 查看详情文件信息表
	 *
	 * @author fengshuonan
	 * @date 2020/11/29 11:29
	 */
	@GetResource(name = "查看详情文件信息表", path = "/Sample/detail", requiredPermission = false)
	public ResponseData detail(@RequestParam(value = "sampleId") String sampleId) {
		long id = Long.valueOf(sampleId).longValue();
		return new SuccessResponseData(sampleService.detail(id));
	}
	/**
	 * 更新文件
	 *
	 * @author majianguo
	 * @date 2020/12/27 13:17
	 */
	@PostResource(name = "更新样本", path = "/Sample/update", requiredPermission = false)
	public ResponseData update(@RequestPart("file") MultipartFile file, Sample sample) {
		Sample fileUploadInfoResult = this.sampleService.update(file,sample);
		return new SuccessResponseData(fileUploadInfoResult);
	}
}
