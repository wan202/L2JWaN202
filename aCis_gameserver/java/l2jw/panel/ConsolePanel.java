package l2jw.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ConsolePanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final JTextArea consoleArea = new JTextArea();
	private final JTextField filterField = new JTextField(18);
	private final JCheckBox autoScrollBox = new JCheckBox("Auto Scroll", true);
	private final JLabel linesLabel = new JLabel("Lines: 0");

	private String fullText = "";

	public ConsolePanel()
	{
		super(new BorderLayout(0, 6));

		setBackground(PanelTheme.BACKGROUND);
		setBorder(PanelTheme.createTitledBorder("Console"));

		PanelTheme.styleConsole(consoleArea);
		consoleArea.setEditable(false);
		consoleArea.setLineWrap(true);
		consoleArea.setWrapStyleWord(true);

		final JScrollPane scrollPane = new JScrollPane(consoleArea);
		scrollPane.getViewport().setBackground(new java.awt.Color(10, 12, 16));
		scrollPane.setBorder(BorderFactory.createLineBorder(PanelTheme.BORDER));

		final JPanel toolbar = new JPanel(new BorderLayout(8, 0));
		toolbar.setBackground(PanelTheme.BACKGROUND);

		final JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		leftPanel.setBackground(PanelTheme.BACKGROUND);

		final JButton clearButton = new JButton("Clear");
		PanelTheme.styleButton(clearButton, PanelTheme.BLUE);

		autoScrollBox.setBackground(PanelTheme.BACKGROUND);
		autoScrollBox.setForeground(PanelTheme.TEXT);
		autoScrollBox.setFocusPainted(false);

		final JLabel filterLabel = new JLabel("Filter:");
		PanelTheme.styleMuted(filterLabel);
		PanelTheme.styleMuted(linesLabel);

		leftPanel.add(clearButton);
		leftPanel.add(autoScrollBox);
		leftPanel.add(filterLabel);
		leftPanel.add(filterField);

		toolbar.add(leftPanel, BorderLayout.WEST);
		toolbar.add(linesLabel, BorderLayout.EAST);

		add(toolbar, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

		clearButton.addActionListener(e -> clearConsole());

		filterField.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void insertUpdate(DocumentEvent e)
			{
				applyFilter();
			}

			@Override
			public void removeUpdate(DocumentEvent e)
			{
				applyFilter();
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				applyFilter();
			}
		});
	}

	public JTextArea getConsoleArea()
	{
		return consoleArea;
	}

	public boolean isAutoScrollEnabled()
	{
		return autoScrollBox.isSelected();
	}

	public void appendText(String text)
	{
		fullText += text;
		applyFilter();
	}

	public void clearConsole()
	{
		fullText = "";
		consoleArea.setText("");
		updateLineCounter("");
	}

	private void applyFilter()
	{
		final String filter = filterField.getText().trim().toLowerCase();

		if (filter.isEmpty())
		{
			consoleArea.setText(fullText);
			updateLineCounter(fullText);
		}
		else
		{
			final String filtered = Arrays.stream(fullText.split("\\R", -1))
				.filter(line -> line.toLowerCase().contains(filter))
				.collect(Collectors.joining(System.lineSeparator()));

			consoleArea.setText(filtered);
			updateLineCounter(filtered);
		}

		if (isAutoScrollEnabled())
			consoleArea.setCaretPosition(consoleArea.getDocument().getLength());
	}

	private void updateLineCounter(String text)
	{
		if (text == null || text.isEmpty())
		{
			linesLabel.setText("Lines: 0");
			return;
		}

		final int lines = text.split("\\R", -1).length;
		linesLabel.setText("Lines: " + lines);
	}
}