package com.example.corpalivapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.corpalivapp.R

@Composable
fun ProgramasScreen(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ){
        Text(
            text = "Programas",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(all = 20.dp)
        )

        Image(
            painter = painterResource(R.drawable.programas),
            contentDescription = "Logo de la aplicación",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(30.dp)).padding(bottom = 20.dp)
        )

        Text(
            text = "Programa de Inclusión Laboral",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Brindamos una variedad de mecanismos diseñados específicamente para promover y facilitar la inclusión laboral de personas con discapacidad múltiple.\n" +
                    "\n" +
                    "Estos mecanismos están orientados a ayudarlas a integrarse de manera efectiva y exitosa en el mundo laboral, garantizando así que puedan desarrollar sus habilidades y alcanzar su máximo potencial en sus entornos de trabajo.",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Text(
            text = "Taller Sociolaboral",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Nos enfocamos en la formación integral de personas, brindándoles habilidades sociales, emocionales y laborales que les permitan integrarse con éxito al mundo laboral."
                    + "\n" +
                    "A través de diversas metodologías, fortalecemos su independencia, autonomía y competencias de empleabilidad. Además de estas habilidades, también nos dedicamos al diseño, fabricación y producción de productos con valor y sentido, fomentando un ambiente de colaboración, aprendizaje y crecimiento mutuo entre beneficiarios y emprendimientos.",
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 20.dp)
        )
    }

}