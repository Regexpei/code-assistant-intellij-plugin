package cn.regexp.code.assistant.ui.issue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Regexpei
 * @date 2024/7/7 19:10
 * @description 下拉选项框
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
