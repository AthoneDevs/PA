package es.projectalpha.pa.toa.utils;

import es.projectalpha.pa.core.api.PAUser;
import es.projectalpha.pa.core.utils.ItemMaker;
import es.projectalpha.pa.core.utils.Sounds;
import es.projectalpha.pa.toa.TOA;
import es.projectalpha.pa.toa.drops.Drop;
import org.bukkit.inventory.Inventory;

public class TOAMenu {

    private static Inventory variado;
    private static Inventory armas;

    private TOA plugin;

    public TOAMenu(TOA instance) {
        this.plugin = instance;

        //Servidores
        variado = plugin.getServer().createInventory(null, 9, "Vender Variado");
        variado.setItem(0, new ItemMaker(Drop.ARROW.getItem()).setLores("&3Precio venta x1: &c6 zenys").build());
        variado.setItem(1, new ItemMaker(Drop.ROTTEN.getItem()).setLores("&3Precio venta x1: &c6 zenys").build());
        variado.setItem(2, new ItemMaker(Drop.ROD.getItem()).setLores("&3Precio venta x1: &c8 zenys").build());

        //Cosmeticos
        armas = plugin.getServer().createInventory(null, 9, "Vender Armas");
    }

    public static void openMenu(PAUser u, MenuType menuType) {
        Inventory clon = null;

        switch (menuType) {
            case VARIADO:
                clon = variado;
                break;
            case ARMAS:
                clon = armas;
                break;
        }
        if (clon != null) {
            u.getPlayer().closeInventory();
            u.getPlayer().openInventory(clon);
            u.sendSound(Sounds.CLICK);
        }
    }

    public enum MenuType {
        VARIADO, ARMAS
    }
}
