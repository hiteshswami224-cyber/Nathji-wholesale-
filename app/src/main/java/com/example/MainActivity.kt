package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.UserRole
import com.example.ui.ScreenTab
import com.example.ui.WholesaleViewModel
import com.example.ui.components.AddAddressDialog
import com.example.ui.components.AddProductDialog
import com.example.ui.components.CustomerAuthDialog
import com.example.ui.components.DeleteProductDialog
import com.example.ui.components.EditProfileDialog
import com.example.ui.components.InvoiceViewDialog
import com.example.ui.components.OrderTrackingDialog
import com.example.ui.components.ProductDetailDialog
import com.example.ui.components.StockAdjustmentDialog
import com.example.ui.components.WholesaleBottomNav
import com.example.ui.components.WholesaleTopBar
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CatalogScreen
import com.example.ui.screens.InventoryScreen
import com.example.ui.screens.OrdersScreen
import com.example.ui.screens.OwnerDashboardScreen
import com.example.ui.screens.ProfitCalculatorScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {

    private val viewModel: WholesaleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                WholesaleApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun WholesaleApp(viewModel: WholesaleViewModel) {
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val showAuthDialog by viewModel.showAuthDialog.collectAsStateWithLifecycle()
    val showAddProductDialog by viewModel.showAddProductDialog.collectAsStateWithLifecycle()
    val productToDelete by viewModel.productToDelete.collectAsStateWithLifecycle()

    val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedSubcategory by viewModel.selectedSubcategory.collectAsStateWithLifecycle()
    val sortOption by viewModel.sortOption.collectAsStateWithLifecycle()
    val inventoryFilter by viewModel.inventoryFilter.collectAsStateWithLifecycle()

    val filteredProducts by viewModel.filteredCatalogProducts.collectAsStateWithLifecycle()
    val rawCartItems by viewModel.rawCartItems.collectAsStateWithLifecycle()
    val enrichedCartItems by viewModel.enrichedCartItems.collectAsStateWithLifecycle()
    val cartSummary by viewModel.cartSummary.collectAsStateWithLifecycle()

    val inventoryProducts by viewModel.filteredInventoryProducts.collectAsStateWithLifecycle()
    val inventoryStats by viewModel.inventoryStats.collectAsStateWithLifecycle()
    val recentInventoryLogs by viewModel.recentInventoryLogs.collectAsStateWithLifecycle()

    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val displayOrders by viewModel.displayOrders.collectAsStateWithLifecycle()
    val allNotifications by viewModel.allNotifications.collectAsStateWithLifecycle()
    val businessProfile by viewModel.businessProfile.collectAsStateWithLifecycle()
    val savedAddresses by viewModel.savedAddresses.collectAsStateWithLifecycle()
    val savedPaymentMethods by viewModel.savedPaymentMethods.collectAsStateWithLifecycle()

    val selectedAddressId by viewModel.selectedAddressId.collectAsStateWithLifecycle()
    val selectedPaymentMethodId by viewModel.selectedPaymentMethodId.collectAsStateWithLifecycle()

    // Modals
    val selectedProductForDetail by viewModel.selectedProductForDetail.collectAsStateWithLifecycle()
    val selectedProductForStockAdjust by viewModel.selectedProductForStockAdjust.collectAsStateWithLifecycle()
    val selectedOrderForInvoice by viewModel.selectedOrderForInvoice.collectAsStateWithLifecycle()
    val selectedOrderForTracking by viewModel.selectedOrderForTracking.collectAsStateWithLifecycle()
    val showEditProfileDialog by viewModel.showEditProfileDialog.collectAsStateWithLifecycle()
    val showAddAddressDialog by viewModel.showAddAddressDialog.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.userMessage.collectLatest { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    // Back button handling
    BackHandler(
        enabled = currentTab != ScreenTab.CATALOG ||
                selectedProductForDetail != null ||
                selectedProductForStockAdjust != null ||
                selectedOrderForInvoice != null ||
                selectedOrderForTracking != null ||
                showAuthDialog ||
                showAddProductDialog ||
                productToDelete != null
    ) {
        when {
            showAuthDialog -> viewModel.closeAuthDialog()
            showAddProductDialog -> viewModel.closeAddProductDialog()
            productToDelete != null -> viewModel.cancelDeleteProduct()
            selectedProductForDetail != null -> viewModel.closeProductDetail()
            selectedProductForStockAdjust != null -> viewModel.closeStockAdjustDialog()
            selectedOrderForInvoice != null -> viewModel.closeOrderInvoice()
            selectedOrderForTracking != null -> viewModel.closeOrderTracking()
            showEditProfileDialog -> viewModel.setShowEditProfile(false)
            showAddAddressDialog -> viewModel.setShowAddAddress(false)
            currentTab != ScreenTab.CATALOG -> viewModel.setTab(ScreenTab.CATALOG)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            WholesaleTopBar(
                currentTab = currentTab,
                currentRole = currentRole,
                customerShopName = businessProfile?.businessName ?: "Nathji Wholesale",
                cartItemCount = cartSummary.totalUnits,
                lowStockCount = inventoryStats.lowStockCount,
                onTabSelected = { viewModel.setTab(it) },
                onSwitchRoleClick = { viewModel.openAuthDialog() }
            )
        },
        bottomBar = {
            WholesaleBottomNav(
                currentTab = currentTab,
                currentRole = currentRole,
                cartItemCount = cartSummary.totalUnits,
                lowStockCount = inventoryStats.lowStockCount,
                onTabSelected = { viewModel.setTab(it) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (currentRole == UserRole.OWNER) {
                // OWNER INTERFACE (Owner Inventory & Full Order Fulfillment)
                when (currentTab) {
                    ScreenTab.ORDERS -> {
                        OwnerDashboardScreen(
                            orders = allOrders,
                            lowStockProducts = inventoryProducts.filter { it.currentStock in 1..it.minStockThreshold },
                            stats = inventoryStats,
                            onOpenOrderTracking = { viewModel.openOrderTracking(it) },
                            onOpenInvoice = { viewModel.openOrderInvoice(it) },
                            onAdvanceOrderStatus = { id, nextStatus, note ->
                                viewModel.advanceTrackingStatus(id, nextStatus, note)
                            },
                            onOpenStockAdjust = { viewModel.openStockAdjustDialog(it) },
                            onNavigateToInventory = { viewModel.setTab(ScreenTab.INVENTORY) },
                            onSwitchToCustomerView = { viewModel.setRole(UserRole.CUSTOMER) }
                        )
                    }
                    ScreenTab.INVENTORY -> {
                        InventoryScreen(
                            products = inventoryProducts,
                            orders = allOrders,
                            stats = inventoryStats,
                            filter = inventoryFilter,
                            recentLogs = recentInventoryLogs,
                            onFilterChange = { viewModel.setInventoryFilter(it) },
                            onAdjustStockClick = { viewModel.openStockAdjustDialog(it) },
                            onAddNewProductClick = { viewModel.openAddProductDialog() },
                            onDeleteProductClick = { viewModel.requestDeleteProduct(it) },
                            onOpenOrderTracking = { viewModel.openOrderTracking(it) },
                            onOpenInvoice = { viewModel.openOrderInvoice(it) }
                        )
                    }
                    ScreenTab.CATALOG -> {
                        CatalogScreen(
                            products = filteredProducts,
                            cartItems = rawCartItems,
                            searchQuery = searchQuery,
                            selectedCategory = selectedCategory,
                            selectedSubcategory = selectedSubcategory,
                            sortOption = sortOption,
                            onSearchChange = { viewModel.setSearchQuery(it) },
                            onCategoryChange = { viewModel.setCategory(it) },
                            onSubcategoryChange = { viewModel.setSubcategory(it) },
                            onSortChange = { viewModel.setSortOption(it) },
                            onProductClick = { viewModel.openProductDetail(it) },
                            onAddLoose = { viewModel.addToCart(it.sku, 1) },
                            onAddBox = { viewModel.addBoxToCart(it) },
                            onAddCarton = { viewModel.addMasterCartonToCart(it) },
                            onDecrement = { viewModel.addToCart(it.sku, -12) },
                            onIncrement = { viewModel.addToCart(it.sku, 12) },
                            isOwnerView = true,
                            onOwnerAdjustStock = { viewModel.openStockAdjustDialog(it) }
                        )
                    }
                    ScreenTab.ACCOUNT -> {
                        AccountScreen(
                            profile = businessProfile,
                            addresses = savedAddresses,
                            paymentMethods = savedPaymentMethods,
                            notifications = allNotifications,
                            onEditProfileClick = { viewModel.setShowEditProfile(true) },
                            onAddAddressClick = { viewModel.setShowAddAddress(true) },
                            onSetDefaultAddress = { viewModel.setDefaultAddress(it) },
                            onDeleteAddress = { viewModel.deleteAddress(it) },
                            onSetDefaultPayment = { viewModel.setDefaultPaymentMethod(it) },
                            onOpenAuthDialog = { viewModel.openAuthDialog() }
                        )
                    }
                    ScreenTab.CALCULATOR, ScreenTab.CART -> {
                        ProfitCalculatorScreen(
                            products = filteredProducts,
                            onAddAssortmentToCart = { packs ->
                                packs.forEach { (prod, qty) ->
                                    viewModel.addToCart(prod.sku, qty)
                                }
                                viewModel.setTab(ScreenTab.CART)
                            }
                        )
                    }
                }
            } else {
                // CUSTOMER INTERFACE (Protected: Hidden from internal inventory tools)
                when (currentTab) {
                    ScreenTab.CATALOG -> {
                        CatalogScreen(
                            products = filteredProducts,
                            cartItems = rawCartItems,
                            searchQuery = searchQuery,
                            selectedCategory = selectedCategory,
                            selectedSubcategory = selectedSubcategory,
                            sortOption = sortOption,
                            onSearchChange = { viewModel.setSearchQuery(it) },
                            onCategoryChange = { viewModel.setCategory(it) },
                            onSubcategoryChange = { viewModel.setSubcategory(it) },
                            onSortChange = { viewModel.setSortOption(it) },
                            onProductClick = { viewModel.openProductDetail(it) },
                            onAddLoose = { viewModel.addToCart(it.sku, 1) },
                            onAddBox = { viewModel.addBoxToCart(it) },
                            onAddCarton = { viewModel.addMasterCartonToCart(it) },
                            onDecrement = { viewModel.addToCart(it.sku, -12) },
                            onIncrement = { viewModel.addToCart(it.sku, 12) },
                            isOwnerView = false
                        )
                    }
                    ScreenTab.CART -> {
                        CartScreen(
                            cartItems = enrichedCartItems,
                            summary = cartSummary,
                            savedAddresses = savedAddresses,
                            savedPaymentMethods = savedPaymentMethods,
                            selectedAddressId = selectedAddressId,
                            selectedPaymentMethodId = selectedPaymentMethodId,
                            onSelectAddress = { viewModel.selectedAddressId.value = it },
                            onSelectPaymentMethod = { viewModel.selectedPaymentMethodId.value = it },
                            onQuantityChange = { sku, qty -> viewModel.setCartQuantity(sku, qty) },
                            onRemoveItem = { viewModel.removeFromCart(it) },
                            onClearCart = { viewModel.clearCart() },
                            onPlaceOrder = { viewModel.placeOrder() },
                            onBrowseCatalog = { viewModel.setTab(ScreenTab.CATALOG) }
                        )
                    }
                    ScreenTab.ORDERS -> {
                        OrdersScreen(
                            orders = displayOrders,
                            isOwnerView = false,
                            onOpenTracking = { viewModel.openOrderTracking(it) },
                            onOpenInvoice = { viewModel.openOrderInvoice(it) },
                            onReorder = { viewModel.reorderOrder(it) },
                            onStartShopping = { viewModel.setTab(ScreenTab.CATALOG) }
                        )
                    }
                    ScreenTab.ACCOUNT -> {
                        AccountScreen(
                            profile = businessProfile,
                            addresses = savedAddresses,
                            paymentMethods = savedPaymentMethods,
                            notifications = allNotifications,
                            onEditProfileClick = { viewModel.setShowEditProfile(true) },
                            onAddAddressClick = { viewModel.setShowAddAddress(true) },
                            onSetDefaultAddress = { viewModel.setDefaultAddress(it) },
                            onDeleteAddress = { viewModel.deleteAddress(it) },
                            onSetDefaultPayment = { viewModel.setDefaultPaymentMethod(it) },
                            onOpenAuthDialog = { viewModel.openAuthDialog() }
                        )
                    }
                    ScreenTab.CALCULATOR, ScreenTab.INVENTORY -> {
                        // Customer cannot access inventory management, route to catalog or profit simulator
                        ProfitCalculatorScreen(
                            products = filteredProducts,
                            onAddAssortmentToCart = { packs ->
                                packs.forEach { (prod, qty) ->
                                    viewModel.addToCart(prod.sku, qty)
                                }
                                viewModel.setTab(ScreenTab.CART)
                            }
                        )
                    }
                }
            }
        }

        // Product Detail Dialog (Stock privacy respected based on isOwnerView)
        selectedProductForDetail?.let { prod ->
            val cartQty = rawCartItems.find { it.sku == prod.sku }?.quantity ?: 0
            ProductDetailDialog(
                product = prod,
                currentCartQty = cartQty,
                isOwnerView = (currentRole == UserRole.OWNER),
                onDismiss = { viewModel.closeProductDetail() },
                onAddToCart = { qty -> viewModel.setCartQuantity(prod.sku, qty) }
            )
        }

        // Stock Adjustment Dialog (Owner Only: Add Stock +, Less Stock -, Physical Audit)
        selectedProductForStockAdjust?.let { prod ->
            StockAdjustmentDialog(
                product = prod,
                onDismiss = { viewModel.closeStockAdjustDialog() },
                onRestock = { sku, delta, notes -> viewModel.restockProduct(sku, delta, notes) },
                onSetDirectStock = { sku, newStock, notes -> viewModel.setDirectStock(sku, newStock, notes) }
            )
        }

        // Order Invoice Dialog
        selectedOrderForInvoice?.let { order ->
            InvoiceViewDialog(
                order = order,
                onDismiss = { viewModel.closeOrderInvoice() },
                onOpenTracking = {
                    viewModel.closeOrderInvoice()
                    viewModel.openOrderTracking(order)
                }
            )
        }

        // Order Tracking Dialog with Carrier integration & Automated Alerts
        selectedOrderForTracking?.let { order ->
            val milestones = remember(order) { viewModel.getTrackingMilestones(order) }
            OrderTrackingDialog(
                order = order,
                milestones = milestones,
                notifications = allNotifications,
                onDismiss = { viewModel.closeOrderTracking() },
                onAdvanceStatus = { orderId, newStatus, location ->
                    viewModel.advanceTrackingStatus(orderId, newStatus, location)
                }
            )
        }

        // Edit Profile Dialog
        if (showEditProfileDialog) {
            businessProfile?.let { prof ->
                EditProfileDialog(
                    profile = prof,
                    onDismiss = { viewModel.setShowEditProfile(false) },
                    onSave = { name, owner, phone, email, gstin, type ->
                        viewModel.updateProfile(name, owner, phone, email, gstin, type)
                    }
                )
            }
        }

        // Add Address Dialog
        if (showAddAddressDialog) {
            AddAddressDialog(
                onDismiss = { viewModel.setShowAddAddress(false) },
                onSave = { label, contact, phone, street, city, state, pincode, isDefault ->
                    viewModel.addSavedAddress(label, contact, phone, street, city, state, pincode, isDefault)
                }
            )
        }

        // Customer Login & Owner Access Dialog
        if (showAuthDialog) {
            CustomerAuthDialog(
                currentProfile = businessProfile,
                onDismiss = { viewModel.closeAuthDialog() },
                onCustomerLoginSuccess = { prof ->
                    viewModel.loginAsCustomer(prof)
                },
                onOwnerLoginSuccess = {
                    viewModel.loginAsOwner()
                }
            )
        }

        // Owner Add New Product Dialog
        if (showAddProductDialog) {
            AddProductDialog(
                onDismiss = { viewModel.closeAddProductDialog() },
                onSaveProduct = { sku, title, msrp, cat, sub, size, caption, shortD, longD, stock, box, carton ->
                    viewModel.addNewProduct(
                        sku = sku,
                        title = title,
                        msrp = msrp,
                        category = cat,
                        subcategory = sub,
                        size = size,
                        caption = caption,
                        shortDesc = shortD,
                        longDesc = longD,
                        initialStock = stock,
                        boxSize = box,
                        cartonSize = carton
                    )
                }
            )
        }

        // Owner Delete Product Dialog
        productToDelete?.let { prod ->
            DeleteProductDialog(
                product = prod,
                onDismiss = { viewModel.cancelDeleteProduct() },
                onConfirmDelete = { sku ->
                    viewModel.deleteProductConfirmed(sku)
                }
            )
        }
    }
}
