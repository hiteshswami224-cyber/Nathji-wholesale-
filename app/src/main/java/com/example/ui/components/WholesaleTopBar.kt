package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.ScreenTab
import com.example.ui.theme.AmberCarton
import com.example.ui.theme.AmberCartonLight
import com.example.ui.theme.AmberContainer
import com.example.ui.theme.MerchantBlue

@Composable
fun WholesaleTopBar(
    currentTab: ScreenTab,
    currentRole: UserRole,
    customerShopName: String,
    cartItemCount: Int,
    lowStockCount: Int,
    onTabSelected: (ScreenTab) -> Unit,
    onSwitchRoleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = if (currentRole == UserRole.OWNER) Color(0xFF1E293B) else MerchantBlue,
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Brand Title & Subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "NATHJI WHOLESALE",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (currentRole == UserRole.OWNER) AmberCarton else AmberCartonLight)
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = if (currentRole == UserRole.OWNER) "OWNER ADMIN" else "B2B FMCG",
                                color = if (currentRole == UserRole.OWNER) Color.White else Color.Black,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = if (currentRole == UserRole.OWNER) {
                            "Warehouse Admin • Stock Add/Less & Dispatch"
                        } else {
                            customerShopName
                        },
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        maxLines = 1
                    )
                }

                // Switch Role Pill (Customer <-> Owner)
                Surface(
                    onClick = onSwitchRoleClick,
                    shape = RoundedCornerShape(20.dp),
                    color = if (currentRole == UserRole.OWNER) AmberContainer else Color.White.copy(alpha = 0.15f),
                    modifier = Modifier.testTag("switch_role_btn")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (currentRole == UserRole.OWNER) Icons.Default.Storefront else Icons.Default.AdminPanelSettings,
                            contentDescription = "Switch Role",
                            tint = if (currentRole == UserRole.OWNER) AmberCarton else Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (currentRole == UserRole.OWNER) "Customer View" else "Owner Admin",
                            color = if (currentRole == UserRole.OWNER) AmberCarton else Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // If Customer: show Cart
                if (currentRole == UserRole.CUSTOMER) {
                    IconButton(
                        onClick = { onTabSelected(ScreenTab.CART) },
                        modifier = Modifier.testTag("topbar_cart_btn")
                    ) {
                        BadgedBox(
                            badge = {
                                if (cartItemCount > 0) {
                                    Badge(containerColor = AmberCartonLight, contentColor = Color.Black) {
                                        Text(text = if (cartItemCount > 99) "99+" else cartItemCount.toString(), fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                tint = if (currentTab == ScreenTab.CART) AmberCartonLight else Color.White
                            )
                        }
                    }
                } else {
                    // If Owner: show low stock alert badge
                    IconButton(
                        onClick = { onTabSelected(ScreenTab.INVENTORY) },
                        modifier = Modifier.testTag("topbar_owner_inventory_btn")
                    ) {
                        BadgedBox(
                            badge = {
                                if (lowStockCount > 0) {
                                    Badge(containerColor = Color(0xFFEF4444), contentColor = Color.White) {
                                        Text(text = lowStockCount.toString(), fontSize = 10.sp)
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Inventory2,
                                contentDescription = "Warehouse Inventory",
                                tint = if (currentTab == ScreenTab.INVENTORY) AmberCartonLight else Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
