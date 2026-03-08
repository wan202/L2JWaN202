package l2jw.panel.player;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;

import l2jw.panel.PanelTheme;

public class PlayersOnlinePanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final DefaultTableModel model = new DefaultTableModel(
		new Object[]
		{
			"Name",
			"Type",
			"Level",
			"Class",
			"Account"
		}, 0)
	{
		private static final long serialVersionUID = 1L;

		@Override
		public boolean isCellEditable(int row, int column)
		{
			return false;
		}
	};

	private final JTable table = new JTable(model);
	private final PlayerActionPanel futureToolsPanel = new PlayerActionPanel();
	private final ScheduledExecutorService scheduler;

	public PlayersOnlinePanel()
	{
		super(new BorderLayout(8, 8));

		setBackground(PanelTheme.BACKGROUND);
		setBorder(PanelTheme.createTitledBorder("Players Online"));

		table.setFillsViewportHeight(true);
		table.setRowHeight(22);
		table.setBackground(PanelTheme.SURFACE);
		table.setForeground(PanelTheme.TEXT);
		table.setGridColor(PanelTheme.BORDER);
		table.setSelectionBackground(PanelTheme.BLUE);
		table.setSelectionForeground(PanelTheme.TEXT);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		table.getSelectionModel().addListSelectionListener(e ->
		{
			if (e.getValueIsAdjusting())
				return;

			final int selectedRow = table.getSelectedRow();
			if (selectedRow < 0)
				return;

			final Object value = model.getValueAt(selectedRow, 0);
			if (value == null)
				return;

			final String playerName = value.toString();
			final Player player = PlayerAdminService.findOnlinePlayer(playerName);

			futureToolsPanel.setSelectedPlayer(player);
		});

		final JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getViewport().setBackground(PanelTheme.SURFACE);
		scrollPane.setBorder(javax.swing.BorderFactory.createLineBorder(PanelTheme.BORDER));

		add(scrollPane, BorderLayout.CENTER);
		add(futureToolsPanel, BorderLayout.SOUTH);

		scheduler = Executors.newSingleThreadScheduledExecutor(r ->
		{
			final Thread thread = new Thread(r, "PlayersOnlinePanel");
			thread.setDaemon(true);
			return thread;
		});

		scheduler.scheduleAtFixedRate(this::refreshData, 0, 3, TimeUnit.SECONDS);
	}

	private void refreshData()
	{
		final List<Player> players = new ArrayList<>(World.getInstance().getPlayers());
		players.sort(Comparator.comparing(Player::getName, String.CASE_INSENSITIVE_ORDER));

		final List<Object[]> rows = new ArrayList<>();

		for (Player player : players)
		{
			final String type;

			if (PlayerCategoryUtil.isGm(player))
				type = "GM";
			else if (PlayerCategoryUtil.isPremium(player))
				type = "Premium";
			else
				type = "Normal";

			rows.add(new Object[]
			{
				player.getName(),
				type,
				player.getStatus().getLevel(),
				player.getClassId().name(),
				"-"
			});
		}

		SwingUtilities.invokeLater(() ->
		{
			model.setRowCount(0);
			for (Object[] row : rows)
				model.addRow(row);
		});
	}

	public void shutdown()
	{
		scheduler.shutdownNow();
	}
}