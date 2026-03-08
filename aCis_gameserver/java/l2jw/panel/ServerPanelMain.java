package l2jw.panel;

import javax.swing.SwingUtilities;

public final class ServerPanelMain
{
	private static volatile GameServerPanel instance;

	private ServerPanelMain()
	{
	}

	public static void start()
	{
		SwingUtilities.invokeLater(() ->
		{
			PanelTheme.applyDefaults();

			if (instance != null && instance.isDisplayable())
			{
				instance.toFront();
				instance.requestFocus();
				return;
			}

			instance = new GameServerPanel();
		});
	}
}