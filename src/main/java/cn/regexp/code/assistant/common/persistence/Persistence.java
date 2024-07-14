package cn.regexp.code.assistant.common.persistence;

import java.io.IOException;
import java.util.List;

/**
 * @author Regexpei
 * @date 2024/7/13 11:27
 * @description 持久化接口
 * @since 1.0.0
 */
public interface Persistence<T> {

    /**
     * 初始化文件路径
     */
    void init() throws IOException;

    /**
     * 加载持久化数据
     */
    List<T> load() throws IOException;

    /**
     * 保存数据
     *
     * @param t 需要保存的数据
     * @throws IOException 持久化时发生IO异常
     */
    void save(T t) throws IOException;


    /**
     * 删除第一个数据
     *
     * @param t 需要删除的数据
     * @throws IOException 删除数据时发生IO异常
     */
    void remove(T t) throws IOException;


    /**
     * 清空数据
     *
     * @throws IOException 删除数据时发生IO异常
     */
    void clear() throws IOException;
}
