package com.example

import com.example.data.local.InitialCatalogData
import com.example.data.model.OrderEntity
import com.example.data.model.ProductEntity
import com.example.data.model.WholesalePricingHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testInitialCatalogData_containsAll12Products() {
        val products = InitialCatalogData.getInitialProducts()
        assertEquals(12, products.size)

        val skus = products.map { it.sku }
        assertTrue(skus.contains("KRN-BM-05"))
        assertTrue(skus.contains("KRN-BM-10"))
        assertTrue(skus.contains("KRN-JP-05"))
        assertTrue(skus.contains("KRN-JP-10"))
        assertTrue(skus.contains("KRN-CS-05"))
        assertTrue(skus.contains("KRN-CS-10"))
        assertTrue(skus.contains("KRN-MM-05"))
        assertTrue(skus.contains("KRN-MM-10"))
        assertTrue(skus.contains("KRN-CN-05"))
        assertTrue(skus.contains("KRN-SD-05"))
        assertTrue(skus.contains("KRN-LL-05"))
        assertTrue(skus.contains("KRN-DG-20"))
    }

    @Test
    fun testBulkTierPricing_calculatesAccurately() {
        // ₹5 MSRP
        val samplePrice = WholesalePricingHelper.calculateUnitPrice(5.0, 1)
        val boxPrice = WholesalePricingHelper.calculateUnitPrice(5.0, 24)
        val cartonPrice = WholesalePricingHelper.calculateUnitPrice(5.0, 72)
        val superPrice = WholesalePricingHelper.calculateUnitPrice(5.0, 216)

        // 16% discount on ₹5 = ₹4.20
        assertEquals(4.20, samplePrice, 0.01)
        // 25% discount on ₹5 = ₹3.75
        assertEquals(3.75, boxPrice, 0.01)
        // 32% discount on ₹5 = ₹3.40
        assertEquals(3.40, cartonPrice, 0.01)
        // 40% discount on ₹5 = ₹3.00
        assertEquals(3.00, superPrice, 0.01)
    }

    @Test
    fun testNextTierTarget_providesUpsellDetails() {
        val nextTier = WholesalePricingHelper.getNextTierTarget(5.0, 12)
        assertNotNull(nextTier)
        assertEquals("Box Pack", nextTier!!.targetTierName)
        assertEquals(12, nextTier.neededUnits) // 24 - 12 = 12
        assertEquals(25.0, nextTier.newDiscountPercent, 0.01)
    }

    @Test
    fun testOrderTrackingCarrierFields() {
        val order = OrderEntity(
            orderId = "NJW-TEST-001",
            totalAmount = 500.0,
            totalRetailValue = 750.0,
            totalRetailerProfit = 250.0,
            totalItemsCount = 144,
            status = "In Transit",
            paymentMethod = "15-Day Khata Credit Line",
            retailerName = "Ganesh Kirana",
            retailerGstin = "08AABCG1234F1Z8",
            retailerAddress = "Jaipur, RJ",
            carrierName = "Delhivery B2B Logistics",
            carrierTrackingNumber = "DLV-B2B-9842103",
            itemsSummary = "Item x144"
        )

        assertEquals("Delhivery B2B Logistics", order.carrierName)
        assertEquals("DLV-B2B-9842103", order.carrierTrackingNumber)
        assertEquals("In Transit", order.status)
        assertTrue(order.smsNotificationSent)
        assertTrue(order.emailNotificationSent)
    }

    @Test
    fun testOwnerProductCreation_andStockCalculations() {
        val newProduct = ProductEntity(
            sku = "KRN-GL-05",
            title = "Krown Glucose Power Pack",
            msrp = 5.0,
            category = "Biscuits",
            subcategory = "Glucose",
            size = "45g Pack",
            caption = "ऊर्जा और स्वाद का संगम",
            shortDescription = "Energy glucose biscuits.",
            longDescription = "Fresh baked wheat and milk enriched glucose biscuits.",
            imageUrl = "https://placehold.co",
            currentStock = 288,
            boxSize = 24,
            masterCartonSize = 72
        )

        assertEquals("KRN-GL-05", newProduct.sku)
        assertEquals(288, newProduct.currentStock)
        // 288 / 72 = 4 Master Cartons
        assertEquals(4, newProduct.currentStock / newProduct.masterCartonSize)
        // 288 / 24 = 12 Boxes
        assertEquals(12, newProduct.currentStock / newProduct.boxSize)
    }

    @Test
    fun testTotalOrderHistoryAggregation() {
        val orders = listOf(
            OrderEntity(
                orderId = "NJW-01",
                totalAmount = 1000.0,
                totalRetailValue = 1500.0,
                totalRetailerProfit = 500.0,
                totalItemsCount = 200,
                status = "Delivered",
                paymentMethod = "COD",
                retailerName = "Shop A",
                retailerGstin = "08A",
                retailerAddress = "Jaipur",
                itemsSummary = "Items"
            ),
            OrderEntity(
                orderId = "NJW-02",
                totalAmount = 2500.0,
                totalRetailValue = 3700.0,
                totalRetailerProfit = 1200.0,
                totalItemsCount = 500,
                status = "Dispatched",
                paymentMethod = "15-Day Khata",
                retailerName = "Shop B",
                retailerGstin = "08B",
                retailerAddress = "Ajmer",
                itemsSummary = "Items"
            )
        )

        val totalRevenue = orders.sumOf { it.totalAmount }
        val totalUnits = orders.sumOf { it.totalItemsCount }
        assertEquals(3500.0, totalRevenue, 0.01)
        assertEquals(700, totalUnits)
    }
}
