package cn.regexp.code.assistant.util;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiUtilBase;

/**
 * @author Regexpei
 * @date 2024/7/7 16:57
 * @description 文件工具类
 * @since 1.0.0
 */
public class FileUtils {

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

}
