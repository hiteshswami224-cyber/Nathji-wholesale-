package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.InitialCatalogData
import com.example.data.model.BusinessProfileEntity
import com.example.data.model.CartItemEntity
import com.example.data.model.InventoryLogEntity
import com.example.data.model.NotificationLogEntity
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.SavedAddressEntity
import com.example.data.model.SavedPaymentMethodEntity
import com.example.data.model.WholesalePricingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WholesaleRepository(private val database: AppDatabase) {

    private val productDao = database.productDao()
    private val cartDao = database.cartDao()
    private val orderDao = database.orderDao()
    private val inventoryLogDao = database.inventoryLogDao()
    private val notificationLogDao = database.notificationLogDao()
    private val profileDao = database.businessProfileDao()

    val allProducts: Flow<List<ProductEntity>> = productDao.getAllProducts()
    val allCartItems: Flow<List<CartItemEntity>> = cartDao.getAllCartItems()
    val allOrders: Flow<List<OrderEntity>> = orderDao.getAllOrders()
    val recentInventoryLogs: Flow<List<InventoryLogEntity>> = inventoryLogDao.getRecentLogs()
    val allNotifications: Flow<List<NotificationLogEntity>> = notificationLogDao.getAllNotifications()
    val businessProfile: Flow<BusinessProfileEntity?> = profileDao.getProfile()
    val savedAddresses: Flow<List<SavedAddressEntity>> = profileDao.getAllAddresses()
    val savedPaymentMethods: Flow<List<SavedPaymentMethodEntity>> = profileDao.getAllPaymentMethods()

    suspend fun seedInitialDataIfNeeded() = withContext(Dispatchers.IO) {
        val count = productDao.getProductCount()
        if (count == 0) {
            val initial = InitialCatalogData.getInitialProducts()
            productDao.insertProducts(initial)

            initial.forEach { prod ->
                inventoryLogDao.insertLog(
                    InventoryLogEntity(
                        sku = prod.sku,
                        productTitle = prod.title,
                        changeAmount = prod.currentStock,
                        resultingStock = prod.currentStock,
                        actionType = "INITIAL_RECEIPT",
                        notes = "Initial warehouse intake batch"
                    )
                )
            }
        }

        // Seed default business profile if not present
        val existingProfile = profileDao.getProfileDirect()
        if (existingProfile == null) {
            profileDao.saveProfile(
                BusinessProfileEntity(
                    id = "PRIMARY_PROFILE",
                    businessName = "Ganesh Kirana & General Store",
                    ownerName = "Ramesh Sharma",
                    email = "sharma.kirana@gmail.com",
                    phone = "+91 98290 41235",
                    gstin = "08AABCG1234F1Z8",
                    businessType = "Retail Kirana & FMCG Store",
                    tradeLicense = "RJ-JPR-2024-88412",
                    creditLimit = 50000.0,
                    creditUsed = 12450.0,
                    creditPaymentDays = 15,
                    isVerified = true,
                    isLoggedIn = true
                )
            )

            // Seed default addresses
            profileDao.insertAddress(
                SavedAddressEntity(
                    label = "Main Retail Storefront",
                    contactPerson = "Ramesh Sharma",
                    phone = "+91 98290 41235",
                    streetAddress = "Shop 14, Main Bazaar, Tripolia Road",
                    city = "Jaipur",
                    state = "Rajasthan",
                    pincode = "302001",
                    isDefault = true
                )
            )
            profileDao.insertAddress(
                SavedAddressEntity(
                    label = "Warehouse Godown #2",
                    contactPerson = "Suresh Sharma",
                    phone = "+91 98290 88721",
                    streetAddress = "Plot 82, Transport Nagar Industrial Area",
                    city = "Jaipur",
                    state = "Rajasthan",
                    pincode = "302004",
                    isDefault = false
                )
            )

            // Seed default payment methods
            profileDao.insertPaymentMethod(
                SavedPaymentMethodEntity(
                    type = "KHATA_CREDIT",
                    title = "15-Day Khata Credit Line",
                    subtitle = "₹37,550 Available • 0% Interest for Verified Kirana",
                    isDefault = true
                )
            )
            profileDao.insertPaymentMethod(
                SavedPaymentMethodEntity(
                    type = "UPI",
                    title = "HDFC Bank Current Account UPI",
                    subtitle = "sharmakirana@hdfcbank",
                    isDefault = false
                )
            )
            profileDao.insertPaymentMethod(
                SavedPaymentMethodEntity(
                    type = "COD",
                    title = "Pay on Delivery (Cash / E-Way UPI)",
                    subtitle = "Inspect cartons before payment at shop",
                    isDefault = false
                )
            )

            // Also seed a demo past order with tracking milestones so the user can immediately experience order tracking
            seedDemoOrder()
        }
    }

    private suspend fun seedDemoOrder() {
        val sampleOrderId = "NJW-261001-492"
        val sampleItems = "Krown Black Magic Pocket Pack (KRN-BM-05) x72 [Master Carton] @ ₹3.40 = ₹244.80\n" +
                "Club Snacks Crackers Mini (KRN-CS-05) x72 [Master Carton] @ ₹3.40 = ₹244.80\n" +
                "Supper Dupper Chocolate Wafer (KRN-SD-05) x72 [Master Carton] @ ₹3.40 = ₹244.80"

        val demoOrder = OrderEntity(
            orderId = sampleOrderId,
            timestamp = System.currentTimeMillis() - (18 * 3600 * 1000L), // 18 hours ago
            totalAmount = 734.40,
            totalRetailValue = 1080.0,
            totalRetailerProfit = 345.60,
            totalItemsCount = 216,
            status = "In Transit",
            paymentMethod = "15-Day Khata Credit Line",
            retailerName = "Ganesh Kirana & General Store",
            retailerGstin = "08AABCG1234F1Z8",
            retailerAddress = "Shop 14, Main Bazaar, Tripolia Road, Jaipur 302001",
            recipientPhone = "+91 98290 41235",
            recipientEmail = "sharma.kirana@gmail.com",
            carrierName = "Delhivery B2B Logistics",
            carrierTrackingNumber = "DLV-B2B-9842103",
            estimatedDeliveryDate = "Today, by 3:30 PM",
            carrierStatusDetails = "Departed Regional Sorting Hub (Jaipur North), vehicle assigned for local Kirana delivery route",
            carrierPhone = "+91 1800 102 3456",
            carrierTrackingUrl = "https://www.delhivery.com/track/package/DLV-B2B-9842103",
            smsNotificationSent = true,
            emailNotificationSent = true,
            itemsSummary = sampleItems
        )
        orderDao.insertOrder(demoOrder)

        // Seed notifications for this demo order
        notificationLogDao.insertNotification(
            NotificationLogEntity(
                orderId = sampleOrderId,
                channel = "SMS",
                recipient = "+91 98290 41235",
                subject = "Order Confirmed",
                messageContent = "NATHJI WHOLESALE: Order #$sampleOrderId (216 units) confirmed! Booked under 15-Day Khata. Delhivery AWB: DLV-B2B-9842103.",
                timestamp = System.currentTimeMillis() - (18 * 3600 * 1000L),
                deliveryStatus = "Delivered"
            )
        )
        notificationLogDao.insertNotification(
            NotificationLogEntity(
                orderId = sampleOrderId,
                channel = "EMAIL",
                recipient = "sharma.kirana@gmail.com",
                subject = "Tax Invoice & Dispatch Details - Order #$sampleOrderId",
                messageContent = "Your NATHJI WHOLESALE FMCG order has been packed and handed over to Delhivery B2B Logistics. Expected delivery: Today by 3:30 PM. Track here: https://www.delhivery.com/track/package/DLV-B2B-9842103",
                timestamp = System.currentTimeMillis() - (6 * 3600 * 1000L),
                deliveryStatus = "Delivered"
            )
        )
    }

    suspend fun updateProfile(profile: BusinessProfileEntity) = withContext(Dispatchers.IO) {
        profileDao.saveProfile(profile)
    }

    suspend fun addSavedAddress(address: SavedAddressEntity) = withContext(Dispatchers.IO) {
        if (address.isDefault) {
            profileDao.clearDefaultAddress()
        }
        profileDao.insertAddress(address)
    }

    suspend fun setDefaultAddress(id: Long) = withContext(Dispatchers.IO) {
        profileDao.clearDefaultAddress()
        profileDao.setDefaultAddress(id)
    }

    suspend fun deleteAddress(id: Long) = withContext(Dispatchers.IO) {
        profileDao.deleteAddress(id)
    }

    suspend fun addSavedPaymentMethod(method: SavedPaymentMethodEntity) = withContext(Dispatchers.IO) {
        if (method.isDefault) {
            profileDao.clearDefaultPaymentMethod()
        }
        profileDao.insertPaymentMethod(method)
    }

    suspend fun setDefaultPaymentMethod(id: Long) = withContext(Dispatchers.IO) {
        profileDao.clearDefaultPaymentMethod()
        profileDao.setDefaultPaymentMethod(id)
    }

    suspend fun addToCart(sku: String, delta: Int) = withContext(Dispatchers.IO) {
        val existing = cartDao.getCartItem(sku)
        val product = productDao.getProductDirect(sku) ?: return@withContext
        val newQty = (existing?.quantity ?: 0) + delta
        if (newQty <= 0) {
            cartDao.deleteItem(sku)
        } else {
            val cappedQty = newQty.coerceAtMost(product.currentStock)
            if (cappedQty > 0) {
                cartDao.insertOrUpdate(CartItemEntity(sku = sku, quantity = cappedQty))
            }
        }
    }

    suspend fun setCartQuantity(sku: String, quantity: Int) = withContext(Dispatchers.IO) {
        val product = productDao.getProductDirect(sku) ?: return@withContext
        if (quantity <= 0) {
            cartDao.deleteItem(sku)
        } else {
            val capped = quantity.coerceAtMost(product.currentStock)
            cartDao.insertOrUpdate(CartItemEntity(sku = sku, quantity = capped))
        }
    }

    suspend fun removeFromCart(sku: String) = withContext(Dispatchers.IO) {
        cartDao.deleteItem(sku)
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        cartDao.clearCart()
    }

    suspend fun addProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        productDao.insertProduct(product)
        if (product.currentStock > 0) {
            inventoryLogDao.insertLog(
                InventoryLogEntity(
                    sku = product.sku,
                    productTitle = product.title,
                    changeAmount = product.currentStock,
                    resultingStock = product.currentStock,
                    actionType = "INITIAL_RECEIPT",
                    notes = "New product introduced by Owner"
                )
            )
        }
    }

    suspend fun deleteProduct(sku: String) = withContext(Dispatchers.IO) {
        val prod = productDao.getProductDirect(sku)
        productDao.deleteProductBySku(sku)
        cartDao.deleteItem(sku)
        if (prod != null) {
            inventoryLogDao.insertLog(
                InventoryLogEntity(
                    sku = sku,
                    productTitle = prod.title,
                    changeAmount = -prod.currentStock,
                    resultingStock = 0,
                    actionType = "PRODUCT_DELETED",
                    notes = "Product removed from warehouse by Owner"
                )
            )
        }
    }

    suspend fun adjustStock(
        sku: String,
        delta: Int,
        notes: String = "Manual stock update",
        actionType: String = if (delta >= 0) "RESTOCK" else "MANUAL_ADJUSTMENT"
    ) = withContext(Dispatchers.IO) {
        val product = productDao.getProductDirect(sku) ?: return@withContext
        val newStock = (product.currentStock + delta).coerceAtLeast(0)
        productDao.updateStock(sku, newStock)

        inventoryLogDao.insertLog(
            InventoryLogEntity(
                sku = sku,
                productTitle = product.title,
                changeAmount = delta,
                resultingStock = newStock,
                actionType = actionType,
                notes = notes
            )
        )
    }

    suspend fun setStockDirect(
        sku: String,
        newStock: Int,
        notes: String = "Physical inventory audit count"
    ) = withContext(Dispatchers.IO) {
        val product = productDao.getProductDirect(sku) ?: return@withContext
        val delta = newStock - product.currentStock
        productDao.updateStock(sku, newStock.coerceAtLeast(0))

        inventoryLogDao.insertLog(
            InventoryLogEntity(
                sku = sku,
                productTitle = product.title,
                changeAmount = delta,
                resultingStock = newStock,
                actionType = "AUDIT_COUNT",
                notes = notes
            )
        )
    }

    suspend fun placeOrder(
        cartItems: List<CartItemEntity>,
        productsMap: Map<String, ProductEntity>,
        retailerName: String,
        retailerGstin: String,
        retailerAddress: String,
        recipientPhone: String,
        recipientEmail: String,
        paymentMethod: String
    ): Result<OrderEntity> = withContext(Dispatchers.IO) {
        if (cartItems.isEmpty()) {
            return@withContext Result.failure(Exception("Cart is empty"))
        }

        // Validate stock
        for (item in cartItems) {
            val prod = productsMap[item.sku]
                ?: return@withContext Result.failure(Exception("Product ${item.sku} not found"))
            if (prod.currentStock < item.quantity) {
                return@withContext Result.failure(
                    Exception("Insufficient stock for ${prod.title}. Available: ${prod.currentStock}, Requested: ${item.quantity}")
                )
            }
        }

        var totalWholesaleAmount = 0.0
        var totalRetailValue = 0.0
        var totalUnits = 0
        val itemsSummaryBuilder = StringBuilder()

        for (item in cartItems) {
            val prod = productsMap[item.sku]!!
            val unitPrice = WholesalePricingHelper.calculateUnitPrice(prod.msrp, item.quantity)
            val lineTotal = unitPrice * item.quantity
            val lineRetailValue = prod.msrp * item.quantity

            totalWholesaleAmount += lineTotal
            totalRetailValue += lineRetailValue
            totalUnits += item.quantity

            val tier = WholesalePricingHelper.getTierForQuantity(item.quantity)
            itemsSummaryBuilder.append(
                "${prod.title} (${item.sku}) x${item.quantity} [${tier.name}] @ ₹${String.format(Locale.US, "%.2f", unitPrice)} = ₹${String.format(Locale.US, "%.2f", lineTotal)}\n"
            )

            // Deduct stock & log movement
            val newStock = prod.currentStock - item.quantity
            productDao.updateStock(prod.sku, newStock)
            inventoryLogDao.insertLog(
                InventoryLogEntity(
                    sku = prod.sku,
                    productTitle = prod.title,
                    changeAmount = -item.quantity,
                    resultingStock = newStock,
                    actionType = "ORDER_DISPATCH",
                    notes = "Order dispatch to $retailerName"
                )
            )
        }

        val totalRetailerProfit = (totalRetailValue - totalWholesaleAmount).coerceAtLeast(0.0)
        val timestamp = System.currentTimeMillis()
        val dateStr = SimpleDateFormat("yyMMdd-HHmm", Locale.US).format(Date(timestamp))
        val orderId = "NJW-$dateStr-${(100..999).random()}"
        val trackingNo = "DLV-B2B-${(1000000..9999999).random()}"

        val order = OrderEntity(
            orderId = orderId,
            timestamp = timestamp,
            totalAmount = totalWholesaleAmount,
            totalRetailValue = totalRetailValue,
            totalRetailerProfit = totalRetailerProfit,
            totalItemsCount = totalUnits,
            status = "Processing",
            paymentMethod = paymentMethod,
            retailerName = retailerName,
            retailerGstin = retailerGstin,
            retailerAddress = retailerAddress,
            recipientPhone = recipientPhone,
            recipientEmail = recipientEmail,
            carrierName = "Delhivery B2B Logistics",
            carrierTrackingNumber = trackingNo,
            estimatedDeliveryDate = "Tomorrow, by 5:00 PM",
            carrierStatusDetails = "Order received at Central Distribution Hub. Warehouse staging scheduled.",
            carrierPhone = "+91 1800 102 3456",
            carrierTrackingUrl = "https://www.delhivery.com/track/package/$trackingNo",
            smsNotificationSent = true,
            emailNotificationSent = true,
            itemsSummary = itemsSummaryBuilder.toString().trim()
        )

        orderDao.insertOrder(order)
        cartDao.clearCart()

        // Trigger automated SMS & Email dispatch notifications
        sendOrderNotification(
            orderId = orderId,
            channel = "SMS",
            recipient = recipientPhone,
            subject = "Order #$orderId Confirmed",
            message = "NATHJI WHOLESALE: Order #$orderId ($totalUnits units, ₹${String.format(Locale.US, "%.2f", totalWholesaleAmount)}) placed successfully! Booked via $paymentMethod. Live tracking AWB: $trackingNo"
        )

        sendOrderNotification(
            orderId = orderId,
            channel = "EMAIL",
            recipient = recipientEmail,
            subject = "Order Confirmation & Wholesale Tax Invoice #$orderId",
            message = "Dear $retailerName, thank you for your wholesale order with NATHJI WHOLESALE. Your order ($totalUnits packs) is being staged for dispatch with Delhivery B2B Logistics (AWB: $trackingNo). Expected delivery: Tomorrow by 5:00 PM. Projected shopkeeper profit: ₹${String.format(Locale.US, "%.2f", totalRetailerProfit)}."
        )

        Result.success(order)
    }

    suspend fun advanceOrderStatus(
        orderId: String,
        newStatus: String,
        carrierLocationNote: String
    ) = withContext(Dispatchers.IO) {
        val order = orderDao.getOrderDirect(orderId) ?: return@withContext
        orderDao.updateOrderTracking(orderId, newStatus, carrierLocationNote)

        // Automated SMS notification for status change
        val smsMsg = when (newStatus) {
            "Dispatched" -> "NATHJI WHOLESALE: Your order #$orderId is DISPATCHED with ${order.carrierName} (AWB: ${order.carrierTrackingNumber}). En route to destination hub."
            "Out for Delivery" -> "NATHJI WHOLESALE: Order #$orderId is OUT FOR DELIVERY! Vehicle assigned for your shop delivery today. Keep delivery verification ready."
            "Delivered" -> "NATHJI WHOLESALE: Order #$orderId has been DELIVERED successfully! E-Way bill signed. Thank you for your business!"
            else -> "NATHJI WHOLESALE: Order #$orderId update: Status is now $newStatus ($carrierLocationNote)."
        }

        sendOrderNotification(
            orderId = orderId,
            channel = "SMS",
            recipient = order.recipientPhone,
            subject = "Order $newStatus",
            message = smsMsg
        )

        // Automated Email notification
        sendOrderNotification(
            orderId = orderId,
            channel = "EMAIL",
            recipient = order.recipientEmail,
            subject = "Shipment Update: Order #$orderId is $newStatus",
            message = "Dear ${order.retailerName}, status update for your order #$orderId:\n\nCurrent Status: $newStatus\nLocation / Note: $carrierLocationNote\nCarrier: ${order.carrierName} (AWB: ${order.carrierTrackingNumber})\nTrack Live: ${order.carrierTrackingUrl}"
        )
    }

    private suspend fun sendOrderNotification(
        orderId: String,
        channel: String,
        recipient: String,
        subject: String,
        message: String
    ) {
        notificationLogDao.insertNotification(
            NotificationLogEntity(
                orderId = orderId,
                channel = channel,
                recipient = recipient,
                subject = subject,
                messageContent = message,
                timestamp = System.currentTimeMillis(),
                deliveryStatus = "Delivered"
            )
        )
    }

    suspend fun reorderItems(order: OrderEntity) = withContext(Dispatchers.IO) {
        val lines = order.itemsSummary.lines()
        for (line in lines) {
            val skuRegex = Regex("\\(([A-Z0-9-]+)\\)\\s+x(\\d+)")
            val match = skuRegex.find(line)
            if (match != null) {
                val sku = match.groupValues[1]
                val qty = match.groupValues[2].toIntOrNull() ?: 24
                addToCart(sku, qty)
            }
        }
    }
}
