package org.ozaii.bskits;

import org.bukkit.plugin.java.JavaPlugin;
import org.ozaii.bskits.command.KitCommand;
import org.ozaii.bskits.command.KitCommandTabCompleter;
import org.ozaii.bskits.dao.KitDao;
import org.ozaii.bskits.dao.KitItemDao;
import org.ozaii.bskits.database.Database;
import org.ozaii.bskits.listener.KitGuiListener;
import org.ozaii.bskits.services.KitService;

import java.sql.SQLException;

public final class BSKits extends JavaPlugin {

    private static volatile BSKits instance;

    private Database database;
    private KitDao kitDao;
    private KitItemDao kitItemDao;
    private KitService kitService;



    @Override
    public void onEnable() {
        System.setProperty("com.j256.ormlite.logger.type", "LOCAL");

        instance = this;

        try {
            connectDatabase("database");
            setupServices();
            getCommand("kit").setExecutor(new KitCommand(kitService));
            getCommand("kit").setTabCompleter(new KitCommandTabCompleter(kitService));
            getServer().getPluginManager().registerEvents(new KitGuiListener(kitService), this);
            getLogger().info("BSKits başarıyla aktif edildi!");
        } catch (Exception e) {
            getLogger().severe("Plugin başlatılırken hata oluştu: " + e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this); // secure closing
        }
    }

    @Override
    public void onDisable() {
        try {
            if (database != null) {database.disconnect();}
            getLogger().info("BSKits başarıyla kapatıldı.");
        } catch (Exception e) {
            getLogger().severe("Veritabanı kapatılırken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Veritabanını başlatır
     *
     * @param fileName .db uzantısı eklemeyin
     */
    private void connectDatabase(String fileName) throws SQLException {
        database = Database.getInstance(getDataFolder() + "/" + fileName + ".db");
        database.connect();
        database.createTable(org.ozaii.bskits.models.Kit.class);
        database.createTable(org.ozaii.bskits.models.KitItem.class);
    }

    /**
     * DAO ve servis katmanlarını başlatır (DI secure)
     */
    private void setupServices() throws SQLException {
        kitDao = new KitDao(database.getConnectionSource());
        kitItemDao = new KitItemDao(database.getConnectionSource());
        kitService = KitService.getInstance(kitDao, kitItemDao);
    }

    // Singleton plugin instance
    public static BSKits getInstance() {
        if (instance == null) {
            synchronized (BSKits.class) {
                if (instance == null) {
                    throw new IllegalStateException("BSKits henüz başlatılmamış!");
                }
            }
        }
        return instance;
    }

    /* Getters. */

    public Database getDatabase() {
        return database;
    }

    public KitService getKitService() {
        return kitService;
    }

    public KitDao getKitDao() {
        return kitDao;
    }

    public KitItemDao getKitItemDao() {
        return kitItemDao;
    }

    /* made by : ozaii in 2025 */
}
