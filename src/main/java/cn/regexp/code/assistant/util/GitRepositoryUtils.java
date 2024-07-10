package cn.regexp.code.assistant.util;

import com.intellij.openapi.project.Project;
import git4idea.GitUtil;
import git4idea.repo.GitRemote;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryManager;

import java.util.List;

/**
 * @author Regexpei
 * @date 2024/7/7 17:20
 * @description git仓库工具类
 * @since 1.0.0
 */
public class GitRepositoryUtils {

    /**
     * 获取当前git仓库
     *
     * @param project 当前项目
     * @return 当前git仓库
     */
    public static GitRepository getCrurentGitRepository(Project project) {
        String basePath = project.getBasePath();
        if (basePath == null) {
            return null;
        }

        List<GitRepository> repositories = GitRepositoryManager.getInstance(project).getRepositories();
        if (repositories.isEmpty()) {
            return null;
        }

        if (repositories.size() == 1) {
            return repositories.get(0);
        }

        for (GitRepository repository : repositories) {
            if (basePath.startsWith(repository.getRoot().getPath())) {
                return repository;
            }
        }

        return null;
    }

    /**
     * 获取当前分支名称
     *
     * @param project 当前项目
     * @return 当前分支名称
     */
    public static String getCurrentBranchName(Project project) {
        GitRepository gitRepository = getCrurentGitRepository(project);
        if (gitRepository != null) {
            return gitRepository.getCurrentBranchName();
        }
        return null;
    }

    /**
     * 获取git仓库地址
     *
     * @param project 当前项目
     * @return 当前git仓库地址
     */
    public static String getGitUrl(Project project) {
        GitRepository gitRepository = getCrurentGitRepository(project);
        if (gitRepository != null) {
            GitRemote gitRemote = GitUtil.getDefaultOrFirstRemote(gitRepository.getRemotes());
            if (gitRemote != null) {
                return gitRemote.getFirstUrl();
            }
        }
        return null;
    }

}
