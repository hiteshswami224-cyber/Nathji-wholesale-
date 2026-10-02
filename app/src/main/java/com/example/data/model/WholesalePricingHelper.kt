package com.example.data.model

data class BulkTierInfo(
    val name: String,
    val minUnits: Int,
    val maxUnits: Int,
    val discountPercent: Double, // Retailer profit margin
    val description: String,
    val isCartonTier: Boolean = false
)

data class NextTierTarget(
    val targetTierName: String,
    val neededUnits: Int,
    val newDiscountPercent: Double,
    val additionalSavingsPerUnit: Double
)

data class EnrichedCartItem(
    val product: ProductEntity,
    val quantity: Int,
    val unitPrice: Double,
    val tierInfo: BulkTierInfo,
    val totalPrice: Double,
    val totalRetailValue: Double,
    val totalProfit: Double,
    val profitMarginPercent: Double,
    val nextTierTarget: NextTierTarget?,
    val isStockAvailable: Boolean
)

object WholesalePricingHelper {
    val TIERS = listOf(
        BulkTierInfo(
            name = "Loose / Sample Pack",
            minUnits = 1,
            maxUnits = 23,
            discountPercent = 16.0,
            description = "1 - 23 units (Standard retail sample)",
            isCartonTier = false
        ),
        BulkTierInfo(
            name = "Box Pack",
            minUnits = 24,
            maxUnits = 71,
            discountPercent = 25.0,
            description = "24 - 71 units (1+ Inner Box)",
            isCartonTier = true
        ),
        BulkTierInfo(
            name = "Master Carton",
            minUnits = 72,
            maxUnits = 215,
            discountPercent = 32.0,
            description = "72 - 215 units (1+ Master Carton)",
            isCartonTier = true
        ),
        BulkTierInfo(
            name = "Super Wholesale",
            minUnits = 216,
            maxUnits = Int.MAX_VALUE,
            discountPercent = 40.0,
            description = "216+ units (3+ Master Cartons)",
            isCartonTier = true
        )
    )

    fun getTierForQuantity(quantity: Int): BulkTierInfo {
        if (quantity <= 0) return TIERS.first()
        return TIERS.lastOrNull { quantity >= it.minUnits } ?: TIERS.first()
    }

    fun calculateUnitPrice(msrp: Double, quantity: Int): Double {
        val tier = getTierForQuantity(quantity)
        val wholesaleMultiplier = (100.0 - tier.discountPercent) / 100.0
        val rawPrice = msrp * wholesaleMultiplier
        return Math.round(rawPrice * 100.0) / 100.0
    }

    fun getNextTierTarget(msrp: Double, currentQuantity: Int): NextTierTarget? {
        val currentTier = getTierForQuantity(currentQuantity)
        val currentIndex = TIERS.indexOf(currentTier)
        if (currentIndex < 0 || currentIndex >= TIERS.size - 1) return null

        val nextTier = TIERS[currentIndex + 1]
        val neededUnits = nextTier.minUnits - currentQuantity
        val currentUnitWholesale = calculateUnitPrice(msrp, currentQuantity)
        val nextUnitWholesale = calculateUnitPrice(msrp, nextTier.minUnits)
        val savingsPerUnit = (currentUnitWholesale - nextUnitWholesale).coerceAtLeast(0.0)

        return NextTierTarget(
            targetTierName = nextTier.name,
            neededUnits = neededUnits,
            newDiscountPercent = nextTier.discountPercent,
            additionalSavingsPerUnit = Math.round(savingsPerUnit * 100.0) / 100.0
        )
    }

    fun formatCurrency(amount: Double): String {
        return "₹" + String.format(java.util.Locale.US, "%.2f", amount)
    }

    fun formatShortCurrency(amount: Double): String {
        return if (amount % 1.0 == 0.0) {
            "₹" + amount.toInt().toString()
        } else {
            "₹" + String.format(java.util.Locale.US, "%.2f", amount)
        }
    }
}
