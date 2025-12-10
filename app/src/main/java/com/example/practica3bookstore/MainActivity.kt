package com.example.practica3bookstore
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.practica3bookstore.data.Book
import com.example.practica3bookstore.data.DataSource
import com.example.practica3bookstore.ui.theme.Practica3BookStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Practica3BookStoreTheme {
                BookstoreApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookstoreApp() {
    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: "books"

    val topBarTitle = when {
        currentRoute.startsWith("booksynopsis/") -> "Book Details"
        currentRoute == "allsynopsis" -> "All Synopses"
        currentRoute == "notifications" -> "Notifications"
        else -> "Books"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topBarTitle) },
                actions = {
                    if (currentRoute == "notifications") {
                        IconButton(onClick = { /* Marcar como leído */ }) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear notifications")
                        }
                    }
                }
            )
        },
        bottomBar = { BookstoreBottomAppBar(navController = navController, currentRoute = currentRoute) }
    ) { innerPadding ->
        BookstoreNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun BookstoreNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = "books",
        modifier = modifier
    ) {
        composable("books") {
            BookListScreen(navController = navController)
        }
        composable("booksynopsis/{bookTitle}") { backStackEntry ->
            val bookTitle = backStackEntry.arguments?.getString("bookTitle")
            val book = DataSource.books.find { it.title == bookTitle }
            if (book != null) {
                BookSynopsisScreen(book = book)
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Book not found")
                }
            }
        }
        composable("allsynopsis") {
            AllSynopsesScreen()
        }
        composable("notifications") {
            NotificationsScreen()
        }
    }
}

@Composable
fun BookListScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
    ) {
        items(DataSource.books) { book ->
            BookCard(
                book = book,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clickable {
                        navController.navigate("booksynopsis/${book.title}")
                    }
            )
        }
    }
}

@Composable
fun BookSynopsisScreen(book: Book) {
    var opinion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = book.image),
                contentDescription = "Book cover",
                modifier = Modifier
                    .size(100.dp)
                    .padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = "by ${book.author}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = book.price,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Text(
            text = "Synopsis:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = book.synopsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Text(
            text = "Your Opinion:",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = opinion,
            onValueChange = { opinion = it },
            label = { Text("Write your opinion about this book") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            singleLine = false,
            maxLines = 4
        )
    }
}

@Composable
fun AllSynopsesScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(DataSource.books) { book ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "by ${book.author}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = book.synopsis,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 3
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationsScreen() {
    val notifications = listOf(
        "New book 'El Amor en los Tiempos del Cólera' has arrived!",
        "Special offer: 20% discount on all books this weekend",
        "Your order #12345 has been shipped",
        "Don't forget to leave a review for 'Cementerio de animales'",
        "New author interview available: Santiago Posteguillo"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Notifications",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(notifications) { notification ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notification",
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = notification,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookCard(book: Book, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = book.image),
                contentDescription = "Book cover",
                modifier = Modifier.size(80.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { /* TODO: Add to favorites */ },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Add to favorites",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = book.price,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun BookstoreBottomAppBar(navController: NavController, currentRoute: String) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomAppBarItem(
                icon = Icons.Default.Home,
                label = "Books",
                route = "books",
                currentRoute = currentRoute,
                navController = navController
            )

            BottomAppBarItem(
                icon = Icons.Default.Info,
                label = "Synopsis",
                route = "allsynopsis",
                currentRoute = currentRoute,
                navController = navController
            )

            BottomAppBarItem(
                icon = Icons.Default.Notifications,
                label = "Notifications",
                route = "notifications",
                currentRoute = currentRoute,
                navController = navController
            )
        }
    }
}

@Composable
fun BottomAppBarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    route: String,
    currentRoute: String,
    navController: NavController
) {
    val isSelected = currentRoute == route ||
            (route == "allsynopsis" && currentRoute.startsWith("booksynopsis/"))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable {
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
            .padding(vertical = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}




@Preview(showBackground = true)
@Composable
fun BookstoreAppPreview() {
    Practica3BookStoreTheme {
        BookstoreApp()
    }
}