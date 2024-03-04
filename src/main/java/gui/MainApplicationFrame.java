package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;
import java.util.ResourceBundle;
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
    private final ResourceBundle bundle = ResourceBundle.getBundle("resources",
        Locale.getDefault());
    private final JDesktopPane desktopPane = new JDesktopPane();
    private class MainMenuBar extends JMenuBar {

        private MainMenuBar() {
            add(createExitMenu());
            add(createLookAndFeelMenu());
            add(createTestMenu());
        }
        private JMenuItem createJMenuItem(String s, Integer m, ActionListener l) {
            JMenuItem item = new JMenuItem(s, m);
            item.addActionListener(l);
            return item;
        }
        private JMenu createLookAndFeelMenu() {

            JMenu lookAndFeelMenu = new JMenu(bundle.getString("lookAndFeelMenu.s"));
            lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
            lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(bundle
                .getString("lookAndFeelMenu.getAccessibleContext.setAccessibleDescription"));
            lookAndFeelMenu.add(createJMenuItem(
                bundle.getString("systemLookAndFeel.text"), KeyEvent.VK_S,
                (event) -> {
                    MainApplicationFrame.this
                        .setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    this.invalidate();
                }));
            lookAndFeelMenu.add(createJMenuItem(bundle
                    .getString("crossplatformLookAndFeel.text"), KeyEvent.VK_S, (event) -> {
                    MainApplicationFrame.this
                        .setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    this.invalidate();
                }));
            return lookAndFeelMenu;
        }

        private JMenu createTestMenu() {

            JMenu testMenu = new JMenu(bundle.getString("testMenu.s"));
            testMenu.setMnemonic(KeyEvent.VK_T);
            testMenu.getAccessibleContext().setAccessibleDescription(bundle
                .getString("testMenu.getAccessibleContext.setAccessibleDescription"));
            testMenu.add(createJMenuItem(
                bundle.getString("addLogMessageItem.text"),
                KeyEvent.VK_S,
                (event) -> Logger
                    .debug(bundle.getString("Logger.debug.strMessage.addLine"))
            ));
            return testMenu;
        }

        private JMenu createExitMenu() {
            JMenu exitMenu = new JMenu(bundle.getString("exitMenu.s"));
            JMenuItem exitMenuItem = createJMenuItem(
                bundle.getString("exitMenuItem.text"),
                KeyEvent.VK_Q,
                (event) -> {
                    WindowEvent closeEvent = new WindowEvent(
                        MainApplicationFrame.this, WindowEvent.WINDOW_CLOSING);
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
                }
            );
            exitMenu.getAccessibleContext().setAccessibleDescription(bundle
                .getString("exitMenu.getAccessibleContext.setAccessibleDescription"));
            exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.SHIFT_DOWN_MASK));
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
        
        
        addWindow(createLogWindow(), 300, 800);

        addWindow(new GameWindow(bundle.getString("gameWindow.title")),
            400, 400);
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
        LogWindow logWindow = new LogWindow(bundle
            .getString("logWindow.title"), Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(bundle.getString("Logger.debug.strMessage.status"));
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame, Integer width, Integer height) {
        if (width != null && height != null) {
            frame.setSize(width, height);
        }
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private void ExitConfirm() {
        int confirm = JOptionPane.showConfirmDialog(this,
            bundle.getString("confirm.message"),
            bundle.getString("confirm.title"),
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void setLookAndFeel(String className)
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
