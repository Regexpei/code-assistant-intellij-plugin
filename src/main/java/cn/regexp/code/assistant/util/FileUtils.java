package cn.regexp.code.assistant.util;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiUtilBase;

import java.util.Collection;

/**
 * @author Regexpei
 * @date 2024/7/7 16:57
 * @description 文件工具类
 * @since 1.0.0
 */
public class FileUtils {

    /**
     * 获取当前编辑器所打开文件的相对路径
     *
     * @param project 当前项目
     * @param editor  编辑器
     * @return 相对路径
     */
    public static String getRelativePathByEditor(Project project, Editor editor) {
        PsiFile psiFile = PsiUtilBase.getPsiFileInEditor(editor, project);
        if (psiFile == null) {
            return null;
        }

        // 获取项目路径
        String basePath = project.getBasePath();
        if (basePath == null) {
            return null;
        }

        // 获取文件的绝对路径
        String filePath = psiFile.getVirtualFile().getPath();
        return filePath.substring(basePath.length());
    }

    /**
     * 跳转到指定文件
     *
     * @param project  当前项目
     * @param filePath 文件路径
     */
    public static void jumpToFile(Project project, String filePath) {
        String fileName = getFileName(filePath);
        if (fileName == null) {
            return;
        }

        // 查找文件，范围限定为当前项目
        Collection<VirtualFile> virtualFiles = FilenameIndex.getVirtualFilesByName(fileName,
                GlobalSearchScope.projectScope(project));
        for (VirtualFile virtualFile : virtualFiles) {
            if (virtualFile.getPath().equals(project.getBasePath() + filePath)) {
                // 若是目标文件，则打开文件
                FileEditorManager.getInstance(project).openFile(virtualFile, true);
            }
        }
    }

    /**
     * 从文件路径中获取文件名称
     *
     * @param filePath 文件路径
     * @return 文件名
     */
    public static String getFileName(String filePath) {
        if (filePath == null) {
            return null;
        }

        int lastIndex = filePath.lastIndexOf("/");
        if (lastIndex == -1) {
            return filePath;
        }

        return filePath.substring(lastIndex + 1);
    }
}
