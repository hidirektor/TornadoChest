package com.infumia.t3sl4.sandikspawner.menu;

import com.infumia.t3sl4.util.inventory.Element;
import com.infumia.t3sl4.util.inventory.Pane;
import com.infumia.t3sl4.util.inventory.element.BasicElement;
import com.infumia.t3sl4.util.inventory.event.ElementBasicEvent;
import com.infumia.t3sl4.util.inventory.event.ElementDragEvent;
import com.infumia.t3sl4.util.inventory.page.ChestPage;
import com.infumia.t3sl4.util.inventory.page.ControllableDownPage;
import com.infumia.t3sl4.util.inventory.pane.BasicPane;
import com.infumia.t3sl4.util.inventory.requirement.NoClickableDownReq;
import com.infumia.t3sl4.util.inventory.target.BasicTarget;
import com.infumia.t3sl4.util.inventory.target.ClickTarget;
import com.infumia.t3sl4.util.inventory.target.DragTarget;
import com.infumia.t3sl4.util.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import com.infumia.t3sl4.sandikspawner.SpawnerAPI;
import com.infumia.t3sl4.sandikspawner.chest.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class GenelAyarlarMenu implements Menu {

    @NotNull
    private SpawnerAPI spawnerAPI;

    @NotNull
    private final IYaml yetkililerMenu;

    @NotNull
    private final IYaml ganimetDeposuMenu;

    @NotNull
    private final IYaml genelAyarlarMenu;

    @NotNull
    private final User user;

    public GenelAyarlarMenu(@NotNull Player player, @NotNull SpawnerAPI spawnerAPI) {
        this.spawnerAPI = spawnerAPI;
        yetkililerMenu = spawnerAPI.yetkilerMenu;
        ganimetDeposuMenu = spawnerAPI.ganimetDeposuMenu;
        genelAyarlarMenu = spawnerAPI.genelAyarlarMenu;
        user = spawnerAPI.findByPlayer(player);
    }

    @Override
    public void show() {
        final ItemStack esyaDusmesiItem = genelAyarlarMenu.getCustomItemStack("elements.esya-dusmesi-element");
        final ItemStack oyuncuEkleItem = genelAyarlarMenu.getCustomItemStack("elements.oyuncu-ekle-element");

        user.replaceAll(esyaDusmesiItem);
        user.replaceAll(oyuncuEkleItem);

        final Element esyaDusmesiElement = new BasicElement(
            esyaDusmesiItem,
            "esya-dusmesi-element",
            new BasicTarget(ElementBasicEvent::cancel),
            new DragTarget(ElementDragEvent::cancel),
            new ClickTarget(event -> {
                event.cancel();
                user.changeEsyaDusmesi();
                show();
            })
        );
        final Element oyuncuEkleElement = new BasicElement(
            oyuncuEkleItem,
            "oyuncu-ekle-element",
            new BasicTarget(ElementBasicEvent::cancel),
            new DragTarget(ElementDragEvent::cancel),
            new ClickTarget(event -> {
                event.cancel();
                event.closeView();
                Bukkit.getScheduler().runTaskLater(spawnerAPI.plugin, () -> new YetkililerMenu(
                    this,
                    spawnerAPI,
                    yetkililerMenu,
                    event.player()
                ).show(), 2L);
            })
        );
        final Pane pane = new BasicPane(
            genelAyarlarMenu.getInt("panes.main-pane.x"),
            genelAyarlarMenu.getInt("panes.main-pane.y"),
            genelAyarlarMenu.getInt("panes.main-pane.height"),
            genelAyarlarMenu.getInt("panes.main-pane.length")
        );

        pane.insert(
            new BasicElement(
                genelAyarlarMenu.getCustomItemStack("elements.ganimet-deposu-element"),
                "ganimet-deposu-element",
                new BasicTarget(ElementBasicEvent::cancel),
                new DragTarget(ElementDragEvent::cancel),
                new ClickTarget(event -> {
                    event.cancel();
                    new GanimetDeposuMenu(
                        spawnerAPI,
                        this,
                        ganimetDeposuMenu,
                        event.player()
                    ).show();
                })
            ),
            genelAyarlarMenu.getInt("elements.ganimet-deposu-element.x"),
            genelAyarlarMenu.getInt("elements.ganimet-deposu-element.y"),
            false
        );
        pane.insert(
            esyaDusmesiElement,
            genelAyarlarMenu.getInt("elements.esya-dusmesi-element.x"),
            genelAyarlarMenu.getInt("elements.esya-dusmesi-element.y"),
            false
        );
        pane.insert(
            oyuncuEkleElement,
            genelAyarlarMenu.getInt("elements.oyuncu-ekle-element.x"),
            genelAyarlarMenu.getInt("elements.oyuncu-ekle-element.y"),
            false
        );

        user.openPage(
            new ControllableDownPage(
                new ChestPage(
                    new Colored(genelAyarlarMenu.getString("title").orElse("")).value(),
                    genelAyarlarMenu.getInt("size"),
                    pane
                ),
                new NoClickableDownReq()
            )
        );
    }

}
