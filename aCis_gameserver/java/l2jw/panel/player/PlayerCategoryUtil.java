package l2jw.panel.player;

import net.sf.l2j.gameserver.model.actor.Player;

public final class PlayerCategoryUtil
{
	private PlayerCategoryUtil()
	{
	}

	public static boolean isGm(Player player)
	{
		return player != null && player.isGM();
	}

	public static boolean isPremium(Player player)
	{
		if (player == null)
			return false;

		return player.getPremServiceData() > System.currentTimeMillis();
	}

	public static boolean isNormal(Player player)
	{
		return player != null && !isGm(player) && !isPremium(player);
	}
}