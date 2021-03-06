package es.projectalpha.pa.rage.tasks;

import es.projectalpha.pa.core.api.PAData;
import es.projectalpha.pa.core.utils.*;
import es.projectalpha.pa.rage.RageGames;
import es.projectalpha.pa.rage.api.RagePlayer;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;
import org.inventivetalent.bossbar.BossBarAPI;

public class GameTask extends BukkitRunnable {

    private final RageGames plugin;
    private int count = 210;

    public GameTask(RageGames instance) {
        this.plugin = instance;
    }

    public void run() {
        plugin.getGm().getPlaying().forEach(p -> {
            BossBarUtils.create(p.getPlayer(), "&cTiempo restante: &6" + count, BossBarAPI.Color.BLUE, BossBarAPI.Style.PROGRESS);
            p.getPlayer().setFireTicks(0);
            p.getPlayer().setHealthScale(20d);
        });

        hasEnoughPlayers();

        switch (count) {
            case 210:
                plugin.getGm().getPlaying().forEach(p -> {
                    p.teleport(plugin.getAm().getRandomSpawn());
                    Title.sendTitle(p.getPlayer(),1,2,1, ChatColor.GREEN + "Ronda de calentamiento","");
                    p.getPlayer().setWalkSpeed(0.3f);
                });
                break;
            case 180:
                plugin.getGm().getPlaying().forEach(p -> {
                    plugin.getGm().resetPoint(p);
                    p.resetPlayer();
                    p.teleport(plugin.getAm().getRandomSpawn());
                    Title.sendTitle(p.getPlayer(),1,2,1,ChatColor.RED + "¡Empieza el juego!","");
                    p.getPlayer().setWalkSpeed(0.3f);
                });
                break;
            case 60:
                plugin.getGm().getPlaying().forEach(p -> {
                    Title.sendTitle(p.getPlayer(),1,2,1,ChatColor.RED + "¡60 segundos!",ChatColor.GOLD + "¡Velocidad x2!");
                    p.getPlayer().setWalkSpeed(0.4f);
                });

                break;
            case 3:
                checkWinner();
                break;
            case 0:
                end();
                break;
        }
        count--;
    }

    private void checkWinner() {
        RagePlayer[] users = plugin.getGm().reorder().keySet().toArray(new RagePlayer[plugin.getGm().reorder().size()]);
        if (users.length == 0) return;

        Utils.broadcastMsg("------------------------");
        Utils.broadcastMsg("");
        Utils.broadcastMsg("1º &c" + users[0].getName() + "&7: &6" + plugin.getGm().getScore().get(users[0]) + " puntos.");
        Utils.broadcastMsg("2º &c" + users[1].getName() + "&7: &6" + plugin.getGm().getScore().get(users[1]) + " puntos.");

        if(users.length >= 3) {
            Utils.broadcastMsg("3º &c" + users[2].getName() + "&7: &6" + plugin.getGm().getScore().get(users[2]) + " puntos.");
        }

        Utils.broadcastMsg("");
        Utils.broadcastMsg("------------------------");

        plugin.getGm().getPlaying().forEach(p ->{
            p.getPlayer().getInventory().clear();
            p.getPlayer().setGameMode(GameMode.SPECTATOR);
            BossBarAPI.removeAllBars(p.getPlayer());
        });
    }

    private void hasEnoughPlayers() {
        if (plugin.getGm().getPlaying().size() > 1) return;

        plugin.getGm().getPlaying().forEach(u -> {
            u.sendMessage(PAData.RG.getPrefix() + "&cEl juego se ha acabado puesto que no hay jugadores");
            u.sendSound(Sounds.LEVEL_UP);
        });
        checkWinner();
        end();
    }

    private void end() {
        new ShutdownTask(plugin).runTaskTimer(plugin, 0, 20);
        GameState.setState(GameState.FINISHED);
        cancel();
    }
}
