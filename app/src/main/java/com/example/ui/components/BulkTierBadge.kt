package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BulkTierInfo
import com.example.ui.theme.AmberCartonDark
import com.example.ui.theme.EmeraldProfitDark

@Composable
fun BulkTierBadge(
    tier: BulkTierInfo,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (tier.discountPercent) {
        40.0 -> Pair(Color(0xFFDCFCE7), EmeraldProfitDark)
        32.0 -> Pair(Color(0xFFDBEAFE), Color(0xFF1D4ED8))
        25.0 -> Pair(Color(0xFFFEF3C7), AmberCartonDark)
        else -> Pair(Color(0xFFF1F5F9), Color(0xFF475569))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = "${tier.name} (${tier.discountPercent.toInt()}% Margin)",
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
