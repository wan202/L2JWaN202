package net.sf.l2j.gameserver.model.zone.type;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.model.zone.type.subtype.SpawnZoneType;

public class RandomZone extends SpawnZoneType
{
	private int _id;
	private String _name;
	private int _time;
	private List<Location> _locations = new ArrayList<>();
	
	public RandomZone(int id)
	{
		super(id);
	}
	
	@Override
	public void setParameter(String name, String value)
	{
		if (name.equals("id"))
			_id = Integer.parseInt(value);
		else if (name.equals("name"))
			_name = value;
		else if (name.equals("time"))
			_time = Integer.parseInt(value);
		else if (name.equals("locs"))
		{
			for (String locs : value.split(";"))
				_locations.add(new Location(Integer.valueOf(locs.split(",")[0]), Integer.valueOf(locs.split(",")[1]), Integer.valueOf(locs.split(",")[2])));
		}
		else
			super.setParameter(name, value);
	}
	
	@Override
	protected void onEnter(Creature character)
	{
		character.setInsideZone(ZoneId.RANDOM, true);
	}
	
	@Override
	protected void onExit(Creature character)
	{
		character.setInsideZone(ZoneId.RANDOM, false);
	}
	
	@Override
	public int getId()
	{
		return _id;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public int getTime()
	{
		return _time;
	}
	
	public Location getLoc()
	{
		return _locations.get(Rnd.get(0, _locations.size() - 1));
	}
}