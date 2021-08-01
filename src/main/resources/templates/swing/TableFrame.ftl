package ${packageName}
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class ${frame.entity.name?cap_first}Frame {
    JPanel parentPanel = new JPanel(new GridBagLayout());
    JPanel leftPanel = new JPanel(new GridBagLayout());
    parentPanel.add(leftPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

    JPanel rightPanel = new JPanel(new GridBagLayout());
    parentPanel.add(rightPanel, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    JScrollPane scrollPane = new JScrollPane();
    rightPanel.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 0.5, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    JTable jTable = new JTable();
    scrollPane.setViewportView(jTable);
<#list frame.items?filter(it -> it.field.column.searchable) as item>
        JLabel ${item.field.name}Label = new JLabel("${item.field.column.comment}");
        <#switch item.class.simpleName>
            <#case "TextFieldItem">
                JTextField ${item.field.name}TextField = new JTextField();
                <#break>
            <#case "PasswordFieldItem">
                JPasswordField passwordField = new JPasswordField();
                <#break>
            <#case "RadioItem">
                ButtonGroup ${item.field.name}ButtonGroup =  new ButtonGroup();
                <#list item.options as option>
                    JRadioButton jRadioButton${option_index} = new JRadioButton("${option.title}");
                    ${item.field.name}ButtonGroup.add(jRadioButton${option_index})
                </#list>
                <#break>
            <#case "SelectItem">
                JComboBox<String> ${item.field.name}ComboBox = new JComboBox<>();
                <#if !item.field.column.associate??>
                    <#list item.options as option>
                        ${item.field.name}ComboBox.add("${option.title}")
                    </#list>
                </#if>
                <#break>
        </#switch>
    </#list>
}