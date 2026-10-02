package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.StatusAmber
import com.example.ui.theme.StatusGreen
import com.example.ui.theme.StatusRed

@Composable
fun StockBadge(
    currentStock: Int,
    minThreshold: Int = 48,
    masterCartonSize: Int = 72,
    isOwnerView: Boolean = false,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, dotColor, text) = when {
        currentStock == 0 -> {
            Tuple4(
                Color(0xFFFEE2E2),
                StatusRed,
                StatusRed,
                if (isOwnerView) "Out of Stock (0 pcs)" else "Out of Stock (अनुपलब्ध)"
            )
        }
        currentStock <= minThreshold -> {
            Tuple4(
                Color(0xFFFEF3C7),
                StatusAmber,
                StatusAmber,
                if (isOwnerView) "Low Stock: $currentStock pcs" else "Limited Stock (सीमित मात्रा)"
            )
        }
        else -> {
            val label = if (isOwnerView) {
                val cartons = currentStock / masterCartonSize
                if (cartons > 0) "$currentStock pcs ($cartons Ctn)" else "$currentStock pcs"
            } else {
                "In Stock (उपलब्ध)"
            }
            Tuple4(
                Color(0xFFDCFCE7),
                StatusGreen,
                StatusGreen,
                label
            )
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(dotColor)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = text,
                color = textColor,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private data class Tuple4<A, B, C, D>(val a: A, val b: B, val c: C, val d: D)
