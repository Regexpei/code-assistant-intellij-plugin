package cn.regexp.code.assistant.service;

import cn.regexp.code.assistant.common.UrlKeyConstant;
import cn.regexp.code.assistant.entity.Issue;
import cn.regexp.code.assistant.entity.Result;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Regexpei
 * @date 2024/7/23 19:46
 * @description 问题 Service
 * @since 1.0.0
 */
@Slf4j
public class IssueService extends CommonService {

    public static IssueService getInstance() {
        return application.getService(IssueService.class);
    }

    /**
     * 添加问题
     *
     * @param issue 问题
     * @return 结果
     */
    public Result<String> addIssue(Issue issue) {
        return post(UrlKeyConstant.ADD_ISSUE_KEY, issue);
    }

}
