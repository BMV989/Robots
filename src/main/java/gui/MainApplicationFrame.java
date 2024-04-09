package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
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
import model.Robot;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    public static final ResourceBundle bundle = ResourceBundle.getBundle("resources",
        Locale.getDefault());
    private final JDesktopPane desktopPane = new JDesktopPane();
    private class MainMenuBar extends JMenuBar {

        private MainMenuBar() {
            add(createExitMenu());
            add(createLookAndFeelMenu());
            add(createTestMenu());
            add(createSaveMenu());
        }
        private JMenuItem createJMenuItem(String s, Integer m, ActionListener l) {
            JMenuItem item = new JMenuItem(s, m);
            item.addActionListener(l);
            return item;
        }
        private JMenu createJMenu(String s, Integer m, String desc) {
            JMenu menu = new JMenu(s);
            menu.setMnemonic(m);
            menu.getAccessibleContext().setAccessibleDescription(desc);
            return menu;
        }
        private JMenu createLookAndFeelMenu() {
            JMenu lookAndFeelMenu = createJMenu(
                bundle.getString("lookAndFeelMenu.s"),
                KeyEvent.VK_V,
                bundle
                    .getString("lookAndFeelMenu.getAccessibleContext.setAccessibleDescription")
            );
            lookAndFeelMenu.add(createJMenuItem(
                bundle.getString("systemLookAndFeel.text"), KeyEvent.VK_S,
                e -> MainApplicationFrame.this
                    .setLookAndFeel(UIManager.getSystemLookAndFeelClassName())));
            lookAndFeelMenu.add(createJMenuItem(bundle
                    .getString("crossplatformLookAndFeel.text"), KeyEvent.VK_S, e ->
                MainApplicationFrame.this
                        .setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName())));
            return lookAndFeelMenu;
        }
        private JMenu createSaveMenu() {
            JMenu saveMenu = createJMenu(
                bundle.getString("saveMenu.s"),
                KeyEvent.VK_M,
                bundle
                    .getString("saveMenu.getAccessibleContext.setAccessibleDescription")
            );
            saveMenu.add(createJMenuItem(
                bundle.getString("saveMenu.s"),
                KeyEvent.VK_0,
                e -> MainApplicationFrame.this.saveWindows()
            ));
            return saveMenu;
        }

        private JMenu createTestMenu() {
            JMenu testMenu = createJMenu(
                bundle.getString("testMenu.s"),
                KeyEvent.VK_T,
                bundle.getString("testMenu.getAccessibleContext.setAccessibleDescription"));
            testMenu.add(createJMenuItem(
                bundle.getString("addLogMessageItem.text"),
                KeyEvent.VK_S,
                e -> Logger
                    .debug(bundle.getString("Logger.debug.strMessage.addLine"))
            ));
            return testMenu;
        }

        private JMenu createExitMenu() {
            JMenu exitMenu = createJMenu(
                bundle.getString("exitMenu.s"),
                KeyEvent.VK_E,
                bundle.getString("exitMenu.getAccessibleContext.setAccessibleDescription")
            );
            JMenuItem exitMenuItem = createJMenuItem(
                bundle.getString("exitMenuItem.text"),
                KeyEvent.VK_Q,
                e -> {
                    WindowEvent closeEvent = new WindowEvent(
                        MainApplicationFrame.this, WindowEvent.WINDOW_CLOSING);
                    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
                }
            );
            exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.SHIFT_DOWN_MASK));
            exitMenu.add(exitMenuItem);
            return exitMenu;
        }
    }
    private void saveWindows() {
        for (var frame : desktopPane.getAllFrames()) {
            if (frame instanceof IFrameWithState)
                ((IFrameWithState) frame).saveWindow();
        }
    }

    public MainApplicationFrame() {
       initUI();
    }
    private void initUI() {
        int inset = 50;
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        var robot = new Robot(10, 10);
        addWindow(new RobotInfo(robot), 300, 300);
        addWindow(createLogWindow());
        addWindow(new GameWindow(robot),
            400, 400);
        for (var frame : desktopPane.getAllFrames())
            if (frame instanceof IFrameWithState)
                ((IFrameWithState) frame).restoreWindow();
        setJMenuBar(new MainMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitConfirm();
            }
        });
    }

    private LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug(bundle.getString("Logger.debug.strMessage.status"));
        return logWindow;
    }
    private void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    private void addWindow(JInternalFrame frame, int width, int height) {
        frame.setSize(width, height);
        addWindow(frame);
    }

    private void exitConfirm() {
        int confirm = JOptionPane.showConfirmDialog(this,
            bundle.getString("confirm.message"),
            bundle.getString("confirm.title"),
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            saveOnExit();
            setVisible(false);
            Arrays.asList(desktopPane.getAllFrames()).forEach(JInternalFrame::dispose);
            dispose();
        }
    }
    private void saveOnExit() {
        int confirm = JOptionPane.showConfirmDialog(this,
            bundle.getString("save.message"),
            bundle.getString("saveMenu.s"),
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) saveWindows();
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
        finally {
            invalidate();
        }
    }
}
