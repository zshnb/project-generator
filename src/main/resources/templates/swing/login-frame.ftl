package ${framePackageName};

import ${configPackageName}.GlobalSession;
import ${configPackageName}.GlobalSqlSessionFactory;
import ${entityPackageName}.User;
import ${mapperPackageName}.RoleMapper;
import ${mapperPackageName}.UserMapper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame {
    private JPanel parentPanel = new JPanel(new GridBagLayout());
    private JTextField userNameTextField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JComboBox<String> roleComboBox = new JComboBox<>();
    private JButton loginButton = new JButton("登录");
    private JButton registerButton = new JButton("注册");
    private final RoleMapper roleMapper = GlobalSqlSessionFactory.getSqlSession().getMapper(RoleMapper.class);
    private final UserMapper userMapper = GlobalSqlSessionFactory.getSqlSession().getMapper(UserMapper.class);

    public LoginFrame() {
        initUI();
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String userName = userNameTextField.getText();
                String password = String.valueOf(passwordField.getPassword());
                String role = (String) roleComboBox.getSelectedItem();
                User user = userMapper.selectByUserNameAndPasswordAndRole(userName, password, role);
                if (user != null) {
                    GlobalSession.user = user;
                    new MainFrame();
                } else {
                    JOptionPane.showMessageDialog(null, "用户名或密码错误");
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new RegisterFrame();
            }
        });
        JFrame frame = new JFrame();
        frame.setLocation(10, 10);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        frame.add(parentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void initUI() {
        JPanel jPanel0 = new JPanel(new GridBagLayout());
        jPanel0.add(new JLabel("用户名"), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        jPanel0.add(userNameTextField, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        parentPanel.add(jPanel0, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        JPanel jPanel1 = new JPanel(new GridBagLayout());
        jPanel1.add(new JLabel("密码"), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        jPanel1.add(passwordField, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        parentPanel.add(jPanel1, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        JPanel jPanel2 = new JPanel(new GridBagLayout());
        jPanel2.add(new JLabel("角色"), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        roleMapper.list().forEach(it -> roleComboBox.addItem(it.getDescription()));
        jPanel2.add(roleComboBox, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        parentPanel.add(jPanel2, new GridBagConstraints(0, 2, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        JPanel operationPanel = new JPanel(new GridBagLayout());
        operationPanel.add(loginButton, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        operationPanel.add(registerButton, new GridBagConstraints(1, 0, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        parentPanel.add(operationPanel, new GridBagConstraints(0, 3, 1, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        parentPanel.add(new JPanel(), new GridBagConstraints(0, 4, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    public static void main(String[] args) {
        new LoginFrame();
    }
}
