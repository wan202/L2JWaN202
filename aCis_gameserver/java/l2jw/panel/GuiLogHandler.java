package l2jw.panel;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.swing.SwingUtilities;

public class GuiLogHandler extends Handler
{
	private static final DateTimeFormatter FORMATTER =
		DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
			.withZone(ZoneId.systemDefault());

	private final ConsolePanel consolePanel;

	public GuiLogHandler(ConsolePanel consolePanel)
	{
		this.consolePanel = consolePanel;
		setLevel(Level.ALL);
	}

	@Override
	public void publish(LogRecord record)
	{
		if (record == null || !isLoggable(record))
			return;

		if (shouldIgnore(record))
			return;

		final StringBuilder sb = new StringBuilder();

		sb.append("[")
			.append(FORMATTER.format(Instant.ofEpochMilli(record.getMillis())))
			.append("] ");

		final String message = resolveMessage(record);
		sb.append(message);

		if (record.getThrown() != null)
		{
			sb.append(System.lineSeparator());

			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			record.getThrown().printStackTrace(pw);
			sb.append(sw);
		}

		sb.append(System.lineSeparator());

		final String text = sb.toString();
		SwingUtilities.invokeLater(() -> consolePanel.appendText(text));
	}

	private boolean shouldIgnore(LogRecord record)
	{
		final String loggerName = record.getLoggerName();
		final String message = resolveMessage(record);

		if (loggerName != null && loggerName.startsWith("l2jw.panel"))
			return true;

		if (message == null || message.isBlank())
			return true;

		if (message.startsWith("[PANEL]"))
			return true;

		return false;
	}

	private String resolveMessage(LogRecord record)
	{
		String message = record.getMessage();

		if (message == null)
			return "";

		final Object[] params = record.getParameters();
		if (params != null && params.length > 0)
		{
			try
			{
				message = MessageFormat.format(message, params);
			}
			catch (IllegalArgumentException e)
			{
				// mantém a mensagem original se não estiver em formato MessageFormat
			}
		}

		return message;
	}

	@Override
	public void flush()
	{
	}

	@Override
	public void close() throws SecurityException
	{
	}
}