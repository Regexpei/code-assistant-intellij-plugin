package cn.regexp.code.assistant.entity;

import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author Regexpei
 * @date 2024/7/7 20:07
 * @description 问题实体类
 * @since 1.0.0
 */
@Getter
@Setter
public class Issue {

    /**
     * 主键
     */
    private Long id;

    /**
     * 问题编号
     */
    private transient String issueNo;

    /**
     * gitUrl
     */
    private String gitUrl;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 分支名称
     */
    private String branchName;

    /**
     * 责任人
     */
    private String assignee;

    /**
     * 登记人
     */
    private String marker;

    /**
     * 问题类型
     */
    private Integer issueType;

    /**
     * 问题级别
     */
    private Integer issueLevel;

    /**
     * 问题状态
     */
    private Integer issueStatus;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 开始位置
     */
    private Integer startPos;

    /**
     * 结束位置
     */
    private Integer endPos;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 代码片段
     */
    private String codeSegment;

    /**
     * 问题描述
     */
    private String issueDetail;

    /**
     * 建议
     */
    private String suggestion;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新时间
     */
    private Date updatedTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
