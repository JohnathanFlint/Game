package world.model;

//My partner wrote the entirety of this class

import java.awt.Color;
import java.awt.Dimension;

public class Floor extends Tile
{
	public Floor(Color color)
	{
		super(true, color, 0.15, false);
	}
	public Floor(Color color,double DangerLevel)
	{
		super(true, color, DangerLevel,false);
	}
}
