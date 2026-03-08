package l2jw.panel;

import java.awt.GridLayout;

import javax.swing.JPanel;

public class HeaderPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final StatusCard cpuCard = new StatusCard("CPU", "N/A");
	private final StatusCard ramCard = new StatusCard("RAM", "0 MB");
	private final StatusCard threadsCard = new StatusCard("Threads", "0");
	private final StatusCard gsCard = new StatusCard("GameServer", "OFFLINE");
	private final StatusCard playersCard = new StatusCard("Players", "0");
	private final StatusCard premiumCard = new StatusCard("Premium", "0");

	public HeaderPanel()
	{
		setLayout(new GridLayout(1, 6, 10, 10));
		setBackground(PanelTheme.BACKGROUND);

		add(cpuCard);
		add(ramCard);
		add(threadsCard);
		add(gsCard);
		add(playersCard);
		add(premiumCard);
	}

	public void setCpu(String value)
	{
		cpuCard.setValue(value);
	}

	public void setRam(String value)
	{
		ramCard.setValue(value);
	}

	public void setThreads(String value)
	{
		threadsCard.setValue(value);
	}

	public void setGameServer(String value)
	{
		gsCard.setValue(value);
	}

	public void setPlayers(String value)
	{
		playersCard.setValue(value);
	}

	public void setPremium(String value)
	{
		premiumCard.setValue(value);
	}
}