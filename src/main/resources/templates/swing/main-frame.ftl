package ${framePackageName};

import ${configPackageName}.GlobalSession;
import ${configPackageName}.GlobalSqlSessionFactory;
import ${mapperPackageName}.MenuMapper;
import ${entityPackageName}.Menu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MainFrame {
    private JPanel parentPanel = new JPanel(new GridBagLayout());
    private MenuMapper menuMapper = GlobalSqlSessionFactory.getSqlSession().getMapper(MenuMapper.class);

    public MainFrame() {
        List<Menu> menus = menuMapper.list(GlobalSession.user.getRole());
        for (int i = 0; i < menus.size(); i++) {
            JButton button0 = new JButton(menus.get(i).getName());
            switch (menus.get(i).getHref()) {
                <#list menus as menu>
                case "${menu.href}": {
                    button0.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            new ${menu.href?cap_first}Frame();
                        }
                    });
                }
                </#list>
            }
            JPanel jPanel0 = new JPanel();
            jPanel0.add(button0);
            parentPanel.add(jPanel0, new GridBagConstraints(0, i, 2, 1, 1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        }
        JFrame frame = new JFrame();
        frame.setLocation(10, 10);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());
        frame.add(parentPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }
}
