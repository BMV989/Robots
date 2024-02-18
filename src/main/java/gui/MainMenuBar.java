package gui;

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import log.Logger;

public class MainMenuBar extends JMenuBar {
    private final MainApplicationFrame mainFrame;

    public MainMenuBar(MainApplicationFrame mainFrame) {
        this.mainFrame = mainFrame;
        JMenu viewModeMenu = createLookAndFeelMenu();
        JMenu testMenu = createTestMenu();
        JMenu exitMenu = createExitMenu();

        add(viewModeMenu);
        add(testMenu);
        add(exitMenu);
    }
    private JMenu createLookAndFeelMenu() {

        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
            "Управление режимом отображения приложения");

        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                mainFrame.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема",
                KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                mainFrame.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }
        return lookAndFeelMenu;
    }
    private JMenu createTestMenu() {

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription("Тестовые команды");

        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог",
                KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> Logger
                .debug("Новая строка"));
            testMenu.add(addLogMessageItem);
        }
        return testMenu;
    }
    private JMenu createExitMenu() {
        JMenu exitMenu = new JMenu("Выход");
        JMenuItem exitMenuItem = new JMenuItem("Выйти из приложения",
            KeyEvent.VK_Q);
        exitMenu.getAccessibleContext().setAccessibleDescription("Закрыть приложение");
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
            InputEvent.SHIFT_DOWN_MASK));
        exitMenuItem.addActionListener((event) -> {
                WindowEvent closeEvent = new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
            }
        );
        exitMenu.add(exitMenuItem);
        return exitMenu;
    }
}
