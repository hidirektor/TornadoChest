package com.infumia.t3sl4.util.oututil.bookapi;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import org.bukkit.Achievement;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.BookMeta.Generation;

public final class BookUtil {
   private static final boolean canTranslateDirectly;

   public static void openPlayer(Player p, ItemStack book) {
      CustomBookOpenEvent event = new CustomBookOpenEvent(p, book, false);
      Bukkit.getPluginManager().callEvent(event);
      if (!event.isCancelled()) {
         p.closeInventory();
         ItemStack hand = p.getItemInHand();
         p.setItemInHand(event.getBook());
         p.updateInventory();
         NmsBookHelper.openBook(p, event.getBook(), event.getHand() == CustomBookOpenEvent.Hand.OFF_HAND);
         p.setItemInHand(hand);
         p.updateInventory();
      }
   }

   public static BookBuilder writtenBook() {
      return new BookBuilder(new ItemStack(Material.WRITTEN_BOOK));
   }

   static {
      boolean success = true;

      try {
         ChatColor.BLACK.asBungee();
      } catch (NoSuchMethodError var2) {
         success = false;
      }

      canTranslateDirectly = success;
   }

   public interface HoverAction {
      Action action();

      BaseComponent[] value();

      static HoverAction showText(BaseComponent... text) {
         return new SimpleHoverAction(Action.SHOW_TEXT, text);
      }

      static HoverAction showText(String text) {
         return new SimpleHoverAction(Action.SHOW_TEXT, new BaseComponent[]{new TextComponent(text)});
      }

      static HoverAction showItem(BaseComponent... item) {
         return new SimpleHoverAction(Action.SHOW_ITEM, item);
      }

      static HoverAction showItem(ItemStack item) {
         return new SimpleHoverAction(Action.SHOW_ITEM, NmsBookHelper.itemToComponents(item));
      }

      static HoverAction showEntity(BaseComponent... entity) {
         return new SimpleHoverAction(Action.SHOW_ENTITY, entity);
      }

      static HoverAction showEntity(UUID uuid, String type, String name) {
         return new SimpleHoverAction(Action.SHOW_ENTITY, NmsBookHelper.jsonToComponents("{id:\"" + uuid + "\",type:\"" + type + "\"name:\"" + name + "\"}"));
      }

      static HoverAction showEntity(Entity entity) {
         return showEntity(entity.getUniqueId(), entity.getType().getName(), entity.getName());
      }

      static HoverAction showAchievement(String achievementId) {
         return new SimpleHoverAction(Action.SHOW_ACHIEVEMENT, new BaseComponent[]{new TextComponent("achievement." + achievementId)});
      }

      static HoverAction showAchievement(Achievement achievement) {
         return showAchievement(AchievementUtil.toId(achievement));
      }

      static HoverAction showStatistic(String statisticId) {
         return new SimpleHoverAction(Action.SHOW_ACHIEVEMENT, new BaseComponent[]{new TextComponent("statistic." + statisticId)});
      }

      public static class SimpleHoverAction implements HoverAction {
         private final Action action;
         private final BaseComponent[] value;

         public SimpleHoverAction(Action action, BaseComponent... value) {
            this.action = action;
            this.value = value;
         }

         public Action action() {
            return this.action;
         }

         public BaseComponent[] value() {
            return this.value;
         }
      }
   }

   public interface ClickAction {
      net.md_5.bungee.api.chat.ClickEvent.Action action();

      String value();

      static ClickAction runCommand(String command) {
         return new SimpleClickAction(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, command);
      }

      /** @deprecated */
      @Deprecated
      static ClickAction suggestCommand(String command) {
         return new SimpleClickAction(net.md_5.bungee.api.chat.ClickEvent.Action.SUGGEST_COMMAND, command);
      }

      static ClickAction openUrl(String url) {
         if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new IllegalArgumentException("Invalid url: \"" + url + "\", it should start with http:// or https://");
         } else {
            return new SimpleClickAction(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, url);
         }
      }

      static ClickAction changePage(int page) {
         return new SimpleClickAction(net.md_5.bungee.api.chat.ClickEvent.Action.CHANGE_PAGE, Integer.toString(page));
      }

      public static class SimpleClickAction implements ClickAction {
         private final net.md_5.bungee.api.chat.ClickEvent.Action action;
         private final String value;

         public net.md_5.bungee.api.chat.ClickEvent.Action action() {
            return this.action;
         }

         public String value() {
            return this.value;
         }

         @ConstructorProperties({"action", "value"})
         public SimpleClickAction(net.md_5.bungee.api.chat.ClickEvent.Action action, String value) {
            this.action = action;
            this.value = value;
         }
      }
   }

   public static class TextBuilder {
      private String text = "";
      private ClickAction onClick = null;
      private HoverAction onHover = null;
      private ChatColor color;
      private ChatColor[] style;

      public TextBuilder() {
         this.color = ChatColor.BLACK;
      }

      public TextBuilder color(ChatColor color) {
         if (color != null && !color.isColor()) {
            throw new IllegalArgumentException("Argument isn't a color!");
         } else {
            this.color = color;
            return this;
         }
      }

      public TextBuilder style(ChatColor... style) {
         ChatColor[] var2 = style;
         int var3 = style.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            ChatColor c = var2[var4];
            if (!c.isFormat()) {
               throw new IllegalArgumentException("Argument isn't a style!");
            }
         }

         this.style = style;
         return this;
      }

      public BaseComponent build() {
         TextComponent res = new TextComponent(this.text);
         if (this.onClick != null) {
            res.setClickEvent(new ClickEvent(this.onClick.action(), this.onClick.value()));
         }

         if (this.onHover != null) {
            res.setHoverEvent(new HoverEvent(this.onHover.action(), this.onHover.value()));
         }

         if (this.color != null) {
            if (BookUtil.canTranslateDirectly) {
               res.setColor(this.color.asBungee());
            } else {
               res.setColor(net.md_5.bungee.api.ChatColor.getByChar(this.color.getChar()));
            }
         }

         if (this.style != null) {
            ChatColor[] var2 = this.style;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               ChatColor c = var2[var4];
               switch(c) {
               case MAGIC:
                  res.setObfuscated(true);
                  break;
               case BOLD:
                  res.setBold(true);
                  break;
               case STRIKETHROUGH:
                  res.setStrikethrough(true);
                  break;
               case UNDERLINE:
                  res.setUnderlined(true);
                  break;
               case ITALIC:
                  res.setItalic(true);
               }
            }
         }

         return res;
      }

      public static TextBuilder of(String text) {
         return (new TextBuilder()).text(text);
      }

      public TextBuilder text(String text) {
         this.text = text;
         return this;
      }

      public TextBuilder onClick(ClickAction onClick) {
         this.onClick = onClick;
         return this;
      }

      public TextBuilder onHover(HoverAction onHover) {
         this.onHover = onHover;
         return this;
      }

      public String text() {
         return this.text;
      }

      public ClickAction onClick() {
         return this.onClick;
      }

      public HoverAction onHover() {
         return this.onHover;
      }

      public ChatColor color() {
         return this.color;
      }
   }

   public static class PageBuilder {
      private List<BaseComponent> text = new ArrayList();

      public PageBuilder add(String text) {
         this.text.add(TextBuilder.of(text).build());
         return this;
      }

      public PageBuilder add(BaseComponent component) {
         this.text.add(component);
         return this;
      }

      public PageBuilder add(BaseComponent... components) {
         this.text.addAll(Arrays.asList(components));
         return this;
      }

      public PageBuilder add(Collection<BaseComponent> components) {
         this.text.addAll(components);
         return this;
      }

      public PageBuilder newLine() {
         this.text.add(new TextComponent("\n"));
         return this;
      }

      public BaseComponent[] build() {
         return (BaseComponent[])this.text.toArray(new BaseComponent[0]);
      }

      public static PageBuilder of(String text) {
         return (new PageBuilder()).add(text);
      }

      public static PageBuilder of(BaseComponent text) {
         return (new PageBuilder()).add(text);
      }

      public static PageBuilder of(BaseComponent... text) {
         PageBuilder res = new PageBuilder();
         BaseComponent[] var2 = text;
         int var3 = text.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            BaseComponent b = var2[var4];
            res.add(b);
         }

         return res;
      }
   }

   public static class BookBuilder {
      private final BookMeta meta;
      private final ItemStack book;

      public BookBuilder(ItemStack book) {
         this.book = book;
         this.meta = (BookMeta)book.getItemMeta();
      }

      public BookBuilder title(String title) {
         this.meta.setTitle(title);
         return this;
      }

      public BookBuilder author(String author) {
         this.meta.setAuthor(author);
         return this;
      }

      public BookBuilder pagesRaw(String... pages) {
         this.meta.setPages(pages);
         return this;
      }

      public BookBuilder pagesRaw(List<String> pages) {
         this.meta.setPages(pages);
         return this;
      }

      public BookBuilder pages(BaseComponent[]... pages) {
         NmsBookHelper.setPages(this.meta, pages);
         return this;
      }

      public BookBuilder pages(List<BaseComponent[]> pages) {
         NmsBookHelper.setPages(this.meta, (BaseComponent[][])pages.toArray(new BaseComponent[0][]));
         return this;
      }

      public BookBuilder generation(Generation generation) {
         this.meta.setGeneration(generation);
         return this;
      }

      public ItemStack build() {
         this.book.setItemMeta(this.meta);
         return this.book;
      }
   }
}
