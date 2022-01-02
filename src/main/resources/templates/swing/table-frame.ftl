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
import java.util.stream.Collectors;
import ${mapperPackageName}.*;
import ${configPackageName}.*;
import ${dtoPackageName}.*;
import ${entityPackageName}.*;
import ${requestPackageName}.*;
<#function camelize(s)>
    <#return s
    ?replace('(^_+)|(_+$)', '', 'r')
    ?replace('\\_+(\\w)?', ' $1', 'r')
    ?replace('([A-Z])', ' $1', 'r')
    ?capitalize
    ?replace(' ' , '')
    ?uncapFirst>
</#function>
<#assign searchable>
    <#if (frame.entity.fields?filter(f -> f.column.searchable)?size > 0)>
        true
    <#else>
        false
    </#if>
</#assign>

public class ${name}Frame {
    JPanel parentPanel = new JPanel(new GridBagLayout());
    JPanel leftPanel = new JPanel(new GridBagLayout());
    JPanel rightPanel = new JPanel(new GridBagLayout());
    JScrollPane scrollPane = new JScrollPane();
    JTable jTable = new JTable();
    <#list frame.operations as operation>
        private JButton ${operation.value}Button = new JButton("${operation.description}");
    </#list>
    <#if searchable??>
        private JButton searchButton = new JButton("搜索");
    </#if>
    private int id;

    <#list frame.items?filter(it -> it.field.column.enableFormItem) as item>
        <#switch item.class.simpleName>
            <#case "TextFieldFrameItem">
                <#if item.field.column.searchable>
                    JTextField search${item.field.name?capFirst}TextField = new JTextField();
                </#if>
                JTextField ${item.field.name}TextField = new JTextField();
                <#break>
            <#case "PasswordFrameItem">
                JPasswordField passwordField = new JPasswordField();
                <#break>
            <#case "RadioFrameItem">
                <#if item.field.column.searchable>
                    ButtonGroup search${item.field.name?capFirst}ButtonGroup = new ButtonGroup();
                </#if>
                ButtonGroup ${item.field.name}ButtonGroup =  new ButtonGroup();
                <#break>
            <#case "SelectFrameItem">
                <#if item.field.column.associate??>
                    <#assign comboxName = "${camelize(item.field.column.associate.targetTableName)}${item.field.column.associate.formItemColumnName?capFirst}ComboBox"/>
                    JComboBox<String> ${comboxName} = new JComboBox<>();
                    <#if item.field.column.searchable>
                        JComboBox<String> search${comboxName?capFirst} = new JComboBox<>();
                    </#if>
                <#else>
                    <#assign comboxName = "${item.field.name}ComboBox"/>
                    JComboBox<String> ${comboxName} = new JComboBox<>();
                    <#if item.field.column.searchable>
                        JComboBox<String> search${comboxName?capFirst} = new JComboBox<>();
                    </#if>
                </#if>
                <#break>
        </#switch>
    </#list>
    <#assign mapper = "${frame.entity.name}Mapper"/>
    private final ${name}Mapper ${mapper} = GlobalSqlSessionFactory.getSqlSession().getMapper(${name}Mapper.class);
    <#if frame.entity.table.associate>
        <#list frame.entity.table.columns?filter(it -> it.associate??) as column>
            <#assign associateClass = "${camelize(column.associate.targetTableName)?capFirst}"/>
            private final ${associateClass}Mapper ${associateClass?uncapFirst}Mapper = GlobalSqlSessionFactory.getSqlSession().getMapper(${associateClass}Mapper.class);
            private List<${associateClass}> ${associateClass?uncapFirst}s = ${associateClass?uncapFirst}Mapper.list();
        </#list>
    </#if>
    private final PermissionMapper permissionMapper = GlobalSqlSessionFactory.getSqlSession().getMapper(PermissionMapper.class);
    private List<String> permissions = permissionMapper.list(GlobalSession.user.getRole(), "${frame.entity.name}")
        .stream().map(Permission::getOperation).collect(Collectors.toList());
    public ${name}Frame() {
        initUI();
        initData(<#if frame.entity.table.associate>${mapper}.findDtos(<#if frame.entity.table.searchable>new List${name}Request()</#if>)<#else>${mapper}.list()</#if>);
        JFrame frame = new JFrame();
        frame.setTitle("${frame.entity.table.comment}界面");
        frame.setLocation(10, 10);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        frame.add(parentPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                <#assign i = 0/>
                <#list frame.entity.fields?filter(it -> it.column.enableTableField && !it.column.associate??) as field>
                    <#assign i = i + 1>
                </#list>
                <#if frame.entity.table.associate??>
                    <#list frame.entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0) as column>
                        <#list column.associate.associateResultColumns as result>
                            <#assign i = i + 1>
                        </#list>
                    </#list>
                </#if>
                int row = jTable.getSelectedRow();
                <#list frame.items?filter(it -> it.field.column.enableFormItem) as item>
                    <#if !item.field.column.associate??>
                        ${item.field.type} ${item.field.name} = (${item.field.type}) jTable.getValueAt(row, ${item_index});
                    </#if>
                    <#switch item.class.simpleName>
                        <#case "SelectFrameItem">
                            <#assign comboBox>
                                <#if item.field.column.associate??>
                                    ${camelize(item.field.column.associate.targetTableName)}${item.field.column.associate.formItemColumnName?capFirst}ComboBox<#t>
                                <#else>
                                    ${item.field.name}ComboBox<#t>
                                </#if>
                            </#assign>
                            <#if item.field.column.associate??>
                                <#assign variableName = "${camelize(item.field.column.associate.targetTableName)}${item.field.column.associate.formItemColumnName?capFirst}"/>
                                String ${variableName} = (String) jTable.getValueAt(row, ${item_index});
                                ${comboBox}.setSelectedItem(${variableName});
                            <#else>
                                ${comboBox}.setSelectedItem(${item.field.name});
                            </#if>
                            <#break>
                        <#case "TextFieldFrameItem">
                            ${item.field.name}TextField.setText(String.valueOf(${item.field.name}));
                            <#break>
                        <#case "PasswordFrameItem">
                            passwordField.setText(String.valueOf(${item.field.name}));
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
                    </#switch>
                </#list>

                id = (int) jTable.getValueAt(row, ${i});
            }
        });
        <#if (frame.operations?filter(it -> it.value == "add")?size > 0)>
        // 添加按钮事件
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                <#list frame.items?filter(it -> it.field.column.enableFormItem) as item>
                    <#switch item.class.simpleName>
                        <#case "TextFieldFrameItem">
                            <#if item.field.type == "String">
                                String ${item.field.name} = ${item.field.name}TextField.getText();
                            <#elseIf item.field.type == "Integer">
                                Integer ${item.field.name} = Integer.valueOf(${item.field.name}TextField.getText());
                            </#if>
                            <#break>
                        <#case "PasswordFrameItem">
                            String ${item.field.name} = passwordField.getText();
                            <#break>
                        <#case "RadioFrameItem">
                            String ${item.field.name} = "";
                            Enumeration<AbstractButton> ${item.field.name}ButtonGroupIterator = ${item.field.name}ButtonGroup.getElements();
                            while (${item.field.name}ButtonGroupIterator.hasMoreElements()) {
                                AbstractButton abstractButton = ${item.field.name}ButtonGroupIterator.nextElement();
                                if (abstractButton.isSelected()) {
                                    ${item.field.name} = abstractButton.getText();
                                }
                            }
                            <#break>
                        <#case "SelectFrameItem">
                            <#assign comboBox>
                                <#if item.field.column.associate??>
                                    <#assign associateFieldVariableName = "${camelize(item.field.column.associate.targetTableName)}${item.field.column.associate.formItemColumnName?capFirst}"/>
                                    ${associateFieldVariableName}ComboBox<#t>
                                <#else>
                                    ${item.field.name}ComboBox<#t>
                                </#if>
                            </#assign>
                            <#if item.field.column.associate??>
                                <#assign associateFieldVariableName = "${camelize(item.field.column.associate.targetTableName)}${item.field.column.associate.formItemColumnName?capFirst}"/>
                                String ${associateFieldVariableName} = ${comboBox}.getSelectedItem().toString();
                                int ${item.field.name} = ${camelize(item.field.column.associate.targetTableName)}s.stream().filter(it -> it.get${item.field.column.associate.formItemColumnName?capFirst}().equals(${associateFieldVariableName})).findAny().get().getId();
                            <#else>
                                String ${item.field.name} = ${comboBox}.getSelectedItem().toString();
                            </#if>
                            <#break>
                    </#switch>
                </#list>
                <#assign params>
                    <#list frame.entity.fields?filter(it -> it.column.enableFormItem) as field>
                        ${field.name}<#if field_has_next>, </#if><#t>
                    </#list>
                </#assign>
                ${frame.entity.name}Mapper.insert(new ${name}(${params}));
                initData(<#if frame.entity.table.associate>${mapper}.findDtos(<#if frame.entity.table.searchable>new List${name}Request()</#if>)<#else>${mapper}.list()</#if>);
            }
        });
        </#if>
        <#if (frame.operations?filter(it -> it.value == "edit")?size > 0)>
        // 编辑按钮事件
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (id == 0) {
                    JOptionPane.showMessageDialog(null, "请先选择记录");
                    return;
                }
                <#list frame.items?filter(it -> it.field.column.enableFormItem) as item>
                    <#switch item.class.simpleName>
                        <#case "TextFieldFrameItem">
                            <#if item.field.type == "String">
                                String ${item.field.name} = ${item.field.name}TextField.getText();
                            <#elseIf item.field.type == "Integer">
                                Integer ${item.field.name} = Integer.valueOf(${item.field.name}TextField.getText());
                            </#if>
                            <#break>
                        <#case "PasswordFrameItem">
                            String ${item.field.name} = passwordField.getText();
                            <#break>
                        <#case "RadioFrameItem">
                            String ${item.field.name} = "";
                            Enumeration<AbstractButton> ${item.field.name}ButtonGroupIterator = ${item.field.name}ButtonGroup.getElements();
                            while (${item.field.name}ButtonGroupIterator.hasMoreElements()) {
                                AbstractButton abstractButton = ${item.field.name}ButtonGroupIterator.nextElement();
                                if (abstractButton.isSelected()) {
                                    ${item.field.name} = abstractButton.getText();
                                }
                            }
                            <#break>
                        <#case "SelectFrameItem">
                            <#assign comboBox>
                                <#if item.field.column.associate??>
                                    <#assign associateFieldVariableName = "${camelize(item.field.column.associate.targetTableName)}${item.field.column.associate.formItemColumnName?capFirst}"/>
                                    ${associateFieldVariableName}ComboBox<#t>
                                <#else>
                                    ${item.field.name}ComboBox<#t>
                                </#if>
                            </#assign>
                            <#if item.field.column.associate??>
                                <#assign associateFieldVariableName = "${camelize(item.field.column.associate.targetTableName)}${item.field.column.associate.formItemColumnName?capFirst}"/>
                                String ${associateFieldVariableName} = ${comboBox}.getSelectedItem().toString();
                                int ${item.field.name} = ${camelize(item.field.column.associate.targetTableName)}s.stream().filter(it -> it.get${item.field.column.associate.formItemColumnName?capFirst}().equals(${associateFieldVariableName})).findAny().get().getId();
                            <#else>
                                String ${item.field.name} = ${comboBox}.getSelectedItem().toString();
                            </#if>
                            <#break>
                    </#switch>
                </#list>
                ${name} ${name?uncapFirst} = ${mapper}.selectById(id);
                <#list frame.entity.fields?filter(it -> it.column.enableFormItem) as field>
                    ${name?uncapFirst}.set${field.name?capFirst}(${field.name});
                </#list>
                ${frame.entity.name}Mapper.update(${name?uncapFirst});
                initData(<#if frame.entity.table.associate>${mapper}.findDtos(<#if frame.entity.table.searchable>new List${name}Request()</#if>)<#else>${mapper}.list()</#if>);
            }
        });
        </#if>
        <#if (frame.operations?filter(it -> it.value == "delete")?size > 0)>
        // 删除按钮事件
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, "删除成功");
                ${mapper}.deleteById(id);
                initData(<#if frame.entity.table.associate>${mapper}.findDtos(<#if frame.entity.table.searchable>new List${name}Request()</#if>)<#else>${mapper}.list()</#if>);
            }
        });
        </#if>
        <#if searchable??>
            // 搜索按钮事件
            searchButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    List${frame.entity.name?capFirst}Request request = new List${frame.entity.name?capFirst}Request();
                    <#list frame.items?filter(it -> it.field.column.searchable) as item>
                        <#switch item.class.simpleName>
                            <#case "TextFieldFrameItem">
                                request.set${item.field.name?capFirst}(search${item.field.name?capFirst}TextField.getText());
                                <#break>
                            <#case "RadioFrameItem">
                                String ${item.field.name} = "";
                                Enumeration<AbstractButton> ${item.field.name}ButtonGroupIterator = search${item.field.name?capFirst}ButtonGroup.getElements();
                                while (${item.field.name}ButtonGroupIterator.hasMoreElements()) {
                                    AbstractButton abstractButton = ${item.field.name}ButtonGroupIterator.nextElement();
                                    if (abstractButton.isSelected()) {
                                        request.set${item.field.name?capFirst}(abstractButton.getText());
                                    }
                                }
                                <#break>
                            <#case "SelectFrameItem">
                                <#if item.field.column.associate??>
                                    <#assign associateFieldVariableName = "${camelize(item.field.column.associate.targetTableName)}${item.field.column.associate.formItemColumnName?capFirst}"/>
                                    String ${associateFieldVariableName} = search${associateFieldVariableName?capFirst}ComboBox.getSelectedItem().toString();
                                    int ${item.field.name} = ${camelize(item.field.column.associate.targetTableName)}s.stream().filter(it -> it.get${item.field.column.associate.formItemColumnName?capFirst}().equals(${associateFieldVariableName})).findAny().get().getId();
                                    request.set${item.field.name?capFirst}(${item.field.name});
                                <#else>
                                    request.set${item.field.name?capFirst}(search${item.field.name?capFirst}ComboBox.getSelectedItem().toString());
                                </#if>
                                <#break>
                        </#switch>
                    </#list>
                    initData(<#if frame.entity.table.associate>${mapper}.findDtos(<#if frame.entity.table.searchable>request)</#if>)<#else>${mapper}.list(<#if frame.entity.table.searchable>request)</#if>)</#if>;
                }
            });
        </#if>
    }

    // 初始化窗口控件
    private void initUI() {
        parentPanel.add(leftPanel, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        parentPanel.add(rightPanel, new GridBagConstraints(1, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        rightPanel.add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 0.5, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        scrollPane.setViewportView(jTable);
        <#if (frame.entity.fields?filter(f -> f.column.searchable)?size > 0)>
            <#assign j = 1/>
            <#list frame.items?filter(it -> it.field.column.searchable) as item>
            JPanel search${item.field.name?capFirst}Panel = new JPanel(new GridBagLayout());
            search${item.field.name?capFirst}Panel.add(new JLabel("${item.field.column.comment}"),
                new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            <#switch item.class.simpleName>
                <#case "TextFieldFrameItem">
                    search${item.field.name?capFirst}Panel.add(search${item.field.name?capFirst}TextField,
                        new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    <#break>
                <#case "RadioFrameItem">
                    <#list item.options as option>
                        JRadioButton search${item.field.name?capFirst}JRadioButton${option_index} = new JRadioButton("${option.title}");
                        search${item.field.name?capFirst}ButtonGroup.add(search${item.field.name?capFirst}JRadioButton${option_index});
                        search${item.field.name?capFirst}Panel.add(search${item.field.name?capFirst}JRadioButton${option_index}, new GridBagConstraints(${option_index + 1}, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    </#list>
                    <#break>
                <#case "SelectFrameItem">
                    <#assign comboBox>
                        <#if item.field.column.associate??>
                            ${camelize(item.field.column.associate.targetTableName)}${item.field.column.associate.formItemColumnName?capFirst}ComboBox<#t>
                        <#else>
                            ${item.field.name}ComboBox<#t>
                        </#if>
                    </#assign>
                    <#if !item.field.column.associate??>
                        <#list item.options as option>
                            ${item.field.name}ComboBox.addItem("${option.title}");
                        </#list>
                    <#else>
                        ${camelize(item.field.column.associate.targetTableName)}s.forEach(it -> search${comboBox?capFirst}.addItem(it.get${item.field.column.associate.formItemColumnName?capFirst}()));
                    </#if>
                    search${item.field.name?capFirst}Panel.add(search${comboBox?capFirst}, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    <#break>
                </#switch>
                rightPanel.add(search${item.field.name?capFirst}Panel,
                    new GridBagConstraints(0, ${j}, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                <#assign j = j + 1/>
            </#list>
            rightPanel.add(searchButton,
                new GridBagConstraints(0, ${j}, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        </#if>

        <#list frame.items?filter(it -> it.field.column.enableFormItem) as item>
            JPanel jPanel${item_index} = new JPanel(new GridBagLayout());
            jPanel${item_index}.add(new JLabel("${item.field.column.comment}"), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            <#switch item.class.simpleName>
                <#case "TextFieldFrameItem">
                    jPanel${item_index}.add(${item.field.name}TextField, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    <#break>
                <#case "PasswordFrameItem">
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
                    <#assign comboBox>
                        <#if item.field.column.associate??>
                            ${camelize(item.field.column.associate.targetTableName)}${item.field.column.associate.formItemColumnName?capFirst}ComboBox<#t>
                        <#else>
                            ${item.field.name}ComboBox<#t>
                        </#if>
                    </#assign>
                    <#if !item.field.column.associate??>
                        <#list item.options as option>
                            ${item.field.name}ComboBox.addItem("${option.title}");
                        </#list>
                    <#else>
                        ${camelize(item.field.column.associate.targetTableName)}s.forEach(it -> ${comboBox}.addItem(it.get${item.field.column.associate.formItemColumnName?capFirst}()));
                    </#if>
                    jPanel${item_index}.add(${comboBox}, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    <#break>
            </#switch>
            leftPanel.add(jPanel${item_index}, new GridBagConstraints(0, ${item_index}, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        </#list>
        JPanel operationPanel = new JPanel(new GridBagLayout());
        <#assign x = 0/>
        <#assign y = 0/>
        <#list frame.operations as operation>
            <#if y == 2>
                <#assign y = 0>
                <#assign x = x + 1>
            </#if>
            if (permissions.contains("${operation.value}")) {
                operationPanel.add(${operation.value}Button, new GridBagConstraints(${x}, ${y}, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            }
            <#assign y = y + 1>
        </#list>
        leftPanel.add(operationPanel, new GridBagConstraints(0, ${frame.items?size}, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    <#assign dataType>
        <#if frame.entity.table.associate>
            ${name}Dto<#t>
        <#else>
            ${name}<#t>
        </#if>
    </#assign>
    // 初始化数据，放进table里
    private void initData(List<${dataType}> ${name?uncapFirst}s) {
        <#assign columnNames>
            <#if frame.entity.table.associate>
                <#list frame.entity.table.columns?filter(it -> it.enableTableField && !it.associate??) as column>
                    "${column.comment}", <#t>
                </#list>
                <#list frame.entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0) as column>
                    <#list column.associate.associateResultColumns as result>
                        "${result.tableFieldTitle}"<#if result_has_next>, </#if><#t>
                    </#list>
                    <#if column_has_next>, </#if><#t>
                </#list>
            <#else>
                <#list frame.entity.table.columns?filter(it -> it.enableTableField && !it.associate??) as column>
                    "${column.comment}"<#if column_has_next>, </#if><#t>
                </#list>
            </#if>
        </#assign>
        Object[] columnNames = new String[]{${columnNames}, ""};
        Object[][] data = new Object[${name?uncapFirst}s.size()][columnNames.length];
        <#assign i = 0>
        for (int i = 0; i < ${name?uncapFirst}s.size(); i++) {
            <#list frame.entity.fields?filter(it -> it.column.enableTableField && !it.column.associate??) as field>
                data[i][${i}] = ${name?uncapFirst}s.get(i).get${field.name?capFirst}();
                <#assign i = i + 1>
            </#list>
            <#if frame.entity.table.associate??>
                <#list frame.entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0) as column>
                    <#list column.associate.associateResultColumns as result>
                        <#assign aliasColumnName = "${camelize(column.associate.targetTableName)?capFirst}${result.originColumnName?capFirst}">
                        data[i][${i}] = ${name?uncapFirst}s.get(i).get${aliasColumnName}();
                        <#assign i = i + 1>
                    </#list>
                </#list>
            </#if>
            data[i][${i}] = ${name?uncapFirst}s.get(i).getId();
        }
        TableModel tableModel = new DefaultTableModel(data, columnNames);
        jTable.setModel(tableModel);
        jTable.getColumnModel().getColumn(${i}).setMinWidth(0);
        jTable.getColumnModel().getColumn(${i}).setMaxWidth(0);
        jTable.updateUI();
    }
}