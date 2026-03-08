package l2jw.panel.player;

import net.sf.l2j.gameserver.model.actor.Player;

public class PlayerSearchResult
{
	private final Player onlinePlayer;
	private final String playerName;
	private final boolean online;

	public PlayerSearchResult(Player onlinePlayer, String playerName, boolean online)
	{
		this.onlinePlayer = onlinePlayer;
		this.playerName = playerName;
		this.online = online;
	}

	public Player getOnlinePlayer()
	{
		return onlinePlayer;
	}

	public String getPlayerName()
	{
		return playerName;
	}

	public boolean isOnline()
	{
		return online;
	}

	public boolean isOffline()
	{
		return !online;
	}
}