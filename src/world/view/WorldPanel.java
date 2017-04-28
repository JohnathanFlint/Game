package world.view;

//My partner wrote the entirety of this class

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import game.controller.GameController;
import game.model.HealthBar;
import game.model.Troll;
import world.model.Room;

public class WorldPanel extends JPanel
{
	private JLabel worldView;
	GameController controller;
	private SpringLayout layout;
	private int height;
	private int width;
	private int blockSize;
	private int xOffset;
	private int yOffset;
	private boolean displayHealth;
	private int miniState;
	private boolean isRendering; 

	public WorldPanel(GameController controller, int width, int heigth)
	{
		super();
		this.displayHealth = true;
		sized(width, heigth);
		layout = new SpringLayout();
		isRendering = false;
		this.controller = controller;
		this.worldView = new JLabel();
		//worldView.setText("Health: 20");
		setup();
		setupListeners();
		miniState = 0;
	}

	public void Render()
	{
		redraw(controller.getMap().getCurrentRoom());
	}

	private void setup()
	{

		this.setLayout(layout);
		this.add(worldView);
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.requestFocusInWindow();
	}

	private void setupListeners()
	{
		this.addKeyListener(new KeyListener()
		{

			@Override
			public void keyPressed(KeyEvent pressed)
			{

				if (pressed.getKeyCode() == KeyEvent.VK_W)
				{
					int[] direction = { 0, -1 };
					controller.getMap().getCurrentRoom().move(direction);
				}
				if (pressed.getKeyCode() == KeyEvent.VK_D)
				{
					int[] direction = { 1, 0 };
					controller.getMap().getCurrentRoom().move(direction);

				}
				if (pressed.getKeyCode() == KeyEvent.VK_A)
				{
					int[] direction = { -1, 0 };
					controller.getMap().getCurrentRoom().move(direction);
				}
				if (pressed.getKeyCode() == KeyEvent.VK_S)
				{
					int[] direction = { 0, 1 };
					controller.getMap().getCurrentRoom().move(direction);
				}
				if (pressed.getKeyCode() == KeyEvent.VK_H)
				{
					if (displayHealth)
					{
						displayHealth = false;
					} else
					{
						displayHealth = true;
					}
				}
				if(pressed.getKeyCode() == KeyEvent.VK_R)
				{
					if(isVisible()){
					controller.increaseHealth();
					if(controller.getMap().getCurrentRoom().isAmbush())
					{
						controller.startCombat(new Troll());
					}
					}
				}
				if(pressed.getKeyCode()== KeyEvent.VK_M)
				{
					if(miniState!=2){
					miniState++;}
					else{miniState = 0;}
				}
				redraw(controller.getMap().getCurrentRoom());

			}

			@Override
			public void keyReleased(KeyEvent arg0)
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void keyTyped(KeyEvent arg0)
			{
				// TODO Auto-generated method stub

			}

		});
	}

	private ImageIcon roomToPicture(Room room)
	{
		int block = this.blockSize;
		BufferedImage map = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int getX = 1; getX <= ((int) room.getSize().getWidth()); getX++)
		{
			for (int getY = 1; getY <= ((int) room.getSize().getHeight()); getY++)
			{
				Color currentColor = room.getTile(new Dimension(getX, getY)).getColor();
				// System.out.println(room.getTile(new
				// Dimension(getX,getY)).getClass().getSimpleName()+getX+getY);
				if (room.getTile(new Dimension(getX, getY)).isInhabited())
				{
					for (int x = 0; x < block; x++)
					{
						for (int y = 0; y < block; y++)
						{

							if ((x > block / 4 && x < (block * 3) / 4) && (y > block / 4 && y < (block * 3) / 4))
							{
								map.setRGB(((getX - 1) * block) + x + xOffset, ((getY - 1) * block) + y + yOffset, Color.red.getRGB());
							} else
							{
								map.setRGB(((getX - 1) * block) + x + xOffset, ((getY - 1) * block) + y + yOffset, currentColor.getRGB());
							}
						}
					}
					
				}
				else if (room.getTile(new Dimension(getX, getY)).gethasMonster())
				{
					Color color = Color.black;
					color = room.getTile(new Dimension(getX,getY)).getMonsterType().getColor();
					
					for (int x = 0; x < block; x++)
					{
						for (int y = 0; y < block; y++)
						{

							if ((x > block / 4 && x < (block * 3) / 4) && (y > block / 4 && y < (block * 3) / 4))
							{
								map.setRGB(((getX - 1) * block) + x + xOffset, ((getY - 1) * block) + y + yOffset, color.getRGB());
							} else
							{
								map.setRGB(((getX - 1) * block) + x + xOffset, ((getY - 1) * block) + y + yOffset, currentColor.getRGB());
							}
						}
					}
					
				}
				else
				{
					for (int x = 0; x < block; x++)
					{
						for (int y = 0; y < block; y++)
						{
							map.setRGB(((getX - 1) * block) + x + xOffset, ((getY - 1) * block) + y + yOffset, currentColor.getRGB());
						}
					}
				}
			}
		}
		if(miniState !=2){
			BufferedImage mini = controller.getMiniMap(miniState);
			for (int x2 = 0; x2 < mini.getWidth(); x2++)
			{
				for (int y2 = 0; y2 < mini.getHeight(); y2++)
				{
					int drawx = x2;
					int drawy = y2;
					if(miniState == 1)
					{
						drawx += ((width-(int)mini.getWidth())/2);
						drawy += ((height-(int)mini.getHeight())/2);
					}

					map.setRGB(drawx, drawy, mini.getRGB(x2, y2));
					

				}
		}}
		if (displayHealth)
		{
			HealthBar bar = controller.getHealthBar();
			BufferedImage health = bar.render();
			for (int x1 = 0; x1 < health.getWidth(); x1++)
			{
				for (int y1 = 0; y1 < health.getHeight(); y1++)
				{
					if (!HealthBar.isTransparent(new Color(health.getRGB(x1, y1))))
					{
						int width = map.getWidth() - 1;
						int drawx = width - ((health.getWidth() - 1) - x1);
						int drawy = y1;
						map.setRGB(drawx, drawy, health.getRGB(x1, y1));

					}

				}
			}}
	
		ImageIcon mapImage = new ImageIcon(map);
		return mapImage;
	}

	private void redraw(Room room)
	{
		if(!isRendering){
		isRendering = true;
		this.remove(worldView);
		worldView = new JLabel();
		worldView = new JLabel(roomToPicture(room));
		this.add(worldView);
		this.repaint();
		this.validate();
		isRendering = false;
		}
	}

	public int[] getRenderSize()
	{
		int[] size = { width, height };
		return size;
	}

	public void resized(int width, int height)
	{
		this.width = width;
		this.height = height;
		if (width > height)
		{
			this.blockSize = height / 7;
		} else
		{
			this.blockSize = width / 7;
		}
		this.xOffset = (this.width - (this.blockSize * 7)) / 2;
		this.yOffset = (this.height - (this.blockSize * 7)) / 2;
		redraw(this.controller.getMap().getCurrentRoom());

	}

	public void sized(int width, int height)
	{
		this.width = width;
		this.height = height;
		if (width > height)
		{
			this.blockSize = height / 7;
		} else
		{
			this.blockSize = width / 7;
		}
		this.xOffset = ((this.width - (this.blockSize * 7)) / 2);
		this.yOffset = ((this.height - (this.blockSize * 7)) / 2);

	}
	public Dimension getMapSize()
	{
		return new Dimension(width,height);
	}
}
