package ${packageName};
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

public class ${frame.entity.name?cap_first}Frame {
    JPanel parentPanel = new JPanel(new GridBagLayout());
    JPanel leftPanel = new JPanel(new GridBagLayout());
    JPanel rightPanel = new JPanel(new GridBagLayout());
    JScrollPane scrollPane = new JScrollPane();
    JTable jTable = new JTable();

    <#list frame.items?filter(it -> it.field.column.searchable) as item>
        JLabel ${item.field.name}Label = new JLabel("${item.field.column.comment}");
        <#switch item.class.simpleName>
            <#case "TextFieldFrameItem">
                JTextField ${item.field.name}TextField = new JTextField();
                <#break>
            <#case "PasswordFieldFrameItem">
                JPasswordField passwordField = new JPasswordField();
                <#break>
            <#case "RadioFrameItem">
                ButtonGroup ${item.field.name}ButtonGroup =  new ButtonGroup();
                <#break>
            <#case "SelectFrameItem">
                JComboBox<String> ${item.field.name}ComboBox = new JComboBox<>();
                <#break>
        </#switch>
    </#list>
    public ${frame.entity.name?cap_first}Frame() {
        initUI();
        JFrame frame = new JFrame();
        frame.setLocation(10, 10);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(parentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void initUI() {
        parentPanel.add(leftPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        parentPanel.add(rightPanel, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        rightPanel.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 0.5, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        scrollPane.setViewportView(jTable);

        <#list frame.items?filter(it -> it.field.column.searchable) as item>
            JPanel jPanel${item_index} = new JPanel(new GridBagLayout());
            jPanel${item_index}.add(${item.field.name}Label, new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            <#switch item.class.simpleName>
                <#case "TextFieldFrameItem">
                    jPanel${item_index}.add(${item.field.name}TextField, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    <#break>
                <#case "PasswordFieldFrameItem">
                    jPanel${item_index}.add(passwordField, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    <#break>
                <#case "RadioFrameItem">
                    <#list item.options as option>
                        JRadioButton ${item.field.name}JRadioButton${option_index} = new JRadioButton("${option.title}");
                        ${item.field.name}ButtonGroup.add(jRadioButton${option_index})
                        jPanel${item_index}.add(${item.field.name}JRadioButton${option_index}, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    </#list>
                    <#break>
                <#case "SelectFrameItem">
                    <#if !item.field.column.associate??>
                        <#list item.options as option>
                            ${item.field.name}ComboBox.add("${option.title}")
                        </#list>
                    </#if>
                    jPanel${item_index}.add(${item.field.name}ComboBox, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    <#break>
            </#switch>
            leftPanel.add(jPanel${item_index}, new GridBagConstraints(${item_index}, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        </#list>
    }
}