package com.infumia.t3sl4.sandikspawner.menu;

import com.infumia.t3sl4.sandikspawner.SpawnerAPI;
import com.infumia.t3sl4.sandikspawner.chest.type.ChestType;
import com.infumia.t3sl4.sandikspawner.chest.user.User;
import com.infumia.t3sl4.util.oututil.anvilapi.anvilgui.AnvilGUI;
import com.infumia.t3sl4.util.title.base.TitlePlayerOf;
import com.infumia.t3sl4.util.inventory.Pane;
import com.infumia.t3sl4.util.inventory.element.BasicElement;
import com.infumia.t3sl4.util.inventory.event.ElementBasicEvent;
import com.infumia.t3sl4.util.inventory.event.ElementClickEvent;
import com.infumia.t3sl4.util.inventory.event.ElementDragEvent;
import com.infumia.t3sl4.util.inventory.page.ChestPage;
import com.infumia.t3sl4.util.inventory.page.ControllableDownPage;
import com.infumia.t3sl4.util.inventory.pane.BasicPane;
import com.infumia.t3sl4.util.inventory.requirement.ClickTypeReq;
import com.infumia.t3sl4.util.inventory.requirement.NoClickableDownReq;
import com.infumia.t3sl4.util.inventory.target.BasicTarget;
import com.infumia.t3sl4.util.inventory.target.ClickTarget;
import com.infumia.t3sl4.util.inventory.target.DragTarget;
import com.infumia.t3sl4.util.itemstack.item.meta.get.DisplayOf;
import com.infumia.t3sl4.util.itemstack.item.meta.get.LoreOf;
import com.infumia.t3sl4.util.itemstack.item.meta.set.SetDisplayOf;
import com.infumia.t3sl4.util.itemstack.item.meta.set.SetLoreOf;
import com.infumia.t3sl4.util.itemstack.item.meta.set.SetMetaOf;
import com.infumia.t3sl4.util.itemstack.item.set.SetTypeOf;
import com.infumia.t3sl4.util.itemstack.util.Colored;
import io.github.portlek.mcyaml.IYaml;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.cactoos.iterable.Mapped;
import org.cactoos.list.ListOf;
import org.cactoos.scalar.MinOf;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public final class GanimetDeposuMenu implements Menu {

    @NotNull
    private final SpawnerAPI spawnerAPI;

    @NotNull
    private final Menu previousMenu;

    @NotNull
    private final IYaml gDYaml;

    @NotNull
    private final User user;

    public GanimetDeposuMenu(@NotNull SpawnerAPI spawnerAPI, @NotNull Menu previousMenu,
                             @NotNull IYaml gDYaml, @NotNull Player player) {
        this.spawnerAPI = spawnerAPI;
        this.previousMenu = previousMenu;
        this.gDYaml = gDYaml;
        user = spawnerAPI.findByPlayer(player);
    }

    @Override
    public void show() {
        final Pane pane1 = new BasicPane(
            gDYaml.getInt("panes.pane-1.x"),
            gDYaml.getInt("panes.pane-1.y"),
            gDYaml.getInt("panes.pane-1.height"),
            gDYaml.getInt("panes.pane-1.length")
        );
        final Pane pane2 = new BasicPane(
            gDYaml.getInt("panes.pane-2.x"),
            gDYaml.getInt("panes.pane-2.y"),
            gDYaml.getInt("panes.pane-2.height"),
            gDYaml.getInt("panes.pane-2.length")
        );
        final Pane pane3 = new BasicPane(
            gDYaml.getInt("panes.pane-3.x"),
            gDYaml.getInt("panes.pane-3.y"),
            gDYaml.getInt("panes.pane-3.height"),
            gDYaml.getInt("panes.pane-3.length")
        );
        final Pane pane4 = new BasicPane(
            gDYaml.getInt("panes.pane-4.x"),
            gDYaml.getInt("panes.pane-4.y"),
            gDYaml.getInt("panes.pane-4.height"),
            gDYaml.getInt("panes.pane-4.length")
        );
        final Pane paneGanimet = new BasicPane(
            gDYaml.getInt("panes.pane-ganimet.x"),
            gDYaml.getInt("panes.pane-ganimet.y"),
            gDYaml.getInt("panes.pane-ganimet.height"),
            gDYaml.getInt("panes.pane-ganimet.length")
        );

        final ItemStack ganimetElement = gDYaml.getCustomItemStack("elements.ganimet-element");

        for (Map.Entry<String, ChestType> entry : spawnerAPI.chestTypes.entrySet()) {
            final ChestType chestType = entry.getValue();
            final ItemStack clone = replaceAll(ganimetElement.clone(), chestType);

            paneGanimet.add(
                new BasicElement(
                    clone,
                    "ganimet-element-" + chestType.getId(),
                    new BasicTarget(ElementBasicEvent::cancel),
                    new DragTarget(ElementDragEvent::cancel),
                    new ClickTarget(ElementClickEvent::cancel),
                    new ClickTarget(ElementClickEvent::cancel),
                    new ClickTarget(event -> {
                        event.cancel();
                        event.closeView();
                        Bukkit.getScheduler().runTaskLater(spawnerAPI.plugin,() -> new AnvilGUI.Builder()
                            .plugin(spawnerAPI.plugin)
                            .text(spawnerAPI.getConfigs().ganimetAnvilAlis)
                            .onComplete(response(chestType))
                            .open(event.player()), 2L);
                    }, new ClickTypeReq(ClickType.RIGHT)),
                    new ClickTarget(event -> {
                        event.cancel();
                        event.closeView();
                        Bukkit.getScheduler().runTaskLater(spawnerAPI.plugin,() -> new AnvilGUI.Builder()
                            .plugin(spawnerAPI.plugin)
                            .text(
                                new MinOf(
                                    user.getAvailableAmountForItem(chestType.getDrop()),
                                    user.getStorageAmountOfChestType(chestType)
                                ).intValue() + "..."
                            )
                            .onComplete(response(chestType))
                            .open(event.player()), 2L);
                    }, new ClickTypeReq(ClickType.SHIFT_RIGHT)),
                    new ClickTarget(event -> {
                        event.cancel();
                        event.closeView();
                        Bukkit.getScheduler().runTaskLater(spawnerAPI.plugin,() -> new AnvilGUI.Builder()
                            .plugin(spawnerAPI.plugin)
                            .text(spawnerAPI.getConfigs().ganimetAnvilSatis)
                            .onComplete(
                                response(
                                    spawnerAPI.getLanguage().errorYouCantTakeThisSoMuch,
                                    amount -> amount <= user.getStorageAmountOfChestType(chestType),
                                    (amount, player) ->
                                        user.sellStorageItem(player, chestType, amount))
                            ).open(event.player()), 2L);
                    }, new ClickTypeReq(ClickType.MIDDLE))
                )
            );
        }

        pane4.insert(
            new BasicElement(
                gDYaml.getCustomItemStack("elements.back-element"),
                "back-element",
                new BasicTarget(ElementBasicEvent::cancel),
                new DragTarget(ElementDragEvent::cancel),
                new ClickTarget(event -> {
                    event.cancel();
                    previousMenu.show();
                })
            ),
            gDYaml.getInt("elements.back-element.x"),
            gDYaml.getInt("elements.back-element.y"),
            false
        );

        user.openPage(
            new ControllableDownPage(
                new ChestPage(
                    new Colored(gDYaml.getString("title").orElse("")).value(),
                    gDYaml.getInt("size"),
                    pane1,
                    pane2,
                    pane3,
                    pane4,
                    paneGanimet
                ),
                new NoClickableDownReq()
            )
        );
    }

    @NotNull
    public ItemStack replaceAll(@NotNull ItemStack item, @NotNull ChestType chestType) {
        if (user.getPlayer() == null) {
            return item;
        }

        return new SetMetaOf(
            new SetTypeOf(
                item,
                chestType.getDrop().getType()
            ),
            new SetLoreOf(
                new SetDisplayOf(
                    item,
                    replaceAll(
                        new DisplayOf(
                            item
                        ).value().orElse(""),
                        chestType
                    )
                ).value(),
                new ListOf<>(
                    new Mapped<>(
                        lore -> replaceAll(lore, chestType),
                        new LoreOf(
                            item
                        ).value()
                    )
                )
            )
        ).value();
    }

    @NotNull
    public String replaceAll(@NotNull String text, @NotNull ChestType chestType) {
        if (user.getPlayer() == null) {
            return text;
        }

        return text
            .replaceAll("%chest-name%", chestType.getName())
            .replaceAll("%chest-amount%", String.valueOf(user.getAmountOfChestType(chestType)))
            .replaceAll("%drop-amount%", String.valueOf(user.getStorageAmountOfChestType(chestType)))
            .replaceAll(
                "%available-amount%",
                String.valueOf(
                    new MinOf(
                        user.getAvailableAmountForItem(chestType.getDrop()),
                        user.getStorageAmountOfChestType(chestType)
                    ).intValue()
                )
            );
    }

    @NotNull
    private BiFunction<Player, String, AnvilGUI.Response> response(@NotNull ChestType chestType) {
        return response(
            spawnerAPI.getLanguage().errorYouCantTakeThisSoMuch,
            amount -> amount <= new MinOf(
                user.getAvailableAmountForItem(chestType.getDrop()),
                user.getStorageAmountOfChestType(chestType)
            ).intValue(),
            (amount, player) -> {
                final ItemStack itemStack = chestType.getDrop().clone();
                final int amount1 = amount / itemStack.getMaxStackSize();
                final int amount2 = amount % itemStack.getMaxStackSize();

                itemStack.setAmount(itemStack.getMaxStackSize());
                IntStream.range(0, amount1).forEach(value -> player.getInventory().addItem(itemStack));
                itemStack.setAmount(amount2);
                player.getInventory().addItem(itemStack);

                user.removeStorage(chestType, amount);
            }
        );
    }

    @NotNull
    private BiFunction<Player, String, AnvilGUI.Response> response(@NotNull String errorTitle,
                                              @NotNull Predicate<Integer> function,
                                              @NotNull BiConsumer<Integer, Player> consumer) {
        return (player, s) -> {
            final int amount;

            try {
                amount = Integer.parseInt(s);
            } catch (Exception exception) {
                new TitlePlayerOf(
                    player
                ).sendTitle(spawnerAPI.getLanguage().errorInputNumber);
                return AnvilGUI.Response.close();
            }

            if (function.test(amount)) {
                consumer.accept(amount, player);
                return AnvilGUI.Response.close();
            }

            new TitlePlayerOf(
                player
            ).sendTitle(new Colored(errorTitle).value());
            return AnvilGUI.Response.close();
        };
    }
}
