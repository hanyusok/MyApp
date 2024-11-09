package com.cafe24.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.cafe24.myapp.ui.theme.MyAppTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.DateTimeUnit
import kotlinx.serialization.Serializable
import androidx.compose.material3.Surface as Surface1

val supabase = createSupabaseClient(
    supabaseUrl = "http://martclinic.cafe24.com:8000/rest/v1/",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.ewogICJyb2xlIjogImFub24iLAogICJpc3MiOiAic3VwYWJhc2UiLAogICJpYXQiOiAxNzMwOTkxNjAwLAogICJleHAiOiAxODg4NzU4MDAwCn0.lA6CORXNZ8FLfK3_Y0dVo7XgavbtrdOfNZh1ursbjQQ"
) {
//    install(Auth)
    install(Postgrest)
    //install other modules
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyAppTheme {
                Surface1 (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                    ){
                    ClinicList()
                }
            }
        }
    }
}

@Serializable
data class Clinic (
    val id: Int,
    val createdAt: DateTimeUnit,
    val province: String,
    val city: String,
    val clinicName: String,
    val phoneNumber: String,
    val category: String,
    val ratAvailable: Boolean,
    val address: String,
)

@Composable
fun ClinicList(){
    val clinics = remember { mutableStateListOf<Clinic>() }
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
            val response = supabase.postgrest["clinic"].select().decodeList<Clinic>()
            clinics.addAll(response)
        }
    }
    LazyColumn{
        items(clinics){clinic ->
            ListItem(headlineContent = { Text(text = clinic.clinicName) })
        }
    }
}