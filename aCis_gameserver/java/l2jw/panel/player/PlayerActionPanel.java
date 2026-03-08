package l2jw.panel.player;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.sf.l2j.gameserver.model.actor.Player;

import l2jw.panel.PanelTheme;

public class PlayerActionPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final JTextField searchField = new JTextField();
	private final JLabel selectedPlayerLabel = new JLabel("Selected: none");
	private final PlayerDetailsPanel detailsPanel = new PlayerDetailsPanel();

	private Player selectedPlayer;
	private PlayerSearchResult selectedResult;

	public PlayerActionPanel()
	{
		super(new BorderLayout(8, 8));

		setBackground(PanelTheme.SURFACE);
		setBorder(PanelTheme.createTitledBorder("Future Tools"));

		PanelTheme.styleLabel(selectedPlayerLabel);

		final JPanel topPanel = new JPanel(new BorderLayout(8, 8));
		topPanel.setBackground(PanelTheme.SURFACE);

		final JButton searchButton = new JButton("Search");
		PanelTheme.styleButton(searchButton, PanelTheme.BLUE);

		topPanel.add(searchField, BorderLayout.CENTER);
		topPanel.add(searchButton, BorderLayout.EAST);

		final JPanel centerPanel = new JPanel(new BorderLayout(8, 8));
		centerPanel.setBackground(PanelTheme.SURFACE);
		centerPanel.add(selectedPlayerLabel, BorderLayout.NORTH);
		centerPanel.add(detailsPanel, BorderLayout.CENTER);

		final JPanel actionsPanel = new JPanel(new GridLayout(2, 3, 8, 8));
		actionsPanel.setBackground(PanelTheme.SURFACE);

		final JButton giveItemButton = new JButton("Give Item");
		final JButton deleteItemButton = new JButton("Delete Item");
		final JButton teleportButton = new JButton("Teleport");
		final JButton kickButton = new JButton("Kick");
		final JButton jailButton = new JButton("Jail");
		final JButton unjailButton = new JButton("Unjail");

		PanelTheme.styleButton(giveItemButton, PanelTheme.GREEN);
		PanelTheme.styleButton(deleteItemButton, PanelTheme.ORANGE);
		PanelTheme.styleButton(teleportButton, PanelTheme.BLUE);
		PanelTheme.styleButton(kickButton, PanelTheme.RED);
		PanelTheme.styleButton(jailButton, PanelTheme.ORANGE);
		PanelTheme.styleButton(unjailButton, PanelTheme.GREEN);

		actionsPanel.add(giveItemButton);
		actionsPanel.add(deleteItemButton);
		actionsPanel.add(teleportButton);
		actionsPanel.add(kickButton);
		actionsPanel.add(jailButton);
		actionsPanel.add(unjailButton);

		add(topPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(actionsPanel, BorderLayout.SOUTH);

		searchButton.addActionListener(e -> searchPlayer());

		giveItemButton.addActionListener(e -> giveItem());
		deleteItemButton.addActionListener(e -> deleteItem());
		teleportButton.addActionListener(e -> teleportPlayer());
		kickButton.addActionListener(e -> kickPlayer());
		jailButton.addActionListener(e -> jailPlayer());
		unjailButton.addActionListener(e -> unjailPlayer());
	}

	public void setSelectedPlayer(Player player)
	{
		selectedPlayer = player;
		selectedResult = (player == null) ? null : new PlayerSearchResult(player, player.getName(), true);

		detailsPanel.setPlayer(player);

		if (player == null)
		{
			selectedPlayerLabel.setText("Selected: none");
			searchField.setText("");
			return;
		}

		selectedPlayerLabel.setText("Selected: " + player.getName() + " [ONLINE]");
		searchField.setText(player.getName());
	}

	public void setSelectedResult(PlayerSearchResult result)
	{
		selectedResult = result;
		selectedPlayer = (result != null) ? result.getOnlinePlayer() : null;

		detailsPanel.setPlayer(selectedPlayer);

		if (result == null)
		{
			selectedPlayerLabel.setText("Selected: none");
			searchField.setText("");
			return;
		}

		if (result.isOnline())
			selectedPlayerLabel.setText("Selected: " + result.getPlayerName() + " [ONLINE]");
		else
			selectedPlayerLabel.setText("Selected: " + result.getPlayerName() + " [OFFLINE]");

		searchField.setText(result.getPlayerName());
	}

	private void searchPlayer()
	{
		final String name = searchField.getText().trim();
		final PlayerSearchResult result = PlayerAdminService.findPlayer(name);

		if (result == null)
		{
			setSelectedResult(null);
			JOptionPane.showMessageDialog(this, "Player não encontrado.");
			return;
		}

		setSelectedResult(result);

		if (result.isOffline())
			JOptionPane.showMessageDialog(this, "Player encontrado offline.");
	}

	private void giveItem()
	{
		if (!validateSelectedPlayer())
			return;

		final String itemIdStr = JOptionPane.showInputDialog(this, "Item ID:");
		final String countStr = JOptionPane.showInputDialog(this, "Count:");

		if (itemIdStr == null || countStr == null)
			return;

		try
		{
			final int itemId = Integer.parseInt(itemIdStr.trim());
			final int count = Integer.parseInt(countStr.trim());

			PlayerAdminService.giveItem(selectedPlayer, itemId, count);
			JOptionPane.showMessageDialog(this, "Item enviado com sucesso.");
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Valores inválidos.");
		}
	}

	private void deleteItem()
	{
		if (!validateSelectedPlayer())
			return;

		final String itemIdStr = JOptionPane.showInputDialog(this, "Item ID:");
		final String countStr = JOptionPane.showInputDialog(this, "Count:");

		if (itemIdStr == null || countStr == null)
			return;

		try
		{
			final int itemId = Integer.parseInt(itemIdStr.trim());
			final int count = Integer.parseInt(countStr.trim());

			PlayerAdminService.deleteItem(selectedPlayer, itemId, count);
			JOptionPane.showMessageDialog(this, "Item removido com sucesso.");
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Valores inválidos.");
		}
	}

	private void teleportPlayer()
	{
		if (!validateSelectedPlayer())
			return;

		final String xStr = JOptionPane.showInputDialog(this, "X:");
		final String yStr = JOptionPane.showInputDialog(this, "Y:");
		final String zStr = JOptionPane.showInputDialog(this, "Z:");

		if (xStr == null || yStr == null || zStr == null)
			return;

		try
		{
			final int x = Integer.parseInt(xStr.trim());
			final int y = Integer.parseInt(yStr.trim());
			final int z = Integer.parseInt(zStr.trim());

			PlayerAdminService.teleportToCoords(selectedPlayer, x, y, z);
			detailsPanel.setPlayer(selectedPlayer);
			JOptionPane.showMessageDialog(this, "Player teleportado com sucesso.");
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Coordenadas inválidas.");
		}
	}

	private void kickPlayer()
	{
		if (!validateSelectedPlayer())
			return;

		PlayerAdminService.kick(selectedPlayer);
		JOptionPane.showMessageDialog(this, "Player kickado com sucesso.");
	}

	private void jailPlayer()
	{
		if (!validateSelectedPlayer())
			return;

		final String minutesStr = JOptionPane.showInputDialog(this, "Minutos de jail:", "10");
		if (minutesStr == null)
			return;

		try
		{
			final int minutes = Integer.parseInt(minutesStr.trim());
			PlayerAdminService.jail(selectedPlayer, minutes);
			detailsPanel.setPlayer(selectedPlayer);
			JOptionPane.showMessageDialog(this, "Player preso com sucesso.");
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this, "Tempo inválido.");
		}
	}

	private void unjailPlayer()
	{
		if (!validateSelectedPlayer())
			return;

		PlayerAdminService.unjail(selectedPlayer);
		detailsPanel.setPlayer(selectedPlayer);
		JOptionPane.showMessageDialog(this, "Player solto com sucesso.");
	}

	private boolean validateSelectedPlayer()
	{
		if (selectedResult == null)
		{
			JOptionPane.showMessageDialog(this, "Selecione um player primeiro.");
			return false;
		}

		if (selectedResult.isOffline())
		{
			JOptionPane.showMessageDialog(this, "Essa ação exige o player online.");
			return false;
		}

		if (selectedPlayer == null)
		{
			JOptionPane.showMessageDialog(this, "Player offline ou indisponível.");
			return false;
		}

		return true;
	}
}