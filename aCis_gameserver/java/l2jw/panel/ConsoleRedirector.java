package l2jw.panel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.SwingUtilities;

public class ConsoleRedirector extends OutputStream
{
	private static final int FLUSH_THRESHOLD = 1024;

	private final ConsolePanel consolePanel;
	private final StringBuilder buffer = new StringBuilder(FLUSH_THRESHOLD);

	private ConsoleRedirector(ConsolePanel consolePanel)
	{
		this.consolePanel = consolePanel;
	}

	public static void redirect(ConsolePanel consolePanel)
	{
		final ConsoleRedirector redirector = new ConsoleRedirector(consolePanel);
		final PrintStream printStream = new PrintStream(redirector, true);

		System.setOut(printStream);
		System.setErr(printStream);
	}

	@Override
	public synchronized void write(int b) throws IOException
	{
		buffer.append((char) b);

		if (b == '\n' || buffer.length() >= FLUSH_THRESHOLD)
			flushBuffer();
	}

	@Override
	public synchronized void write(byte[] b, int off, int len) throws IOException
	{
		if (b == null || len <= 0)
			return;

		buffer.append(new String(b, off, len));

		if (buffer.indexOf("\n") >= 0 || buffer.length() >= FLUSH_THRESHOLD)
			flushBuffer();
	}

	@Override
	public synchronized void flush() throws IOException
	{
		flushBuffer();
	}

	private void flushBuffer()
	{
		if (buffer.isEmpty())
			return;

		final String text = buffer.toString();
		buffer.setLength(0);

		SwingUtilities.invokeLater(() ->
		{
			consolePanel.appendText(text);

			if (consolePanel.isAutoScrollEnabled())
			{
				consolePanel.getConsoleArea()
					.setCaretPosition(consolePanel.getConsoleArea().getDocument().getLength());
			}
		});
	}
}