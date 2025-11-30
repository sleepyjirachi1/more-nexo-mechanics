package org.autumn.morenexomechanics;

/* Factories */
import org.autumn.morenexomechanics.api.MechanicFactories;
import org.autumn.morenexomechanics.mechanics.factories.SwappableEnchantsFactory;
import org.autumn.morenexomechanics.mechanics.factories.PotionAurasFactory;
import org.autumn.morenexomechanics.mechanics.factories.StatTrackFactory;

/* Listeners */
import org.autumn.morenexomechanics.mechanics.listeners.SwappableEnchantsListener;
import org.autumn.morenexomechanics.mechanics.listeners.StatTrackListener;

/* Runnables */
import org.autumn.morenexomechanics.mechanics.runnables.PotionAurasTask;

/* Nexo */
import com.nexomc.nexo.api.events.NexoMechanicsRegisteredEvent;
import com.nexomc.nexo.mechanics.MechanicsManager;
import com.nexomc.nexo.utils.logs.Logs;

/* Bukkit */
import org.bukkit.event.Listener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private SwappableEnchantsFactory swappableEnchantsFactory;
    private PotionAurasFactory potionAurasFactory;
    private StatTrackFactory statTrackFactory;

    @Override
    public void onEnable() {
        /* Init mechanic factories */
        swappableEnchantsFactory = new SwappableEnchantsFactory();
        potionAurasFactory = new PotionAurasFactory();
        statTrackFactory = new StatTrackFactory();

        /* Init for the API */
        MechanicFactories.bootstrap(
                swappableEnchantsFactory,
                potionAurasFactory,
                statTrackFactory
        );

        /* Registering */
        registerListeners();
        registerCommands();
        hookIntoNexoMechanics();

        /* Create runnable instances */
        new PotionAurasTask(this, potionAurasFactory).start();
    }

    private void registerListeners() {
        registerListener(new SwappableEnchantsListener(this, swappableEnchantsFactory));
        registerListener(new StatTrackListener(this, statTrackFactory));
    }

    private void registerCommands() {
        /* None Implemented */
    }

    private void hookIntoNexoMechanics() {
        Logs.logInfo("MoreNexoMechanics → Waiting for Nexo registry event...");

        registerListener(new Listener() {
            @EventHandler
            public void nRegister(NexoMechanicsRegisteredEvent event) {
                MechanicsManager.INSTANCE.registerMechanicFactory(swappableEnchantsFactory, true);
                MechanicsManager.INSTANCE.registerMechanicFactory(potionAurasFactory, true);
                MechanicsManager.INSTANCE.registerMechanicFactory(statTrackFactory, true);

                Logs.logInfo("Registered Swappable Enchants");
                Logs.logInfo("Registered Potion Auras");
                Logs.logInfo("Registered Stat Track");
            }
        });
    }

    private void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand command = getCommand(name);
        if (command == null) {
            throw new IllegalStateException("Command '" + name + "' is not registered!");
        }
        command.setExecutor(executor);
    }

}
