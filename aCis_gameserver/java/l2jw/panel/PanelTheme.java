package l2jw.panel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

public final class PanelTheme
{
	public static final Color BACKGROUND = new Color(17, 20, 26);
	public static final Color SURFACE = new Color(24, 28, 36);
	public static final Color SURFACE_2 = new Color(30, 35, 44);
	public static final Color BORDER = new Color(58, 66, 79);
	public static final Color TEXT = new Color(235, 238, 242);
	public static final Color MUTED = new Color(168, 176, 189);

	public static final Color BLUE = new Color(59, 130, 246);
	public static final Color GREEN = new Color(34, 197, 94);
	public static final Color ORANGE = new Color(245, 158, 11);
	public static final Color RED = new Color(239, 68, 68);

	public static final Font FONT = new Font("Segoe UI", Font.PLAIN, 13);
	public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
	public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 15);
	public static final Font FONT_CARD = new Font("Segoe UI", Font.BOLD, 16);
	public static final Font FONT_CONSOLE = new Font("Consolas", Font.PLAIN, 13);

	private PanelTheme()
	{
	}

	public static void applyDefaults()
	{
		UIManager.put("Panel.background", new ColorUIResource(BACKGROUND));
		UIManager.put("Label.foreground", new ColorUIResource(TEXT));
		UIManager.put("Label.font", new FontUIResource(FONT));
		UIManager.put("Button.font", new FontUIResource(FONT_BOLD));
		UIManager.put("Button.foreground", new ColorUIResource(TEXT));
		UIManager.put("TextArea.background", new ColorUIResource(new Color(10, 12, 16)));
		UIManager.put("TextArea.foreground", new ColorUIResource(TEXT));
		UIManager.put("TextArea.caretForeground", new ColorUIResource(TEXT));
		UIManager.put("TextArea.font", new FontUIResource(FONT_CONSOLE));
	}

	public static JPanel createSurface()
	{
		JPanel panel = new JPanel();
		panel.setBackground(SURFACE);
		panel.setBorder(BorderFactory.createLineBorder(BORDER));
		return panel;
	}

	public static TitledBorder createTitledBorder(String title)
	{
		return BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder(BORDER),
			title,
			TitledBorder.LEFT,
			TitledBorder.TOP,
			FONT_TITLE,
			TEXT
		);
	}

	public static void styleConsole(JTextArea area)
	{
		area.setBackground(new Color(10, 12, 16));
		area.setForeground(TEXT);
		area.setCaretColor(TEXT);
		area.setFont(FONT_CONSOLE);
		area.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
	}

	public static void styleButton(JButton button, Color color)
	{
		button.setBackground(color);
		button.setForeground(TEXT);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
		button.setOpaque(true);
		button.setFont(FONT_BOLD);
	}

	public static void styleLabel(JLabel label)
	{
		label.setForeground(TEXT);
		label.setFont(FONT_BOLD);
	}

	public static void styleMuted(JLabel label)
	{
		label.setForeground(MUTED);
		label.setFont(FONT);
	}
}