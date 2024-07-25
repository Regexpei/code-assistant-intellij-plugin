package cn.regexp.code.assistant.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author Regexpei
 * @date 2024/7/24 23:47
 * @description 表格数据信息
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
public class TableDataInfo<T> implements Serializable {

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> rows;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<T> list, int total) {
        this.rows = list;
        this.total = total;
    }

    public boolean isSuccess() {
        return code == 200;
    }
}
