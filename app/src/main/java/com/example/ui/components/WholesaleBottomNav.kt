package com.example.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.ScreenTab
import com.example.ui.theme.AmberCarton

@Composable
fun WholesaleBottomNav(
    currentTab: ScreenTab,
    currentRole: UserRole,
    cartItemCount: Int,
    lowStockCount: Int,
    onTabSelected: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        if (currentRole == UserRole.CUSTOMER) {
            // CUSTOMER TABS
            // 1. Catalog
            NavigationBarItem(
                selected = currentTab == ScreenTab.CATALOG,
                onClick = { onTabSelected(ScreenTab.CATALOG) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == ScreenTab.CATALOG) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                        contentDescription = "Catalog"
                    )
                },
                label = { Text(text = "Catalog", fontSize = 11.sp, fontWeight = if (currentTab == ScreenTab.CATALOG) FontWeight.Bold else FontWeight.Normal) },
                modifier = Modifier.testTag("nav_customer_catalog")
            )

            // 2. Cart
            NavigationBarItem(
                selected = currentTab == ScreenTab.CART,
                onClick = { onTabSelected(ScreenTab.CART) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (cartItemCount > 0) {
                                Badge(containerColor = AmberCarton, contentColor = Color.White) {
                                    Text(text = cartItemCount.toString(), fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.CART) Icons.Filled.ShoppingCart else Icons.Outlined.ShoppingCart,
                            contentDescription = "Cart"
                        )
                    }
                },
                label = { Text(text = "Cart", fontSize = 11.sp, fontWeight = if (currentTab == ScreenTab.CART) FontWeight.Bold else FontWeight.Normal) },
                modifier = Modifier.testTag("nav_customer_cart")
            )

            // 3. My Orders & Tracking
            NavigationBarItem(
                selected = currentTab == ScreenTab.ORDERS,
                onClick = { onTabSelected(ScreenTab.ORDERS) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == ScreenTab.ORDERS) Icons.Filled.Description else Icons.Outlined.Description,
                        contentDescription = "My Orders"
                    )
                },
                label = { Text(text = "My Orders", fontSize = 11.sp, fontWeight = if (currentTab == ScreenTab.ORDERS) FontWeight.Bold else FontWeight.Normal) },
                modifier = Modifier.testTag("nav_customer_orders")
            )

            // 4. Customer Account / Profile
            NavigationBarItem(
                selected = currentTab == ScreenTab.ACCOUNT,
                onClick = { onTabSelected(ScreenTab.ACCOUNT) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == ScreenTab.ACCOUNT) Icons.Filled.AccountCircle else Icons.Outlined.AccountCircle,
                        contentDescription = "Account"
                    )
                },
                label = { Text(text = "Profile", fontSize = 11.sp, fontWeight = if (currentTab == ScreenTab.ACCOUNT) FontWeight.Bold else FontWeight.Normal) },
                modifier = Modifier.testTag("nav_customer_account")
            )

            // 5. Margin Calculator
            NavigationBarItem(
                selected = currentTab == ScreenTab.CALCULATOR,
                onClick = { onTabSelected(ScreenTab.CALCULATOR) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == ScreenTab.CALCULATOR) Icons.Filled.Calculate else Icons.Outlined.Calculate,
                        contentDescription = "Margins"
                    )
                },
                label = { Text(text = "Margins", fontSize = 11.sp, fontWeight = if (currentTab == ScreenTab.CALCULATOR) FontWeight.Bold else FontWeight.Normal) },
                modifier = Modifier.testTag("nav_customer_margins")
            )
        } else {
            // OWNER TABS
            // 1. All Orders & Dispatch
            NavigationBarItem(
                selected = currentTab == ScreenTab.ORDERS,
                onClick = { onTabSelected(ScreenTab.ORDERS) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == ScreenTab.ORDERS) Icons.Filled.Dashboard else Icons.Outlined.Dashboard,
                        contentDescription = "All Orders"
                    )
                },
                label = { Text(text = "Orders", fontSize = 11.sp, fontWeight = if (currentTab == ScreenTab.ORDERS) FontWeight.Bold else FontWeight.Normal) },
                modifier = Modifier.testTag("nav_owner_orders")
            )

            // 2. Inventory Management (Add-Less Stock & Audit)
            NavigationBarItem(
                selected = currentTab == ScreenTab.INVENTORY,
                onClick = { onTabSelected(ScreenTab.INVENTORY) },
                icon = {
                    BadgedBox(
                        badge = {
                            if (lowStockCount > 0) {
                                Badge(containerColor = Color(0xFFEF4444), contentColor = Color.White) {
                                    Text(text = lowStockCount.toString(), fontSize = 9.sp)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (currentTab == ScreenTab.INVENTORY) Icons.Filled.Inventory else Icons.Outlined.Inventory,
                            contentDescription = "Inventory"
                        )
                    }
                },
                label = { Text(text = "Stock +/-", fontSize = 11.sp, fontWeight = if (currentTab == ScreenTab.INVENTORY) FontWeight.Bold else FontWeight.Normal) },
                modifier = Modifier.testTag("nav_owner_inventory")
            )

            // 3. Wholesale Catalog Master
            NavigationBarItem(
                selected = currentTab == ScreenTab.CATALOG,
                onClick = { onTabSelected(ScreenTab.CATALOG) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == ScreenTab.CATALOG) Icons.Filled.ShoppingBag else Icons.Outlined.ShoppingBag,
                        contentDescription = "Catalog"
                    )
                },
                label = { Text(text = "Catalog", fontSize = 11.sp, fontWeight = if (currentTab == ScreenTab.CATALOG) FontWeight.Bold else FontWeight.Normal) },
                modifier = Modifier.testTag("nav_owner_catalog")
            )

            // 4. Customers & Khata Accounts
            NavigationBarItem(
                selected = currentTab == ScreenTab.ACCOUNT,
                onClick = { onTabSelected(ScreenTab.ACCOUNT) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == ScreenTab.ACCOUNT) Icons.Filled.Group else Icons.Outlined.Group,
                        contentDescription = "Customers"
                    )
                },
                label = { Text(text = "Khata", fontSize = 11.sp, fontWeight = if (currentTab == ScreenTab.ACCOUNT) FontWeight.Bold else FontWeight.Normal) },
                modifier = Modifier.testTag("nav_owner_customers")
            )

            // 5. Bulk Margin Simulator
            NavigationBarItem(
                selected = currentTab == ScreenTab.CALCULATOR,
                onClick = { onTabSelected(ScreenTab.CALCULATOR) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == ScreenTab.CALCULATOR) Icons.Filled.Calculate else Icons.Outlined.Calculate,
                        contentDescription = "ROI Calc"
                    )
                },
                label = { Text(text = "ROI", fontSize = 11.sp, fontWeight = if (currentTab == ScreenTab.CALCULATOR) FontWeight.Bold else FontWeight.Normal) },
                modifier = Modifier.testTag("nav_owner_calc")
            )
        }
    }
}
