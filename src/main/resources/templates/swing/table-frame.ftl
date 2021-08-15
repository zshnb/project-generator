<#assign name = "${frame.entity.name?capFirst}"/>
package ${packageName};
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Enumeration;
import ${mapperPackageName}.*;
import ${configPackageName}.*;
import ${entityPackageName}.${name};

public class ${name}Frame {
    JPanel parentPanel = new JPanel(new GridBagLayout());
    JPanel leftPanel = new JPanel(new GridBagLayout());
    JPanel rightPanel = new JPanel(new GridBagLayout());
    JScrollPane scrollPane = new JScrollPane();
    JTable jTable = new JTable();
    private JButton addButton = new JButton("添加");
    private JButton updateButton = new JButton("修改");
    private JButton deleteButton = new JButton("删除");
    private int id;

    <#list frame.items?filter(it -> it.field.column.enableFormItem) as item>
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
    <#assign mapper = "${frame.entity.name}Mapper"/>
    private final PermissionMapper permissionMapper = GlobalSqlSessionFactory.getSqlSession().getMapper(PermissionMapper.class);
    private final ${name}Mapper ${mapper} = GlobalSqlSessionFactory.getSqlSession().getMapper(${name}Mapper.class);
    public ${name}Frame() {
        initUI();
        initData(${mapper}.list());
        JFrame frame = new JFrame();
        frame.setLocation(10, 10);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        frame.add(parentPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = jTable.getSelectedRow();
                <#list frame.items?filter(it -> it.field.column.enableFormItem) as item>
                    String ${item.field.name} = (String) jTable.getValueAt(row, ${item_index});
                    <#switch item.class.simpleName>
                    <#case "TextFieldFrameItem">
                        ${item.field.name}TextField.setText(${item.field.name});
                        <#break>
                    <#case "PasswordFieldFrameItem">
                        <#break>
                    <#case "RadioFrameItem">
                        Enumeration<AbstractButton> ${item.field.name}ButtonGroupIterator = ${item.field.name}ButtonGroup.getElements();
                        while (${item.field.name}ButtonGroupIterator.hasMoreElements()) {
                            AbstractButton abstractButton = ${item.field.name}ButtonGroupIterator.nextElement();
                            if (abstractButton.getText().equals(${item.field.name})) {
                                abstractButton.setSelected(true);
                                break;
                            }
                        }
                        <#break>
                    <#case "SelectFrameItem">
                        ${item.field.name}ComboBox.setSelectedItem(${item.field.name});
                        <#break>
                    </#switch>
                </#list>
                id = (int) jTable.getValueAt(row, ${frame.entity.fields?filter(it -> it.column.enableTableField)?size});
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                <#list frame.items?filter(it -> it.field.column.enableFormItem) as item>
                    <#switch item.class.simpleName>
                        <#case "TextFieldFrameItem">
                            String ${item.field.name} = ${item.field.name}TextField.getText();
                            <#break>
                        <#case "PasswordFieldFrameItem">
                            String ${item.field.name} = passwordField.getText();
                            <#break>
                        <#case "RadioFrameItem">
                            String ${item.field.name} = "";
                            Enumeration<AbstractButton> ${item.field.name}ButtonGroupIterator = ${item.field.name}ButtonGroup.getElements();
                            while (${item.field.name}ButtonGroupIterator.hasMoreElements()) {
                                AbstractButton abstractButton = ${item.field.name}ButtonGroupIterator.nextElement();
                                if (abstractButton.isSelected()) {
                                    type = abstractButton.getText();
                                }
                            }
                            <#break>
                        <#case "SelectFrameItem">
                            String ${item.field.name} = ${item.field.name}ComboBox.getSelectedItem().toString();
                            <#break>
                    </#switch>
                </#list>
                <#assign params>
                    <#list frame.entity.fields?filter(it -> it.column.enableFormItem) as field>
                        ${field.name}<#if field_has_next>, </#if><#t>
                    </#list>
                </#assign>
                ${frame.entity.name}Mapper.insert(new ${name}(${params}));
                List<${name}> ${name?uncapFirst}s = ${mapper}.list();
                initData(${name?uncapFirst}s);
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                <#list frame.items?filter(it -> it.field.column.enableFormItem) as item>
                    <#switch item.class.simpleName>
                        <#case "TextFieldFrameItem">
                            String ${item.field.name} = ${item.field.name}TextField.getText();
                            <#break>
                        <#case "PasswordFieldFrameItem">
                            String ${item.field.name} = passwordField.getText();
                            <#break>
                        <#case "RadioFrameItem">
                            String ${item.field.name} = "";
                            Enumeration<AbstractButton> ${item.field.name}ButtonGroupIterator = ${item.field.name}ButtonGroup.getElements();
                            while (${item.field.name}ButtonGroupIterator.hasMoreElements()) {
                                AbstractButton abstractButton = ${item.field.name}ButtonGroupIterator.nextElement();
                                if (abstractButton.isSelected()) {
                                    type = abstractButton.getText();
                                }
                            }
                            <#break>
                        <#case "SelectFrameItem">
                            String ${item.field.name} = ${item.field.name}ComboBox.getSelectedItem().toString();
                            <#break>
                    </#switch>
                </#list>
                <#assign params>
                    <#list frame.entity.fields?filter(it -> it.column.enableFormItem) as field>
                        ${field.name}<#if field_has_next>, </#if><#t>
                    </#list>
                </#assign>
                ${frame.entity.name}Mapper.update(new ${name}(id, ${params}));
                List<${name}> ${name?uncapFirst}s = ${mapper}.list();
                initData(${name?uncapFirst}s);
            }
        });
    }

    private void initUI() {
        parentPanel.add(leftPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        parentPanel.add(rightPanel, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        rightPanel.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 0.5, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        scrollPane.setViewportView(jTable);

        <#list frame.items?filter(it -> it.field.column.enableFormItem) as item>
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
                        ${item.field.name}ButtonGroup.add(${item.field.name}JRadioButton${option_index});
                        jPanel${item_index}.add(${item.field.name}JRadioButton${option_index}, new GridBagConstraints(${option_index + 1}, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    </#list>
                    <#break>
                <#case "SelectFrameItem">
                    <#if !item.field.column.associate??>
                        <#list item.options as option>
                            ${item.field.name}ComboBox.addItem("${option.title}");
                        </#list>
                    </#if>
                    jPanel${item_index}.add(${item.field.name}ComboBox, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    <#break>
            </#switch>
            leftPanel.add(jPanel${item_index}, new GridBagConstraints(0, ${item_index}, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        </#list>
        JPanel operationPanel = new JPanel(new GridBagLayout());
        operationPanel.add(addButton, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        operationPanel.add(updateButton, new GridBagConstraints(0, 1, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        operationPanel.add(deleteButton, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        leftPanel.add(operationPanel, new GridBagConstraints(0, ${frame.items?size}, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    // 初始化数据，放进table里
    private void initData(List<${name}> ${name?uncapFirst}s) {
        <#assign columnNames>
            <#list frame.entity.table.columns?filter(it -> it.enableTableField) as column>
                "${column.comment}"<#if column_has_next>, </#if><#t>
            </#list>
        </#assign>
        Object[] columnNames = new String[]{${columnNames}, ""};
        Object[][] data = new Object[${name?uncapFirst}s.size()][columnNames.length];
        for (int i = 0; i < ${name?uncapFirst}s.size(); i++) {
            <#list frame.entity.fields?filter(it -> it.column.enableTableField) as field>
                data[i][${field_index}] = ${name?uncapFirst}s.get(i).get${field.name?capFirst}();
            </#list>
            data[i][${frame.entity.fields?filter(it -> it.column.enableTableField)?size}] = ${name?uncapFirst}s.get(i).getId();
        }
        TableModel tableModel = new DefaultTableModel(data, columnNames);
        jTable.setModel(tableModel);
        jTable.getColumnModel().getColumn(${frame.entity.fields?filter(it -> it.column.enableTableField)?size}).setMinWidth(0);
        jTable.getColumnModel().getColumn(${frame.entity.fields?filter(it -> it.column.enableTableField)?size}).setMaxWidth(0);
        jTable.updateUI();
    }
}