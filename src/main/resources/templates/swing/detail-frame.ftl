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
<#assign dataType>
    <#if frame.entity.table.associate>
        ${name}Dto<#t>
    <#else>
        ${name}<#t>
    </#if>
</#assign>
public class ${name}DetailFrame {
    JPanel parentPanel = new JPanel(new GridBagLayout());
    JScrollPane scrollPane = new JScrollPane();

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
    private ${dataType} ${name?uncapFirst};

    public ${name}DetailFrame(${dataType} ${name?uncapFirst}) {
        initUI();
        JFrame frame = new JFrame();
        frame.setTitle("${frame.entity.table.comment}界面");
        frame.setLocation(10, 10);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        frame.add(parentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // 初始化窗口控件
    private void initUI() {
        <#list frame.items?filter(it -> it.field.column.enableFormItem) as item>
            JPanel jPanel${item_index} = new JPanel(new GridBagLayout());
            jPanel${item_index}.add(new JLabel("${item.field.column.comment}"), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
            <#switch item.class.simpleName>
                <#case "TextFieldFrameItem">
                    ${item.field.name}TextField.setText(${name?uncapFirst}.get${item.field.name?capFirst}());
                    jPanel${item_index}.add(${item.field.name}TextField, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    <#break>
                <#case "PasswordFrameItem">
                    passwordField.setText(${name?uncapFirst}.get${item.field.name?capFirst});
                    jPanel${item_index}.add(passwordField, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    <#break>
                <#case "RadioFrameItem">
                    <#list item.options as option>
                        JRadioButton ${item.field.name}JRadioButton${option_index} = new JRadioButton("${option.title}");
                        ${item.field.name}ButtonGroup.add(${item.field.name}JRadioButton${option_index});
                        jPanel${item_index}.add(${item.field.name}JRadioButton${option_index}, new GridBagConstraints(${option_index + 1}, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    </#list>
                        Enumeration<AbstractButton> ${item.field.name}ButtonGroupIterator = ${item.field.name}ButtonGroup.getElements();
                            while (${item.field.name}ButtonGroupIterator.hasMoreElements()) {
                            AbstractButton abstractButton = ${item.field.name}ButtonGroupIterator.nextElement();
                                if (abstractButton.getText().equals(${name?uncapFirst}.get${item.field.name?capFirst}())) {
                                    abstractButton.setSelected(true);
                                    break;
                                }
                        }
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
                        ${comboBox}.setSelectedItem(${name?uncapFirst}.get${item.field.name?capFirst}());
                    <#else>
                        ${camelize(item.field.column.associate.targetTableName)}s.forEach(it -> ${comboBox}.addItem(it.get${item.field.column.associate.formItemColumnName?capFirst}()));
                        <#assign variableName = "${camelize(item.field.column.associate.targetTableName)}${item.field.column.associate.formItemColumnName?capFirst}"/>
                        ${comboBox}.setSelectedItem(${name?uncapFirst}.get${variableName}());
                    </#if>
                    jPanel${item_index}.add(${comboBox}, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
                    <#break>
            </#switch>
            parentPanel.add(jPanel${item_index}, new GridBagConstraints(0, ${item_index}, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        </#list>
    }
}