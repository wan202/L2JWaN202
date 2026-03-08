package l2jw.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;

import javax.swing.JPanel;

import net.sf.l2j.gameserver.model.World;

public class PlayerChartPanel extends JPanel
{
    private final LinkedList<Integer> values = new LinkedList<>();

    public PlayerChartPanel()
    {
        setBackground(Color.BLACK);
    }

    public void update()
    {
        int players = World.getInstance().getPlayers().size();

        values.add(players);

        if (values.size() > 60)
            values.removeFirst();

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setColor(Color.GREEN);

        int w = getWidth();
        int h = getHeight();

        int size = values.size();

        if (size < 2)
            return;

        int step = w / 60;

        Integer[] array = values.toArray(new Integer[0]);

        for (int i = 1; i < size; i++)
        {
            int x1 = (i - 1) * step;
            int x2 = i * step;

            int y1 = h - array[i - 1];
            int y2 = h - array[i];

            g.drawLine(x1, y1, x2, y2);
        }
    }
}