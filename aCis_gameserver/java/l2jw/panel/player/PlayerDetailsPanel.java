package l2jw.panel.player;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.l2j.gameserver.enums.PunishmentType;
import net.sf.l2j.gameserver.model.actor.Player;

import l2jw.panel.PanelTheme;

public class PlayerDetailsPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final JLabel nameLabel = new JLabel("Name: -");
	private final JLabel levelLabel = new JLabel("Level: -");
	private final JLabel classLabel = new JLabel("Class: -");
	private final JLabel hpLabel = new JLabel("HP: -");
	private final JLabel mpLabel = new JLabel("MP: -");
	private final JLabel cpLabel = new JLabel("CP: -");
	private final JLabel coordsLabel = new JLabel("Coords: -");
	private final JLabel gmLabel = new JLabel("GM: -");
	private final JLabel premiumLabel = new JLabel("Premium: -");
	private final JLabel jailLabel = new JLabel("Jail: -");

	public PlayerDetailsPanel()
	{
		super(new GridLayout(5, 2, 8, 4));

		setBackground(PanelTheme.SURFACE);
		setBorder(PanelTheme.createTitledBorder("Player Details"));

		PanelTheme.styleLabel(nameLabel);
		PanelTheme.styleLabel(levelLabel);
		PanelTheme.styleLabel(classLabel);
		PanelTheme.styleLabel(hpLabel);
		PanelTheme.styleLabel(mpLabel);
		PanelTheme.styleLabel(cpLabel);
		PanelTheme.styleLabel(coordsLabel);
		PanelTheme.styleLabel(gmLabel);
		PanelTheme.styleLabel(premiumLabel);
		PanelTheme.styleLabel(jailLabel);

		add(nameLabel);
		add(levelLabel);
		add(classLabel);
		add(hpLabel);
		add(mpLabel);
		add(cpLabel);
		add(coordsLabel);
		add(gmLabel);
		add(premiumLabel);
		add(jailLabel);
	}

	public void setPlayer(Player player)
	{
		if (player == null)
		{
			nameLabel.setText("Name: -");
			levelLabel.setText("Level: -");
			classLabel.setText("Class: -");
			hpLabel.setText("HP: -");
			mpLabel.setText("MP: -");
			cpLabel.setText("CP: -");
			coordsLabel.setText("Coords: -");
			gmLabel.setText("GM: -");
			premiumLabel.setText("Premium: -");
			jailLabel.setText("Jail: -");
			return;
		}

		nameLabel.setText("Name: " + player.getName());
		levelLabel.setText("Level: " + player.getStatus().getLevel());
		classLabel.setText("Class: " + player.getClassId().name());
		hpLabel.setText("HP: " + (int) player.getStatus().getHp() + " / " + player.getStatus().getMaxHp());
		mpLabel.setText("MP: " + (int) player.getStatus().getMp() + " / " + player.getStatus().getMaxMp());
		cpLabel.setText("CP: " + (int) player.getStatus().getCp() + " / " + player.getStatus().getMaxCp());
		coordsLabel.setText("Coords: " + player.getX() + ", " + player.getY() + ", " + player.getZ());
		gmLabel.setText("GM: " + (player.isGM() ? "Yes" : "No"));
		premiumLabel.setText("Premium: " + (PlayerCategoryUtil.isPremium(player) ? "Yes" : "No"));
		jailLabel.setText("Jail: " + (player.getPunishment().getType() == PunishmentType.JAIL ? "Yes" : "No"));
	}
}