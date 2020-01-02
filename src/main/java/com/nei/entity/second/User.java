package com.nei.entity.second;

import com.nei.entity.Identity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @desc：用户模型
 * @date 2019/09/26
 */
@Data
@Table(name = "user")
public class User implements Identity, Serializable {

    /**
     * 自增主键
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 年龄
     */
    @Column(name = "`age`")
    private Integer age;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "`update_time`")
    private Date updateTime;

    /**
     * 是否逻辑删除：0-否，1-是
     */
    @Column(name = "`is_deleted`")
    private Boolean isDeleted;

}