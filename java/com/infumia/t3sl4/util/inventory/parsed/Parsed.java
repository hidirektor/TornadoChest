package com.infumia.t3sl4.util.inventory.parsed;

import com.infumia.t3sl4.util.inventory.Page;
import com.infumia.t3sl4.util.inventory.Pane;
import com.infumia.t3sl4.util.inventory.Target;
import java.util.Map;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Parsed {
   void showTo(@NotNull Player var1);

   @NotNull
   Page createPage();

   @NotNull
   Map<String, Pane> createPanes();

   @NotNull
   Pane getOrCreatePaneById(@NotNull String var1);

   void createAndInsertElement(@NotNull Pane var1, boolean var2, @NotNull String var3, @NotNull Target... var4);

   void createAndInsertElement(@NotNull Pane var1, @NotNull String var2, @NotNull Target... var3);

   void createAndInsertElement(@NotNull String var1, boolean var2, @NotNull String var3, @NotNull Target... var4);

   void createAndInsertElement(@NotNull String var1, @NotNull String var2, @NotNull Target... var3);
}
