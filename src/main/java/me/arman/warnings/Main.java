package me.arman.warnings;

import java.io.File;

import me.arman.warnings.commands.AddWarningsCommand;
import me.arman.warnings.commands.ClearWarningsCommand;
import me.arman.warnings.commands.ResetWarningsCommand;
import me.arman.warnings.commands.SetWarningsCommand;
import me.arman.warnings.commands.TakeWarningsCommand;
import me.arman.warnings.commands.WarnCommand;
import me.arman.warnings.commands.WarningInfoCommand;
import me.arman.warnings.commands.WarningsCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	public DataFile dFile;

	@Override
	public void onEnable() {
		this.dFile = new DataFile(this);
		defaultConfigSetup();
		registerCommands();
	}

	@Override
	public void onDisable() {

	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("ReloadWarnings")) {
			if (!sender.hasPermission("warnings.reload")) {
				sender.sendMessage(ChatColor.RED
						+ "You can not reload the configuration!");
				return true;
			}
			reloadConfig();
			sender.sendMessage(ChatColor.GREEN + "Reloaded the configuration!");

		}
		return false;
	}

	private void defaultConfigSetup() {
		File config = new File(getDataFolder(), "config.yml");
		if (!config.exists()) {
			this.saveResource("config.yml", false);
		}
	}

	private void registerCommands() {
		getCommand("Warn").setExecutor(new WarnCommand(this));
		getCommand("Warnings").setExecutor(new WarningsCommand(this));
		getCommand("ClearWarnings").setExecutor(new ClearWarningsCommand(this));
		getCommand("ResetWarnings").setExecutor(new ResetWarningsCommand(this));
		getCommand("SetWarnings").setExecutor(new SetWarningsCommand(this));
		getCommand("AddWarnings").setExecutor(new AddWarningsCommand(this));
		getCommand("TakeWarnings").setExecutor(new TakeWarningsCommand(this));
		getCommand("WarningInfo").setExecutor(new WarningInfoCommand(this));

		if (getConfig().getBoolean("resetEnabled")) {
			Bukkit.getScheduler().scheduleSyncRepeatingTask(
					this,
					new Runnable() {

						public void run() {
							for (String key : dFile.getConfig().getKeys(false)) {
								dFile.getConfig().set(key, null);
							}
							dFile.saveConfig();
							getLogger().info(
									"Warnings have been automatically reset!");
						}
					}, getConfig().getLong("resetTime") * 1200,
					getConfig().getLong("resetTime") * 1200);

		}
	}
}
