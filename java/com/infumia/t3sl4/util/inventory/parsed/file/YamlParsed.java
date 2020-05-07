package com.infumia.t3sl4.util.inventory.parsed.file;

import com.infumia.t3sl4.util.inventory.Page;
import com.infumia.t3sl4.util.inventory.Pane;
import com.infumia.t3sl4.util.inventory.Requirement;
import com.infumia.t3sl4.util.inventory.Target;
import com.infumia.t3sl4.util.inventory.element.BasicElement;
import com.infumia.t3sl4.util.inventory.event.ElementBasicEvent;
import com.infumia.t3sl4.util.inventory.event.ElementClickEvent;
import com.infumia.t3sl4.util.inventory.event.ElementDragEvent;
import com.infumia.t3sl4.util.inventory.page.ChestPage;
import com.infumia.t3sl4.util.inventory.pane.BasicPane;
import com.infumia.t3sl4.util.inventory.parsed.Parsed;
import com.infumia.t3sl4.util.inventory.target.BasicTarget;
import com.infumia.t3sl4.util.inventory.target.ClickTarget;
import com.infumia.t3sl4.util.inventory.target.DragTarget;
import com.cryptomorin.xseries.XMaterial;
import io.github.portlek.mcyaml.IYaml;
import io.github.portlek.mcyaml.mck.MckFileConfiguration;
import com.infumia.t3sl4.util.reflection.LoggerOf;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import org.bukkit.entity.Player;
import org.cactoos.iterable.Mapped;
import org.cactoos.map.MapEntry;
import org.cactoos.map.MapOf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class YamlParsed implements Parsed {
   private static final Logger LOGGER = new LoggerOf(new Class[]{YamlParsed.class});
   @NotNull
   private final IYaml yaml;
   @NotNull
   private final Map<String, Pane> cachePanes = new HashMap();
   @Nullable
   private Page cachePage;

   public YamlParsed(@NotNull IYaml yaml) {
      this.yaml = yaml;
   }

   public void showTo(@NotNull Player player) {
      Page page = this.createPage();
      List<Pane> panes = new ArrayList(this.createPanes().values());
      IntStream.range(0, panes.size()).forEach((i) -> {
         page.add((Pane)panes.get(i), i);
      });
      page.showTo(player);
   }

   @NotNull
   public Page createPage() {
      if (this.cachePage != null) {
         return this.cachePage;
      } else {
         String id = (String)this.yaml.getString("id").orElse("");
         if (id.isEmpty()) {
            LOGGER.warning("#createPage() -> Cannot create page from yaml, cause of it has not 'id' property!");
            return this.createEmptyPage();
         } else {
            String title = (String)this.yaml.getString("title").orElse("");
            int size = this.yaml.getInt("size");
            if (size / 9 != 0) {
               LOGGER.warning("#createPage() -> Cannot create page from yaml with id " + id + ", cause of it's not multiplies of nine or there is not a 'size' property!");
               return this.createEmptyPage();
            } else {
               this.cachePage = new ChestPage(title, size, new Pane[0]);
               return this.cachePage;
            }
         }
      }
   }

   @NotNull
   public Map<String, Pane> createPanes() {
      if (this.cachePanes.isEmpty()) {
         return this.cachePanes;
      } else {
         if (this.yaml.getSection("panes") instanceof MckFileConfiguration) {
            this.yaml.createSection("panes");
         }

         this.cachePanes.putAll(new MapOf(new Mapped((key) -> {
            return new MapEntry(key, new BasicPane(this.yaml.getInt("panes." + key + ".x"), this.yaml.getInt("panes." + key + ".y"), this.yaml.getInt("panes." + key + ".height"), this.yaml.getInt("panes." + key + ".length")));
         }, this.yaml.getSection("panes").getKeys(false))));
         return this.cachePanes;
      }
   }

   @NotNull
   public Pane getOrCreatePaneById(@NotNull String id) {
      if (this.cachePanes.containsKey(id)) {
         return (Pane)this.cachePanes.get(id);
      } else {
         Pane pane = new BasicPane(this.yaml.getInt("panes." + id + ".x"), this.yaml.getInt("panes." + id + ".y"), this.yaml.getInt("panes." + id + ".height"), this.yaml.getInt("panes." + id + ".length"));
         this.cachePanes.put(id, pane);
         return pane;
      }
   }

   public void createAndInsertElement(@NotNull Pane pane, boolean encrypt, @NotNull String id, @NotNull Target... targets) {
      pane.insert(new BasicElement(this.yaml.getCustomItemStack("elements." + id), id, encrypt, targets), this.yaml.getInt("elements." + id + ".x"), this.yaml.getInt("elements." + id + ".y"), false);
   }

   public void createAndInsertElement(@NotNull Pane pane, @NotNull String id, @NotNull Target... targets) {
      this.createAndInsertElement(pane, true, id, targets);
   }

   public void createAndInsertElement(@NotNull String paneId, boolean encrypt, @NotNull String id, @NotNull Target... targets) {
      this.createAndInsertElement(this.getOrCreatePaneById(paneId), encrypt, id, targets);
   }

   public void createAndInsertElement(@NotNull String paneId, @NotNull String id, @NotNull Target... targets) {
      this.createAndInsertElement(paneId, true, id, targets);
   }

   @NotNull
   private Page createEmptyPage() {
      return new ChestPage("", 9, new Pane[]{new BasicPane(0, 0, 1, 9, new BasicElement(XMaterial.TNT.parseItem(), new Target[]{new BasicTarget(ElementBasicEvent::cancel, new Requirement[0]), new DragTarget(ElementDragEvent::cancel, new Requirement[0]), new ClickTarget(ElementClickEvent::cancel, new Requirement[0])}))});
   }
}
