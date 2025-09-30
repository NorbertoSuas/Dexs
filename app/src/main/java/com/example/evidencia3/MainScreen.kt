package com.example.evidencia3
import com.example.evidencia3.ui.theme.RedPrimary
import com.example.evidencia3.ui.theme.Black
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.NavigationDrawerItemDefaults
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.vector.ImageVector
import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.*
import android.os.Bundle
import androidx.compose.ui.platform.LocalLifecycleOwner
import android.content.Intent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.draw.alpha
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge

// Modern DrawerOption for Material icons
data class DrawerOption(val title: String)

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedDrawerItem by remember { mutableStateOf("Ventas") }
    var cartItems by remember { mutableStateOf(listOf<Product>()) }
    var animatingItem by remember { mutableStateOf<Product?>(null) }
    val drawerOptions = listOf(
        DrawerOption("Ventas"),
        DrawerOption("Contacto"),
        DrawerOption("Reseñas"),
        DrawerOption("Cart")
    )

    // Animation values
    val animatedOffset = remember { Animatable(0f) }
    val animatedAlpha = remember { Animatable(1f) }

    // Handle back button press
    BackHandler(enabled = selectedDrawerItem != "Ventas") {
        selectedDrawerItem = "Ventas"
    }

    // Start background music service
    DisposableEffect(Unit) {
        val serviceIntent = Intent(context, BackgroundSoundService::class.java)
        context.startService(serviceIntent)
        
        onDispose {
            context.stopService(serviceIntent)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = com.example.evidencia3.ui.theme.White
                ) {
                    // Drawer header with only a bigger logo, no app name, all white background
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp), // more vertical space for bigger logo
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.mipmap.dexterlogo),
                            contentDescription = "Dexter Logo",
                            modifier = Modifier.size(128.dp) // bigger logo
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    drawerOptions.forEach { option ->
                        NavigationDrawerItem(
                            label = {
                                Text(option.title, color = Black, fontWeight = FontWeight.Medium)
                            },
                            selected = selectedDrawerItem == option.title,
                            onClick = {
                                selectedDrawerItem = option.title
                                scope.launch { drawerState.close() }
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(com.example.evidencia3.ui.theme.White)
                    .padding(top = 16.dp, bottom = 16.dp)
            ) {
                // Modern top bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(com.example.evidencia3.ui.theme.White)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = RedPrimary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Dex´s",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = RedPrimary
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    BadgedBox(
                        badge = {
                            if (cartItems.isNotEmpty()) {
                                Badge { Text(cartItems.size.toString()) }
                            }
                        }
                    ) {
                        IconButton(onClick = { selectedDrawerItem = "Cart" }) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                tint = RedPrimary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Main content based on drawer selection
                when (selectedDrawerItem) {
                    "Ventas" -> InicioPage(
                        onAddToCart = { product ->
                            animatingItem = product
                            scope.launch {
                                animatedOffset.snapTo(0f)
                                animatedAlpha.snapTo(1f)
                                animatedOffset.animateTo(
                                    targetValue = 100f,
                                    animationSpec = tween(500, easing = FastOutSlowInEasing)
                                )
                                animatedAlpha.animateTo(
                                    targetValue = 0f,
                                    animationSpec = tween(500)
                                )
                                delay(500)
                                cartItems = cartItems + product
                                animatingItem = null
                            }
                        }
                    )
                    "Contacto" -> ContactPage()
                    "Reseñas" -> ReviewsPage(
                        listOf(
                            Review("Dexter Morgan", R.mipmap.dextermorgan, 5, "La mejor app para ventas de ropa, muy intuitiva."),
                            Review("Debra Morgan", R.mipmap.debmorgan, 4, "Me encanta la variedad de productos."),
                            Review("Angel Batista", R.mipmap.angelbatista, 5, "Excelente atención al cliente y precios justos."),
                            Review("Rita Bennett", R.mipmap.rita, 5, "Fácil de usar y muy confiable."),
                            Review("Vince Masuka", R.mipmap.masuja, 4, "Gran experiencia de compra, volveré a usarla.")
                        )
                    )
                    "Cart" -> CartPage(
                        cartItems = cartItems,
                        onRemoveItem = { product ->
                            cartItems = cartItems.filter { it != product }
                        },
                        onCheckout = {
                            // TODO: Implement checkout functionality
                        }
                    )
                }
            }
        }

        // Animated item being added to cart
        animatingItem?.let { product ->
            Card(
                modifier = Modifier
                    .offset { IntOffset(0, animatedOffset.value.toInt()) }
                    .alpha(animatedAlpha.value)
                    .size(60.dp)
                    .align(Alignment.TopCenter),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun InicioPage(onAddToCart: (Product) -> Unit) {
    // 6 representative clothes from Dexter series
    val dexterClothes = listOf(
        Product(1, "Dexter's Kill Apron", "Dexter", R.mipmap.dexter_apron, "Apron"),
        Product(2, "Debra's Police Uniform", "Debra Morgan", R.mipmap.debra_uniform, "Uniform"),
        Product(3, "Batista's Fedora", "Angel Batista", R.mipmap.batista_fedora, "Hat"),
        Product(4, "Rita's Dress", "Rita Bennett", R.mipmap.rita_dress, "Dress"),
        Product(5, "Masuka's Lab Coat", "Vince Masuka", R.mipmap.masuka_labcoat, "Lab Coat"),
        Product(6, "Dexter's Henley Shirt", "Dexter", R.mipmap.dexter_henley, "Shirt")
    )
    Column(modifier = Modifier.fillMaxSize().padding(12.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Ropa en Venta",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { /* TODO: Implement search logic */ }) {
                Icon(Icons.Default.Search, contentDescription = "Buscar", tint = RedPrimary)
            }
        }
        ProductGrid(products = dexterClothes, onAddToCart = onAddToCart)
    }
}

@Composable
fun ReviewsPage(reviews: List<Review>) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Reseñas de Clientes", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /* TODO: Filter */ }) {
                Icon(Icons.Default.Search, contentDescription = "Buscar", tint = Color(0xFFE5733B))
            }
        }
        
        // Reviews List
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp)
        ) {
            reviews.forEach { review ->
                ReviewCard(review)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ContactPage() {
    val mapView = rememberMapViewWithLifecycle()
    val tecmilenioLatLng = LatLng(25.620704897822215, -100.29194124597694)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(com.example.evidencia3.ui.theme.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Dexter logo as header accent
        Image(
            painter = painterResource(id = R.mipmap.dexterlogo),
            contentDescription = "Dexter Logo",
            modifier = Modifier.size(96.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Contacto",
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            color = RedPrimary
        )
        Spacer(modifier = Modifier.height(20.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Email: contacto@evidencia3.com", fontSize = 16.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Teléfono: +52 123 456 7890", fontSize = 16.sp, color = Color.Black)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Dexter phone image below info
        Image(
            painter = painterResource(id = R.mipmap.dextelefono),
            contentDescription = "Dexter Phone",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        // Modern rounded map container
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            AndroidView(
                factory = { context ->
                    mapView.apply {
                        getMapAsync { googleMap ->
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tecmilenioLatLng, 15f))
                            googleMap.addMarker(MarkerOptions().position(tecmilenioLatLng).title("Tecmilenio Las Torres Monterrey"))
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    DisposableEffect(lifecycle) {
        val observer = object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                mapView.onCreate(Bundle())
            }
            override fun onStart(owner: LifecycleOwner) = mapView.onStart()
            override fun onResume(owner: LifecycleOwner) = mapView.onResume()
            override fun onPause(owner: LifecycleOwner) = mapView.onPause()
            override fun onStop(owner: LifecycleOwner) = mapView.onStop()
            override fun onDestroy(owner: LifecycleOwner) = mapView.onDestroy()
        }
        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
    return mapView
}

data class Review(
    val name: String,
    val avatarRes: Int,
    val rating: Int,
    val comment: String
)

@Composable
fun ReviewCard(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = review.avatarRes),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFBE9E7)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(review.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Row {
                        repeat(review.rating) {
                            Icon(
                                painter = painterResource(android.R.drawable.btn_star_big_on),
                                contentDescription = null,
                                tint = Color(0xFFFFC107),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(review.comment, fontSize = 14.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ProductGrid(products: List<Product>, onAddToCart: (Product) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(8.dp).fillMaxSize(),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductCard(product = product, onAddToCart = { onAddToCart(product) })
        }
    }
}

@Composable
fun ProductCard(product: Product, onAddToCart: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(product.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(product.brand, color = Color.Gray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onAddToCart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = RedPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add to cart",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add to Cart")
            }
        }
    }
} 