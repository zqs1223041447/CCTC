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
package cn.stylefeng.guns.modular.business.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;

import java.sql.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文件信息表
 * </p>
 *
 * @author stylefeng
 * @date 2020/6/7 22:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("zqs_sample")
public class Sample extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	  @TableId(value = "sample_id", type = IdType.ASSIGN_ID)
	  private Long sampleId;


	  

//	状态
	  @TableField("sample_status")
	  private String sampleStatus;
//	  样本仓库（样本夹）
	  @TableField("sample_bucket")
	    private String sampleBucket;
//样本名称（上传时候的样本全名）
	  @TableField("sample_origin_name")
	    private String sampleOriginName;
//后缀
	  @TableField("sample_suffix")
	    private String sampleSuffix;
//大小
	  @TableField("sample_size_kb")
	    private Long sampleSizeKb;
//存储名称
	  @TableField("sample_object_name")
	    private String sampleObjectName;
//存储路径
	  @TableField("sample_path")
	    private String samplePath;
	  @TableField("create_time")
	    private Date createTime;
	  @TableField("create_user")
	    private Long createUser;
	  @TableField("create_user_name")
	    private String createUserName;
	  @TableField("update_time")
	    private Date updateTime;
	  @TableField("update_user")
	    private Long updateUser;
	  @TableField("update_user_name")
	    private String updateUserName;
//任务ID
	  @TableField("task_id")
	    private Long taskId;
//	    病毒手动分类：1、木马病毒
	  @TableField("sample_type")
	    private String sampleType;

	  @TableField("sample_en_name")
	  private String sampleEnName;
	  @TableField("sample_zh_name")
	  private String sampleZhName;
	  @TableField("sample_introduction")
	  private String sampleIntroduction;
	  
}
