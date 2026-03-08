package l2jw.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.sf.l2j.gameserver.Shutdown;
import net.sf.l2j.gameserver.model.World;

import l2jw.panel.player.PlayersOnlinePanel;

public class GameServerPanel extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static final String PANEL_NAME = "ControlPanel";

	private final ServerStatusPanel monitorPanel = new ServerStatusPanel();
	private final ConsolePanel consolePanel = new ConsolePanel();
	private final QuickInfoPanel quickInfoPanel = new QuickInfoPanel();
	private final PlayersOnlinePanel playersOnlinePanel = new PlayersOnlinePanel();

	private GuiLogHandler guiLogHandler;

	public GameServerPanel()
	{
		super("L2JWaN202 GameServer Panel");

		setLayout(new BorderLayout(10, 10));
		setSize(1380, 820);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(PanelTheme.BACKGROUND);

		initComponents();
		initWindowListener();

		setVisible(true);

		consolePanel.appendText("GameServer: Running in Control Panel." + System.lineSeparator());
		installLogHandler();
	}

	private void initComponents()
	{
		final JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
		rightPanel.setBackground(PanelTheme.BACKGROUND);
		rightPanel.add(quickInfoPanel, BorderLayout.NORTH);
		rightPanel.add(playersOnlinePanel, BorderLayout.CENTER);

		final JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
		centerPanel.setBackground(PanelTheme.BACKGROUND);
		centerPanel.add(consolePanel);
		centerPanel.add(rightPanel);

		final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
		buttonPanel.setBackground(PanelTheme.BACKGROUND);

		final JButton announceButton = new JButton("Announce");
		final JButton restartButton = new JButton("Restart");
		final JButton shutdownButton = new JButton("Shutdown");
		final JButton abortButton = new JButton("Abort");

		PanelTheme.styleButton(announceButton, PanelTheme.BLUE);
		PanelTheme.styleButton(restartButton, PanelTheme.ORANGE);
		PanelTheme.styleButton(shutdownButton, PanelTheme.RED);
		PanelTheme.styleButton(abortButton, PanelTheme.GREEN);

		announceButton.addActionListener(e -> announceMessage());
		restartButton.addActionListener(e -> restartServer());
		shutdownButton.addActionListener(e -> shutdownServer());
		abortButton.addActionListener(e -> abortShutdown());

		buttonPanel.add(announceButton);
		buttonPanel.add(restartButton);
		buttonPanel.add(shutdownButton);
		buttonPanel.add(abortButton);

		add(monitorPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void initWindowListener()
	{
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				final int confirm = JOptionPane.showConfirmDialog(
					GameServerPanel.this,
					"Fechar o painel e desligar o GameServer?",
					"Confirmar fechamento",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE
				);

				if (confirm != JOptionPane.YES_OPTION)
				{
					setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					return;
				}

				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

				try
				{
					Shutdown.getInstance().startShutdown(null, PANEL_NAME, 0, false);
				}
				catch (Exception ex)
				{
					consolePanel.appendText("[PANEL] Falha ao desligar o servidor: " + ex.getMessage() + System.lineSeparator());
				}
			}

			@Override
			public void windowClosed(WindowEvent e)
			{
				monitorPanel.shutdown();
				playersOnlinePanel.shutdown();

				if (guiLogHandler != null)
					Logger.getLogger("").removeHandler(guiLogHandler);
			}
		});
	}

	private void installLogHandler()
	{
		try
		{
			guiLogHandler = new GuiLogHandler(consolePanel);
			Logger.getLogger("").addHandler(guiLogHandler);
		}
		catch (Exception e)
		{
			consolePanel.appendText("Falha ao instalar log handler: " + e.getMessage() + System.lineSeparator());
		}
	}

	private void announceMessage()
	{
		final String message = JOptionPane.showInputDialog(
			this,
			"Digite a mensagem para anunciar:",
			"Announce",
			JOptionPane.INFORMATION_MESSAGE
		);

		if (message == null)
			return;

		final String trimmed = message.trim();
		if (trimmed.isEmpty())
			return;

		World.announceToOnlinePlayers(trimmed, true);
		consolePanel.appendText("[PANEL] Announce enviado: " + trimmed + System.lineSeparator());
		quickInfoPanel.refresh();
	}

	private void restartServer()
	{
		final int confirm = JOptionPane.showConfirmDialog(
			this,
			"Deseja reiniciar o GameServer em 30 segundos?",
			"Confirmar Restart",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE
		);

		if (confirm != JOptionPane.YES_OPTION)
			return;

		Shutdown.getInstance().startShutdown(null, PANEL_NAME, 30, true);
		consolePanel.appendText("[PANEL] Restart agendado para 30 segundos." + System.lineSeparator());
		quickInfoPanel.refresh();
	}

	private void shutdownServer()
	{
		final int confirm = JOptionPane.showConfirmDialog(
			this,
			"Deseja desligar o GameServer em 10 segundos?",
			"Confirmar Shutdown",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE
		);

		if (confirm != JOptionPane.YES_OPTION)
			return;

		Shutdown.getInstance().startShutdown(null, PANEL_NAME, 10, false);
		consolePanel.appendText("[PANEL] Shutdown agendado para 10 segundos." + System.lineSeparator());
		quickInfoPanel.refresh();
	}

	private void abortShutdown()
	{
		final int confirm = JOptionPane.showConfirmDialog(
			this,
			"Deseja abortar o processo atual de shutdown/restart?",
			"Confirmar Abort",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.WARNING_MESSAGE
		);

		if (confirm != JOptionPane.YES_OPTION)
			return;

		try
		{
			Shutdown.getInstance().abort(null, PANEL_NAME);
			consolePanel.appendText("[PANEL] Processo de shutdown/restart abortado." + System.lineSeparator());
		}
		catch (NoSuchMethodError | Exception ex)
		{
			consolePanel.appendText("[PANEL] Seu Shutdown.abort(Player, String) ainda não foi criado." + System.lineSeparator());
		}

		quickInfoPanel.refresh();
	}
}