package l2jw.panel;

import java.awt.BorderLayout;
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

public class ServerStatusPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final HeaderPanel headerPanel = new HeaderPanel();
	private final JLabel detailsLabel = new JLabel("GS Uptime: 0s");

	private final ScheduledExecutorService scheduler;

	public ServerStatusPanel()
	{
		super(new BorderLayout(0, 8));

		setOpaque(true);
		setBackground(PanelTheme.BACKGROUND);
		setBorder(PanelTheme.createTitledBorder("GameServer Monitor"));

		PanelTheme.styleMuted(detailsLabel);

		add(headerPanel, BorderLayout.CENTER);
		add(detailsLabel, BorderLayout.SOUTH);

		scheduler = Executors.newSingleThreadScheduledExecutor(r ->
		{
			Thread thread = new Thread(r, "ServerStatusPanel");
			thread.setDaemon(true);
			return thread;
		});

		scheduler.scheduleAtFixedRate(this::refreshStats, 0, 1, TimeUnit.SECONDS);
	}

	private void refreshStats()
	{
		final String cpuText = getCpuUsage();
		final String ramText = getUsedMemoryMb() + " MB / " + getTotalMemoryMb() + " MB";
		final String threadText = String.valueOf(getThreadCount());

		final GameServer gs = GameServer.getInstance();

		final String gsStatus = ((gs != null && gs.getSelectorThread() != null) ? "ONLINE" : "OFFLINE");
		final String playersText = String.valueOf((gs != null) ? World.getInstance().getPlayers().size() : 0);
		final String detailsText = "GS Uptime: " + formatUptime(getGameServerUptime());

		SwingUtilities.invokeLater(() ->
		{
			headerPanel.setCpu(cpuText);
			headerPanel.setRam(ramText);
			headerPanel.setThreads(threadText);
			headerPanel.setGameServer(gsStatus);
			headerPanel.setPlayers(playersText);

			detailsLabel.setText(detailsText);
		});
	}

	private static String getCpuUsage()
	{
		try
		{
			var bean = ManagementFactory.getOperatingSystemMXBean();
			if (bean instanceof OperatingSystemMXBean osBean)
			{
				double load = osBean.getProcessCpuLoad();
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
		Runtime runtime = Runtime.getRuntime();
		return (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
	}

	private static long getTotalMemoryMb()
	{
		Runtime runtime = Runtime.getRuntime();
		return runtime.totalMemory() / 1024 / 1024;
	}

	private static int getThreadCount()
	{
		ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
		return threadBean.getThreadCount();
	}

	private static long getGameServerUptime()
	{
		GameServer server = GameServer.getInstance();
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