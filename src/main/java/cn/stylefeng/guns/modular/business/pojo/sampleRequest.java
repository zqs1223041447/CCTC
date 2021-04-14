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
package cn.stylefeng.guns.modular.business.pojo;

import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 文件信息表
 * 注意：
 * 1、如果文件上传不关心版本号只关心文件本身，则业务关联fileCode,文件升级code不变
 * 2、如果业务场景类似合同这类，文件升级不影响已签发的文件，则业务关联fileId，每次版本升级都会生成新的fileId
 *
 * @author majianguo
 * @date 2020/12/27 12:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class sampleRequest extends BaseRequest {

	/**
	 * 主键ID
	 */
	  private Long sampleId;


	  

//	状态
	  private String sampleStatus;
//	  样本仓库（样本夹）

	    private String sampleBucket;
//样本名称（上传时候的样本全名）
	    private String sampleOriginName;
//后缀
	    private String sampleSuffix;
//大小
	    private Long sampleSizeKb;
//存储名称
	    private String sampleObjectName;
//存储路径
	    private String samplePath;

	    private Date createTime;

	    private Long createUser;

	    private Date updateTime;

	    private Long updateUser;
//任务ID
	    private Long taskId;
//	    病毒手动分类：1、木马病毒
	    private String sampleType;

   
   

}
