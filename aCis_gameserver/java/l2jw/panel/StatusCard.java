package l2jw.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatusCard extends JPanel
{
	private static final long serialVersionUID = 1L;

	private final JLabel titleLabel = new JLabel();
	private final JLabel valueLabel = new JLabel();

	public StatusCard(String title, String value)
	{
		setLayout(new BorderLayout(0, 4));
		setBackground(PanelTheme.SURFACE_2);
		setBorder(BorderFactory.createCompoundBorder(
			BorderFactory.createLineBorder(PanelTheme.BORDER),
			BorderFactory.createEmptyBorder(8, 10, 8, 10)
		));
		setPreferredSize(new Dimension(150, 64));

		titleLabel.setText(title);
		valueLabel.setText(value);

		PanelTheme.styleMuted(titleLabel);
		valueLabel.setForeground(PanelTheme.TEXT);
		valueLabel.setFont(PanelTheme.FONT_CARD);

		add(titleLabel, BorderLayout.NORTH);
		add(valueLabel, BorderLayout.CENTER);
	}

	public void setValue(String value)
	{
		valueLabel.setText(value);
	}
}