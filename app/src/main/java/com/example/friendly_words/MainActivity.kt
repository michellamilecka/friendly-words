package com.example.friendly_words

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                ScreenNavigation()
            }
        }
    }
}

@Composable
fun ScreenNavigation() {
    var showFirstScreen by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(5000L)
        showFirstScreen = false
    }

    if (showFirstScreen) {
        InformationScreen()
    } else {
        MainScreen()
    }
}

@Composable
fun InformationScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2EB2D6))
    ) {
        Spacer(modifier = Modifier.height(55.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Przyjazne Słowa Ustawienia",
                color = Color.White,
                fontSize = 55.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Rozpoznawanie słów na podstawie zdjęć - konfiguracja",
                color = Color.White,
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.9f),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 100.dp, topEnd = 100.dp),
            elevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Aplikacje powstają w ramach wspólnego projektu \"non-profit\" i \"open-source\" ",
                    fontSize = 27.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Politechniki Gdańskiej i fundacji Instytut Wspomagania Rozwoju Dziecka w Gdańsku (http://iwrd.pl/).",
                    fontSize = 26.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "IWRD, Politechnika Gdańska, ani żaden uczestnik projektu nie odnosi korzyści materialnych z udziału w ",
                    fontSize = 26.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "wytwarzaniu aplikacji",
                    fontSize = 26.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Więcej informacji na stronie: https://tiapps.org/",
                    fontSize = 26.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Do poprawnego działania potrzebne jest para aplikacji: Przyjazne Słowa i Przyjazne Słowa Ustawienia!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(85.dp))
                    Image(
                        painter = painterResource(id = R.drawable.pg_logo),
                        contentDescription = "Politechnika Gdańska Logo",
                        modifier = Modifier.size(250.dp)
                    )
                    Spacer(modifier = Modifier.width(95.dp))
                    Image(
                        painter = painterResource(id = R.drawable.iwrd_logo),
                        contentDescription = "Instytut Wspomagania Rozwoju Dziecka Logo",
                        modifier = Modifier.size(400.dp)
                    )
                    Spacer(modifier = Modifier.width(70.dp))
                    Image(
                        painter = painterResource(id = R.drawable.eti_logo),
                        contentDescription = "ETI Logo",
                        modifier = Modifier.size(300.dp)
                    )
                    Spacer(modifier = Modifier.width(80.dp))
                }
            }
        }
    }
}

