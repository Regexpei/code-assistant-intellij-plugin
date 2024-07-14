package cn.regexp.code.assistant.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Regexpei
 * @date 2024/7/7 19:10
 * @description 下拉选项框
 * @since 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
public class SimpleComboBoxItem {

    private String code;
    private String desc;

    @Override
    public String toString() {
        return desc;
    }

}
