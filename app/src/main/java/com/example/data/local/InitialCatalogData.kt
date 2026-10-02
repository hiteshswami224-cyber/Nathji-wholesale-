package com.example.data.local

import com.example.data.model.ProductEntity

object InitialCatalogData {
    fun getInitialProducts(): List<ProductEntity> = listOf(
        ProductEntity(
            sku = "KRN-BM-05",
            title = "Krown Black Magic Pocket Pack",
            msrp = 5.0,
            category = "Biscuits",
            subcategory = "Cream Biscuits",
            size = "25g Pack",
            caption = "डार्क चॉकलेट और क्रीमी वैनिला का जादू, अब आपके पॉकेट में!",
            shortDescription = "Dark chocolate biscuit with vanilla cream.",
            longDescription = "Premium dark chocolate sandwich cookies with a rich and creamy vanilla filling inside.",
            imageUrl = "https://placehold.co",
            currentStock = 288, // 4 Master Cartons
            minStockThreshold = 48,
            boxSize = 24,
            masterCartonSize = 72
        ),
        ProductEntity(
            sku = "KRN-BM-10",
            title = "Krown Black Magic Regular",
            msrp = 10.0,
            category = "Biscuits",
            subcategory = "Cream Biscuits",
            size = "55g Pack",
            caption = "प्रीमियम डार्क चॉकलेट बिस्कुट, डबल वैनिला क्रीम मजे के साथ।",
            shortDescription = "Dark chocolate biscuit with vanilla cream.",
            longDescription = "Medium size premium dark chocolate sandwich cookies with extra creamy vanilla filling.",
            imageUrl = "https://placehold.co+10",
            currentStock = 216, // 3 Master Cartons
            minStockThreshold = 48,
            boxSize = 24,
            masterCartonSize = 72
        ),
        ProductEntity(
            sku = "KRN-JP-05",
            title = "Jackpot Elaichi Crème Small",
            msrp = 5.0,
            category = "Biscuits",
            subcategory = "Cream Biscuits",
            size = "30g Pack",
            caption = "ताज़ा इलायची क्रीम का अनोखा स्वाद, जो हर बाइट को बनाए खास।",
            shortDescription = "Traditional cardamom flavored cream biscuit.",
            longDescription = "Crispy sandwich cookies layered with aromatic and sweet cardamom cream filling.",
            imageUrl = "https://placehold.co",
            currentStock = 144, // 2 Master Cartons
            minStockThreshold = 48,
            boxSize = 24,
            masterCartonSize = 72
        ),
        ProductEntity(
            sku = "KRN-JP-10",
            title = "Jackpot Elaichi Crème Mid",
            msrp = 10.0,
            category = "Biscuits",
            subcategory = "Cream Biscuits",
            size = "65g Pack",
            caption = "असली इलायची की खुशबू और रिच क्रीम, आपकी शाम की चाय के लिए परफेक्ट।",
            shortDescription = "Traditional cardamom flavored cream biscuit.",
            longDescription = "Standard daily pack filled with aromatic cardamom cream layers for home consumption.",
            imageUrl = "https://placehold.co+10",
            currentStock = 36, // Low stock demo!
            minStockThreshold = 48,
            boxSize = 24,
            masterCartonSize = 72
        ),
        ProductEntity(
            sku = "KRN-CS-05",
            title = "Club Snacks Crackers Mini",
            msrp = 5.0,
            category = "Biscuits",
            subcategory = "Crackers",
            size = "40g Pack",
            caption = "हल्का, खस्ता और हल्का नमकीन क्रैकर, जब भी हो छोटी भूख।",
            shortDescription = "Light crispy and salty snack crackers.",
            longDescription = "Lightly baked flaky crackers with a touch of salt. An ideal choice for evening snacks.",
            imageUrl = "https://placehold.co",
            currentStock = 360, // 5 Master Cartons
            minStockThreshold = 48,
            boxSize = 24,
            masterCartonSize = 72
        ),
        ProductEntity(
            sku = "KRN-CS-10",
            title = "Club Snacks Crackers Standard",
            msrp = 10.0,
            category = "Biscuits",
            subcategory = "Crackers",
            size = "85g Pack",
            caption = "क्रिस्पी नमकीन स्वाद, जो चाय की हर चुस्की का मज़ा बढ़ा दे।",
            shortDescription = "Light crispy and salty snack crackers.",
            longDescription = "Full value pack of lightly baked flaky salted crackers suited for tea pairings.",
            imageUrl = "https://placehold.co+10",
            currentStock = 180,
            minStockThreshold = 48,
            boxSize = 24,
            masterCartonSize = 72
        ),
        ProductEntity(
            sku = "KRN-MM-05",
            title = "Miss Marie Pocket Pack",
            msrp = 5.0,
            category = "Biscuits",
            subcategory = "Marie Biscuits",
            size = "36g Pack",
            caption = "दूध की अच्छाई से भरपूर, बेहद हल्की और क्रिस्पी मैरी बिस्कुट।",
            shortDescription = "Light crispy biscuit baked with goodness of milk.",
            longDescription = "Traditional crunchy and light milk marie biscuits perfect for dunking in morning tea.",
            imageUrl = "https://placehold.co",
            currentStock = 432, // 6 Master Cartons
            minStockThreshold = 48,
            boxSize = 24,
            masterCartonSize = 72
        ),
        ProductEntity(
            sku = "KRN-MM-10",
            title = "Miss Marie Regular Pack",
            msrp = 10.0,
            category = "Biscuits",
            subcategory = "Marie Biscuits",
            size = "66g Pack",
            caption = "सुबह की चाय में डुबाने के लिए हर घर की पसंदीदा पारंपरिक मैरी।",
            shortDescription = "Light crispy biscuit baked with goodness of milk.",
            longDescription = "Standard home pack of light milk marie biscuits keeping crispness intact.",
            imageUrl = "https://placehold.co+10",
            currentStock = 288,
            minStockThreshold = 48,
            boxSize = 24,
            masterCartonSize = 72
        ),
        ProductEntity(
            sku = "KRN-CN-05",
            title = "Krown Coconut Biscuits Small",
            msrp = 5.0,
            category = "Biscuits",
            subcategory = "Sweet Biscuits",
            size = "12 Pcs Pack",
            caption = "असली नारियल के क्रंच और सोंधी खुशबू से भरपूर क्रिस्पी बिस्कुट।",
            shortDescription = "Delicious crunchy coconut flavoured biscuits.",
            longDescription = "Infused with real aroma of coconut and baked to a crisp texture value pack.",
            imageUrl = "https://placehold.co",
            currentStock = 24, // Very low stock!
            minStockThreshold = 48,
            boxSize = 24,
            masterCartonSize = 72
        ),
        ProductEntity(
            sku = "KRN-SD-05",
            title = "Supper Dupper Chocolate Wafer",
            msrp = 5.0,
            category = "Wafers",
            subcategory = "Chocolate Wafers",
            size = "18g Pack",
            caption = "क्रंची वेफर लेयर्स और मखमली चॉकलेट कोटिंग का सुपर कॉम्बिनेशन।",
            shortDescription = "Chocolate coated crisp wafer biscuits.",
            longDescription = "Crafted with delicate crisp wafer layers and enrobed in a rich velvety chocolate coating.",
            imageUrl = "https://placehold.co",
            currentStock = 216,
            minStockThreshold = 48,
            boxSize = 24,
            masterCartonSize = 72
        ),
        ProductEntity(
            sku = "KRN-LL-05",
            title = "Lots of Love Choco Cookie",
            msrp = 5.0,
            category = "Biscuits",
            subcategory = "Cookies",
            size = "34g Pack",
            caption = "मुँह में घुल जाने वाली सॉफ्ट कुकी, ढेर सारे चोको-चिप्स के साथ!",
            shortDescription = "Soft crumbly cookie with chocolaty bursts.",
            longDescription = "Premium bakery style cookie dotted with rich chocolate chips and soft baked texture.",
            imageUrl = "https://placehold.co",
            currentStock = 144,
            minStockThreshold = 48,
            boxSize = 24,
            masterCartonSize = 72
        ),
        ProductEntity(
            sku = "KRN-DG-20",
            title = "Krown Digestive Premium",
            msrp = 20.0,
            category = "Biscuits",
            subcategory = "Health Biscuits",
            size = "120g Pack",
            caption = "हाई-फाइबर और गेंहू की शक्ति से भरपूर, सेहतमंद स्नैकिंग का सही चुनाव।",
            shortDescription = "High fiber wheat digestive biscuits.",
            longDescription = "Healthy wheat-rich high bran biscuits providing premium fiber content.",
            imageUrl = "https://placehold.co",
            currentStock = 96,
            minStockThreshold = 48,
            boxSize = 24,
            masterCartonSize = 72
        )
    )
}
