package net.sf.l2j.gameserver.taskmanager;

import net.sf.l2j.commons.pool.ThreadPool;
import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.manager.ZoneManager;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.zone.type.RandomZone;

public final class RandomZoneTaskManager implements Runnable
{
	private int _id;
	private int _timer;
	
	public RandomZoneTaskManager()
	{
		if (getTotalZones() > 1 && Config.RANDOM_PVP_ZONE)
			ThreadPool.scheduleAtFixedRate(this, 1000, 1000);
	}
	
	@Override
	public void run()
	{
		if (_timer >= 0)
			_timer--;
		else
			selectNextZone();
		
		switch (_timer)
		{
			case 0:
				World.announceToOnlinePlayers("PvP zone will has been changed.", true);
				break;
			case 5:
			case 15:
			case 30:
				World.announceToOnlinePlayers("PvP zone will change in " + _timer + " second(s).", true);
				break;
			case 60:
			case 300:
			case 600:
			case 900:
			case 1800:
				World.announceToOnlinePlayers("PvP zone will change in " + _timer / 60 + " minute(s).", true);
				break;
			case 3600:
			case 7200:
				World.announceToOnlinePlayers("PvP zone will change in " + (_timer / 60) / 60 + " hour(s).", true);
				break;
		}
	}
	
	public int getZoneId()
	{
		return _id;
	}
	
	public void selectNextZone()
	{
		int nextZoneId = Rnd.get(1, getTotalZones());
		
		if (getZoneId() != nextZoneId)
		{
			_id = nextZoneId;
			_timer = getCurrentZone().getTime();
			
			World.announceToOnlinePlayers("New PvP zone: " + getCurrentZone().getName(), true);
		}
	}
	
	public int getTimer()
	{
		return _timer;
	}
	
	public String getName() {
	    RandomZone zone = getCurrentZone();
	    return zone != null ? zone.getName() : "Unknown Zone";
	}
	
	public final RandomZone getCurrentZone()
	{
		return ZoneManager.getInstance().getAllZones(RandomZone.class).stream().filter(t -> t.getId() == getZoneId()).findFirst().orElse(null);
	}
	
	public static final int getTotalZones()
	{
		return ZoneManager.getInstance().getAllZones(RandomZone.class).size();
	}
	
	public static final RandomZoneTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final RandomZoneTaskManager INSTANCE = new RandomZoneTaskManager();
	}
}