package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private class MainMenuBar extends JMenuBar {

        private MainMenuBar() {
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
                    MainApplicationFrame.this
                        .setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    this.invalidate();
                });
                lookAndFeelMenu.add(systemLookAndFeel);
            }

            {
                JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема",
                    KeyEvent.VK_S);
                crossplatformLookAndFeel.addActionListener((event) -> {
                    MainApplicationFrame.this
                        .setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
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
                    WindowEvent closeEvent = new WindowEvent(
                        MainApplicationFrame.this, WindowEvent.WINDOW_CLOSING);
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
                }
            );
            exitMenu.add(exitMenuItem);
            return exitMenu;
        }
    }

    public MainApplicationFrame() {
        int inset = 50;
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        
        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        setJMenuBar(new MainMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ExitConfirm();
            }
        });
    }
    
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void ExitConfirm() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Вы уверены?",
            "Выйти",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    protected void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
