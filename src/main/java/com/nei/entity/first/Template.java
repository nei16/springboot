package com.nei.entity.first;

import com.nei.entity.Identity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @desc：模板模型
 * @date 2019/09/26
 */
@Data
@Table(name = "template")
public class Template implements Identity {

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