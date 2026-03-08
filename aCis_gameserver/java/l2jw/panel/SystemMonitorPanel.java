package l2jw.panel;

import java.awt.GridLayout;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.sun.management.OperatingSystemMXBean;

import net.sf.l2j.gameserver.GameServer;
import net.sf.l2j.gameserver.model.World;

public class SystemMonitorPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final JLabel statusLabel = new JLabel("Status: OFFLINE");
	private final JLabel cpuLabel = new JLabel("CPU: 0%");
	private final JLabel ramLabel = new JLabel("RAM: 0 MB");
	private final JLabel playersLabel = new JLabel("Players: 0");
	private final JLabel threadsLabel = new JLabel("Threads: 0");
	private final JLabel uptimeLabel = new JLabel("Uptime: 0s");

	private final ScheduledExecutorService scheduler;

	public SystemMonitorPanel()
	{
		super(new GridLayout(2, 3, 10, 8));

		setOpaque(true);
		setBackground(PanelTheme.SURFACE);
		setBorder(javax.swing.BorderFactory.createTitledBorder(
			javax.swing.BorderFactory.createLineBorder(PanelTheme.BORDER),
			"Server Monitor",
			javax.swing.border.TitledBorder.LEFT,
			javax.swing.border.TitledBorder.TOP,
			PanelTheme.FONT_TITLE,
			PanelTheme.TEXT));

		PanelTheme.styleLabel(statusLabel);
		PanelTheme.styleLabel(cpuLabel);
		PanelTheme.styleLabel(ramLabel);
		PanelTheme.styleLabel(playersLabel);
		PanelTheme.styleLabel(threadsLabel);
		PanelTheme.styleLabel(uptimeLabel);

		add(statusLabel);
		add(cpuLabel);
		add(ramLabel);
		add(playersLabel);
		add(threadsLabel);
		add(uptimeLabel);

		scheduler = Executors.newSingleThreadScheduledExecutor(r ->
		{
			final Thread thread = new Thread(r, "SystemMonitorPanel");
			thread.setDaemon(true);
			return thread;
		});

		scheduler.scheduleAtFixedRate(this::refreshStats, 0, 1, TimeUnit.SECONDS);
	}

	private void refreshStats()
	{
		final String statusText = "Status: " + getServerStatus();
		final String cpuText = "CPU: " + getCpuUsage();
		final String ramText = "RAM: " + getUsedMemoryMb() + " MB / " + getTotalMemoryMb() + " MB";
		final String playersText = "Players: " + World.getInstance().getPlayers().size();
		final String threadsText = "Threads: " + getThreadCount();
		final String uptimeText = "Uptime: " + formatUptime(getUptimeMillis());

		SwingUtilities.invokeLater(() ->
		{
			statusLabel.setText(statusText);
			cpuLabel.setText(cpuText);
			ramLabel.setText(ramText);
			playersLabel.setText(playersText);
			threadsLabel.setText(threadsText);
			uptimeLabel.setText(uptimeText);
		});
	}

	private static String getServerStatus()
	{
		try
		{
			final GameServer server = GameServer.getInstance();
			if (server != null && server.getSelectorThread() != null)
				return "ONLINE";
		}
		catch (Exception e)
		{
		}
		return "OFFLINE";
	}

	private static String getCpuUsage()
	{
		try
		{
			final var bean = ManagementFactory.getOperatingSystemMXBean();
			if (bean instanceof OperatingSystemMXBean osBean)
			{
				final double load = osBean.getProcessCpuLoad();
				if (load >= 0)
					return String.format("%.2f%%", load * 100.0);
			}
		}
		catch (Exception e)
		{
		}
		return "N/A";
	}

	private static long getUsedMemoryMb()
	{
		final Runtime runtime = Runtime.getRuntime();
		return (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
	}

	private static long getTotalMemoryMb()
	{
		final Runtime runtime = Runtime.getRuntime();
		return runtime.totalMemory() / 1024 / 1024;
	}

	private static int getThreadCount()
	{
		final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
		return threadBean.getThreadCount();
	}

	private static long getUptimeMillis()
	{
		final GameServer server = GameServer.getInstance();
		if (server == null)
			return 0L;

		return System.currentTimeMillis() - server.getServerStartTime();
	}

	private static String formatUptime(long millis)
	{
		long totalSeconds = millis / 1000;
		long days = totalSeconds / 86400;
		long hours = (totalSeconds % 86400) / 3600;
		long minutes = (totalSeconds % 3600) / 60;
		long seconds = totalSeconds % 60;

		if (days > 0)
			return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);

		if (hours > 0)
			return String.format("%02dh %02dm %02ds", hours, minutes, seconds);

		if (minutes > 0)
			return String.format("%02dm %02ds", minutes, seconds);

		return seconds + "s";
	}

	public void shutdown()
	{
		scheduler.shutdownNow();
	}
}