package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.corpalivapp.R

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo organizacion",
            modifier = Modifier.padding(bottom = 40.dp, top = 20.dp)
        )
        Text(
            text = """
                Somos una corporación sin fines de lucro con más de 30 años de historia trabajando por la discapacidad múltiple. Creemos en el valor de las personas con discapacidad múltiple y promovemos su desarrollo.
            """.trimIndent(),
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 40.dp)
        )
        Text(
            text = "Noticias Relevantes",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Image(
                painter = painterResource(R.drawable.noticia_1),
                contentDescription = "Primer servicio",
                modifier = Modifier.size(110.dp).clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Corpaliv obtiene aprobación para nuevo proyecto: “Impulso Laboral: ...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Formación, acompañamiento y alianzas con empresas comprometidas con ...",
                    fontSize = 14.sp
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(R.drawable.noticia_2),
                contentDescription = "Primer servicio",
                modifier = Modifier.size(110.dp).clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Exitosas Prácticas Laborales con beneficiarias y ...",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Durante el mes de julio beneficiarias y beneficiarios de Albalab, el programa de ...",
                    fontSize = 14.sp
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Facebook
            IconButton(
                onClick = {
                    //Abrir Facebook
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.logo_facebook),
                    contentDescription = "Facebook",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}