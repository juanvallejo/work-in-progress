package com.rs.tools;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.rs.Settings;
import com.rs.cache.Cache;
import com.rs.game.item.Item;
import com.rs.game.player.Player;
import com.rs.game.player.Skills;
import com.rs.utils.Encrypt;
import com.rs.utils.SerializableFilesManager;
import com.rs.utils.Utils;

public class AccountModification extends JFrame {
	private static final long serialVersionUID = 6688741257614555809L;
	private JPanel pane;
	private JTextField usernameText;
	private JTextField passwordText;
	private static String username;

	public static void main(String[] args) throws IOException {
		Cache.init();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AccountModification frame = new AccountModification();
					frame.setVisible(true);
					UIManager.setLookAndFeel(UIManager
							.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public AccountModification() {
		setTitle("Account Modification");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 245, 270);
		pane = new JPanel();
		pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(pane);
		pane.setLayout(null);
		JLabel lblUsername = new JLabel("Enter username");
		JLabel lblPassword = new JLabel("Password:");
		JButton dltBank = new JButton("Delete bank");
		JButton dltInv = new JButton("Delete inventory");
		JButton dltPouch = new JButton("Delete money pouch");
		JButton dltEquipment = new JButton("Delete equipment");
		JButton rmvStats = new JButton("Remove stats");
		usernameText = new JTextField();
		passwordText = new JTextField();
		lblUsername.setBounds(15, 11, 92, 14);
		usernameText.setBounds(15, 25, 194, 20);
		lblPassword.setBounds(15, 50, 92, 14);
		passwordText.setBounds(15, 64, 194, 20);
		dltBank.setBounds(15, 89, 194, 20);
		dltInv.setBounds(15, 114, 194, 20);
		dltPouch.setBounds(15, 139, 194, 20);
		dltEquipment.setBounds(15, 164, 194, 20);
		rmvStats.setBounds(15, 189, 194, 20);
		pane.add(lblUsername);
		pane.add(usernameText);
		pane.add(lblPassword);
		pane.add(passwordText);
		pane.add(dltBank);
		pane.add(dltInv);
		pane.add(dltPouch);
		pane.add(dltEquipment);
		pane.add(rmvStats);
		usernameText.setColumns(10);
		passwordText.setColumns(10);
		usernameText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!SerializableFilesManager.containsPlayer(usernameText
						.getText())) {
					sendNotification("Error choosing account!",
							"Account name: \"" + usernameText.getText()
									+ "\" does not exist.", true);
					usernameText.setText("");
					return;
				}
				username = usernameText.getText();
				usernameText.setText(Utils.formatPlayerNameForDisplay(username));
			}
		});
		if (!SerializableFilesManager.containsPlayer(usernameText.getText()))
			return;
		passwordText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePassword(passwordText.getText());
				passwordText.setText("");
			}
		});
		dltBank.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				confirmBankDelete("Deleting bank",
						"Are you sure you wish to delete this user's bank? "
								+ "This action is irreversible.");
			}
		});
		dltInv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				confirmInvDelete("Deleting inventory",
						"Are you sure you wish to delete this user's inventory? "
								+ "This action is irreversible.");
			}
		});
		dltPouch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				confirmPouchDelete("Deleting money pouch",
						"Are you sure you wish to delete this user's money pouch? "
								+ "This action is irreversible.");
			}
		});
		dltEquipment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				confirmEquipmentDelete("Deleting money pouch",
						"Are you sure you wish to delete this user's money pouch? "
								+ "This action is irreversible.");
			}
		});
		rmvStats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				confirmLevelDelete("Removing stats",
						"Are you sure you wish to delete this user's levels? "
								+ "This action is irreversible.");
			}
		});
	}

	private void sendNotification(String title, String message, boolean error) {
		JOptionPane.showMessageDialog(null, message, title,
				(error ? JOptionPane.ERROR_MESSAGE
						: JOptionPane.INFORMATION_MESSAGE));
	}

	private void changePassword(String password) {
		String name = Utils.formatPlayerNameForProtocol(username);
		Player player = SerializableFilesManager.loadPlayer(name);
		player.setUsername(name);
		player.setPassword(Encrypt.encryptSHA1(password));
		sendNotification("Success!", Utils.formatPlayerNameForDisplay(username)
				+ "'s password has successfully been changed to \"" + password
				+ "\".", false);
		SerializableFilesManager.savePlayer(player);
		return;
	}

	private void deleteBank() {
		String name = Utils.formatPlayerNameForProtocol(username);
		Player player = SerializableFilesManager.loadPlayer(name);
		player.setUsername(name);
		for (int i = 0; i < Utils.getItemDefinitionsSize(); i++) {
			player.getBank().removeItem(i);
			int[] BankSlot = player.getBank().getItemSlot(i);
			player.getBank().removeItem(BankSlot, Integer.MAX_VALUE, false,
					true);
		}
		sendNotification("Success!", Utils.formatPlayerNameForDisplay(username)
				+ "'s bank has successfully been deleted.", false);
		SerializableFilesManager.savePlayer(player);
		needsNotified(player);
		return;
	}

	private void confirmBankDelete(String title, String message) {
		int option = JOptionPane.showConfirmDialog(null, message, title,
				JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			deleteBank();
		}
	}

	private void confirmInvDelete(String title, String message) {
		int option = JOptionPane.showConfirmDialog(null, message, title,
				JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			deleteInventory();
		}
	}

	private void confirmPouchDelete(String title, String message) {
		int option = JOptionPane.showConfirmDialog(null, message, title,
				JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			deleteMoneyPouch();
		}
	}

	private void confirmEquipmentDelete(String title, String message) {
		int option = JOptionPane.showConfirmDialog(null, message, title,
				JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			deleteEquipment();
		}
	}

	private void confirmLevelDelete(String title, String message) {
		int option = JOptionPane.showConfirmDialog(null, message, title,
				JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			deleteLevels();
		}
	}

	private void deleteLevels() {
		String name = Utils.formatPlayerNameForProtocol(username);
		Player player = SerializableFilesManager.loadPlayer(name);
		player.setUsername(name);
		//player.getSkills().resetAll(false);
		player.setHitpoints(player.getSkills().getLevel(Skills.HITPOINTS) * 10);
		player.getPrayer().setPrayerpoints(
				player.getSkills().getLevel(Skills.PRAYER) * 10);
		if (player.getCombatDefinitions().getSpellBook() != 192)
			player.getCombatDefinitions().setSpellBook(0);
		if (player.getPrayer().isAncientCurses())
			//player.getPrayer().switchAncientCurses();
		//player.getSkills().resetAllCounterXP(false);
		sendNotification("Success!", Utils.formatPlayerNameForDisplay(username)
				+ "'s progress has successfully been deleted.", false);
		SerializableFilesManager.savePlayer(player);
		needsNotified(player);
		return;
	}

	private void deleteEquipment() {
		String name = Utils.formatPlayerNameForProtocol(username);
		Player player = SerializableFilesManager.loadPlayer(name);
		player.setUsername(name);
		for (int i = 0; i < Utils.getItemDefinitionsSize(); i++)
			player.getEquipment().getItems()
					.removeAll(new Item(i, Integer.MAX_VALUE));
		sendNotification("Success!", Utils.formatPlayerNameForDisplay(username)
				+ "'s equipment has successfully been deleted.", false);
		SerializableFilesManager.savePlayer(player);
		needsNotified(player);
		return;
	}

	private void deleteMoneyPouch() {
		String name = Utils.formatPlayerNameForProtocol(username);
		Player player = SerializableFilesManager.loadPlayer(name);
		player.setUsername(name);
		//player.setMoney(0);
		sendNotification("Success!", Utils.formatPlayerNameForDisplay(username)
				+ "'s money pouch has successfully been deleted.", false);
		SerializableFilesManager.savePlayer(player);
		needsNotified(player);
		return;
	}

	private void deleteInventory() {
		String name = Utils.formatPlayerNameForProtocol(username);
		Player player = SerializableFilesManager.loadPlayer(name);
		player.setUsername(name);
		for (int i = 0; i < Utils.getItemDefinitionsSize(); i++)
			player.getInventory().getItems()
					.removeAll(new Item(i, Integer.MAX_VALUE));
		sendNotification("Success!", Utils.formatPlayerNameForDisplay(username)
				+ "'s inventory has successfully been deleted.", false);
		SerializableFilesManager.savePlayer(player);
		needsNotified(player);
		return;
	}

	private void needsNotified(Player player) {
		player.out("While you were gone, some changes were made to your account, if you have any questions, please contact us at "
				+ Settings.WEBSITE_LINK);
	}

}