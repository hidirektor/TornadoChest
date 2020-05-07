package com.infumia.t3sl4.util.itemstack.util;

import java.util.List;

import org.cactoos.Scalar;
import org.cactoos.text.TextEnvelope;
import org.jetbrains.annotations.NotNull;

public final class ListToString extends TextEnvelope {
   public ListToString(@NotNull List<String> list) {
      super((Scalar<String>) () -> {
         StringBuilder sb = new StringBuilder();
         list.forEach((s) -> {
            sb.append(s).append("\n");
         });
         return sb.toString();
      });
   }
}
