package com.csy.druid.demo.beans;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class Contract implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Contract one() {
		return new Contract();
	}

	@TableId(type = IdType.AUTO)
	private Integer id;
	private Integer type;
	private String oaType;
	private Long oaContractId;
	private String name;
	private String contractCode;
	private String adminCode;
	private String companyName;
	private String companyShorterName;
	private String organizerDepartmentId;
	private String organizerDepartmentName;
	private String organizerUsername;
	private String organizerUserMemberId;
	private Date applyTime;
	private Integer status;
	private Integer deleted;
	private Integer flowStatus;
	private Integer paperApplyStatus;
	private Date startTime;
	private Date endTime;
	private String ourSignSubject;
	private Integer createUser;
	private String createUsername;
	private Integer updateUser;
	private String updateUsername;
	private Date createTime;
	private Date updateTime;
	private String operator;
	private Date operateTime;
	private String operatorUsername;
	private Integer isTest;

}