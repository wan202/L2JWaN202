package l2jw.panel;

import java.awt.GridLayout;
import java.net.InetAddress;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.GameServer;

public class QuickInfoPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final JLabel javaLabel = new JLabel("Java: -");
	private final JLabel osLabel = new JLabel("OS: -");
	private final JLabel userLabel = new JLabel("User: -");
	private final JLabel hostLabel = new JLabel("Host: -");
	private final JLabel gsPortLabel = new JLabel("GS Port: -");
	private final JLabel gsBindLabel = new JLabel("GS Bind: -");
	private final JLabel heapMaxLabel = new JLabel("Heap Max: -");
	private final JLabel heapUsedLabel = new JLabel("Heap Used: -");
	private final JLabel statusLabel = new JLabel("Status: -");

	public QuickInfoPanel()
	{
		super(new GridLayout(9, 1, 4, 4));

		setBackground(PanelTheme.BACKGROUND);
		setBorder(PanelTheme.createTitledBorder("GameServer Info"));

		PanelTheme.styleLabel(javaLabel);
		PanelTheme.styleLabel(osLabel);
		PanelTheme.styleLabel(userLabel);
		PanelTheme.styleLabel(hostLabel);
		PanelTheme.styleLabel(gsPortLabel);
		PanelTheme.styleLabel(gsBindLabel);
		PanelTheme.styleLabel(heapMaxLabel);
		PanelTheme.styleLabel(heapUsedLabel);
		PanelTheme.styleLabel(statusLabel);

		add(javaLabel);
		add(osLabel);
		add(userLabel);
		add(hostLabel);
		add(gsPortLabel);
		add(gsBindLabel);
		add(heapMaxLabel);
		add(heapUsedLabel);
		add(statusLabel);

		refresh();
	}

	public void refresh()
	{
		SwingUtilities.invokeLater(() ->
		{
			javaLabel.setText("Java: " + System.getProperty("java.version"));
			osLabel.setText("OS: " + System.getProperty("os.name"));
			userLabel.setText("User: " + System.getProperty("user.name"));
			hostLabel.setText("Host: " + getHostName());

			gsPortLabel.setText("GS Port: " + Config.GAMESERVER_PORT);
			gsBindLabel.setText("GS Bind: " + Config.GAMESERVER_HOSTNAME);

			heapMaxLabel.setText("Heap Max: " + getMaxMemoryMb() + " MB");
			heapUsedLabel.setText("Heap Used: " + getUsedMemoryMb() + " MB");

			GameServer gs = GameServer.getInstance();
			statusLabel.setText("Status: " + ((gs != null && gs.getSelectorThread() != null) ? "RUNNING" : "STOPPED"));
		});
	}

	private static String getHostName()
	{
		try
		{
			return InetAddress.getLocalHost().getHostName();
		}
		catch (Exception e)
		{
			return "Unknown";
		}
	}

	private static long getUsedMemoryMb()
	{
		Runtime runtime = Runtime.getRuntime();
		return (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
	}

	private static long getMaxMemoryMb()
	{
		Runtime runtime = Runtime.getRuntime();
		return runtime.maxMemory() / 1024 / 1024;
	}
}