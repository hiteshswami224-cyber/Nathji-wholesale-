package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BulkTierInfo
import com.example.data.model.BusinessProfileEntity
import com.example.data.model.CartItemEntity
import com.example.data.model.EnrichedCartItem
import com.example.data.model.InventoryLogEntity
import com.example.data.model.NotificationLogEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.SavedAddressEntity
import com.example.data.model.SavedPaymentMethodEntity
import com.example.data.model.TrackingMilestone
import com.example.data.model.WholesalePricingHelper
import com.example.data.repository.WholesaleRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class ScreenTab {
    CATALOG,
    INVENTORY,
    CART,
    ORDERS,
    ACCOUNT,
    CALCULATOR
}

enum class SortOption(val label: String) {
    FEATURED("Featured"),
    MARGIN_HIGH("Highest Retailer Margin"),
    PRICE_LOW("MSRP: Low to High"),
    PRICE_HIGH("MSRP: High to Low"),
    STOCK_HIGH("Highest Stock"),
    STOCK_LOW("Lowest Stock")
}

enum class InventoryFilter(val label: String) {
    ALL("All Products"),
    LOW_STOCK("Low Stock Alerts"),
    OUT_OF_STOCK("Out of Stock"),
    HEALTHY("Healthy Stock")
}

data class CartSummary(
    val totalUnits: Int = 0,
    val totalRetailValue: Double = 0.0,
    val totalWholesaleAmount: Double = 0.0,
    val totalRetailerProfit: Double = 0.0,
    val averageMarginPercent: Double = 0.0,
    val itemCount: Int = 0,
    val hasOutOfStockItems: Boolean = false
)

data class InventoryStats(
    val totalSkus: Int = 0,
    val totalUnitsInStock: Int = 0,
    val totalCartonsInStock: Int = 0,
    val totalInventoryValue: Double = 0.0,
    val lowStockCount: Int = 0,
    val outOfStockCount: Int = 0
)

class WholesaleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WholesaleRepository(AppDatabase.getDatabase(application))

    // Navigation tab
    private val _currentTab = MutableStateFlow(ScreenTab.CATALOG)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    // Search and filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedSubcategory = MutableStateFlow("All")
    val selectedSubcategory: StateFlow<String> = _selectedSubcategory.asStateFlow()

    private val _sortOption = MutableStateFlow(SortOption.FEATURED)
    val sortOption: StateFlow<SortOption> = _sortOption.asStateFlow()

    private val _inventoryFilter = MutableStateFlow(InventoryFilter.ALL)
    val inventoryFilter: StateFlow<InventoryFilter> = _inventoryFilter.asStateFlow()

    // Modals & detail sheets
    private val _selectedProductForDetail = MutableStateFlow<ProductEntity?>(null)
    val selectedProductForDetail: StateFlow<ProductEntity?> = _selectedProductForDetail.asStateFlow()

    private val _selectedProductForStockAdjust = MutableStateFlow<ProductEntity?>(null)
    val selectedProductForStockAdjust: StateFlow<ProductEntity?> = _selectedProductForStockAdjust.asStateFlow()

    private val _selectedOrderForInvoice = MutableStateFlow<OrderEntity?>(null)
    val selectedOrderForInvoice: StateFlow<OrderEntity?> = _selectedOrderForInvoice.asStateFlow()

    private val _selectedOrderForTracking = MutableStateFlow<OrderEntity?>(null)
    val selectedOrderForTracking: StateFlow<OrderEntity?> = _selectedOrderForTracking.asStateFlow()

    // Account dialogs
    private val _showEditProfileDialog = MutableStateFlow(false)
    val showEditProfileDialog: StateFlow<Boolean> = _showEditProfileDialog.asStateFlow()

    private val _showAddAddressDialog = MutableStateFlow(false)
    val showAddAddressDialog: StateFlow<Boolean> = _showAddAddressDialog.asStateFlow()

    // Owner Product management dialogs
    private val _showAddProductDialog = MutableStateFlow(false)
    val showAddProductDialog: StateFlow<Boolean> = _showAddProductDialog.asStateFlow()

    private val _productToDelete = MutableStateFlow<ProductEntity?>(null)
    val productToDelete: StateFlow<ProductEntity?> = _productToDelete.asStateFlow()

    // Role & Authentication
    private val _currentRole = MutableStateFlow(com.example.data.model.UserRole.CUSTOMER)
    val currentRole: StateFlow<com.example.data.model.UserRole> = _currentRole.asStateFlow()

    private val _showAuthDialog = MutableStateFlow(false)
    val showAuthDialog: StateFlow<Boolean> = _showAuthDialog.asStateFlow()

    // Toast/Snackbar notifications
    private val _userMessage = MutableSharedFlow<String>()
    val userMessage = _userMessage.asSharedFlow()

    // Bulk Calculator State
    val simulatorInvestment = MutableStateFlow(10000.0)

    // Data streams from repository
    val allProducts: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rawCartItems: StateFlow<List<CartItemEntity>> = repository.allCartItems
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allOrders: StateFlow<List<OrderEntity>> = repository.allOrders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val recentInventoryLogs: StateFlow<List<InventoryLogEntity>> = repository.recentInventoryLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allNotifications: StateFlow<List<NotificationLogEntity>> = repository.allNotifications
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val businessProfile: StateFlow<BusinessProfileEntity?> = repository.businessProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val savedAddresses: StateFlow<List<SavedAddressEntity>> = repository.savedAddresses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedPaymentMethods: StateFlow<List<SavedPaymentMethodEntity>> = repository.savedPaymentMethods
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Checkout Address & Payment
    val selectedAddressId = MutableStateFlow<Long?>(null)
    val selectedPaymentMethodId = MutableStateFlow<Long?>(null)

    // Enriched Cart Items with bulk tier calculations
    val enrichedCartItems: StateFlow<List<EnrichedCartItem>> = combine(allProducts, rawCartItems) { products, cartItems ->
        val productMap = products.associateBy { it.sku }
        cartItems.mapNotNull { cartItem ->
            val product = productMap[cartItem.sku] ?: return@mapNotNull null
            val tier = WholesalePricingHelper.getTierForQuantity(cartItem.quantity)
            val unitPrice = WholesalePricingHelper.calculateUnitPrice(product.msrp, cartItem.quantity)
            val totalPrice = unitPrice * cartItem.quantity
            val totalRetailValue = product.msrp * cartItem.quantity
            val totalProfit = (totalRetailValue - totalPrice).coerceAtLeast(0.0)
            val marginPercent = if (totalRetailValue > 0) (totalProfit / totalRetailValue) * 100.0 else 0.0
            val nextTier = WholesalePricingHelper.getNextTierTarget(product.msrp, cartItem.quantity)

            EnrichedCartItem(
                product = product,
                quantity = cartItem.quantity,
                unitPrice = unitPrice,
                tierInfo = tier,
                totalPrice = totalPrice,
                totalRetailValue = totalRetailValue,
                totalProfit = totalProfit,
                profitMarginPercent = marginPercent,
                nextTierTarget = nextTier,
                isStockAvailable = product.currentStock >= cartItem.quantity
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart Summary
    val cartSummary: StateFlow<CartSummary> = enrichedCartItems.combine(allProducts) { items, _ ->
        var totalUnits = 0
        var totalRetail = 0.0
        var totalWholesale = 0.0
        var hasOutOfStock = false

        for (item in items) {
            totalUnits += item.quantity
            totalRetail += item.totalRetailValue
            totalWholesale += item.totalPrice
            if (!item.isStockAvailable) hasOutOfStock = true
        }

        val totalProfit = (totalRetail - totalWholesale).coerceAtLeast(0.0)
        val avgMargin = if (totalRetail > 0) (totalProfit / totalRetail) * 100.0 else 0.0

        CartSummary(
            totalUnits = totalUnits,
            totalRetailValue = totalRetail,
            totalWholesaleAmount = totalWholesale,
            totalRetailerProfit = totalProfit,
            averageMarginPercent = avgMargin,
            itemCount = items.size,
            hasOutOfStockItems = hasOutOfStock
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CartSummary())

    // Display orders based on role: Customer sees their own orders; Owner sees all wholesale orders!
    val displayOrders: StateFlow<List<OrderEntity>> = combine(allOrders, currentRole, businessProfile) { orders, role, profile ->
        if (role == com.example.data.model.UserRole.OWNER) {
            orders
        } else {
            val phone = profile?.phone ?: ""
            val name = profile?.businessName ?: ""
            val customerFiltered = orders.filter { it.recipientPhone == phone || it.retailerName.equals(name, ignoreCase = true) }
            if (customerFiltered.isEmpty()) orders.take(2) else customerFiltered
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Filtered & Sorted Catalog Products
    val filteredCatalogProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        searchQuery,
        selectedCategory,
        selectedSubcategory,
        sortOption
    ) { products, query, category, subcategory, sort ->
        var list = products

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.sku.lowercase().contains(q) ||
                it.caption.lowercase().contains(q) ||
                it.category.lowercase().contains(q) ||
                it.subcategory.lowercase().contains(q)
            }
        }

        if (category != "All") {
            list = list.filter { it.category.equals(category, ignoreCase = true) }
        }

        if (subcategory != "All") {
            list = list.filter { it.subcategory.equals(subcategory, ignoreCase = true) }
        }

        when (sort) {
            SortOption.FEATURED -> list
            SortOption.MARGIN_HIGH -> list.sortedByDescending {
                WholesalePricingHelper.TIERS.maxOf { tier -> tier.discountPercent }
            }
            SortOption.PRICE_LOW -> list.sortedBy { it.msrp }
            SortOption.PRICE_HIGH -> list.sortedByDescending { it.msrp }
            SortOption.STOCK_HIGH -> list.sortedByDescending { it.currentStock }
            SortOption.STOCK_LOW -> list.sortedBy { it.currentStock }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Inventory filtered products & stats
    val inventoryStats: StateFlow<InventoryStats> = allProducts.combine(MutableStateFlow(Unit)) { products, _ ->
        val totalUnits = products.sumOf { it.currentStock }
        val totalCartons = products.sumOf { it.currentStock / it.masterCartonSize }
        val totalVal = products.sumOf { it.currentStock * it.baseWholesaleCost }
        val lowStock = products.count { it.currentStock in 1..it.minStockThreshold }
        val outOfStock = products.count { it.currentStock == 0 }

        InventoryStats(
            totalSkus = products.size,
            totalUnitsInStock = totalUnits,
            totalCartonsInStock = totalCartons,
            totalInventoryValue = totalVal,
            lowStockCount = lowStock,
            outOfStockCount = outOfStock
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InventoryStats())

    val filteredInventoryProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        searchQuery,
        inventoryFilter
    ) { products, query, filter ->
        var list = products

        if (query.isNotBlank()) {
            val q = query.trim().lowercase()
            list = list.filter {
                it.title.lowercase().contains(q) ||
                it.sku.lowercase().contains(q)
            }
        }

        when (filter) {
            InventoryFilter.ALL -> list
            InventoryFilter.LOW_STOCK -> list.filter { it.currentStock in 1..it.minStockThreshold }
            InventoryFilter.OUT_OF_STOCK -> list.filter { it.currentStock == 0 }
            InventoryFilter.HEALTHY -> list.filter { it.currentStock > it.minStockThreshold }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
        }
    }

    fun setTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategory(cat: String) {
        _selectedCategory.value = cat
        _selectedSubcategory.value = "All"
    }

    fun setSubcategory(subcat: String) {
        _selectedSubcategory.value = subcat
    }

    fun setSortOption(sort: SortOption) {
        _sortOption.value = sort
    }

    fun setInventoryFilter(filter: InventoryFilter) {
        _inventoryFilter.value = filter
    }

    fun openProductDetail(product: ProductEntity) {
        _selectedProductForDetail.value = product
    }

    fun closeProductDetail() {
        _selectedProductForDetail.value = null
    }

    fun openStockAdjustDialog(product: ProductEntity) {
        _selectedProductForStockAdjust.value = product
    }

    fun closeStockAdjustDialog() {
        _selectedProductForStockAdjust.value = null
    }

    fun openOrderInvoice(order: OrderEntity) {
        _selectedOrderForInvoice.value = order
    }

    fun closeOrderInvoice() {
        _selectedOrderForInvoice.value = null
    }

    fun openOrderTracking(order: OrderEntity) {
        _selectedOrderForTracking.value = order
    }

    fun closeOrderTracking() {
        _selectedOrderForTracking.value = null
    }

    fun setShowEditProfile(show: Boolean) {
        _showEditProfileDialog.value = show
    }

    fun setShowAddAddress(show: Boolean) {
        _showAddAddressDialog.value = show
    }

    // Cart Actions
    fun addToCart(sku: String, delta: Int) {
        viewModelScope.launch {
            repository.addToCart(sku, delta)
            val prod = allProducts.value.find { it.sku == sku }
            val name = prod?.title ?: sku
            _userMessage.emit("Updated $name: ${if (delta > 0) "+$delta" else delta} units")
        }
    }

    fun addBoxToCart(product: ProductEntity) {
        addToCart(product.sku, product.boxSize)
    }

    fun addMasterCartonToCart(product: ProductEntity) {
        addToCart(product.sku, product.masterCartonSize)
    }

    fun setCartQuantity(sku: String, qty: Int) {
        viewModelScope.launch {
            repository.setCartQuantity(sku, qty)
        }
    }

    fun removeFromCart(sku: String) {
        viewModelScope.launch {
            repository.removeFromCart(sku)
            _userMessage.emit("Item removed from cart")
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
            _userMessage.emit("Cart cleared")
        }
    }

    // Inventory Actions
    fun restockProduct(sku: String, delta: Int, notes: String = "Wholesale shipment received") {
        viewModelScope.launch {
            repository.adjustStock(sku, delta, notes, "RESTOCK")
            _userMessage.emit("Added +$delta units to stock")
            closeStockAdjustDialog()
        }
    }

    fun setDirectStock(sku: String, newStock: Int, notes: String = "Physical count adjustment") {
        viewModelScope.launch {
            repository.setStockDirect(sku, newStock, notes)
            _userMessage.emit("Stock level set to $newStock units")
            closeStockAdjustDialog()
        }
    }

    // Owner Product Management (Add/Remove Products)
    fun openAddProductDialog() {
        _showAddProductDialog.value = true
    }

    fun closeAddProductDialog() {
        _showAddProductDialog.value = false
    }

    fun requestDeleteProduct(product: ProductEntity) {
        _productToDelete.value = product
    }

    fun cancelDeleteProduct() {
        _productToDelete.value = null
    }

    fun deleteProductConfirmed(sku: String) {
        viewModelScope.launch {
            val prod = allProducts.value.find { it.sku == sku }
            val name = prod?.title ?: sku
            repository.deleteProduct(sku)
            _productToDelete.value = null
            _userMessage.emit("Product $name removed from warehouse")
        }
    }

    fun addNewProduct(
        sku: String,
        title: String,
        msrp: Double,
        category: String,
        subcategory: String,
        size: String,
        caption: String,
        shortDesc: String,
        longDesc: String,
        initialStock: Int,
        boxSize: Int,
        cartonSize: Int
    ) {
        viewModelScope.launch {
            val cleanSku = sku.trim().uppercase(Locale.US)
            val existing = allProducts.value.find { it.sku.equals(cleanSku, ignoreCase = true) }
            if (existing != null) {
                _userMessage.emit("Error: SKU '$cleanSku' already exists in catalog!")
                return@launch
            }

            val newProd = ProductEntity(
                sku = cleanSku,
                title = title.trim(),
                msrp = msrp,
                category = category.trim(),
                subcategory = subcategory.trim(),
                size = size.trim(),
                caption = caption.trim(),
                shortDescription = shortDesc.trim(),
                longDescription = longDesc.trim(),
                imageUrl = "https://placehold.co",
                currentStock = initialStock,
                minStockThreshold = 48,
                boxSize = if (boxSize > 0) boxSize else 24,
                masterCartonSize = if (cartonSize > 0) cartonSize else 72
            )

            repository.addProduct(newProd)
            closeAddProductDialog()
            _userMessage.emit("New product '${newProd.title}' added to warehouse catalog with $initialStock units")
        }
    }

    // Checkout
    fun placeOrder() {
        viewModelScope.launch {
            val cartList = rawCartItems.value
            val prodMap = allProducts.value.associateBy { it.sku }
            val profile = businessProfile.value
            val addresses = savedAddresses.value
            val paymentMethods = savedPaymentMethods.value

            val activeAddress = addresses.find { it.id == selectedAddressId.value }
                ?: addresses.find { it.isDefault }
                ?: addresses.firstOrNull()

            val activePayment = paymentMethods.find { it.id == selectedPaymentMethodId.value }
                ?: paymentMethods.find { it.isDefault }
                ?: paymentMethods.firstOrNull()

            val name = profile?.businessName ?: "Ganesh Kirana & General Store"
            val gstin = profile?.gstin ?: "08AABCG1234F1Z8"
            val phone = profile?.phone ?: "+91 98290 41235"
            val email = profile?.email ?: "sharma.kirana@gmail.com"
            val addressText = if (activeAddress != null) {
                "${activeAddress.label}: ${activeAddress.streetAddress}, ${activeAddress.city}, ${activeAddress.state} - ${activeAddress.pincode}"
            } else {
                "Shop 14, Main Bazaar, Tripolia Road, Jaipur 302001"
            }
            val paymentText = activePayment?.title ?: "15-Day Khata Credit Line"

            val result = repository.placeOrder(
                cartItems = cartList,
                productsMap = prodMap,
                retailerName = name,
                retailerGstin = gstin,
                retailerAddress = addressText,
                recipientPhone = phone,
                recipientEmail = email,
                paymentMethod = paymentText
            )

            result.onSuccess { order ->
                _userMessage.emit("Order #${order.orderId} placed! Tracking details sent via SMS & Email.")
                _selectedOrderForTracking.value = order
                _currentTab.value = ScreenTab.ORDERS
            }.onFailure { err ->
                _userMessage.emit("Order failed: ${err.message}")
            }
        }
    }

    fun reorderOrder(order: OrderEntity) {
        viewModelScope.launch {
            repository.reorderItems(order)
            _userMessage.emit("Cart updated with items from order #${order.orderId}")
            _currentTab.value = ScreenTab.CART
        }
    }

    // Order Tracking & Carrier Milestone simulation
    fun advanceTrackingStatus(orderId: String, nextStatus: String, carrierLocation: String) {
        viewModelScope.launch {
            repository.advanceOrderStatus(orderId, nextStatus, carrierLocation)
            // Refresh currently viewed tracking order
            val updated = allOrders.value.find { it.orderId == orderId }
            if (updated != null) {
                _selectedOrderForTracking.value = updated.copy(
                    status = nextStatus,
                    carrierStatusDetails = carrierLocation
                )
            }
            _userMessage.emit("Order status updated to '$nextStatus'. Automated SMS & Email dispatched!")
        }
    }

    // Profile & Address management
    fun updateProfile(
        businessName: String,
        ownerName: String,
        phone: String,
        email: String,
        gstin: String,
        businessType: String
    ) {
        viewModelScope.launch {
            val current = businessProfile.value ?: BusinessProfileEntity()
            val updated = current.copy(
                businessName = businessName,
                ownerName = ownerName,
                phone = phone,
                email = email,
                gstin = gstin,
                businessType = businessType
            )
            repository.updateProfile(updated)
            _userMessage.emit("Business profile saved successfully")
            setShowEditProfile(false)
        }
    }

    fun addSavedAddress(
        label: String,
        contactPerson: String,
        phone: String,
        streetAddress: String,
        city: String,
        state: String,
        pincode: String,
        isDefault: Boolean
    ) {
        viewModelScope.launch {
            repository.addSavedAddress(
                SavedAddressEntity(
                    label = label,
                    contactPerson = contactPerson,
                    phone = phone,
                    streetAddress = streetAddress,
                    city = city,
                    state = state,
                    pincode = pincode,
                    isDefault = isDefault
                )
            )
            _userMessage.emit("Shipping address added")
            setShowAddAddress(false)
        }
    }

    fun setDefaultAddress(id: Long) {
        viewModelScope.launch {
            repository.setDefaultAddress(id)
            selectedAddressId.value = id
            _userMessage.emit("Default shipping address updated")
        }
    }

    fun deleteAddress(id: Long) {
        viewModelScope.launch {
            repository.deleteAddress(id)
            _userMessage.emit("Address deleted")
        }
    }

    fun setDefaultPaymentMethod(id: Long) {
        viewModelScope.launch {
            repository.setDefaultPaymentMethod(id)
            selectedPaymentMethodId.value = id
            _userMessage.emit("Default payment method updated")
        }
    }

    fun setRole(role: com.example.data.model.UserRole) {
        _currentRole.value = role
        viewModelScope.launch {
            if (role == com.example.data.model.UserRole.OWNER) {
                _userMessage.emit("Switched to Owner Admin Console (मालिक कंसोल)")
            } else {
                _userMessage.emit("Switched to Customer / Retailer Portal (ग्राहक पोर्टल)")
            }
        }
    }

    fun openAuthDialog() {
        _showAuthDialog.value = true
    }

    fun closeAuthDialog() {
        _showAuthDialog.value = false
    }

    fun loginAsCustomer(newProfile: BusinessProfileEntity) {
        viewModelScope.launch {
            repository.updateProfile(newProfile)
            _currentRole.value = com.example.data.model.UserRole.CUSTOMER
            _userMessage.emit("Welcome, ${newProfile.businessName}!")
        }
    }

    fun loginAsOwner() {
        _currentRole.value = com.example.data.model.UserRole.OWNER
        viewModelScope.launch {
            _userMessage.emit("Authenticated as Store Owner (मालिक कंसोल)")
        }
    }

    fun getTrackingMilestones(order: OrderEntity): List<TrackingMilestone> {
        val stages = listOf(
            "Processing" to "Order Confirmed & Staging",
            "Packed & Staged" to "Cartons Palletized & AWB Generated",
            "Dispatched" to "Handed over to ${order.carrierName}",
            "In Transit" to "Departed Regional Sorting Hub",
            "Out for Delivery" to "Local Delivery Vehicle Out for Delivery",
            "Delivered" to "Delivered & Proof of Delivery Verified"
        )

        val currentStageIndex = when (order.status) {
            "Processing" -> 0
            "Packed & Staged" -> 1
            "Dispatched" -> 2
            "In Transit" -> 3
            "Out for Delivery" -> 4
            "Delivered" -> 5
            else -> 0
        }

        val baseTime = order.timestamp
        val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.US)

        return stages.mapIndexed { index, (stage, desc) ->
            val isDone = index <= currentStageIndex
            val isCurrent = index == currentStageIndex
            val timeOffset = index * (4 * 3600 * 1000L) // +4 hours per milestone
            val timeText = if (isDone) sdf.format(Date(baseTime + timeOffset)) else "Estimated"

            TrackingMilestone(
                stage = stage,
                title = stage,
                description = if (isCurrent && order.carrierStatusDetails.isNotBlank()) order.carrierStatusDetails else desc,
                timestampText = timeText,
                isCompleted = isDone,
                isCurrent = isCurrent
            )
        }
    }
}
