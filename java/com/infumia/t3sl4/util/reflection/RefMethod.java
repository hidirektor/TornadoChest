package com.infumia.t3sl4.util.reflection;

import org.jetbrains.annotations.NotNull;

public interface RefMethod {
   @NotNull
   RefMethodExecuted of(@NotNull Object var1);

   @NotNull
   Object call(@NotNull Object var1, @NotNull Object... var2);
}
