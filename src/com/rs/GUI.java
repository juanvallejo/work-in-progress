package com.rs;

import javax.swing.*;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.*;

import com.rs.game.World;
import com.rs.game.player.Player;

/**
 * 
 * @author 'Corey 2010' <Server Developer>
 */
public class GUI extends JFrame {

	private static final long serialVersionUID = 2825303979914354794L;

	private JButton adminAction;
	private JButton donatorAction;
	private JButton exDonatorAction;
	private JButton helperButton;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JPanel jPanel1;
	private JPanel jPanel2;;
	private JTabbedPane jTabbedPane1;
	private JButton modAction;
	private JButton sponsorAction;
	private JTextField userField;
	private JMenuBar menuBar;
	private JMenu jm1;
	private JMenu jm2;
	private JMenuItem jmi1;
	private JMenuItem jmi2;
	private JTextArea yellArea;
	private JButton yellButton;
	private JScrollPane scrollPane;
	private JLabel yellTextLabel;
	private JButton disableYell;
	private JButton enableYell;
	private JLabel Credits;
	private JButton updateButton;

	private Player player;

	public GUI() {
		init();
		setTitle("Indigo - Control Panel");
		setResizable(false);

		// Theme
		UIManager.put("nimbusBase", new Color(191, 98, 4));
		UIManager.put("nimbusBlueGrey", new Color(57, 105, 138));
		UIManager.put("control", new Color(169, 46, 34));
	}

	private void init() {
		jTabbedPane1 = new JTabbedPane();
		jPanel1 = new JPanel();
		jLabel1 = new JLabel();
		userField = new JTextField();
		adminAction = new JButton();
		modAction = new JButton();
		donatorAction = new JButton();
		exDonatorAction = new JButton();
		sponsorAction = new JButton();
		helperButton = new JButton();
		jLabel2 = new JLabel();
		menuBar = new JMenuBar();
		jm1 = new JMenu();
		jm2 = new JMenu();
		jmi1 = new JMenuItem();
		jmi2 = new JMenuItem();
		yellArea = new JTextArea();
		yellButton = new JButton();
		jPanel2 = new JPanel();
		scrollPane = new JScrollPane();
		yellTextLabel = new JLabel();
		disableYell = new JButton();
		enableYell = new JButton();
		Credits = new JLabel();
		updateButton = new JButton();

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jLabel1.setFont(new Font("Tahoma", 1, 11));
		jLabel1.setText("Username: ");
		jLabel1.setForeground(new Color(0, 255, 255));

		jm1.setText("File");
		jm1.add(jmi1);
		jmi1.setText("Exit");
		menuBar.add(jm1);
		jmi1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				menuItem1ActionPerformed(evt);
			}
		});

		jm2.setText("About");
		jm2.add(jmi2);
		jmi2.setText("About");
		jmi2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				menuItem2ActionPerformed(evt);
			}
		});
		menuBar.add(jm2);

		setJMenuBar(menuBar);

		adminAction.setText("Administrator");
		adminAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				adminActionActionPerformed(evt);
			}
		});

		modAction.setText("Moderator");
		modAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				modActionActionPerformed(evt);
			}
		});

		donatorAction.setText("Donator");
		donatorAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				donatorActionActionPerformed(evt);
			}
		});

		exDonatorAction.setText("Extreme Donator");
		exDonatorAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				exDonatorActionActionPerformed(evt);
			}
		});

		sponsorAction.setText("Sponsor");
		sponsorAction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				sponsorActionActionPerformed(evt);
			}
		});

		helperButton.setText("Helper");
		helperButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				helperButtonActionPerformed(evt);
			}
		});

		updateButton.setFont(new java.awt.Font("Tahoma", 1, 12));
		updateButton.setForeground(new java.awt.Color(0, 255, 255));
		updateButton.setText("Update Server");
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				updateButtonActionPerformed(evt);
			}
		});

		jLabel2.setFont(new Font("Times New Roman", 1, 12));
		jLabel2.setText("Corey 2010");
		jLabel2.setForeground(new Color(0, 255, 255));

		GroupLayout jPanel1Layout = new GroupLayout(jPanel1);

		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout
				.setHorizontalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addGap(69,
																				69,
																				69)
																		.addComponent(
																				jLabel1)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				userField,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				123,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGroup(
																jPanel1Layout
																		.createSequentialGroup()
																		.addGap(136,
																				136,
																				136)
																		.addComponent(
																				jLabel2)))
										.addContainerGap())
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																jPanel1Layout
																		.createSequentialGroup()
																		.addGroup(
																				jPanel1Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING,
																								false)
																						.addComponent(
																								adminAction,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								153,
																								Short.MAX_VALUE)
																						.addComponent(
																								donatorAction,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								sponsorAction,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))
																		.addGap(10,
																				10,
																				10)
																		.addGroup(
																				jPanel1Layout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								exDonatorAction,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								helperButton,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								modAction,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))
																		.addContainerGap())
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																jPanel1Layout
																		.createSequentialGroup()
																		.addGap(0,
																				0,
																				Short.MAX_VALUE)
																		.addComponent(
																				updateButton,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				128,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(102,
																				102,
																				102)))));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel1Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jLabel1)
														.addComponent(
																userField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(22, 22, 22)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																adminAction)
														.addComponent(modAction))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																donatorAction)
														.addComponent(
																exDonatorAction))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(
												jPanel1Layout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																sponsorAction)
														.addComponent(helperButton))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												updateButton,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												31, Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jLabel2).addGap(6, 6, 6)));

		jTabbedPane1.addTab("Promoting", jPanel1);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1));
		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1));

		yellArea.setColumns(20);
		yellArea.setRows(5);
		scrollPane.setViewportView(yellArea);

		yellButton.setText("Yell");
		yellButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				yellButtonActionPerformed(evt);
			}
		});

		yellTextLabel.setFont(new Font("Tahoma", 1, 11));
		yellTextLabel.setText("Enter the yell text in the box below:");
		yellTextLabel.setToolTipText("");
		yellTextLabel.setForeground(new Color(0, 255, 255));

		disableYell.setText("Disable Yell");
		disableYell.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (Player target : World.getPlayers()) {
					if (target.getRights() < 1) {
						//target.setYellDisabled(true);
						target.getPackets().sendGameMessage("Yell has been disabled for the time being by staff.");
					}
					JOptionPane.showMessageDialog(rootPane,
							"Yell has been disabled for the time being.");
				}
			}
		});

		Credits.setText("Maintained by Juan Vallejo");
		Credits.setForeground(new Color(0, 255, 255));

		enableYell.setText("Enable Yell");
		enableYell.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (Player target : World.getPlayers()) {
					if (target.getRights() < 1) {
						//target.setYellDisabled(false);
						target.getPackets().sendGameMessage("Yell has been enabled for the time being by staff.");
					}
					JOptionPane.showMessageDialog(rootPane,
							"Yell has been enabled for the time being.");
				}
			}
		});
		GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout
				.setHorizontalGroup(jPanel2Layout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								GroupLayout.Alignment.TRAILING,
								jPanel2Layout.createSequentialGroup()
										.addGap(0, 0, Short.MAX_VALUE)
										.addComponent(yellTextLabel)
										.addGap(65, 65, 65))
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																GroupLayout.Alignment.LEADING)
														.addComponent(
																scrollPane)
														.addGroup(
																jPanel2Layout
																		.createSequentialGroup()
																		.addComponent(
																				disableYell,
																				GroupLayout.PREFERRED_SIZE,
																				99,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(2,
																				2,
																				2)
																		.addComponent(
																				yellButton,
																				GroupLayout.DEFAULT_SIZE,
																				111,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				enableYell,
																				GroupLayout.PREFERRED_SIZE,
																				95,
																				GroupLayout.PREFERRED_SIZE)
																		.addGap(4,
																				4,
																				4))
														.addGroup(
																jPanel2Layout
																		.createSequentialGroup()
																		.addComponent(
																				Credits)
																		.addGap(0,
																				0,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		jPanel2Layout
				.setVerticalGroup(jPanel2Layout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								jPanel2Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(yellTextLabel,
												GroupLayout.PREFERRED_SIZE, 14,
												GroupLayout.PREFERRED_SIZE)
										.addGap(8, 8, 8)
										.addComponent(scrollPane,
												GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(Credits)
										.addPreferredGap(
												LayoutStyle.ComponentPlacement.RELATED,
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												jPanel2Layout
														.createParallelGroup(
																GroupLayout.Alignment.BASELINE)
														.addComponent(
																disableYell)
														.addComponent(
																yellButton)
														.addComponent(
																enableYell))
										.addContainerGap()));

		jTabbedPane1.addTab("Yell", jPanel2);

		pack();
	}

	private void updateButtonActionPerformed(ActionEvent evt) {
		int delay = 60;
		try {
			World.safeShutdown(true, delay);
			System.err.println("[Launcher] Initiating Update...");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(rootPane, "A error has occured.",
					"ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void yellButtonActionPerformed(ActionEvent evt) {
		if (this.yellArea.getText().equals(""))
			JOptionPane.showMessageDialog(rootPane,
					"Please enter some text in the text area.", "ERROR",
					JOptionPane.ERROR_MESSAGE);
		else {
			String yellText = yellArea.getText();
			for (Player players : World.getPlayers()) {
				players.getPackets().sendGameMessage(
						"[<col=ff00ff>Console</col>] - <col=00FF00>" + yellText
								+ " </col>");
			}
		}
	}

	private void menuItem2ActionPerformed(ActionEvent evt) {
		JOptionPane.showMessageDialog(rootPane,
				"This Graphical User Interface was created by: \n Corey 2010.");
	}

	private void menuItem1ActionPerformed(ActionEvent evt) {
		System.exit(1);
	}

	private void adminActionActionPerformed(ActionEvent evt) {
		if (this.userField.getText().equals("")) {
			JOptionPane.showMessageDialog(rootPane,
					"You must enter a username", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		for (Player player : World.getPlayers()) {
			String admin = userField.getText();
			if (player.getUsername().equalsIgnoreCase(admin)) {
				player.setRights(2);
				player.getPackets().sendGameMessage("You have been promoted to <img=1>Administrator.");
				JOptionPane.showMessageDialog(rootPane, "You have promoted "
						+ player.getUsername() + " to Administrator.");
			} else {
				JOptionPane
						.showMessageDialog(rootPane, "Something went wrong?");
			}
		}
	}

	private void modActionActionPerformed(ActionEvent evt) {
		if (this.userField.getText().equals("")) {
			JOptionPane.showMessageDialog(rootPane,
					"You must enter a username", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		for (Player player : World.getPlayers()) {
			if (player.getUsername().equalsIgnoreCase(userField.getText())) {
				player.setRights(1);
				player.getPackets().sendGameMessage("You have been promoted to <img=0>Moderator.");
				JOptionPane.showMessageDialog(rootPane, "You have promoted "
						+ player.getUsername() + " to Moderator.");
			} else {
				JOptionPane
						.showMessageDialog(rootPane, "Something went wrong?");
			}
		}
	}

	private void donatorActionActionPerformed(ActionEvent evt) {
		if (this.userField.getText().equals("")) {
			JOptionPane.showMessageDialog(rootPane,
					"You must enter a username", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
		for (Player player : World.getPlayers()) {
			if (player.getUsername().equalsIgnoreCase(userField.getText())) {
				player.setDonator(true);
				player.getPackets().sendGameMessage("You have been promoted to donator.");
				JOptionPane.showMessageDialog(rootPane, "You have promoted "
						+ player.getUsername() + " to donator.");
			} else {
				JOptionPane
						.showMessageDialog(rootPane, "Something went wrong?");
			}
		}
	}

	private void exDonatorActionActionPerformed(ActionEvent evt) {
		// TODO Make extreme donator.
		JOptionPane.showMessageDialog(rootPane, "TODO: Make extreme donator.",
				"TODO", JOptionPane.ERROR_MESSAGE);
	}

	private void sponsorActionActionPerformed(ActionEvent evt) {
		// TODO Make extreme donator.
		JOptionPane.showMessageDialog(rootPane, "TODO: Sponsor.", "TODO",
				JOptionPane.ERROR_MESSAGE);
	}

	private void helperButtonActionPerformed(ActionEvent evt) {
		// TODO Make extreme donator.
		JOptionPane.showMessageDialog(rootPane, "TODO: Helper", "TODO",
				JOptionPane.ERROR_MESSAGE);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}