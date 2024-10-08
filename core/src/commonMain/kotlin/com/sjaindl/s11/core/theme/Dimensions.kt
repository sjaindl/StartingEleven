package com.sjaindl.s11.core.theme

import androidx.compose.ui.unit.dp

class Dimensions {
   val spacing = Spacing()
   val cardCornerRadius = 12.dp
   val cardDefaultElevation = 8.dp

   class Spacing() {
      val none = 0.dp
      val xxs = 4.dp
      val xs = 8.dp
      val s = 12.dp
      val md = 16.dp
      val lg = 24.dp
      val xl = 32.dp
      val xxl = 48.dp
   }
}
