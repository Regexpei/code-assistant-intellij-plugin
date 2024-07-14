package cn.regexp.code.assistant.common.persistence;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.intellij.openapi.project.Project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Regexpei
 * @date 2024/7/13 11:30
 * @description 将数据持久化为 JSON 文件
 * @since 1.0.0
 */
public class JsonPersistence<T> implements Persistence<T> {

    /**
     * 持久化路径
     */
    private final Path persistencePath;
    /**
     * 持久化文件路径
     */
    private final Path persistenceFilePath;
    /**
     * 持久化类
     */
    private final Class<T> persistenceClass;

    public JsonPersistence(Project project, Class<T> persistenceClass) {
        // .idea
        this.persistencePath = new File(project.getBasePath(), Project.DIRECTORY_STORE_FOLDER).toPath();
        this.persistenceFilePath = persistencePath.resolve(persistenceClass.getSimpleName() + ".json");
        this.persistenceClass = persistenceClass;
    }

    public static <T> JsonPersistence<T> getInstance(Project project, Class<T> persistenceClass) {
        return new JsonPersistence<>(project, persistenceClass);
    }

    @Override
    public void init() throws IOException {
        Files.createDirectories(persistencePath);
        if (Files.notExists(persistenceFilePath)) {
            Files.createFile(persistenceFilePath);
        }
    }

    @Override
    public List<T> load() {
        String content = FileUtil.readUtf8String(persistenceFilePath.toFile());
        return JSON.parseArray(content, persistenceClass);
    }

    @Override
    public void save(T t) throws IOException {
        init();

        List<T> list = load();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(t);

        FileUtil.writeUtf8String(JSON.toJSONString(list), persistenceFilePath.toFile());
    }

    @Override
    public void remove(Object obj) {
        if (Files.notExists(persistenceFilePath)) {
            return;
        }

        List<T> list = load();
        list.removeIf(s -> s.equals(JSON.toJSONString(obj)));
        FileUtil.writeUtf8Lines(list, persistenceFilePath.toFile());
    }

    @Override
    public void clear() throws IOException {
        Files.deleteIfExists(persistenceFilePath);
    }

}
