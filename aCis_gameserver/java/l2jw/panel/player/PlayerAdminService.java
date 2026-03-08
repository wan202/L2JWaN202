package l2jw.panel.player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sf.l2j.commons.pool.ConnectionPool;

import net.sf.l2j.gameserver.enums.PunishmentType;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.location.Location;

public final class PlayerAdminService
{
	private static final String SELECT_CHARACTER_BY_NAME =
		"SELECT char_name FROM characters WHERE char_name=? LIMIT 1";

	private PlayerAdminService()
	{
	}

	public static Player findOnlinePlayer(String name)
	{
		if (name == null || name.isBlank())
			return null;

		return World.getInstance().getPlayer(name.trim());
	}

	public static PlayerSearchResult findPlayer(String name)
	{
		if (name == null || name.isBlank())
			return null;

		final String trimmedName = name.trim();

		final Player onlinePlayer = findOnlinePlayer(trimmedName);
		if (onlinePlayer != null)
			return new PlayerSearchResult(onlinePlayer, onlinePlayer.getName(), true);

		if (existsOfflinePlayer(trimmedName))
			return new PlayerSearchResult(null, trimmedName, false);

		return null;
	}

	private static boolean existsOfflinePlayer(String name)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_CHARACTER_BY_NAME))
		{
			ps.setString(1, name);

			try (ResultSet rs = ps.executeQuery())
			{
				return rs.next();
			}
		}
		catch (Exception e)
		{
			return false;
		}
	}

	public static void kick(Player player)
	{
		if (player == null)
			return;

		player.logout(false);
	}

	public static void jail(Player player, int minutes)
	{
		if (player == null)
			return;

		player.getPunishment().setType(PunishmentType.JAIL, minutes);
	}

	public static void unjail(Player player)
	{
		if (player == null)
			return;

		player.getPunishment().setType(PunishmentType.NONE, 0);
	}

	public static void teleportToCoords(Player player, int x, int y, int z)
	{
		if (player == null)
			return;

		player.teleportTo(x, y, z, 0);
	}

	public static void teleportToPlayer(Player player, Player target)
	{
		if (player == null || target == null)
			return;

		player.teleportTo(target.getPosition(), 0);
	}

	public static void teleportPlayerToPlayer(Player target, Player destination)
	{
		if (target == null || destination == null)
			return;

		target.teleportTo(destination.getPosition(), 0);
	}

	public static void teleportToLocation(Player player, Location location)
	{
		if (player == null || location == null)
			return;

		player.teleToLocation(location);
	}

	public static void giveItem(Player player, int itemId, int count)
	{
		if (player == null || itemId <= 0 || count <= 0)
			return;

		player.addItem(itemId, count, true);
	}

	public static void deleteItem(Player player, int itemId, int count)
	{
		if (player == null || itemId <= 0 || count <= 0)
			return;

		player.destroyItemByItemId(itemId, count, true);
	}
}