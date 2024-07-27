package cn.regexp.code.assistant.enums;

import cn.regexp.code.assistant.ui.SimpleComboBoxItem;

import javax.swing.*;

/**
 * @author Regexpei
 * @date 2024/7/27 12:03
 * @description 枚举接口
 * @since 1.0.0
 */
public interface BaseEnum {

    SimpleComboBoxItem DEFAULT_ITEM = new SimpleComboBoxItem("default", "---请选择---");

    /**
     * 获取 code
     *
     * @return 编号
     */
    int getCode();

    /**
     * 获取描述
     *
     * @return 描述
     */
    String getDesc();

    /**
     * 初始化下拉框
     *
     * @param comboBox 下拉框
     * @param values   枚举数组
     * @param <E>      枚举类型
     */
    static <E extends Enum<E> & BaseEnum> void initComboBox(JComboBox<SimpleComboBoxItem> comboBox, E[] values) {
        comboBox.addItem(DEFAULT_ITEM);
        comboBox.setSelectedItem(DEFAULT_ITEM);
        for (E value : values) {
            SimpleComboBoxItem item = new SimpleComboBoxItem(String.valueOf(value.getCode()), value.getDesc());
            comboBox.addItem(item);
        }
    }
}
